package com.ntankard.Tracking.Dispaly.Frames.MainFrame.DatabaseLists;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Bank;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class ReferenceTypesPanel extends UpdatableJPanel {

    // The data displayed (clone of the data in the database)
    private List<Currency> currency_list = new ArrayList<>();
    private List<Category> category_list = new ArrayList<>();
    private List<Bank> bank_list = new ArrayList<>();
    private List<FundEvent> fundEvent_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Category> category_panel;
    private DynamicGUI_DisplayList<Currency> currency_panel;
    private DynamicGUI_DisplayList<Bank> bank_panel;
    private DynamicGUI_DisplayList<FundEvent> fundEvent_panel;

    /**
     * Constructor
     */
    public ReferenceTypesPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        // The GUI components
        category_panel = DynamicGUI_DisplayList.newIntractableTable(category_list, new MemberClass(Category.class), true, ALWAYS_DISPLAY, this);
        currency_panel = DynamicGUI_DisplayList.newIntractableTable(currency_list, new MemberClass(Currency.class), true, ALWAYS_DISPLAY, this);
        bank_panel = DynamicGUI_DisplayList.newIntractableTable(bank_list, new MemberClass(Bank.class), true, ALWAYS_DISPLAY, this);
        fundEvent_panel = DynamicGUI_DisplayList.newIntractableTable(fundEvent_list, new MemberClass(FundEvent.class), true, ALWAYS_DISPLAY, this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Category", category_panel);
        master_tPanel.addTab("Currency", currency_panel);
        master_tPanel.addTab("Bank", bank_panel);
        master_tPanel.addTab("Non Period Fund Event", fundEvent_panel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        category_list.clear();
        currency_list.clear();
        bank_list.clear();
        fundEvent_list.clear();

        category_list.addAll(TrackingDatabase.get().get(Category.class));
        currency_list.addAll(TrackingDatabase.get().get(Currency.class));
        bank_list.addAll(TrackingDatabase.get().get(Bank.class));
        fundEvent_list.addAll(TrackingDatabase.get().get(FundEvent.class));

        category_panel.update();
        currency_panel.update();
        bank_panel.update();
        fundEvent_panel.update();
    }
}
