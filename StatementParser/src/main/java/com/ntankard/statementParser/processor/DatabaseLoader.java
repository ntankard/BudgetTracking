package com.ntankard.statementParser.processor;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.statementParser.dataBase.*;
import com.ntankard.statementParser.processor.typeProcessors.GaicaProcessor;
import com.ntankard.statementParser.processor.typeProcessors.RakutenProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntankard.javaObjectDatabase.util.FileUtil.findFilesInDirectory;
import static com.ntankard.javaObjectDatabase.util.FileUtil.findFoldersInDirectory;
import static com.ntankard.statementParser.processor.StatementParser.parseAllInstances;

public class DatabaseLoader {

    public static String STATEMENT_PATH = "\\Statement\\";

    public static Database loadDatabase(Database database, String savePath) {


        String fullStatementPath = savePath + STATEMENT_PATH;
        Map<String, TransactionPeriod> periods = new HashMap<>();
        for (BankAccount bankAccount : database.get(BankAccount.class)) {

            List<String> statementFolders = findFoldersInDirectory(fullStatementPath + bankAccount.getPath());
            StatementFolder pastStatementFolder = null;
            for (String statementFolderPath : statementFolders) {
                periods.putIfAbsent(statementFolderPath, new TransactionPeriod(database, Integer.parseInt(statementFolderPath.split("-")[0]), Integer.parseInt(statementFolderPath.split("-")[1])).add());
                StatementFolder statementFolder = new StatementFolder(bankAccount, periods.get(statementFolderPath), statementFolderPath, fullStatementPath + bankAccount.getPath() + "\\" + statementFolderPath, pastStatementFolder).add();
                pastStatementFolder = statementFolder;

                List<String> statementInstances = findFilesInDirectory(statementFolder.getPath());
                statementInstances.removeIf(s -> !s.endsWith(".csv"));
                Map<StatementInstance, List<StatementInstanceLine>> data = new HashMap<>();
                statementInstances.sort(String::compareTo);

                String StatementInstancePath = statementInstances.get(statementInstances.size() - 1);

                // for (String StatementInstancePath : statementInstances) {
                StatementInstance statementInstance = new StatementInstance(statementFolder, StatementInstancePath, statementFolder.getPath() + "\\" + StatementInstancePath).add();
                switch (bankAccount.getName()) {
                    case "Gaica":
                        data.put(statementInstance, GaicaProcessor.parseInstance(statementInstance, periods));
                        break;
                    case "Rakuten":
                        data.put(statementInstance, RakutenProcessor.parseInstance(statementInstance, periods));
                        break;
                    default:
                        throw new RuntimeException();
                }
                //}
                parseAllInstances(statementFolder, data);
            }
        }

        return database;
    }
}
