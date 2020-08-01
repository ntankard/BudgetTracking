package com.ntankard.Tracking.Dispaly.Util.LocaleInspectors;

import com.ntankard.dynamicGUI.Gui.Util.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;

import java.text.NumberFormat;

public class SetTransfer_LocaleSource implements CurrencyDecoder_NumberFormatSource {

    /**
     * {@inheritDoc
     */
    @Override
    public NumberFormat getNumberFormat(Object rowObject, String contextName) {
        Transfer moneyEvent = (Transfer) rowObject;
        switch (contextName) {
            case "Value":
                return moneyEvent.toChaneGetSourceTransfer().getCurrency().getNumberFormat();
            case "DestinationValue":
                return moneyEvent.toChangeGetDestinationTransfer().getCurrency().getNumberFormat();
        }
        throw new RuntimeException("Unknown field");
    }
}
