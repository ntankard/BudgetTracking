package com.ntankard.statementParser.processor;

import com.ntankard.statementParser.dataBase.*;
import com.ntankard.statementParser.dataBase.transactionGroup.Matching_TransactionGroup;
import com.ntankard.statementParser.dataBase.transactionGroup.Regex_TransactionGroup;
import com.ntankard.statementParser.dataBase.transactionGroup.Single_TransactionGroup;
import com.ntankard.statementParser.dataBase.transactionGroup.TransactionGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class StatementParser {

    public static void parseAllInstances(StatementFolder statementFolder, Map<StatementInstance, List<StatementInstanceLine>> instanceMap) {
        List<Transaction> transactions = findTransactions(instanceMap);

        Map<TransactionPeriod, List<Transaction>> split = new HashMap<>();
        for (Transaction transaction : transactions) {
            split.putIfAbsent(transaction.getTransactionPeriod(), new ArrayList<>());
            split.get(transaction.getTransactionPeriod()).add(transaction);
        }

        for (Map.Entry<TransactionPeriod, List<Transaction>> monthSet : split.entrySet()) {
            group(statementFolder, monthSet.getValue(), monthSet.getKey());
        }
    }

    public static void group(StatementFolder statementFolder, List<Transaction> transactions, TransactionPeriod transactionPeriod) {
        List<GroupRegex> groupRegexes = statementFolder.getBankAccount().getChildren(GroupRegex.class);
        List<Transaction> toFind = new ArrayList<>(transactions);

        Map<GroupRegex, TransactionGroup> groups = new HashMap<>();

        // Search the regex ones
        for (GroupRegex groupRegex : groupRegexes) {
            List<Transaction> toRemove = new ArrayList<>();
            for (Transaction transaction : toFind) {
                if (Pattern.matches(groupRegex.getRegex(), transaction.getDescription())) {
                    if (!groups.containsKey(groupRegex)) {
                        groups.put(groupRegex, new Regex_TransactionGroup(groupRegex, statementFolder, transactionPeriod).add());
                    }
                    transaction.setTransactionGroup(groups.get(groupRegex));
                    toRemove.add(transaction);
                }
            }
            toFind.removeAll(toRemove);
        }

        // Search for matching description
        List<Transaction> toCheck = new ArrayList<>(toFind);
        List<List<Transaction>> matched = new ArrayList<>();
        while (!toCheck.isEmpty()) {
            Transaction toCompare = toCheck.get(0);
            List<Transaction> possible = new ArrayList<>();
            possible.add(toCompare);

            for (Transaction transaction : toCheck) {
                if (!transaction.equals(toCompare)) {
                    if (transaction.getDescription().equals(toCompare.getDescription())) {
                        possible.add(transaction);
                    }
                }
            }

            toCheck.removeAll(possible);
            if (possible.size() > 1) {
                matched.add(possible);
            }
        }
        for (List<Transaction> grouped : matched) {
            Matching_TransactionGroup matching_transactionGroup = new Matching_TransactionGroup(statementFolder, grouped.get(0).getDescription(), transactionPeriod).add();
            for (Transaction transaction : grouped) {
                transaction.setTransactionGroup(matching_transactionGroup);
                toFind.remove(transaction);
            }
        }

        // The rest are individual
        if (toFind.size() != 0) {
            Single_TransactionGroup single_transactionGroup = new Single_TransactionGroup(statementFolder, "ALL", transactionPeriod).add();

            for (Transaction transaction : toFind) {
                transaction.setTransactionGroup(single_transactionGroup);
            }
        }
    }

    public static List<Transaction> findTransactions(Map<StatementInstance, List<StatementInstanceLine>> instanceMap) {
        List<StatementInstance> keys = new ArrayList<>(instanceMap.keySet());
        List<Transaction> transactions = new ArrayList<>();

        if (instanceMap.size() > 1) {
            throw new RuntimeException();
        }

        for (StatementInstance statementInstance : keys) {
            List<StatementInstanceLine> instances = instanceMap.get(statementInstance);

            for (StatementInstanceLine statementInstanceLine : instances) {
                // if (!knowTransactions.containsKey(statementInstanceLine.getRawLine())) {

                StringBuilder concat = new StringBuilder();
                for (String part : statementInstanceLine.getLine()) {
                    concat.append(",").append(part);
                }

                Transaction transaction = new Transaction(statementInstanceLine.getStatementInstance().getBankAccount(), statementInstanceLine.getTransactionPeriod(), statementInstanceLine.getDate(), statementInstanceLine.getDescription(), statementInstanceLine.getValue(), concat.toString()).add();
                //knowTransactions.put(transaction.getRawLine(), transaction);
                transactions.add(transaction);
                // }
                // Transaction transaction = knowTransactions.get(statementInstanceLine.getRawLine());
                if (transaction == null) {
                    throw new RuntimeException();
                }
                statementInstanceLine.setTransaction(transaction);
            }
        }
        return transactions;
    }
}
