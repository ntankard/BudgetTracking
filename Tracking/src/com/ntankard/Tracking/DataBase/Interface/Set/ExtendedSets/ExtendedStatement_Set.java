package com.ntankard.Tracking.DataBase.Interface.Set.ExtendedSets;

import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Interface.ClassExtension.ExtendedStatement;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;

import java.util.ArrayList;
import java.util.List;

public class ExtendedStatement_Set implements ObjectSet<ExtendedStatement> {

    // The Statement set for a period
    private Children_Set<Statement, Period> coreSet;

    /**
     * Constructor
     */
    public ExtendedStatement_Set(Period core) {
        this.coreSet = new Children_Set<>(Statement.class, core);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<ExtendedStatement> get() {
        List<ExtendedStatement> extendedStatements = new ArrayList<>();

        for (Statement statement : coreSet.get()) {
            extendedStatements.add(new ExtendedStatement(statement.getPeriod(), statement.getBank(), statement));
        }

        return extendedStatements;
    }
}
