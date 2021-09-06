package com.ntankard.budgetTracking.display.util.localeInspectors;

import com.ntankard.dynamicGUI.gui.util.decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer;

import java.text.NumberFormat;

public class HalfTransfer_LocaleSource implements CurrencyDecoder_NumberFormatSource {

    /**
     * @inheritDoc
     */
    @Override
    public NumberFormat getNumberFormat(Object rowObject, String contextName) {
        HalfTransfer moneyEvent = (HalfTransfer) rowObject;
        return moneyEvent.getCurrency().getNumberFormat();
    }
}
