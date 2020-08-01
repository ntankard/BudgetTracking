package com.ntankard.Tracking.Dispaly.Util.LocaleInspectors;

import com.ntankard.dynamicGUI.Gui.Util.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;

import java.text.NumberFormat;

public class HalfTransfer_LocaleSource implements CurrencyDecoder_NumberFormatSource {

    /**
     * {@inheritDoc
     */
    @Override
    public NumberFormat getNumberFormat(Object rowObject, String contextName) {
        HalfTransfer moneyEvent = (HalfTransfer) rowObject;
        return moneyEvent.getCurrency().getNumberFormat();
    }
}
