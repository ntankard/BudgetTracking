package com.ntankard.statementParser.processor.typeProcessors;

import com.ntankard.statementParser.dataBase.BlankLine;
import com.ntankard.statementParser.dataBase.StatementInstance;
import com.ntankard.statementParser.dataBase.StatementInstanceLine;
import com.ntankard.statementParser.dataBase.TransactionPeriod;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.ntankard.javaObjectDatabase.util.FileUtil.readLines;

public class RakutenProcessor {
    public static List<StatementInstanceLine> parseInstance(StatementInstance statementInstance, Map<String, TransactionPeriod> periods) {
        List<String[]> lines = readLines(statementInstance.getPath());
        List<StatementInstanceLine> toReturn = new ArrayList<>();

        for (String[] line : lines) {

            if (line.length == 10 || line.length == 11) {
                try {
                    Date date = new SimpleDateFormat("yyyy/MM/dd").parse(line[0].replace("\"", ""));

                    periods.putIfAbsent(TransactionPeriod.getNameString(date), new TransactionPeriod(statementInstance.getTrackingDatabase(), date).add());
                    TransactionPeriod transactionPeriod = periods.get(TransactionPeriod.getNameString(date));

                    String description = line[1].replace("\"", "").replace("ＶＩＳＡ国内利用　VS ", "");

                    String valueString;
                    valueString = line[4].replace("\"", "");

                    Double value = Double.parseDouble(valueString);

                    toReturn.add(new StatementInstanceLine(statementInstance, transactionPeriod, date, description, value, line).add());
                    continue;
                } catch (Exception ignored) {
                    if (!line[0].equals("\"\"")) {
                        System.out.println();
                    }
                }
            } else {
                System.out.println();
            }

            new BlankLine(statementInstance, line).add();
        }
        return toReturn;
    }
}
