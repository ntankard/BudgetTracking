package com.ntankard.Tracking.Dispaly.Frames.MainFrame;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.CategorySet;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToCategorySet;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToVirtualCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.VirtualCategory;
import com.ntankard.Tracking.DataBase.Interface.Set.Full_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.CategorySet_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.CategoryToCategorySet_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.CategoryToVirtualCategory_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.VirtualCategory_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class CategorySetPanel extends UpdatableJPanel {

    // The GUI components
    private DataObject_DisplayList<CategorySet> categorySet_panel;
    private DataObject_DisplayList<VirtualCategory> virtualCategory_panel;
    private DataObject_DisplayList<CategoryToCategorySet> categoryToCategorySet_panel;
    private DataObject_DisplayList<CategoryToVirtualCategory> categoryToVirtualCategory_panel;

    // The GUI component controllers
    private ListSelectionListener selectionListener;
    private OneParent_Children_Set<VirtualCategory, CategorySet> virtualCategory_set;
    private VirtualCategory_ElementController virtualCategory_elementController;

    private OneParent_Children_Set<CategoryToCategorySet, CategorySet> categoryToCategorySet_set;
    private CategoryToCategorySet_ElementController categoryToCategorySet_elementController;

    private OneParent_Children_Set<CategoryToVirtualCategory, VirtualCategory> categoryToVirtualCategory_set;
    private CategoryToVirtualCategory_ElementController categoryToVirtualCategory_elementController;

    // Displayed core data
    private CategorySet selectedCategorySet = null;
    private VirtualCategory selectedVirtualCategory = null;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public CategorySetPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        selectionListener = e -> update();

        categorySet_panel = new DataObject_DisplayList<>(CategorySet.class, new Full_Set<>(CategorySet.class), false, this);
        categorySet_panel.addControlButtons(new CategorySet_ElementController(this));
        categorySet_panel.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);

        virtualCategory_set = new OneParent_Children_Set<>(VirtualCategory.class, null);
        virtualCategory_elementController = new VirtualCategory_ElementController(this);
        virtualCategory_panel = new DataObject_DisplayList<>(VirtualCategory.class, virtualCategory_set, false, this);
        virtualCategory_panel.addControlButtons(virtualCategory_elementController);
        virtualCategory_panel.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);

        categoryToCategorySet_set = new OneParent_Children_Set<>(CategoryToCategorySet.class, null);
        categoryToCategorySet_elementController = new CategoryToCategorySet_ElementController(this);
        categoryToCategorySet_panel = new DataObject_DisplayList<>(CategoryToCategorySet.class, categoryToCategorySet_set, false, this);
        categoryToCategorySet_panel.addControlButtons(categoryToCategorySet_elementController);

        categoryToVirtualCategory_set = new OneParent_Children_Set<>(CategoryToVirtualCategory.class, null);
        categoryToVirtualCategory_elementController = new CategoryToVirtualCategory_ElementController(this);
        categoryToVirtualCategory_panel = new DataObject_DisplayList<>(CategoryToVirtualCategory.class, categoryToVirtualCategory_set, false, this);
        categoryToVirtualCategory_panel.addControlButtons(categoryToVirtualCategory_elementController);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridheight = 2;

        gridBagConstraints.gridx = 0;
        this.add(categorySet_panel, gridBagConstraints);

        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridx = 1;
        this.add(virtualCategory_panel, gridBagConstraints);

        gridBagConstraints.weighty = 2;
        this.add(categoryToCategorySet_panel, gridBagConstraints);

        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridx = 2;
        this.add(categoryToVirtualCategory_panel, gridBagConstraints);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        // Turn off the listeners to prevent a infinite loop
        categorySet_panel.getMainPanel().getListSelectionModel().removeListSelectionListener(selectionListener);
        virtualCategory_panel.getMainPanel().getListSelectionModel().removeListSelectionListener(selectionListener);

        // Find out the current status of the lists
        int categorySet_panel_max = categorySet_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        int categorySet_panel_min = categorySet_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        int virtualCategory_panel_max = virtualCategory_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        int virtualCategory_panel_min = virtualCategory_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();

        // Update the CategorySet panel and select the same element as before
        categorySet_panel.update();
        categorySet_panel.getMainPanel().getListSelectionModel().setSelectionInterval(categorySet_panel_max, categorySet_panel_min);

        // Find the selected CategorySet
        CategorySet newSelectedCategorySet = null;
        List<?> selected = categorySet_panel.getMainPanel().getSelectedItems();
        if (selected.size() == 1) {
            newSelectedCategorySet = ((CategorySet) selected.get(0));
        }

        // If the selected CategorySet was changed repopulate the VirtualCategory panel and clear its selection
        if (newSelectedCategorySet != selectedCategorySet) {
            selectedCategorySet = newSelectedCategorySet;
            virtualCategory_set.setParent(selectedCategorySet);
            virtualCategory_elementController.setCategorySet(selectedCategorySet);
            categoryToCategorySet_set.setParent(selectedCategorySet);
            categoryToCategorySet_elementController.setCategorySet(selectedCategorySet);
            virtualCategory_panel_min = -1;
            virtualCategory_panel_max = -1;
        }

        // Update the VirtualCategory panel
        categoryToCategorySet_panel.update();
        virtualCategory_panel.update();
        if (virtualCategory_set.get().size() - 1 >= virtualCategory_panel_max) {
            virtualCategory_panel.getMainPanel().getListSelectionModel().setSelectionInterval(virtualCategory_panel_max, virtualCategory_panel_min);
        } else {
            virtualCategory_panel.getMainPanel().getListSelectionModel().setSelectionInterval(-1, -1);
        }

        // Find the selected VirtualCategory
        VirtualCategory newSelectedVirtualCategory = null;
        List<?> selectedVC = virtualCategory_panel.getMainPanel().getSelectedItems();
        if (selectedVC.size() == 1) {
            newSelectedVirtualCategory = ((VirtualCategory) selectedVC.get(0));
        }

        // If the selected VirtualCategory was changed repopulate the CategoryToVirtualCategory panel and clear its selection
        if (newSelectedVirtualCategory != selectedVirtualCategory) {
            selectedVirtualCategory = newSelectedVirtualCategory;
            categoryToVirtualCategory_set.setParent(selectedVirtualCategory);
            categoryToVirtualCategory_elementController.setVirtualCategory(selectedVirtualCategory);
        }

        // Update the CategoryToVirtualCategory panel
        categoryToVirtualCategory_panel.update();

        // Turn on the listeners
        categorySet_panel.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
        virtualCategory_panel.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
    }
}
