package com.ntankard.budgetTracking.display.util.localeInspectors;

import com.ntankard.budgetTracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.dynamicGUI.gui.util.decoder.CurrencyDecoder_NumberFormatSource;

import java.text.NumberFormat;

public class CurrencyBound_LocaleSource implements CurrencyDecoder_NumberFormatSource {

    /**
     * @inheritDoc
     */
    @Override
    public NumberFormat getNumberFormat(Object rowObject, String contextName) {
        CurrencyBound moneyEvent = (CurrencyBound) rowObject;
        return moneyEvent.getCurrency().getNumberFormat();
    }
}