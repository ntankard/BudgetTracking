package com.ntankard.Tracking.Dispaly.Util.Comparators;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;

import java.util.Comparator;

public class Ordered_Comparator implements Comparator<DataObject> {

    /**
     * {@inheritDoc
     */
    @Override
    public int compare(DataObject o1, DataObject o2) {
        if (((Ordered) o1).getOrder().equals(((Ordered) o2).getOrder())) {
            return 0;
        } else if (((Ordered) o1).getOrder() > ((Ordered) o2).getOrder()) {
            return 1;
        }
        return -1;
    }
}
