package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.TransactionLine.*;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.Translation.TranslationList;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.StatementBankTransfer;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.awt.*;
import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementDocument.StatementDocument_StatementFolder;
import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementFolder.StatementFolder_Bank;
import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementFolder.StatementFolder_TranslationTypes;
import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.TransactionLine.*;
import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.Translation.Translation_Original;
import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.Translation.Translation_Translated;
import static com.ntankard.budgetTracking.dataBase.core.pool.Bank.Bank_Currency;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.*;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class StatementTransaction extends Displayable_DataObject implements CurrencyBound {

    public interface StatementTransactionList extends List<StatementTransaction> {
    }

    private static final String StatementTransaction_Prefix = "StatementTransaction_";

    public static final String StatementTransaction_TransactionLines = StatementTransaction_Prefix + "TransactionLines";
    public static final String StatementTransaction_CoreLine = StatementTransaction_Prefix + "CoreLine";
    public static final String StatementTransaction_Value = StatementTransaction_Prefix + "Value";
    public static final String StatementTransaction_Description = StatementTransaction_Prefix + "Description";
    public static final String StatementTransaction_Translation = StatementTransaction_Prefix + "Translation";
    public static final String StatementTransaction_Currency = StatementTransaction_Prefix + "Currency";
    public static final String StatementTransaction_StatementBankTransfer = StatementTransaction_Prefix + "StatementBankTransfer";
    public static final String StatementTransaction_Bank = StatementTransaction_Prefix + "Bank";
    public static final String StatementTransaction_Period = StatementTransaction_Prefix + "Period";
    public static final String StatementTransaction_StatementFolder = StatementTransaction_Prefix + "StatementFolder";
    public static final String StatementTransaction_TranslationTypes = StatementTransaction_Prefix + "TranslationTypes";
    public static final String StatementTransaction_TranslationList = StatementTransaction_Prefix + "TranslationList";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new ListDataField_Schema<>(StatementTransaction_TransactionLines, TransactionLineList.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_CoreLine, TransactionLine.class, true));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Value, Double.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Description, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Translation, Translation.class, true));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Currency, Currency.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_StatementBankTransfer, StatementBankTransfer.class, true));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Bank, Bank.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Period, Period.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_StatementFolder, StatementFolder.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_TranslationTypes, TranslationTypes.class));
        dataObjectSchema.add(new ListDataField_Schema<>(StatementTransaction_TranslationList, TranslationList.class));
        // Children

        // TransactionLines ============================================================================================
        dataObjectSchema.get(StatementTransaction_TransactionLines).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<List<TransactionLine>>get(StatementTransaction_TransactionLines).setDataCore_schema(
                createSelfParentList(TransactionLine.class, null));
        // CoreLine ====================================================================================================
        dataObjectSchema.get(StatementTransaction_CoreLine).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<TransactionLine>get(StatementTransaction_CoreLine).setDataCore_schema(
                new Derived_DataCore_Schema<TransactionLine, StatementTransaction>
                        (container -> container.getTransactionLines().size() == 0 ? null : container.getTransactionLines().get(0)
                                , makeSourceChain(StatementTransaction_TransactionLines)));
        // Value =======================================================================================================
        dataObjectSchema.get(StatementTransaction_Value).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(StatementTransaction_Value).setDataCore_schema(
                createDirectDerivedDataCore(container -> 0.0,
                        StatementTransaction_CoreLine, TransactionLine_Value));
        // Description =================================================================================================
        dataObjectSchema.<String>get(StatementTransaction_Description).setDataCore_schema(
                createDirectDerivedDataCore(container -> "",
                        StatementTransaction_CoreLine, TransactionLine_Description));
        // Translation =================================================================================================
        dataObjectSchema.<Translation>get(StatementTransaction_Translation).setDataCore_schema(
                new Derived_DataCore_Schema<Translation, StatementTransaction>
                        (container -> {
                            for (Translation translation : container.getTranslationList()) {
                                // Does this rule apply to this StatementTransaction
                                boolean matched = false;
                                if (translation.getOriginal().contains("'")) { // OR
                                    String[] toCheck = translation.getOriginal().split("'");
                                    for (String check : toCheck) {
                                        if (container.getDescription().contains(check)) {
                                            matched = true;
                                            break;
                                        }
                                    }
                                } else if (translation.getOriginal().contains("~")) { // AND
                                    String[] toCheck = translation.getOriginal().split("~");
                                    matched = true;
                                    for (String check : toCheck) {
                                        if (!container.getDescription().contains(check)) {
                                            matched = false;
                                            break;
                                        }
                                    }
                                } else { // 1 only
                                    matched = container.getDescription().contains(translation.getOriginal());
                                }
                                if (matched) {
                                    return translation;
                                }
                            }
                            return null;
                        }
                                , Source_Factory.append(Source_Factory.makeSharedStepSourceChain(StatementTransaction_TranslationList, Translation_Original, Translation_Translated)
                                , makeSourceChain(StatementTransaction_Description))));
        // Currency ====================================================================================================
        dataObjectSchema.get(StatementTransaction_Currency).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<Currency>get(StatementTransaction_Currency).setDataCore_schema(
                createDirectDerivedDataCore(container -> container.getTrackingDatabase().getDefault(Currency.class),
                        StatementTransaction_CoreLine, TransactionLine_StatementDocument, StatementDocument_StatementFolder, StatementFolder_Bank, Bank_Currency));
        // StatementBankTransfer =======================================================================================
        dataObjectSchema.get(StatementTransaction_StatementBankTransfer).setManualCanEdit(true);
        // Bank ========================================================================================================
        dataObjectSchema.get(StatementTransaction_Bank).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<Bank>get(StatementTransaction_Bank).setDataCore_schema(
                createDirectDerivedDataCore(container -> container.getTrackingDatabase().getDefault(Bank.class),
                        StatementTransaction_CoreLine, TransactionLine_StatementDocument, StatementDocument_StatementFolder, StatementFolder_Bank));
        // Period ======================================================================================================
        dataObjectSchema.get(StatementTransaction_Period).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<Period>get(StatementTransaction_Period).setDataCore_schema(
                createDirectDerivedDataCore(container -> container.getTrackingDatabase().getDefault(Period.class),
                        StatementTransaction_CoreLine, TransactionLine_Period));
        // StatementFolder =============================================================================================
        dataObjectSchema.get(StatementTransaction_StatementFolder).getProperty(Display_Properties.class).setCustomColor((rowObject, value) -> ((StatementTransaction) rowObject).getPeriod() == ((StatementTransaction) rowObject).getStatementFolder().getPeriod() ? null : Color.ORANGE);
        // TranslationTypes ============================================================================================
        dataObjectSchema.get(StatementTransaction_TranslationTypes).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<TranslationTypes>get(StatementTransaction_TranslationTypes).setDataCore_schema(
                createDirectDerivedDataCore(container -> container.getTrackingDatabase().getDefault(TranslationTypes.class),
                        StatementTransaction_StatementFolder, StatementFolder_TranslationTypes));
        // TranslationList =============================================================================================
        dataObjectSchema.get(StatementTransaction_TranslationList).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<List<Translation>>get(StatementTransaction_TranslationList).setDataCore_schema(
                createMultiParentList(Translation.class, StatementTransaction_TranslationTypes));
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(StatementTransaction.class);
    }

    /**
     * Constructor
     */
    public StatementTransaction(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public StatementTransaction(StatementBankTransfer statementBankTransfer, StatementFolder statementFolder) {
        super(statementFolder.getTrackingDatabase()
                , StatementTransaction_StatementBankTransfer, statementBankTransfer
                , StatementTransaction_StatementFolder, statementFolder
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
        return getCoreLine().getRawLine();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public List<TransactionLine> getTransactionLines() {
        return get(StatementTransaction_TransactionLines);
    }

    public TransactionLine getCoreLine() {
        return get(StatementTransaction_CoreLine);
    }

    public Double getValue() {
        return get(StatementTransaction_Value);
    }

    public String getDescription() {
        return get(StatementTransaction_Description);
    }

    public Translation getTranslation() {
        return get(StatementTransaction_Translation);
    }

    public Currency getCurrency() {
        return get(StatementTransaction_Currency);
    }

    public StatementBankTransfer getStatementBankTransfer() {
        return get(StatementTransaction_StatementBankTransfer);
    }

    public Bank getBank() {
        return get(StatementTransaction_Bank);
    }

    public Period getPeriod() {
        return get(StatementTransaction_Period);
    }

    public StatementFolder getStatementFolder() {
        return get(StatementTransaction_StatementFolder);
    }

    public TranslationTypes getTranslationTypes() {
        return get(StatementTransaction_TranslationTypes);
    }

    public List<Translation> getTranslationList() {
        return get(StatementTransaction_TranslationList);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setStatementBankTransfer(StatementBankTransfer statementBankTransfer) {
        set(StatementTransaction_StatementBankTransfer, statementBankTransfer);
    }
}
