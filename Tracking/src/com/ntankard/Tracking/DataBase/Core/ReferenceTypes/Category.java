package com.ntankard.Tracking.DataBase.Core.ReferenceTypes;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Category extends DataObject {

    // My values
    private String id;
    private int order;

    /**
     * Constructor
     */
    public Category(String id, int order) {
        this.id = id;
        this.order = order;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public int getOrder() {
        return order;
    }
}