package com.ntankard.budgetTracking.dataBase.core.fileManagement;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.FileInterface;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class UnusedFile extends DataObject implements FileInterface {

    private static final String UnusedFile_Prefix = "UnusedFile_";

    public static final String UnusedFile_FileName = UnusedFile_Prefix + "FileName";
    public static final String UnusedFile_ContainerPath = UnusedFile_Prefix + "ContainerPath";
    public static final String UnusedFile_FullPath = UnusedFile_Prefix + "FullPath";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(UnusedFile_FileName, String.class));
        dataObjectSchema.add(new DataField_Schema<>(UnusedFile_ContainerPath, String.class));
        dataObjectSchema.add(new DataField_Schema<>(UnusedFile_FullPath, String.class));
        // Children

        // FullPath ====================================================================================================
        dataObjectSchema.<String>get(UnusedFile_FullPath).setDataCore_schema(
                new Derived_DataCore_Schema<String, UnusedFile>
                        (container -> container.getTrackingDatabase().getFilesPath() + "\\" + container.getContainerPath() + "\\" + container.getFileName()
                                , makeSourceChain(UnusedFile_ContainerPath)
                                , makeSourceChain(UnusedFile_FileName)));
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(UnusedFile.class);
    }

    /**
     * Constructor
     */
    public UnusedFile(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public UnusedFile(Database database, String fileName, String containerPath) {
        this(database);
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , UnusedFile_FileName, fileName
                , UnusedFile_ContainerPath, containerPath
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
        return getContainerPath() + "//" + getFileName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Implementations #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public String getFileName() {
        return get(UnusedFile_FileName);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getContainerPath() {
        return get(UnusedFile_ContainerPath);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getFullPath() {
        return get(UnusedFile_FullPath);
    }
}
