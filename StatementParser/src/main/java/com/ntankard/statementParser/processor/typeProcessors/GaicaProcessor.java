package com.ntankard.statementParser.processor.typeProcessors;

import com.ntankard.statementParser.dataBase.BlankLine;
import com.ntankard.statementParser.dataBase.StatementInstance;
import com.ntankard.statementParser.dataBase.StatementInstanceLine;
import com.ntankard.statementParser.dataBase.TransactionPeriod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ntankard.javaObjectDatabase.util.FileUtil.readLines;

public class GaicaProcessor {

    public static List<StatementInstanceLine> parseInstance(StatementInstance statementInstance, Map<String, TransactionPeriod> periods) {
        List<String[]> lines = readLines(statementInstance.getPath());
        List<StatementInstanceLine> toReturn = new ArrayList<>();
        for (String[] line : lines) {

            if (line.length == 4 || line.length == 3) {
                try {
                    Date date = new SimpleDateFormat("yyyy/MM/dd").parse(line[0]);

                    periods.putIfAbsent(TransactionPeriod.getNameString(date), new TransactionPeriod(statementInstance.getTrackingDatabase(), date).add());
                    TransactionPeriod transactionPeriod = periods.get(TransactionPeriod.getNameString(date));

                    String description = line[1];

                    String valueString;
                    if (line.length == 3) {
                        valueString = line[2];
                    } else {
                        valueString = line[2] + line[3];
                    }
                    valueString = valueString.replace("\"", "");

                    Double value = Double.parseDouble(valueString);

                    toReturn.add(new StatementInstanceLine(statementInstance, transactionPeriod, date, description, value, line).add());
                    continue;
                } catch (Exception ignored) {

                }
            }

            new BlankLine(statementInstance, line).add();
        }
        return toReturn;
    }
}
