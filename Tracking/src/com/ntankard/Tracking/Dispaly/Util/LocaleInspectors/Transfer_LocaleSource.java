package com.ntankard.Tracking.Dispaly.Util.LocaleInspectors;

import com.ntankard.DynamicGUI.Util.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;

import java.text.NumberFormat;

public class Transfer_LocaleSource implements CurrencyDecoder_NumberFormatSource {

    /**
     * {@inheritDoc
     */
    @Override
    public NumberFormat getNumberFormat(Object rowObject, String contextName) {
        Transfer moneyEvent = (Transfer) rowObject;
        switch (contextName) {
            case "SourceValue":
                return moneyEvent.getSourceCurrency().getNumberFormat();
            case "DestinationValue":
                return moneyEvent.getDestinationCurrency().getNumberFormat();
        }
        throw new RuntimeException("Imposable value");
    }
}
