package com.ntankard.Tracking.Dispaly.Util.LocaleInspectors;

import com.ntankard.DynamicGUI.Util.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.Tracking.DataBase.Core.CurrencyBound;

import java.text.NumberFormat;

public class CurrencyBound_LocaleSource implements CurrencyDecoder_NumberFormatSource {

    /**
     * {@inheritDoc
     */
    @Override
    public NumberFormat getNumberFormat(Object rowObject) {
        CurrencyBound moneyEvent = (CurrencyBound) rowObject;
        return moneyEvent.getCurrency().getNumberFormat();
    }
}