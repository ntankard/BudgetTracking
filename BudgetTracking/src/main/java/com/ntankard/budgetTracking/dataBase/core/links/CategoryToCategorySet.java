package com.ntankard.budgetTracking.dataBase.core.links;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataField.validator.shared.Multi_FieldValidator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dataBase.core.CategorySet;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;

import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.budgetTracking.dataBase.core.CategorySet.CategorySet_Order;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class CategoryToCategorySet extends DataObject implements Ordered {

    public interface CategoryToCategorySetList extends List<CategoryToCategorySet> {
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String CategoryToCategorySet_CategorySet = "getCategorySet";
    public static final String CategoryToCategorySet_SolidCategory = "getSolidCategory";
    public static final String CategoryToCategorySet_OrderImpl = "getOrderImpl";
    public static final String CategoryToCategorySet_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        Multi_FieldValidator<CategoryToCategorySet> sharedFilter = new Multi_FieldValidator<>(
                (newValues, pastValues, container) ->
                        !((CategorySet) newValues[1]).getUsedCategories().contains((SolidCategory) newValues[0]) || newValues[0].equals(container.getSolidCategory()),
                "CategoryToCategorySet", CategoryToCategorySet_SolidCategory, CategoryToCategorySet_CategorySet);

        // ID
        // CategorySet ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToCategorySet_CategorySet, CategorySet.class));
        dataObjectSchema.get(CategoryToCategorySet_CategorySet).addValidator(sharedFilter.getValidator(CategoryToCategorySet_CategorySet));
        // SolidCategory ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToCategorySet_SolidCategory, SolidCategory.class));
        dataObjectSchema.get(CategoryToCategorySet_SolidCategory).setManualCanEdit(true);
        dataObjectSchema.get(CategoryToCategorySet_SolidCategory).addValidator(sharedFilter.getValidator(CategoryToCategorySet_SolidCategory));
        // OrderImpl ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToCategorySet_OrderImpl, Integer.class));
        dataObjectSchema.get(CategoryToCategorySet_OrderImpl).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(CategoryToCategorySet_OrderImpl).setManualCanEdit(true);
        // Order ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToCategorySet_Order, Integer.class));
        dataObjectSchema.get(CategoryToCategorySet_Order).setDataCore_schema(
                new Derived_DataCore_Schema<>
                        (container -> ((CategoryToCategorySet) container).getCategorySet().getOrder() * 1000 + ((CategoryToCategorySet) container).getOrderImpl()
                                , makeSourceChain(CategoryToCategorySet_OrderImpl)
                                , makeSourceChain(CategoryToCategorySet_CategorySet, CategorySet_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(CategoryToCategorySet.class);
    }

    /**
     * Constructor
     */
    public CategoryToCategorySet(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public CategoryToCategorySet(CategorySet categorySet, SolidCategory solidCategory, Integer orderImpl) {
        this(categorySet.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , CategoryToCategorySet_CategorySet, categorySet
                , CategoryToCategorySet_SolidCategory, solidCategory
                , CategoryToCategorySet_OrderImpl, orderImpl
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("SolidCategory")) {
            List<T> toReturn = super.sourceOptions(type, fieldName);
            toReturn.removeAll(getCategorySet().getUsedCategories());
            toReturn.add((T) getSolidCategory());
            return toReturn;
        }
        return super.sourceOptions(type, fieldName);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove() {
        super.remove_impl();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public CategorySet getCategorySet() {
        return get(CategoryToCategorySet_CategorySet);
    }

    public SolidCategory getSolidCategory() {
        return get(CategoryToCategorySet_SolidCategory);
    }

    public Integer getOrderImpl() {
        return get(CategoryToCategorySet_OrderImpl);
    }

    @Override
    public Integer getOrder() {
        return get(CategoryToCategorySet_Order);
    }
}