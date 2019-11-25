package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class CategoryTransfer extends Transfer<Category, Category> {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getPeriod", "getSource", "getDestination", "getCurrency"})
    public CategoryTransfer(Integer id, String description, Double value, Period period, Category source, Category destination, Currency currency) {
        super(id, description, value, period, source, destination, currency);
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Destination")) {
            List<T> toReturn = new ArrayList<>(super.sourceOptions(type, fieldName));
            toReturn.remove(getSource());
            return toReturn;
        }
        return super.sourceOptions(type, fieldName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 3)
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @DisplayProperties(order = 6)
    public Category getSource() {
        return super.getSource();
    }

    @Override
    @DisplayProperties(order = 9)
    public Category getDestination() {
        return super.getDestination();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSource(Category source) {
        this.source.notifyChildUnLink(this);
        this.source = source;

        List<Category> options = sourceOptions(Category.class, "Destination");
        if (getSource().equals(getDestination()) || !options.contains(getDestination())) {
            setDestination(options.get(0));
        }

        this.source.notifyChildLink(this);
    }

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestination(Category destination) {
        super.setDestination(destination);
    }
}
