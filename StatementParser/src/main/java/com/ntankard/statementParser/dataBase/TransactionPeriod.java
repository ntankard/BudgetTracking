package com.ntankard.statementParser.dataBase;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.Date;

public class TransactionPeriod extends DataObject implements Ordered {

    private static final String TransactionPeriod_Prefix = "TransactionPeriod_";

    public static final String TransactionPeriod_Year = TransactionPeriod_Prefix + "Year";
    public static final String TransactionPeriod_Month = TransactionPeriod_Prefix + "Month";
    public static final String TransactionPeriod_DateString = TransactionPeriod_Prefix + "DateString";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(TransactionPeriod_Year, Integer.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionPeriod_Month, Integer.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionPeriod_DateString, String.class));
        // ChildrenField

        // TransactionPeriod_DateString ================================================================================
        dataObjectSchema.<String>get(TransactionPeriod_DateString).setDataCore_schema(
                new Derived_DataCore_Schema<String, TransactionPeriod>
                        (dataObject -> getNameString(dataObject.getYear(), dataObject.getMonth())
                                , new End_Source_Schema<>(TransactionPeriod_Month)
                                , new End_Source_Schema<>(TransactionPeriod_Year)));
        // =============================================================================================================

        return dataObjectSchema.finaliseContainer(TransactionPeriod.class);
    }

    /**
     * Constructor
     */
    public TransactionPeriod(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public TransactionPeriod(Database database, Integer year, Integer month) {
        super(database
                , TransactionPeriod_Year, year
                , TransactionPeriod_Month, month
        );
    }

    /**
     * Constructor
     */
    public TransactionPeriod(Database database, Date date) {
        super(database
                , TransactionPeriod_Year, date.getYear() - 100
                , TransactionPeriod_Month, date.getMonth() + 1
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
        return getDateString();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Integer getOrder() {
        return getYear() * 20 + getMonth();
    }

    public static String getNameString(Integer year, Integer month){
        return  year + "-" + String.format("%02d" , month);
    }

    public static String getNameString(Date date){
        return TransactionPeriod.getNameString(date.getYear() - 100, date.getMonth() + 1);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Integer getYear() {
        return get(TransactionPeriod_Year);
    }

    public Integer getMonth() {
        return get(TransactionPeriod_Month);
    }

    public String getDateString() {
        return get(TransactionPeriod_DateString);
    }
}
