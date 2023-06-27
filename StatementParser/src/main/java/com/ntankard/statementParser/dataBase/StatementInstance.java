package com.ntankard.statementParser.dataBase;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

public class StatementInstance extends DataObject {

    private static final String StatementInstance_Prefix = "StatementInstance_";

    public static final String StatementInstance_StatementFolder = StatementInstance_Prefix + "StatementFolder";
    public static final String StatementInstance_Name = StatementInstance_Prefix + "Name";
    public static final String StatementInstance_Path = StatementInstance_Prefix + "Path";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(StatementInstance_StatementFolder, StatementFolder.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementInstance_Name, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementInstance_Path, String.class));
        // ChildrenField

        return dataObjectSchema.finaliseContainer(StatementInstance.class);
    }

    /**
     * Constructor
     */
    public StatementInstance(StatementFolder statementFolder, String name, String path) {
        super(statementFolder.getTrackingDatabase()
                , StatementInstance_StatementFolder, statementFolder
                , StatementInstance_Name, name
                , StatementInstance_Path, path
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### General #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return getName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementFolder getBankAccount() {
        return get(StatementInstance_StatementFolder);
    }

    public String getName() {
        return get(StatementInstance_Name);
    }

    public String getPath() {
        return get(StatementInstance_Path);
    }
}
