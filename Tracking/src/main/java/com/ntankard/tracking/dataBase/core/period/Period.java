package com.ntankard.tracking.dataBase.core.period;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.interfaces.summary.Period_Summary;
import com.ntankard.tracking.dataBase.interfaces.summary.pool.Category_Summary;
import com.ntankard.tracking.dataBase.interfaces.summary.pool.FundEvent_Summary;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;

public abstract class Period extends DataObject implements Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = DataObject.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.addObjectFactory(Period_Summary.Factory);
        dataObjectSchema.addObjectFactory(Category_Summary.Factory);
        dataObjectSchema.addObjectFactory(FundEvent_Summary.Factory);

        // ID
        // Parents
        // Children

        return dataObjectSchema.endLayer(Period.class);
    }

    /**
     * Constructor
     */
    public Period(Database database) {
        super(database);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Speciality ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Dose this period exist within this range of time?
     *
     * @param start    The start of the range
     * @param duration The duration of the range (in months)
     * @return True if this period is withing the range
     */
    public boolean isWithin(Period start, Integer duration) {
        int diff = this.getOrder() - start.getOrder();
        return diff >= 0 && diff < duration;
    }
}
