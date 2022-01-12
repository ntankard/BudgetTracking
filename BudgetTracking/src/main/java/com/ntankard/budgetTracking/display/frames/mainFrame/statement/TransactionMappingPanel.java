package com.ntankard.budgetTracking.display.frames.mainFrame.statement;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementFolder;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransactionTranslationAutoGroup;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.StatementBankTransfer;
import com.ntankard.budgetTracking.display.util.elementControllers.StatementBankTransfer_ElementController;
import com.ntankard.budgetTracking.display.util.panels.DataObject_DisplayList;
import com.ntankard.dynamicGUI.gui.containers.DynamicGUI_DisplayList.ListControl_Button;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.exception.corrupting.CorruptingException;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;
import com.ntankard.javaObjectDatabase.util.set.SetFilter;
import com.ntankard.javaObjectDatabase.util.set.TwoParent_Children_Set;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;

public class TransactionMappingPanel extends UpdatableJPanel {

    // Core data the panel is built around
    private final StatementFolder statementFolder;

    // Core database
    private final Database database;

    // The GUI components
    private ListSelectionListener selectionListener;
    private DataObject_DisplayList<StatementBankTransfer> statementBankTransfer_list;
    private DataObject_DisplayList<StatementTransaction> unlinked_statementTransaction_list;
    private DataObject_DisplayList<StatementTransaction> linked_statementTransaction_list;
    private ListControl_Button<?> link_btn;

    // Source of data to display and process
    private OneParent_Children_Set<StatementTransaction, StatementBankTransfer> linked_statementTransaction_set;
    private TwoParent_Children_Set<StatementTransaction, Period, Bank> unlinked_statementTransaction_set;

    // Buffered selection data for fast access for buttons
    private StatementBankTransfer selectedStatementBankTransfer = null;
    private List<StatementTransaction> selectedUnlinkedStatementTransaction = new ArrayList<>();
    private List<StatementTransaction> selectedLinkedStatementTransaction = new ArrayList<>();

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    protected TransactionMappingPanel(StatementFolder statementFolder, Updatable master) {
        super(master);
        this.statementFolder = statementFolder;
        this.database = statementFolder.getTrackingDatabase();
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        selectionListener = e -> update();

        // List of all StatementBankTransfer
        statementBankTransfer_list = new DataObject_DisplayList<>(database.getSchema(),
                StatementBankTransfer.class,
                new TwoParent_Children_Set<>(StatementBankTransfer.class, statementFolder.getPeriod(), statementFolder.getBank()),
                false,
                this);
        statementBankTransfer_list.addControlButtons(new StatementBankTransfer_ElementController(statementFolder, this));
        statementBankTransfer_list.getMainPanel().getListSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // List of unlinked StatementTransaction
        unlinked_statementTransaction_set = new TwoParent_Children_Set<>(StatementTransaction.class, statementFolder.getPeriod(), statementFolder.getBank(), new SetFilter<StatementTransaction>() {
            @Override
            protected boolean shouldAdd_Impl(StatementTransaction dataObject) {
                return dataObject.getStatementBankTransfer() == null;
            }
        });
        unlinked_statementTransaction_list = new DataObject_DisplayList<>(database.getSchema(),
                StatementTransaction.class,
                unlinked_statementTransaction_set,
                false,
                this);
        link_btn = new ListControl_Button<>("Link", unlinked_statementTransaction_list, ListControl_Button.EnableCondition.MULTI, false);
        link_btn.addActionListener(e -> link());
        unlinked_statementTransaction_list.addButton(link_btn);
        JButton auto_btn = new JButton("Auto");
        auto_btn.addActionListener(e -> autoAssign());
        unlinked_statementTransaction_list.addButton(auto_btn);
        ListControl_Button<?> single_btn = new ListControl_Button<>("Single", unlinked_statementTransaction_list, ListControl_Button.EnableCondition.SINGLE, false);
        single_btn.addActionListener(e -> single());
        unlinked_statementTransaction_list.addButton(single_btn);
        JButton print_btn = new JButton("Print");
        print_btn.addActionListener(e -> printReport());
        unlinked_statementTransaction_list.addButton(print_btn);
        JButton remain_btn = new JButton("Remaining");
        remain_btn.addActionListener(e -> groupRemaining());
        unlinked_statementTransaction_list.addButton(remain_btn);


        // List of StatementTransaction linked to StatementBankTransfer
        linked_statementTransaction_set = new OneParent_Children_Set<>(StatementTransaction.class, null);
        linked_statementTransaction_list = new DataObject_DisplayList<>(database.getSchema(),
                StatementTransaction.class,
                linked_statementTransaction_set,
                false,
                this);
        ListControl_Button<?> unlink_btn = new ListControl_Button<>("Un Link", linked_statementTransaction_list, ListControl_Button.EnableCondition.MULTI, false);
        unlink_btn.addActionListener(e -> unlink());
        linked_statementTransaction_list.addButton(unlink_btn);

        // Layout
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;

        gridBagConstraints.gridx = 0;
        this.add(statementBankTransfer_list, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        this.add(unlinked_statementTransaction_list, gridBagConstraints);

        gridBagConstraints.gridx = 2;
        this.add(linked_statementTransaction_list, gridBagConstraints);

        // Enable
        statementBankTransfer_list.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
        unlinked_statementTransaction_list.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
        linked_statementTransaction_list.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
    }

    /**
     * Call back for the assign button. Will move all unlinked StatementTransaction into a StatementBankTransfer if there is a rule to link them
     */
    private void autoAssign() {
        List<StatementTransaction> toMatch = unlinked_statementTransaction_set.get();

        for (StatementTransactionTranslationAutoGroup group : statementFolder.getBank().getChildren(StatementTransactionTranslationAutoGroup.class)) {

            // Is anything left to match?
            if (toMatch.isEmpty()) {
                break;
            }

            List<StatementTransaction> toRemove = new ArrayList<>();
            StatementBankTransfer statementBankTransfer = null;
            for (StatementTransaction tryMatch : toMatch) {
                // TODO this should be done in the object itself
                if (group.getTranslation().getOriginal().contains(",") && group.getTranslation().getOriginal().contains("~")) {
                    throw new UnsupportedOperationException();
                }

                // Does this rule apply to this StatementTransaction
                boolean matched = false;
                if (group.getTranslation().getOriginal().contains("'")) { // OR
                    String[] toCheck = group.getTranslation().getOriginal().split("'");
                    for (String check : toCheck) {
                        if (tryMatch.getDescription().contains(check)) {
                            matched = true;
                            break;
                        }
                    }
                } else if (group.getTranslation().getOriginal().contains("~")) { // AND
                    String[] toCheck = group.getTranslation().getOriginal().split("~");
                    matched = true;
                    for (String check : toCheck) {
                        if (!tryMatch.getDescription().contains(check)) {
                            matched = false;
                            break;
                        }
                    }
                } else { // 1 only
                    matched = tryMatch.getDescription().contains(group.getTranslation().getOriginal());
                }

                if (matched) {

                    // Is there already a StatementBankTransfer or do we have to make one?
                    if (statementBankTransfer == null) {

                        List<StatementBankTransfer> existing = new TwoParent_Children_Set<>(StatementBankTransfer.class, group, statementFolder.getPeriod()).get();
                        if (existing.isEmpty()) {
                            statementBankTransfer = new StatementBankTransfer(statementFolder.getPeriod(), statementFolder.getBank(), null, group.getPool(), "G-" + group.getTranslation().getTranslated(), group, group.getMultiply()).add();
                        } else if (existing.size() == 1) {
                            statementBankTransfer = existing.get(0);
                        } else {
                            throw new CorruptingException(database, "More than 1 StatementBankTransfer points to a StatementTransactionTranslationAutoGroup (auto generated into 2 objects");
                        }
                    }

                    // Do the match
                    tryMatch.setStatementBankTransfer(statementBankTransfer);
                    toRemove.add(tryMatch);
                }
            }

            toMatch.removeAll(toRemove);
        }
        update();
    }

    /**
     * Call back for the unlink button. Unlink an individual transaction
     */
    private void unlink() {
        for (StatementTransaction statementTransaction : selectedLinkedStatementTransaction) {
            statementTransaction.setStatementBankTransfer(null);
        }
        update();
    }

    /**
     * Call back for the link button. Link an individual transaction
     */
    private void link() {
        if (selectedStatementBankTransfer != null) {
            for (StatementTransaction statementTransaction : selectedUnlinkedStatementTransaction) {
                statementTransaction.setStatementBankTransfer(selectedStatementBankTransfer);
            }
            update();
        }
    }

    /**
     * Call back for the single button. Create a single StatementBankTransfer from a StatementTransaction
     */
    private void single() {
        StatementTransaction toLink = selectedUnlinkedStatementTransaction.get(0);
        StatementBankTransfer newTransfer = new StatementBankTransfer(statementFolder.getPeriod(), statementFolder.getBank(), null, database.getDefault(SolidCategory.class), "G-" + toLink.getDescription(), null, 1.0).add();
        toLink.setStatementBankTransfer(newTransfer);
        update();
        statementBankTransfer_list.getMainPanel().setSelectedItems(Collections.singletonList(newTransfer));
        update();
    }

    /**
     * Call back for the print button. Prints a report of all the unassigned transactions
     */
    private void printReport() {
        List<StatementTransaction> unlinked = unlinked_statementTransaction_set.get();
        Double total = 0.0;
        Map<String, StatementTransaction> names = new HashMap<>();
        Map<String, Double> totals = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();
        for (StatementTransaction statementTransaction : unlinked) {
            if (!names.containsKey(statementTransaction.getDescription())) {
                names.put(statementTransaction.getDescription(), statementTransaction);

            }
            totals.putIfAbsent(statementTransaction.getDescription(), 0.0);
            counts.putIfAbsent(statementTransaction.getDescription(), 0);

            totals.put(statementTransaction.getDescription(), totals.get(statementTransaction.getDescription()) + statementTransaction.getValue());
            counts.put(statementTransaction.getDescription(), counts.get(statementTransaction.getDescription()) + 1);
            total += statementTransaction.getValue();
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(counts.entrySet());
        list.sort((o1, o2) -> {
            int comp = o2.getValue().compareTo(o1.getValue());
            if (comp == 0) {
                return totals.get(o2.getKey()).compareTo(totals.get(o1.getKey()));
            }
            return comp;
        });

        list.forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getValue() + " " + NumberFormat.getCurrencyInstance(Locale.JAPAN).format(totals.get(stringIntegerEntry.getKey())) + " " + stringIntegerEntry.getKey()));
        System.out.println();
        System.out.println(NumberFormat.getCurrencyInstance(Locale.JAPAN).format(total));
    }

    /**
     * Call back for the remaining button. Groups all remaining transaction into an accounted one
     */
    private void groupRemaining() {
        StatementBankTransfer newTransfer = new StatementBankTransfer(statementFolder.getPeriod(), statementFolder.getBank(), null, database.getDefault(SolidCategory.class), "G-Other", null, 1.0).add();
        unlinked_statementTransaction_set.get().forEach(statementTransaction -> statementTransaction.setStatementBankTransfer(newTransfer));
        update();
        statementBankTransfer_list.getMainPanel().setSelectedItems(Collections.singletonList(newTransfer));
        update();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        // Disable new selections
        statementBankTransfer_list.getMainPanel().getListSelectionModel().removeListSelectionListener(selectionListener);
        unlinked_statementTransaction_list.getMainPanel().getListSelectionModel().removeListSelectionListener(selectionListener);
        linked_statementTransaction_list.getMainPanel().getListSelectionModel().removeListSelectionListener(selectionListener);

        // Get the current selections
        getSelections();

        // Update the UI based on selections
        linked_statementTransaction_set.setParent(selectedStatementBankTransfer);
        if (selectedStatementBankTransfer == null) {
            link_btn.setForceOverride(false);
        } else {
            link_btn.setForceOverride(null);
        }

        // re-render
        statementBankTransfer_list.update();
        unlinked_statementTransaction_list.update();
        linked_statementTransaction_list.update();

        // Reselect the items again
        statementBankTransfer_list.getMainPanel().setSelectedItems(Collections.singletonList(selectedStatementBankTransfer));
        unlinked_statementTransaction_list.getMainPanel().setSelectedItems(selectedUnlinkedStatementTransaction);
        linked_statementTransaction_list.getMainPanel().setSelectedItems(selectedLinkedStatementTransaction);

        // Get the selections again as some may no longer exist
        getSelections();

        // Re enable new selections
        statementBankTransfer_list.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
        unlinked_statementTransaction_list.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
        linked_statementTransaction_list.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
    }

    /**
     * Read the current selected items from all lists
     */
    private void getSelections() {
        if (statementBankTransfer_list.getMainPanel().getSelectedItems().isEmpty()) {
            selectedStatementBankTransfer = null;
        } else {
            selectedStatementBankTransfer = statementBankTransfer_list.getMainPanel().getSelectedItems().get(0);
        }
        selectedUnlinkedStatementTransaction = unlinked_statementTransaction_list.getMainPanel().getSelectedItems();
        selectedLinkedStatementTransaction = linked_statementTransaction_list.getMainPanel().getSelectedItems();
    }
}
