package com.ntankard.Tracking.DataBase.Interface.Set.ExtendedSets;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.ClassExtension.ExtendedStatement;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;

import java.util.ArrayList;
import java.util.List;

public class ExtendedStatement_Set implements ObjectSet<ExtendedStatement> {

    private Period core;

    /**
     * Constructor
     */
    public ExtendedStatement_Set(Period core) {
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<ExtendedStatement> get() {
        List<ExtendedStatement> extendedStatements = new ArrayList<>();

        for (Bank bank : TrackingDatabase.get().get(Bank.class)) {
            extendedStatements.add(new ExtendedStatement(core, bank));
        }

        return extendedStatements;
    }
}
