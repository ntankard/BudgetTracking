package com.ntankard.budgetTracking.display.frames.mainFrame.statement;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementDocument;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementFolder;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.TransactionLine;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.dynamicGUI.gui.util.table.TableColumnAdjuster;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.exception.corrupting.CorruptingException;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntankard.javaObjectDatabase.util.FileUtil.readRawLines;

public class FileCheckPanel extends UpdatableJPanel {

    // Core database
    private final Database database;

    // The GUI components
    private final List<TransactionMappingPanel> transactionMappingPanelList = new ArrayList<>();

    /**
     * Constructor
     */
    public FileCheckPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        JTabbedPane master_tPanel = new JTabbedPane();

        for (Period period : database.get(ExistingPeriod.class)) {
            List<StatementFolder> statementFolderList = period.getChildren(StatementFolder.class);
            Map<Bank, JTabbedPane> bankPanes = new HashMap<>();

            // Are there any files for this period?
            if (!statementFolderList.isEmpty()) {
                JTabbedPane period_tPanel = new JTabbedPane();

                // For each individual file (in bank subfolder)
                for (StatementFolder folder : statementFolderList) {

                    // Add the bank for the first time
                    if (!bankPanes.containsKey(folder.getBank())) {
                        bankPanes.put(folder.getBank(), new JTabbedPane());
                        period_tPanel.addTab(folder.getBank().toString(), bankPanes.get(folder.getBank()));
                    }
                    JTabbedPane files = new JTabbedPane();
                    TransactionMappingPanel transactionMappingPanel = new TransactionMappingPanel(folder, this);
                    transactionMappingPanelList.add(transactionMappingPanel);
                    bankPanes.get(folder.getBank()).addTab("TransactionMapping", transactionMappingPanel);
                    bankPanes.get(folder.getBank()).addTab("Files", files);

                    // Generate each files comparison table
                    files.addTab("All", new JScrollPane(generateCombinedFile(folder)));
                    for (StatementDocument document : folder.getChildren(StatementDocument.class)) {
                        files.addTab(document.getFileName(), new JScrollPane(generateIndividualFile(document)));
                    }
                }
                master_tPanel.addTab(period.toString(), period_tPanel);
            }
        }

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    private JPanel generateCombinedFile(StatementFolder folder) {
        JPanel toReturn = new JPanel(new BorderLayout());
        List<StatementDocument> documents = folder.getChildren(StatementDocument.class);
        List<StatementTransaction> transactions = folder.getChildren(StatementTransaction.class);
        String[] columnNames = new String[documents.size()];
        Object[][] data = new Object[transactions.size()][documents.size()];

        int i = 0;
        for (StatementDocument document : documents) {
            columnNames[i++] = document.getFileName();
        }

        int row = 0;
        for (StatementTransaction transaction : transactions) {
            List<TransactionLine> lines = transaction.getChildren(TransactionLine.class);
            int col = 0;
            for (StatementDocument document : documents) {
                TransactionLine match = null;

                // See if this file has this transaction
                for (TransactionLine line : lines) {
                    if (line.getStatementDocument().equals(document)) {
                        match = line;
                        break;
                    }
                }

                if (match != null) {
                    lines.remove(match);
                    data[row][col] = match.getRawLine();
                }
                col++;
            }
            row++;
        }

        // Highlight missing data
        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (data[row][column] == null) {
                    comp.setBackground(Color.red);
                } else {
                    comp.setBackground(Color.white);
                }
                return comp;
            }
        };

        // Resize to match data
        TableColumnAdjuster tableColumnAdjuster = new TableColumnAdjuster(table);
        tableColumnAdjuster.adjustColumns();

        toReturn.add(table, BorderLayout.CENTER);
        return toReturn;
    }

    private JPanel generateIndividualFile(StatementDocument statementDocument) {
        JPanel toReturn = new JPanel(new BorderLayout());
        List<String> lines = readRawLines(statementDocument.getFullPath());
        String[] columnNames = {"Raw",
                "Date",
                "Description",
                "Value"
        };
        Object[][] data = new Object[lines.size()][4];

        // Generate the static data (files are only read on load so update can be ignored)
        List<TransactionLine> transactionLines = statementDocument.getChildren(TransactionLine.class);
        int lineIndex = 0;

        for (int i = 0; i < lines.size(); i++) {
            String lineData = lines.get(i).replace(',', ';');
            data[i][0] = lineData;

            if (lineIndex < transactionLines.size()) {
                TransactionLine transactionLine = transactionLines.get(lineIndex);
                if (lineData.equals(transactionLine.getRawLine())) {

                    data[i][1] = new SimpleDateFormat("yyyy/MM/dd").format(transactionLine.getDate());
                    data[i][2] = transactionLine.getDescription();
                    data[i][3] = transactionLine.getValue();

                    lineIndex++;
                }
            }
        }

        if (lineIndex != transactionLines.size()) {
            throw new CorruptingException(database, "Statement lines are missing");
        }

        // Highlight the dead rows
        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (data[row][2] == null && column == 0) {
                    comp.setBackground(Color.gray);
                } else {
                    comp.setBackground(Color.white);
                }
                return comp;
            }
        };

        // Resize to match data
        TableColumnAdjuster tableColumnAdjuster = new TableColumnAdjuster(table);
        tableColumnAdjuster.adjustColumns();

        toReturn.add(table, BorderLayout.CENTER);
        return toReturn;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        // TODO not everything is updated here, you need to regen the custom table as well
        transactionMappingPanelList.forEach(TransactionMappingPanel::update);
    }
}
