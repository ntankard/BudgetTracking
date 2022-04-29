package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

public class Translation extends DataObject {

    public interface TranslationList extends List<Translation> {
    }

    private static final String Translation_Prefix = "Translation_";

    public static final String Translation_Original = Translation_Prefix + "Original";
    public static final String Translation_Translated = Translation_Prefix + "Translated";
    public static final String Translation_TranslationTypes = Translation_Prefix + "TranslationTypes";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(Translation_Original, String.class));
        dataObjectSchema.add(new DataField_Schema<>(Translation_Translated, String.class));
        dataObjectSchema.add(new DataField_Schema<>(Translation_TranslationTypes, TranslationTypes.class));
        // Children

        // Original ====================================================================================================
        dataObjectSchema.get(Translation_Original).setManualCanEdit(true);
        // Translated ==================================================================================================
        dataObjectSchema.get(Translation_Translated).setManualCanEdit(true);
        // TranslationTypes ============================================================================================
        dataObjectSchema.get(Translation_TranslationTypes).setManualCanEdit(true);
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(Translation.class);
    }

    /**
     * Constructor
     */
    public Translation(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public Translation(Database database, String original, String translated, TranslationTypes translationTypes) {
        super(database
                , Translation_Original, original
                , Translation_Translated, translated
                , Translation_TranslationTypes, translationTypes
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
        return getTranslated() + "  -  " + getOriginal();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getOriginal() {
        return get(Translation_Original);
    }

    public String getTranslated() {
        return get(Translation_Translated);
    }

    public TranslationTypes getTranslationTypes() {
        return get(Translation_TranslationTypes);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setOriginal(String original) {
        set(Translation_Original, original);
    }

    public void setTranslated(String translated) {
        set(Translation_Translated, translated);
    }

    public void setTranslationTypes(TranslationTypes translationTypes) {
        set(Translation_TranslationTypes, translationTypes);
    }
}
