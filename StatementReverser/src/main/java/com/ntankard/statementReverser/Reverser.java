package com.ntankard.statementReverser;

import com.ntankard.javaObjectDatabase.database.io.Database_IO_Util;
import com.ntankard.javaObjectDatabase.util.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntankard.javaObjectDatabase.database.io.Database_IO_Util.INSTANCE_CLASSES_PATH;

public class Reverser {

    public static String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";

    public static String toLoadPath = Database_IO_Util.getLatestSaveDirectory(savePath + Database_IO_Util.ROOT_DATA_PATH) + INSTANCE_CLASSES_PATH;

    public static String[] toRemove = {
            "1.csv,Statement\\Rakuten\\22-02"
            , "2.csv,Statement\\Rakuten\\22-01"
            , "3.csv,Statement\\Rakuten\\21-12"
            , "1.csv,Statement\\Rakuten\\22-01"
            , "2.csv,Statement\\Rakuten\\21-12"
            , "1.csv,Statement\\Rakuten\\21-12"
    };

    public static String getKey(String line) {
        return line.split(",")[0];
    }

    public static void main(String[] args) {
        String toLoadPath = Database_IO_Util.getLatestSaveDirectory(savePath + Database_IO_Util.ROOT_DATA_PATH);
        System.out.println(toLoadPath);

        String statementDocumentPath = toLoadPath + "Classes\\StatementDocument.csv";
        List<String> allLines = FileUtil.readRawLines(statementDocumentPath);

        Map<String, List<String>> fileToKey = new HashMap<>();

        for (String line : allLines) {
            for (String remove : toRemove) {
                if (line.contains(remove)) {
                    fileToKey.putIfAbsent("StatementDocument.csv", new ArrayList<>());
                    fileToKey.get("StatementDocument.csv").add(getKey(line));
                    break;
                }
            }
        }

        String transactionLinePath = toLoadPath + "Classes\\TransactionLine.csv";
        allLines = FileUtil.readRawLines(transactionLinePath);
        for (String line : allLines) {
            for (String remove : fileToKey.get("StatementDocument.csv")) {
                if (line.contains(remove)) {
                    fileToKey.putIfAbsent("TransactionLine.csv", new ArrayList<>());
                    fileToKey.get("TransactionLine.csv").add(getKey(line));

                    String[] part = line.split(",");
                    fileToKey.putIfAbsent("StatementTransaction.csv", new ArrayList<>());
                    fileToKey.get("StatementTransaction.csv").add(part[6]);

                    break;
                }
            }
        }

        for (String file : fileToKey.keySet()) {
            String fileToPrune = toLoadPath + "Classes\\" + file;
            allLines = FileUtil.readRawLines(fileToPrune);
            List<String> toRemove = new ArrayList<>();
            for (String line : allLines) {
                for (String key : fileToKey.get(file)) {
                    if (line.startsWith(key)) {
                        toRemove.add(line);
                        break;
                    }
                }
            }

            allLines.removeAll(toRemove);

            // Uncomment to activate
            //FileUtil.writeRawLines(fileToPrune, allLines);
        }
    }

    public static void findAllKeys(List<String> toRemoveKeys, Map<String, List<String>> fileToKey) {
        int startSize;
        int endSize = toRemoveKeys.size();
        int loopCount = 0;
        do {
            startSize = endSize;
            findDependency(toRemoveKeys, fileToKey);
            endSize = toRemoveKeys.size();
            loopCount++;
        } while (startSize != endSize);
    }

    public static void findDependency(List<String> toRemoveKeys, Map<String, List<String>> fileToKey) {
        for (String file : FileUtil.findFilesInDirectory(toLoadPath)) {
            List<String> allLines = FileUtil.readRawLines(toLoadPath + file);
            for (String line : allLines) {
                for (String key : toRemoveKeys) {
                    if (line.contains(key) && !line.startsWith(key)) {
                        String lineKey = getKey(line);
                        if (!toRemoveKeys.contains(lineKey)) {
                            toRemoveKeys.add(lineKey);
                            fileToKey.putIfAbsent(file, new ArrayList<>());
                            fileToKey.get(file).add(getKey(lineKey));
                        }
                        break;
                    }
                }
            }
        }
    }
}
