package com.ntankard.statementParser.dataBase;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.statementParser.dataBase.transactionGroup.TransactionGroup;

import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataContext.NOT_FALSE;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataContext.ZERO_SCALE;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY_YEN;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createMultiParentList;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createSelfParentList;
import static com.ntankard.statementParser.dataBase.Transaction.Transaction_Value;
import static com.ntankard.statementParser.dataBase.transactionGroup.TransactionGroup.TransactionGroup_Sum;

public class StatementFolder extends DataObject {

    private static final String StatementFolder_Prefix = "StatementFolder_";

    public static final String StatementFolder_BankAccount = StatementFolder_Prefix + "BankAccount";
    public static final String StatementFolder_TransactionPeriod = StatementFolder_Prefix + "TransactionPeriod";
    public static final String StatementFolder_Name = StatementFolder_Prefix + "Name";
    public static final String StatementFolder_Path = StatementFolder_Prefix + "Path";
    public static final String StatementFolder_TransactionGroups = StatementFolder_Prefix + "TransactionGroups";
    public static final String StatementFolder_TransactionGroupSum = StatementFolder_Prefix + "GroupSum";
    public static final String StatementFolder_PeriodTransactionGroups = StatementFolder_Prefix + "PeriodTransactionGroups";
    public static final String StatementFolder_PeriodTransactionGroupSum = StatementFolder_Prefix + "PeriodSum";
    public static final String StatementFolder_Transactions = StatementFolder_Prefix + "Transactions";
    public static final String StatementFolder_TransactionSum = StatementFolder_Prefix + "TransactionSum";
    public static final String StatementFolder_Delta = StatementFolder_Prefix + "Delta";
    public static final String StatementFolder_Valid = StatementFolder_Prefix + "Valid";
    public static final String StatementFolder_PastStatementFolder = StatementFolder_Prefix + "PastStatementFolder";
    public static final String StatementFolder_Balance = StatementFolder_Prefix + "Balance";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_BankAccount, BankAccount.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_TransactionPeriod, TransactionPeriod.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_Name, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_Path, String.class));
        dataObjectSchema.add(new ListDataField_Schema<>(StatementFolder_TransactionGroups, TransactionGroup.TransactionGroupList.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_TransactionGroupSum, Double.class));
        dataObjectSchema.add(new ListDataField_Schema<>(StatementFolder_PeriodTransactionGroups, TransactionGroup.TransactionGroupList.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_PeriodTransactionGroupSum, Double.class));
        dataObjectSchema.add(new ListDataField_Schema<>(StatementFolder_Transactions, Transaction.TransactionList.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_TransactionSum, Double.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_Balance, Double.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_Delta, Double.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_Valid, Boolean.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_PastStatementFolder, StatementFolder.class, true));
        // ChildrenField

        // StatementFolder_BankAccount =================================================================================
        dataObjectSchema.get(StatementFolder_BankAccount).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // StatementFolder_TransactionPeriod =================================================================================
        dataObjectSchema.get(StatementFolder_TransactionPeriod).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // StatementFolder_Path ========================================================================================
        dataObjectSchema.get(StatementFolder_Path).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // StatementFolder_TransactionGroups ===========================================================================
        dataObjectSchema.get(StatementFolder_TransactionGroups).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.<List<TransactionGroup>>get(StatementFolder_TransactionGroups).setDataCore_schema(createSelfParentList(TransactionGroup.class, null));
        // StatementFolder_TransactionGroupSum =========================================================================
        dataObjectSchema.get(StatementFolder_TransactionGroupSum).getProperty(Display_Properties.class).setDataType(CURRENCY_YEN);
        dataObjectSchema.<Double>get(StatementFolder_TransactionGroupSum).setDataCore_schema(
                new Derived_DataCore_Schema<Double, StatementFolder>(
                        container -> {
                            Double sum = 0.0;
                            for (TransactionGroup transactionGroup : container.getTransactionGroups()) {
                                sum += transactionGroup.getSum();
                            }
                            return sum;
                        }
                        , Source_Factory.makeSharedStepSourceChain(StatementFolder_TransactionGroups, TransactionGroup_Sum)));
        // StatementFolder_PeriodTransactionGroups ===========================================================================
        dataObjectSchema.get(StatementFolder_PeriodTransactionGroups).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.<List<TransactionGroup>>get(StatementFolder_PeriodTransactionGroups).setDataCore_schema(createMultiParentList(TransactionGroup.class, StatementFolder_BankAccount, StatementFolder_TransactionPeriod));
        // StatementFolder_PeriodTransactionGroupSum =========================================================================
        dataObjectSchema.get(StatementFolder_PeriodTransactionGroupSum).getProperty(Display_Properties.class).setDataType(CURRENCY_YEN);
        dataObjectSchema.<Double>get(StatementFolder_PeriodTransactionGroupSum).setDataCore_schema(
                new Derived_DataCore_Schema<Double, StatementFolder>(
                        container -> {
                            Double sum = 0.0;
                            for (TransactionGroup transactionGroup : container.getPeriodTransactionGroups()) {
                                sum += transactionGroup.getSum();
                            }
                            return sum;
                        }
                        , Source_Factory.makeSharedStepSourceChain(StatementFolder_PeriodTransactionGroups, TransactionGroup_Sum)));
        // StatementFolder_Transactions ================================================================================
        dataObjectSchema.get(StatementFolder_Transactions).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.<List<Transaction>>get(StatementFolder_Transactions).setDataCore_schema(createSelfParentList(Transaction.class, null));
        // StatementFolder_TransactionSum ==============================================================================
        dataObjectSchema.get(StatementFolder_TransactionSum).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.get(StatementFolder_TransactionSum).getProperty(Display_Properties.class).setDataType(CURRENCY_YEN);
        dataObjectSchema.<Double>get(StatementFolder_TransactionSum).setDataCore_schema(
                new Derived_DataCore_Schema<Double, StatementFolder>(
                        container -> {
                            Double sum = 0.0;
                            for (Transaction transaction : container.getTransactions()) {
                                sum += transaction.getValue();
                            }
                            return sum;
                        }
                        , Source_Factory.makeSharedStepSourceChain(StatementFolder_Transactions, Transaction_Value)));
        // StatementFolder_Balance ================================================================================================
        dataObjectSchema.get(StatementFolder_Balance).getProperty(Display_Properties.class).setDataType(CURRENCY_YEN);
        dataObjectSchema.<Double>get(StatementFolder_Balance).setDataCore_schema(
                new Derived_DataCore_Schema<Double, StatementFolder>(
                        container -> {
                            Double start = 0.0;
                            Double payment = 0.0;
                            if(container.getPastStatementFolder() != null){
                                start = container.getPastStatementFolder().getBalance();
                                payment = container.getPastStatementFolder().getTransactionGroupSum();
                            }

                            return start + container.getPeriodTransactionGroupSum() - payment;
                        }
                        , Source_Factory.makeSourceChain(StatementFolder_PeriodTransactionGroupSum)
                        , Source_Factory.makeSourceChain(StatementFolder_PastStatementFolder, StatementFolder_Balance)
                        , Source_Factory.makeSourceChain(StatementFolder_PastStatementFolder, StatementFolder_TransactionGroupSum)));
        // StatementFolder_Delta ================================================================================================
        dataObjectSchema.get(StatementFolder_Delta).getProperty(Display_Properties.class).setDataType(CURRENCY_YEN);
        dataObjectSchema.get(StatementFolder_Delta).getProperty(Display_Properties.class).setDataContext(ZERO_SCALE);
        dataObjectSchema.<Double>get(StatementFolder_Delta).setDataCore_schema(
                new Derived_DataCore_Schema<Double, StatementFolder>
                        (dataObject -> dataObject.getPeriodTransactionGroupSum() - dataObject.getTransactionGroupSum()
                                , new End_Source_Schema<>(StatementFolder_TransactionGroupSum)
                                , new End_Source_Schema<>(StatementFolder_PeriodTransactionGroupSum)));
        // StatementFolder_Delta ================================================================================================
        dataObjectSchema.get(StatementFolder_Valid).getProperty(Display_Properties.class).setDataContext(NOT_FALSE);
        dataObjectSchema.<Boolean>get(StatementFolder_Valid).setDataCore_schema(
                new Derived_DataCore_Schema<Boolean, StatementFolder>
                        (dataObject -> dataObject.getTransactionSum().equals(dataObject.getTransactionGroupSum())
                                , new End_Source_Schema<>(StatementFolder_TransactionGroupSum)
                                , new End_Source_Schema<>(StatementFolder_TransactionSum)));

        return dataObjectSchema.finaliseContainer(StatementFolder.class);
    }

    /**
     * Constructor
     */
    public StatementFolder(BankAccount bankAccount, TransactionPeriod transactionPeriod, String name, String path, StatementFolder statementFolder) {
        super(bankAccount.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , StatementFolder_BankAccount, bankAccount
                , StatementFolder_TransactionPeriod, transactionPeriod
                , StatementFolder_Name, name
                , StatementFolder_Path, path
                , StatementFolder_PastStatementFolder, statementFolder
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
        return getName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public TransactionPeriod getTransactionPeriod() {
        return get(StatementFolder_TransactionPeriod);
    }

    public BankAccount getBankAccount() {
        return get(StatementFolder_BankAccount);
    }

    public String getName() {
        return get(StatementFolder_Name);
    }

    public String getPath() {
        return get(StatementFolder_Path);
    }

    public List<Transaction> getTransactions() {
        return get(StatementFolder_Transactions);
    }

    public List<TransactionGroup> getTransactionGroups() {
        return get(StatementFolder_TransactionGroups);
    }

    public List<TransactionGroup> getPeriodTransactionGroups() {
        return get(StatementFolder_PeriodTransactionGroups);
    }

    public Double getTransactionGroupSum() {
        return get(StatementFolder_TransactionGroupSum);
    }

    public Double getPeriodTransactionGroupSum() {
        return get(StatementFolder_PeriodTransactionGroupSum);
    }

    public Double getTransactionSum() {
        return get(StatementFolder_TransactionSum);
    }

    public StatementFolder getPastStatementFolder(){
        return get(StatementFolder_PastStatementFolder);
    }

    public Double getBalance(){
        return get(StatementFolder_Balance);
    }

}
