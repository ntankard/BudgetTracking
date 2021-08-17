package com.ntankard.statementParser;


import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.javaObjectDatabase.database.io.Database_IO;
import com.ntankard.statementParser.display.frames.mainFrame.MasterFrame;

import java.util.HashMap;
import java.util.Map;

import static com.ntankard.statementParser.processor.DatabaseLoader.loadDatabase;

public class StatementParser {
    public static String databasePath = "com.ntankard.statementParser.dataBase";
    public static String savePath = "C:\\Users\\Nicholas\\Google Drive\\NBudgetTrackingParserData";

    public static Database createDataBase() {
        Map<String, String> nameMap = new HashMap<>();
        return Database_IO.read(Database_Schema.getSchemaFromPackage(databasePath), databasePath, savePath, nameMap);
    }

    public static void main(String[] args) {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true); // TODO remove
        Database database = createDataBase();
        loadDatabase(database, savePath);
        MasterFrame.open(database);
    }
}
