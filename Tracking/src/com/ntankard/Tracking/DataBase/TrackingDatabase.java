package com.ntankard.Tracking.DataBase;

import com.ntankard.Tracking.DataBase.Core.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackingDatabase {

    // Core Containers
    private List<Currency> currencies       = new ArrayList<>();
    private List<Bank> banks                = new ArrayList<>();
    private List<Period> periods            = new ArrayList<>();
    private List<Statement> statements      = new ArrayList<>();
    private List<Transaction> transactions  = new ArrayList<>();

    // Other Containers
    private Map<String,Currency> currencyMap                = new HashMap<>();
    private Map<String,Bank> bankMap                        = new HashMap<>();
    private Map<String,Period> periodMap                    = new HashMap<>();
    private Map<Period, Map<Bank,Statement>> statementMap   = new HashMap<>();

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Populate Data ######################################################
    //------------------------------------------------------------------------------------------------------------------

    public void addCurrency(Currency currency){
        this.currencies.add(currency);
        this.currencyMap.put(currency.getId(),currency);
    }
    public void addBank(Bank bank){
        this.banks.add(bank);
        this.bankMap.put(bank.getId(),bank);
    }
    public void addPeriod(Period period){
        periods.add(period);
        statementMap.put(period,new HashMap<>());
        periodMap.put(period.getId(),period);
    }
    public void addStatement(Statement statement){
        statements.add(statement);
        statementMap.get(statement.getIdPeriod()).put(statement.getIdBank(),statement);
    }
    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Map accessors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    public Currency getCurrency(String currencyID) {
        return currencyMap.get(currencyID);
    }
    public Bank getBank(String bankId){
        return bankMap.get(bankId);
    }
    public Period getPeriod(String periodId) {
        return periodMap.get(periodId);
    }
    public Statement getStatement(Bank bankID, Period periodID){
        return statementMap.get(periodID).get(bankID);
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public List<Currency> getCurrencies() {
        return currencies;
    }
    public List<Bank> getBanks() {
        return banks;
    }
    public List<Period> getPeriods() {
        return periods;
    }
    public List<Statement> getStatements() {
        return statements;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }
}