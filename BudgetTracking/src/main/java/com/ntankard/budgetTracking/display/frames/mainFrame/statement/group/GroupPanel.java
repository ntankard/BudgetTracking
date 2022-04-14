package com.ntankard.budgetTracking.display.frames.mainFrame.statement.group;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransactionTranslationAutoGroup;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.Translation;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.display.util.elementControllers.Translation_ElementController;
import com.ntankard.budgetTracking.display.util.panels.DataObject_DisplayList;
import com.ntankard.dynamicGUI.gui.containers.DynamicGUI_DisplayList.ListControl_Button;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.Full_Set;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;
import com.ntankard.javaObjectDatabase.util.set.SetFilter;

import java.awt.*;
import java.util.List;

public class GroupPanel extends UpdatableJPanel {

    // The GUI components
    private DataObject_DisplayList<Bank> bank_panel;
    private DataObject_DisplayList<Translation> nonMappedTranslation_panel;
    private DataObject_DisplayList<Translation> nonBankTranslation_panel;
    private DataObject_DisplayList<StatementTransactionTranslationAutoGroup> autoGroup_panel;
    private OneParent_Children_Set<StatementTransactionTranslationAutoGroup, Bank> autoGroup_set;

    // Core database
    private final Database database;

    // Buffered selection data for fast access for buttons
    private Bank selectedBank = null;

    /**
     * Constructor
     */
    public GroupPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        bank_panel = new DataObject_DisplayList<>(database.getSchema(), Bank.class, new Full_Set<>(database, Bank.class,
                new SetFilter<Bank>(null) {
                    @Override
                    protected boolean shouldAdd_Impl(Bank dataObject) {
                        return dataObject.getChildren(StatementTransactionTranslationAutoGroup.class).size() != 0;
                    }
                }),
                false, this);
        bank_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> {
            selectBank();
            nonMappedTranslation_panel.update();
            nonBankTranslation_panel.update();
        });

        nonMappedTranslation_panel = new DataObject_DisplayList<>(database.getSchema(), Translation.class, new Full_Set<>(database, Translation.class,
                new SetFilter<Translation>(null) {
                    @Override
                    protected boolean shouldAdd_Impl(Translation dataObject) {
                        if (selectedBank == null) {
                            return false;
                        }
                        return dataObject.getChildren(StatementTransactionTranslationAutoGroup.class).size() == 0;
                    }
                })
                , false, this);
        nonMappedTranslation_panel.addControlButtons(new Translation_ElementController(database, this));
        ListControl_Button<?> nonMappedTranslation_btn = new ListControl_Button<>("Link", nonMappedTranslation_panel, ListControl_Button.EnableCondition.SINGLE, false);
        nonMappedTranslation_btn.addActionListener(e -> {
            new StatementTransactionTranslationAutoGroup(selectedBank, nonMappedTranslation_panel.getMainPanel().getSelectedItems().get(0), database.getDefault(SolidCategory.class), 1.0).add();
            notifyUpdate();
        });
        nonMappedTranslation_panel.addButton(nonMappedTranslation_btn);

        nonBankTranslation_panel = new DataObject_DisplayList<>(database.getSchema(), Translation.class, new Full_Set<>(database, Translation.class,
                new SetFilter<Translation>(null) {
                    @Override
                    protected boolean shouldAdd_Impl(Translation dataObject) {
                        if (selectedBank == null) {
                            return false;
                        }
                        List<StatementTransactionTranslationAutoGroup> children = dataObject.getChildren(StatementTransactionTranslationAutoGroup.class);
                        for (StatementTransactionTranslationAutoGroup group : children) {
                            if (group.getBank() == selectedBank) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                , false, this);
        nonBankTranslation_panel.addControlButtons(new Translation_ElementController(database, this));
        ListControl_Button<?> nonBankTranslation_btn = new ListControl_Button<>("Link", nonBankTranslation_panel, ListControl_Button.EnableCondition.SINGLE, false);
        nonBankTranslation_btn.addActionListener(e -> {
            new StatementTransactionTranslationAutoGroup(selectedBank, nonBankTranslation_panel.getMainPanel().getSelectedItems().get(0), database.getDefault(SolidCategory.class), 1.0).add();
            notifyUpdate();
        });
        nonBankTranslation_panel.addButton(nonBankTranslation_btn);

        autoGroup_set = new OneParent_Children_Set<>(StatementTransactionTranslationAutoGroup.class, null);
        autoGroup_panel = new DataObject_DisplayList<>(database.getSchema(), StatementTransactionTranslationAutoGroup.class, autoGroup_set, false, this);

        GridBagConstraints summaryContainer_C = new GridBagConstraints();
        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;
        summaryContainer_C.weighty = 1;

        summaryContainer_C.gridx = 0;
        this.add(bank_panel, summaryContainer_C);

        summaryContainer_C.gridx = 1;
        this.add(nonMappedTranslation_panel, summaryContainer_C);

        summaryContainer_C.gridx = 2;
        this.add(nonBankTranslation_panel, summaryContainer_C);

        summaryContainer_C.gridx = 3;
        this.add(autoGroup_panel, summaryContainer_C);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        if (bank_panel != null) {
            int max = bank_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
            int min = bank_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
            bank_panel.update();
            bank_panel.getMainPanel().getListSelectionModel().setSelectionInterval(min, max);
        }
        selectBank();

        nonMappedTranslation_panel.update();
        nonBankTranslation_panel.update();
    }

    /**
     * Grab the selected bank and update the UI
     */
    private void selectBank() {
        selectedBank = null;
        if (bank_panel != null) {
            List<?> selected = bank_panel.getMainPanel().getSelectedItems();
            if (selected.size() == 1) {
                selectedBank = ((Bank) selected.get(0));
            }
        }

        autoGroup_set.setParent(selectedBank);
        autoGroup_panel.update();
    }
}
