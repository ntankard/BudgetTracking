package com.ntankard.tracking.dispaly.util.localeInspectors;

import com.ntankard.dynamicGUI.gui.util.decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.tracking.dataBase.core.transfer.Transfer;

import java.text.NumberFormat;

public class SetTransfer_LocaleSource implements CurrencyDecoder_NumberFormatSource {

    /**
     * @inheritDoc
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
