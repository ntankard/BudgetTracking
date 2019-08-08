package com.ntankard.Tracking.DataBase;

import com.ntankard.Tracking.DataBase.Core.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TrackingDatabase_Reader {

    public static TrackingDatabase read(){
        TrackingDatabase data = new TrackingDatabase();

        readCurrency(data);
        readBank(data);
        readPeriod(data);
        readStatement(data);
        readTransaction(data);

        return data;
    }

    /**
     * Read a set of currencies
     * @param data The database to place the data
     */
    private static void readCurrency(TrackingDatabase data){
        String csvFile = "C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData\\Currency.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines)
        {
            String id = lines[0];
            double amount = Double.parseDouble(lines[1]);
            data.addCurrency(new Currency(id,amount));
        }
    }

    /**
     * Read a set of periods
     * @param data The database to place the data
     */
    private static void readPeriod(TrackingDatabase data){
        String csvFile = "C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData\\Period.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines)
        {
            int month = Integer.parseInt(lines[0]);
            int year = Integer.parseInt(lines[1]);
            data.addPeriod(Period.Month(month,year));
        }
    }

    /**
     * Read a set of bank accounts
     * @param data The database to place the data
     */
    private static void readBank(TrackingDatabase data){
        String csvFile = "C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData\\Bank.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines)
        {
            String id = lines[0];
            String currencyID = lines[1];
            data.getCurrency(currencyID);

            data.addBank(new Bank(id,data.getCurrency(currencyID)));
        }
    }

    /**
     * Read a set of statements
     * @param data The database to place the data
     */
    private static void readStatement(TrackingDatabase data){
        String csvFile = "C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData\\Statement.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines)
        {
            String bankId = lines[0];
            String periodId = lines[1];

            data.addStatement(new Statement(data.getBank(bankId),data.getPeriod(periodId)));
        }
    }

    /**
     * Read a set of transactions
     * @param data The database to place the data
     */
    private static void readTransaction(TrackingDatabase data){
        String csvFile = "C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData\\Transaction.csv";
        ArrayList<String[]> allLines = readLines(csvFile);
        for (String[] lines : allLines)
        {
            String bankID = lines[0];
            String PeriodID = lines[1];
            String id = lines[2];
            String description = lines[3];
            double value = Double.parseDouble(lines[4]);

            data.addTransaction(new Transaction(data.getStatement(data.getBank(bankID),data.getPeriod(PeriodID)),id,description,value));
        }
    }

    /**
     * Read lines from a csv file
     * @param csvFile The path to the file to read
     * @return ALl lines read from the file
     */
    private static ArrayList<String[]> readLines(String csvFile){
        BufferedReader br = null;
        String line = "";
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
}