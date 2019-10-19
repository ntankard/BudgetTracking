package com.ntankard.Tracking.DataBase;

import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.*;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Bank;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrackingDatabase_Reader {

    /**
     * Save the database to a new directory
     *
     * @param data     The data to save
     * @param corePath The directory to put the folder
     */
    public static void save(TrackingDatabase data, String corePath) {
        // Find the next save dir
        int max = 0;
        List<String> folders = findFoldersInDirectory(corePath);
        for (String s : folders) {
            int value = Integer.parseInt(s);
            if (value > max) {
                max = value;
            }
        }
        String csvFile = corePath + "\\" + (max + 1) + "\\";

        // Make the folder
        new File(csvFile).mkdir();

        // Save the data
        saveCurrency(data, csvFile);
        saveCategory(data, csvFile);
        saveBank(data, csvFile);
        savePeriod(data, csvFile);
        saveStatement(data, csvFile);
        saveTransaction(data, csvFile);
        saveCategoryTransfer(data, csvFile);
        savePeriodTransfer(data, csvFile);
        saveFund(data, csvFile);
        savePeriodFundTransfer(data, csvFile);
        saveFundEvent(data, csvFile);
        saveFundChargeTransfer(data, csvFile);
    }

    /**
     * Read all files for the database from the latest save folder
     *
     * @param corePath The path that files are located in
     * @return The database
     */
    public static TrackingDatabase read(String corePath) {
        TrackingDatabase data = TrackingDatabase.get();

        // Find the latest data
        int max = 0;
        List<String> folders = findFoldersInDirectory(corePath);
        for (String s : folders) {
            int value = Integer.parseInt(s);
            if (value > max) {
                max = value;
            }
        }
        String csvFile = corePath + "\\" + max + "\\";

        // Read the data
        readCurrency(data, csvFile);
        readCategory(data, csvFile);
        readBank(data, csvFile);
        readPeriod(data, csvFile);
        readStatement(data, csvFile);
        readTransaction(data, csvFile);
        readCategoryTransfer(data, csvFile);
        readPeriodTransfer(data, csvFile);
        readFund(data, csvFile);
        readFundEvent(data, csvFile);
        readPeriodFundTransfer(data, csvFile);
        readFundChargeTransfer(data, csvFile);

        data.finalizeData();
        return data;
    }

    /**
     * Read a set of currencies
     *
     * @param data The database to place the data
     */
    private static void readCurrency(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "Currency.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {
            String id = lines[0];
            double toSecondary = Double.parseDouble(lines[1]);
            double toPrimary = Double.parseDouble(lines[2]);
            boolean isPrimary = Boolean.parseBoolean(lines[3]);
            data.addCurrency(new Currency(id, toSecondary, toPrimary, isPrimary));
        }
    }

    /**
     * Save a set of currencies
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void saveCurrency(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (Currency t : data.getCurrencies()) {
            List<String> line = new ArrayList<>();
            line.add(t.getId());
            line.add(t.getToSecondary() + "");
            line.add(t.getToPrimary() + "");
            line.add(t.isPrimary() + "");
            lines.add(line);
        }

        writeLines(csvFile + "Currency.csv", lines);
    }

    /**
     * Read a set of categories
     *
     * @param data The database to place the data
     */
    private static void readCategory(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "Category.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {
            String id = lines[0];
            int order = Integer.parseInt(lines[1]);
            data.addCategory(new Category(id, order));
        }
    }

    /**
     * Save a set of categories
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void saveCategory(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (Category t : data.getCategories()) {
            List<String> line = new ArrayList<>();
            line.add(t.getId());
            line.add(t.getOrder() + "");
            lines.add(line);
        }

        writeLines(csvFile + "Category.csv", lines);
    }

    /**
     * Read a set of periods
     *
     * @param data The database to place the data
     */
    private static void readPeriod(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "Period.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {
            int month = Integer.parseInt(lines[0]);
            int year = Integer.parseInt(lines[1]);
            data.addPeriod(Period.Month(month, year));
        }
    }

    /**
     * Save a set of periods
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void savePeriod(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (Period t : data.getPeriods()) {
            List<String> line = new ArrayList<>();
            line.add((t.getStart().get(Calendar.MONTH) + 1) + "");
            line.add((t.getStart().get(Calendar.YEAR)) + "");
            lines.add(line);
        }

        writeLines(csvFile + "Period.csv", lines);
    }


    /**
     * Read a set of bank accounts
     *
     * @param data The database to place the data
     */
    private static void readBank(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "Bank.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {
            String bank = lines[0];
            String account = lines[1];
            String currencyID = lines[2];
            int order = Integer.parseInt(lines[3]);

            data.addBank(new Bank(bank, account, data.getCurrency(currencyID), order));
        }
    }


    /**
     * Save a set of bank
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void saveBank(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (Bank t : data.getBanks()) {
            List<String> line = new ArrayList<>();
            line.add(t.getIdBank());
            line.add(t.getIdAccount());
            line.add(t.getCurrency().getId());
            line.add(t.getOrder() + "");
            lines.add(line);
        }

        writeLines(csvFile + "Bank.csv", lines);
    }


    /**
     * Read a set of statements
     *
     * @param data The database to place the data
     */
    private static void readStatement(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "Statement.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {
            String bankId = lines[0];
            String periodId = lines[1];
            double start = Double.parseDouble(lines[2]);
            double end = Double.parseDouble(lines[3]);
            double transferIn = Double.parseDouble(lines[4]);
            double transferOut = Double.parseDouble(lines[5]);

            data.addStatement(new Statement(data.getBank(bankId), data.getPeriod(periodId), start, end, transferIn, transferOut));
        }
    }

    /**
     * Save a set of statements
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void saveStatement(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (Statement t : data.getStatements()) {
            List<String> line = new ArrayList<>();
            line.add(t.getIdBank().getId());
            line.add(t.getIdPeriod().getId());
            line.add(t.getStart() + "");
            line.add(t.getEnd() + "");
            line.add(t.getTransferIn() + "");
            line.add(t.getTransferOut() + "");
            lines.add(line);
        }

        writeLines(csvFile + "Statement.csv", lines);

    }

    /**
     * Read a set of transactions
     *
     * @param data The database to place the data
     */
    private static void readTransaction(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "Transaction.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {
            String bankID = lines[0];
            String PeriodID = lines[1];
            String id = lines[2];
            String description = lines[3];
            double value = Double.parseDouble(lines[4]);
            String categoryId = lines[5];

            data.addTransaction(new Transaction(data.getStatement(data.getBank(bankID), data.getPeriod(PeriodID)), id, description, value, data.getCategory(categoryId)));
        }
    }

    /**
     * Save a set of transactions
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void saveTransaction(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (Transaction t : data.getTransactions()) {
            List<String> line = new ArrayList<>();
            line.add(t.getSourceContainer().getIdBank().getId());
            line.add(t.getSourceContainer().getIdPeriod().getId());
            line.add(t.getIdCode());
            line.add(t.getDescription());
            line.add(t.getValue().toString());
            line.add(t.getDestinationCategory().getId());
            lines.add(line);
        }

        writeLines(csvFile + "Transaction.csv", lines);
    }

    /**
     * Read a set of categoryTransfers
     *
     * @param data The database to place the data
     */
    private static void readCategoryTransfer(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "CategoryTransfer.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {
            String PeriodID = lines[0];
            String id = lines[1];
            String sourceCategoryId = lines[2];
            String destinationCategoryId = lines[3];
            String currencyID = lines[4];
            String description = lines[5];
            double value = Double.parseDouble(lines[6]);

            data.addCategoryTransfer(new CategoryTransfer(data.getPeriod(PeriodID), id, data.getCategory(sourceCategoryId), data.getCategory(destinationCategoryId), data.getCurrency(currencyID), description, value));
        }
    }

    /**
     * Save a set of categoryTransfers
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void saveCategoryTransfer(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (CategoryTransfer t : data.getCategoryTransfers()) {
            List<String> line = new ArrayList<>();
            line.add(t.getSourceContainer().getId());
            line.add(t.getIdCode());
            line.add(t.getSourceCategory().getId());
            line.add(t.getDestinationCategory().getId());
            line.add(t.getCurrency().getId());
            line.add(t.getDescription());
            line.add(t.getValue().toString());
            lines.add(line);
        }

        writeLines(csvFile + "CategoryTransfer.csv", lines);
    }

    /**
     * Read a set of periodTransfers
     *
     * @param data The database to place the data
     */
    private static void readPeriodTransfer(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "PeriodTransfer.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {

            String id = lines[0];
            String sourcePeriodId = lines[1];
            String destinationPeriodId = lines[2];
            String currencyID = lines[3];
            String categoryID = lines[4];
            String description = lines[5];
            double value = Double.parseDouble(lines[6]);

            data.addPeriodTransfer(new PeriodTransfer(
                    id,
                    data.getPeriod(sourcePeriodId),
                    data.getPeriod(destinationPeriodId),
                    data.getCurrency(currencyID),
                    data.getCategory(categoryID),
                    description,
                    value
            ));
        }
    }

    /**
     * Save a set of periodTransfers
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void savePeriodTransfer(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (PeriodTransfer t : data.getPeriodTransfers()) {
            List<String> line = new ArrayList<>();
            line.add(t.getId());
            line.add(t.getSourceContainer().getId());
            line.add(t.getDestinationContainer().getId());
            line.add(t.getCurrency().getId());
            line.add(t.getSourceCategory().getId());
            line.add(t.getDescription());
            line.add(t.getValue().toString());
            lines.add(line);
        }

        writeLines(csvFile + "PeriodTransfer.csv", lines);
    }

    /**
     * Read a set of nonPeriodFunds
     *
     * @param data The database to place the data
     */
    private static void readFund(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "Fund.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {

            String id = lines[0];

            data.addFund(new Fund(
                    id
            ));
        }
    }

    /**
     * Save a set of nonPeriodFunds
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void saveFund(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (Fund t : data.getFunds()) {
            List<String> line = new ArrayList<>();
            line.add(t.getId());
            lines.add(line);
        }

        writeLines(csvFile + "Fund.csv", lines);
    }

    /**
     * Read a set of PeriodFundTransfer
     *
     * @param data The database to place the data
     */
    private static void readPeriodFundTransfer(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "PeriodFundTransfer.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {

            String id = lines[0];
            String sourcePeriodId = lines[1];
            String destinationFundId = lines[2];
            String categoryID = lines[3];
            String fundEventId = lines[4];
            String currencyID = lines[5];
            String description = lines[6];
            double value = Double.parseDouble(lines[7]);

            data.addPeriodFundTransfer(new PeriodFundTransfer(
                    id,
                    data.getPeriod(sourcePeriodId),
                    data.getFund(destinationFundId),
                    data.getCategory(categoryID),
                    data.getFundEvent(fundEventId),
                    data.getCurrency(currencyID),
                    description,
                    value
            ));
        }
    }

    /**
     * Save a set of PeriodFundTransfer
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void savePeriodFundTransfer(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (PeriodFundTransfer t : data.getPeriodFundTransfers()) {
            List<String> line = new ArrayList<>();
            line.add(t.getId());
            line.add(t.getSourceContainer().getId());
            line.add(t.getDestinationContainer().getId());
            line.add(t.getSourceCategory().getId());
            line.add(t.getDestinationCategory().getId());
            line.add(t.getCurrency().getId());
            line.add(t.getDescription());
            line.add(t.getValue().toString());
            lines.add(line);
        }

        writeLines(csvFile + "PeriodFundTransfer.csv", lines);
    }

    /**
     * Read a set of PeriodFundTransfer
     *
     * @param data The database to place the data
     */
    private static void readFundEvent(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "FundEvent.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {

            String fundCode = lines[0];
            String idCode = lines[1];

            data.addFundEvent(new FundEvent(
                    data.getFund(fundCode),
                    idCode
            ));
        }
    }

    /**
     * Save a set of fundEvent
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void saveFundEvent(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (FundEvent t : data.getFundEvents()) {
            List<String> line = new ArrayList<>();
            line.add(t.getIdFund().getId());
            line.add(t.getIdCode());
            lines.add(line);
        }

        writeLines(csvFile + "FundEvent.csv", lines);
    }

    /**
     * Read a set of PeriodFundTransfer
     *
     * @param data The database to place the data
     */
    private static void readFundChargeTransfer(TrackingDatabase data, String csvFileRoot) {
        String csvFile = csvFileRoot + "FundChargeTransfer.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines) {

            String id = lines[0];
            String sourcePeriodId = lines[1];
            String destinationFundId = lines[2];
            String currencyID = lines[4];
            String description = lines[5];
            double value = Double.parseDouble(lines[6]);

            data.addFundChargeTransfer(new FundChargeTransfer(
                    id,
                    data.getPeriod(sourcePeriodId),
                    data.getFund(destinationFundId),
                    data.getCurrency(currencyID),
                    description,
                    value
            ));
        }
    }

    /**
     * Save a set of PeriodFundTransfer
     *
     * @param data    The database to get the data
     * @param csvFile The path to write the files to
     */
    private static void saveFundChargeTransfer(TrackingDatabase data, String csvFile) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (FundChargeTransfer t : data.getFundChargeTransfers()) {
            List<String> line = new ArrayList<>();
            line.add(t.getId());
            line.add(t.getSourceContainer().getId());
            line.add(t.getDestinationContainer().getId());
            line.add(t.getCurrency().getId());
            line.add(t.getDescription());
            line.add(t.getValue().toString());
            lines.add(line);
        }

        writeLines(csvFile + "FundChargeTransfer.csv", lines);
    }

    /**
     * Read lines from a csv file
     *
     * @param csvFile The path to the file to read
     * @return ALl lines read from the file
     */
    private static ArrayList<String[]> readLines(String csvFile) {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        ArrayList<String[]> allLines = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(cvsSplitBy);
                allLines.add(lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return allLines;
    }

    /**
     * Write lines to a csv file
     *
     * @param path  The path to write the files to
     * @param lines The lines to write
     */
    private static void writeLines(String path, List<List<String>> lines) {
        try {
            FileWriter fw = new FileWriter(path);
            for (List<String> line : lines) {
                for (String s : line) {
                    fw.write(s);
                    fw.write(",");
                }
                fw.write('\n');
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Find the folders in a directory
     *
     * @param directoryPath The path to search
     * @return A list of folders in the directory
     */
    private static List<String> findFoldersInDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        FileFilter directoryFileFilter = File::isDirectory;

        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        assert directoryListAsFile != null;
        List<String> foldersInDirectory = new ArrayList<>(directoryListAsFile.length);
        for (File directoryAsFile : directoryListAsFile) {
            foldersInDirectory.add(directoryAsFile.getName());
        }

        return foldersInDirectory;
    }
}
