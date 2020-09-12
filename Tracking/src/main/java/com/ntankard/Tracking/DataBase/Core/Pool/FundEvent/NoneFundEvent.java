package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

public class NoneFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundEvent.getFieldContainer();

        // ID
        // Name
        // Category
        // Parents
        // Children

        return fieldContainer.finaliseContainer(NoneFundEvent.class);
    }

    /**
     * Create a new SavingsFundEvent object
     */
    public static NoneFundEvent make(Integer id, String name, SolidCategory solidCategory) {
        return assembleDataObject(NoneFundEvent.getFieldContainer(), new NoneFundEvent()
                , DataObject_Id, id
                , NamedDataObject_Name, name
                , FundEvent_Category, solidCategory
        );
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        super.remove_impl();
    }
}
