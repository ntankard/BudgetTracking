package com.ntankard.Tracking.Dispaly.Util;

import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.CurrencyDecoder_LocaleSource;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;

import java.util.Locale;

public class StatementLocaleInspector implements CurrencyDecoder_LocaleSource {

    /**
     * {@inheritDoc
     */
    @Override
    public Locale getLocale(Object rowObject) {
        Statement statement = (Statement) rowObject;
        if (statement.getIdBank().getCurrency().getId().equals("YEN")) {
            return Locale.JAPAN;
        }
        return Locale.US;
    }
}
