package com.ntankard.statementParser.dataBase.transactionGroup;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.statementParser.dataBase.BankAccount;
import com.ntankard.statementParser.dataBase.StatementFolder;
import com.ntankard.statementParser.dataBase.Transaction;
import com.ntankard.statementParser.dataBase.TransactionPeriod;

import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY_YEN;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createSelfParentList;
import static com.ntankard.statementParser.dataBase.StatementFolder.StatementFolder_BankAccount;
import static com.ntankard.statementParser.dataBase.Transaction.Transaction_Value;

public abstract class TransactionGroup extends DataObject implements Ordered {

    public interface TransactionGroupList extends List<TransactionGroup> {
    }

    private static final String TransactionGroup_Prefix = "TransactionGroup_";

    public static final String TransactionGroup_StatementFolder = TransactionGroup_Prefix + "StatementFolder";
    public static final String TransactionGroup_TransactionPeriod = TransactionGroup_Prefix + "TransactionPeriod";
    public static final String TransactionGroup_Sum = TransactionGroup_Prefix + "Sum";
    public static final String TransactionGroup_Count = TransactionGroup_Prefix + "Count";
    public static final String TransactionGroup_Name = TransactionGroup_Prefix + "Name";
    public static final String TransactionGroup_Transactions = TransactionGroup_Prefix + "Transactions";
    public static final String TransactionGroup_BankAccount = TransactionGroup_Prefix + "BankAccount";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(TransactionGroup_StatementFolder, StatementFolder.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionGroup_TransactionPeriod, TransactionPeriod.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionGroup_Sum, Double.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionGroup_Count, Integer.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionGroup_Name, String.class));
        dataObjectSchema.add(new ListDataField_Schema<>(TransactionGroup_Transactions, Transaction.TransactionList.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionGroup_BankAccount, BankAccount.class));
        // ChildrenField

        // TransactionGroup_StatementFolder ============================================================================
        dataObjectSchema.get(TransactionGroup_StatementFolder).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // TransactionGroup_Sum ========================================================================================
        dataObjectSchema.get(TransactionGroup_Sum).getProperty(Display_Properties.class).setDataType(CURRENCY_YEN);
        dataObjectSchema.<Double>get(TransactionGroup_Sum).setDataCore_schema(
                new Derived_DataCore_Schema<Double, TransactionGroup>(
                        container -> {
                            Double sum = 0.0;
                            for (Transaction transaction : container.getTransactions()) {
                                sum += transaction.getValue();
                            }
                            return sum;
                        }
                        , Source_Factory.makeSharedStepSourceChain(TransactionGroup_Transactions, Transaction_Value)));
        // TransactionGroup_Count ======================================================================================
        dataObjectSchema.<Integer>get(TransactionGroup_Count).setDataCore_schema(
                new Derived_DataCore_Schema<Integer, TransactionGroup>(
                        container -> container.getTransactions().size()
                        , Source_Factory.makeSharedStepSourceChain(TransactionGroup_Transactions, Transaction_Value)));
        // TransactionGroup_Name =======================================================================================
        dataObjectSchema.get(TransactionGroup_Name).setManualCanEdit(true);
        // TransactionGroup_Transactions ===============================================================================
        dataObjectSchema.<List<Transaction>>get(TransactionGroup_Transactions).setDataCore_schema(createSelfParentList(Transaction.class, null));
        // TransactionGroup_BankAccount ================================================================================
        dataObjectSchema.<BankAccount>get(TransactionGroup_BankAccount).setDataCore_schema(createDirectDerivedDataCore(TransactionGroup_StatementFolder, StatementFolder_BankAccount));
        // =============================================================================================================

        return dataObjectSchema.endLayer(TransactionGroup.class);
    }

    /**
     * Constructor
     */
    public TransactionGroup(Database database, Object... args) {
        super(database, args);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### General #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public Integer getOrder() {
        return -(int) Math.round(getSum());
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementFolder getStatementFolder() {
        return get(TransactionGroup_StatementFolder);
    }

    public List<Transaction> getTransactions() {
        return get(TransactionGroup_Transactions);
    }

    public Double getSum() {
        return get(TransactionGroup_Sum);
    }
}
