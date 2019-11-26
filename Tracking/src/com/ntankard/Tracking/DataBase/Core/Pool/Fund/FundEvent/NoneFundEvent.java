package com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;

import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

public class NoneFundEvent extends FundEvent {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getFund"})
    public NoneFundEvent(Integer id, String name, Fund fund) {
        super(id, name, fund);
    }
}
