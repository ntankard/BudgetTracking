package com.ntankard.Tracking.Dispaly.Util;

import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.CurrencyDecoder_LocaleSource;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;

import java.util.Locale;

public class MoneyEventLocaleInspector implements CurrencyDecoder_LocaleSource {

    /**
     * {@inheritDoc
     */
    @Override
    public Locale getLocale(Object rowObject) {
        MoneyEvent moneyEvent = (MoneyEvent) rowObject;
        if (moneyEvent.getCurrency().getId().equals("YEN")) {
            return Locale.JAPAN;
        }
        return Locale.US;
    }
}
