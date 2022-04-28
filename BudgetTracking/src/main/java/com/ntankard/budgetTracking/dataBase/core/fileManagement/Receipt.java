package com.ntankard.budgetTracking.dataBase.core.fileManagement;

import com.ntankard.budgetTracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Static_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.FileInterface;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class Receipt extends DataObject implements FileInterface {

    private static final String Receipt_Prefix = "Receipt_";

    public static final String Receipt_FileName = "getFileName";
    public static final String Receipt_BankTransfer = "getBankTransfer";
    public static final String Receipt_ContainerPath = "getContainerPath";
    public static final String Receipt_FullPath = Receipt_Prefix + "FullPath";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(Receipt_FileName, String.class));
        dataObjectSchema.add(new DataField_Schema<>(Receipt_BankTransfer, BankTransfer.class));
        dataObjectSchema.add(new DataField_Schema<>(Receipt_ContainerPath, String.class));
        dataObjectSchema.add(new DataField_Schema<>(Receipt_FullPath, String.class));
        // Children

        // BankTransfer ================================================================================================
        dataObjectSchema.get(Receipt_BankTransfer).setManualCanEdit(true);
        // ContainerPath ===============================================================================================
        dataObjectSchema.get(Receipt_ContainerPath).setDataCore_schema(new Static_DataCore_Schema<>("Receipts"));
        // FullPath ====================================================================================================
        dataObjectSchema.<String>get(Receipt_FullPath).setDataCore_schema(
                new Derived_DataCore_Schema<String, Receipt>
                        (container -> container.getTrackingDatabase().getFilesPath() + "\\" + container.getContainerPath() + "\\" + container.getFileName()
                                , makeSourceChain(Receipt_ContainerPath)
                                , makeSourceChain(Receipt_FileName)));
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(Receipt.class);
    }

    /**
     * Constructor
     */
    public Receipt(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public Receipt(String fileName, BankTransfer bankTransfer) {
        super(bankTransfer.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Receipt_FileName, fileName
                , Receipt_BankTransfer, bankTransfer
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Implementations #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public String getFileName() {
        return get(Receipt_FileName);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getContainerPath() {
        return get(Receipt_ContainerPath);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getFullPath() {
        return get(Receipt_FullPath);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public BankTransfer getBankTransfer() {
        return get(Receipt_BankTransfer);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setBankTransfer(BankTransfer bankTransfer) {
        set(Receipt_BankTransfer, bankTransfer);
    }
}
