package com.ntankard.statementParser.dataBase;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

public class BlankLine extends DataObject {

    private static final String BlankLine_Prefix = "BlankLine_";

    public static final String BlankLine_StatementInstance = BlankLine_Prefix + "StatementInstance";
    public static final String BlankLine_Line = BlankLine_Prefix + "Line";
    public static final String BlankLine_RawLine = BlankLine_Prefix + "RawLine";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(BlankLine_StatementInstance, StatementInstance.class));
        dataObjectSchema.add(new DataField_Schema<>(BlankLine_Line, String[].class));
        dataObjectSchema.add(new DataField_Schema<>(BlankLine_RawLine, String.class));
        // ChildrenField

        return dataObjectSchema.finaliseContainer(BlankLine.class);
    }

    /**
     * Constructor
     */
    public BlankLine(StatementInstance statementInstance, String[] line) {
        super(statementInstance.getTrackingDatabase());

        StringBuilder concat = new StringBuilder();
        for (String part : line) {
            concat.append(",").append(part);
        }

        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , BlankLine_StatementInstance, statementInstance
                , BlankLine_Line, line
                , BlankLine_RawLine, concat.toString()
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementInstance getStatementInstance() {
        return get(BlankLine_StatementInstance);
    }

    public String[] getLine() {
        return get(BlankLine_Line);
    }

    public String getRawLine() {
        return get(BlankLine_RawLine);
    }
}
