package com.ntankard.budgetTracking.dataBase.core.baseObject;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public abstract class NamedDataObject extends DataObject {

    public static final String NamedDataObject_Name = "getName";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(NamedDataObject_Name, String.class));
        // Children

        // Name ========================================================================================================
        dataObjectSchema.get(NamedDataObject_Name).setManualCanEdit(true);
        //==============================================================================================================

        return dataObjectSchema.endLayer(NamedDataObject.class);
    }

    /**
     * Constructor
     */
    public NamedDataObject(Database database, Object... args) {
        super(database, args);
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

    public String getName() {
        return get(NamedDataObject_Name);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setName(String name) {
        set(NamedDataObject_Name, name);
    }
}
