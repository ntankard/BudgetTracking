package com.ntankard.budgetTracking.processing;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementDocument;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.TransactionLine;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ntankard.javaObjectDatabase.util.FileUtil.readRawLines;

public class Gaica_BankProcessing {

    public static List<TransactionLine> processGaica(StatementDocument statementDocument, Map<String, ExistingPeriod> periodMap) {
        List<TransactionLine> toReturn = new ArrayList<>();
        String cvsSplitBy = ",";

        // Read the file
        List<String> rawLines = readRawLines(statementDocument.getFullPath());
        for (String rawLine : rawLines) {

            // Separate each line
            String[] line = rawLine.split(cvsSplitBy);
            //noinspection StatementWithEmptyBody
            if (line.length == 4 || line.length == 3) {
                try {
                    // Process Date
                    Date date = new SimpleDateFormat("yyyy/MM/dd").parse(line[0]);
                    ExistingPeriod period = periodMap.get(new SimpleDateFormat("yy-MM").format(date));

                    // Process Description
                    String description = line[1];

                    // Process Value
                    String valueString;
                    if (line.length == 3) {
                        valueString = line[2];
                    } else {
                        valueString = line[2] + line[3];
                    }
                    valueString = valueString.replace("\"", "");
                    Double value = Double.parseDouble(valueString);

                    // Create line
                    rawLine = rawLine.replace(',', ';');
                    toReturn.add(new TransactionLine(statementDocument, period, date, value, description, null, rawLine).add());
                } catch (Exception ignored) {
                    // Malformed line (probably not a transaction line)
                }
            } else {
                // Other type of line
            }
        }
        return toReturn;
    }
}
