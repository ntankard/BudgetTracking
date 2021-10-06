package com.ntankard.budgetTracking.processing;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.UnusedFile;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.*;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.subContainers.TreeNode;
import com.ntankard.javaObjectDatabase.exception.corrupting.CorruptingException;
import com.ntankard.javaObjectDatabase.exception.nonCorrupting.NonCorruptingException;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;
import com.ntankard.javaObjectDatabase.util.set.TwoParent_Children_Set;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetTracking {
    public static void process(Database database) {
        loadStatements(database);
    }

    public static void loadStatements(Database database) {
        // Map all the periods so we can find it based on folder name
        Map<String, ExistingPeriod> periodMap = new HashMap<>();
        for (ExistingPeriod existingPeriod : database.get(ExistingPeriod.class)) {
            periodMap.put(existingPeriod.getYear().toString().substring(2) + "-" + String.format("%02d", existingPeriod.getMonth()), existingPeriod);
        }

        // Find the bank accounts based on string
        Bank gaica = null;
        Bank rakuten = null;
        for (Bank bank : database.get(Bank.class)) {
            switch (bank.getName()) {
                case "Gaica-Jap":
                    gaica = bank;
                    break;
                case "Rakuten":
                    rakuten = bank;
                    break;
            }
        }
        if (gaica == null || rakuten == null) {
            throw new CorruptingException(database, "Can not find the expected bank accounts");
        }

        // For each file
        TreeNode<String> statement = database.getFileMap().getFolderTree().getChild("Statement");
        while (statement != null) {

            // Get a file to parse
            TreeNode<String> bankFolder = statement.children.get(0);
            TreeNode<String> folder = bankFolder.children.get(0);
            List<String> files = folder.getChildData();
            files.sort(String::compareTo);
            TreeNode<String> file = folder.getChild(files.get(0));

            // Remove unneeded files
            if (file.size() != 0) {
                TreeNode<String> subFile = file.children.get(0);
                String path = statement.data + "\\" + bankFolder.data + "\\" + folder.data + "\\" + file.data;
                new UnusedFile(database, subFile.data, path).add();

                // Check if any instances are left
                statement = database.getFileMap().getFolderTree().getChild("Statement");
                continue;
            }

            // Get all relevant data objects related to the file
            ExistingPeriod existingPeriod = periodMap.get(folder.data);
            Bank bank = null;
            if (bankFolder.data.equals("Gaica")) {
                bank = gaica;
            } else if (bankFolder.data.equals("Rakuten")) {
                bank = rakuten;
            }
            String path = statement.data + "\\" + bankFolder.data + "\\" + folder.data;
            StatementDocument past = null;

            // Find the existing folder or make one
            List<StatementFolder> statementFolders = new TwoParent_Children_Set<StatementFolder, Bank, Period>(StatementFolder.class, bank, existingPeriod).get();
            StatementFolder statementFolder;
            if (statementFolders.size() > 1) {
                throw new CorruptingException(database, "Multiple instances for the same folder");
            } else if (statementFolders.size() == 1) {
                statementFolder = statementFolders.get(0);
            } else {
                statementFolder = new StatementFolder(existingPeriod, bank).add();
            }

            // Find the past document
            List<StatementDocument> statementDocuments = new OneParent_Children_Set<>(StatementDocument.class, statementFolder).get();
            statementDocuments.sort(Comparator.comparing(StatementDocument::getFileName));
            if (statementDocuments.size() != 0) {
                past = statementDocuments.get(statementDocuments.size() - 1);
            }

            // Create and process the core instance
            StatementDocument statementDocument = new StatementDocument(statementFolder, file.data, path, past).add();
            processStatementDocument(statementDocument, periodMap);

            // Check if any instances are left
            statement = database.getFileMap().getFolderTree().getChild("Statement");
        }
    }

    public static void processStatementDocument(StatementDocument statementDocument, Map<String, ExistingPeriod> periodMap) {
        if (statementDocument.getStatementFolder().getBank().getName().equals("Gaica-Jap")) {
            List<TransactionLine> created = Gaica_BankProcessing.processGaica(statementDocument, periodMap);

            if (statementDocument.getPastInstance() == null) {  // First document for this period, add all lines
                for (TransactionLine line : created) {
                    StatementTransaction group = new StatementTransaction(statementDocument.getStatementFolder()).add();
                    line.setStatementTransaction(group);
                }
            } else { // Map to existing lines
                List<StatementTransaction> existing = new OneParent_Children_Set<>(StatementTransaction.class, statementDocument.getStatementFolder()).get();

                int oldKey = 0;
                int newKey = 0;

                while (true) {
                    if (oldKey == existing.size()) {
                        TransactionLine line = created.get(newKey);
                        StatementTransaction newGroup = new StatementTransaction(statementDocument.getStatementFolder()).add();
                        line.setStatementTransaction(newGroup);
                        newKey++;
                    } else {
                        StatementTransaction group = existing.get(oldKey);
                        TransactionLine line = created.get(newKey);
                        if (matches(group, line)) {
                            oldKey++;
                            newKey++;
                            line.setStatementTransaction(group);
                        } else {
                            StatementTransaction newGroup = new StatementTransaction(statementDocument.getStatementFolder()).add();
                            line.setStatementTransaction(newGroup);
                            newKey++;
                        }
                    }

                    if (newKey == created.size()) {
                        if (oldKey < existing.size()) {
                            throw new NonCorruptingException("A line has been removed in a subsequent statement");
                        }
                        break;
                    }
                }
            }
        }
    }

    public static boolean matches(StatementTransaction oldLine, TransactionLine newLine) {
        if (!oldLine.getCoreLine().getDate().equals(newLine.getDate())) {
            return false;
        }

        if (!oldLine.getCoreLine().getDescription().equals(newLine.getDescription())) {
            return false;
        }

        return oldLine.getCoreLine().getValue().equals(newLine.getValue());
    }
}
