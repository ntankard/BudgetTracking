package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.FileInterface;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class StatementDocument extends Displayable_DataObject implements FileInterface {

    private static final String StatementDocument_Prefix = "StatementDocument_";

    public static final String StatementDocument_StatementFolder = StatementDocument_Prefix + "StatementFolder";
    public static final String StatementDocument_FileName = StatementDocument_Prefix + "FileName";
    public static final String StatementDocument_ContainerPath = StatementDocument_Prefix + "ContainerPath";
    public static final String StatementDocument_FullPath = StatementDocument_Prefix + "FullPath";
    public static final String StatementDocument_PastInstance = StatementDocument_Prefix + "PastInstance";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(StatementDocument_StatementFolder, StatementFolder.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementDocument_FileName, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementDocument_ContainerPath, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementDocument_FullPath, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementDocument_PastInstance, StatementDocument.class, true));
        // Children

        // FullPath ====================================================================================================
        dataObjectSchema.<String>get(StatementDocument_FullPath).setDataCore_schema(
                new Derived_DataCore_Schema<String, StatementDocument>
                        (container -> container.getTrackingDatabase().getFilesPath() + "\\" + container.getContainerPath() + "\\" + container.getFileName()
                                , makeSourceChain(StatementDocument_ContainerPath)
                                , makeSourceChain(StatementDocument_FileName)));
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(StatementDocument.class);
    }

    /**
     * Constructor
     */
    public StatementDocument(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public StatementDocument(StatementFolder statementFolder, String fileName, String containerPath, StatementDocument pastInstance) {
        super(statementFolder.getTrackingDatabase()
                , StatementDocument_StatementFolder, statementFolder
                , StatementDocument_FileName, fileName
                , StatementDocument_ContainerPath, containerPath
                , StatementDocument_PastInstance, pastInstance
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
        return getContainerPath() + "\\" + getFileName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Implementations #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public String getFileName() {
        return get(StatementDocument_FileName);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getContainerPath() {
        return get(StatementDocument_ContainerPath);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getFullPath() {
        return get(StatementDocument_FullPath);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementFolder getStatementFolder() {
        return get(StatementDocument_StatementFolder);
    }

    public StatementDocument getPastInstance() {
        return get(StatementDocument_PastInstance);
    }
}
