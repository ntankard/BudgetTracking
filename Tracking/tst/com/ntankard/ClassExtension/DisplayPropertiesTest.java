package com.ntankard.ClassExtension;

import com.ntankard.TestUtil.ClassInspectionUtil;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.SubContainers.DataObjectClassTree;
import com.ntankard.Tracking.Util.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DisplayPropertiesTest {

    /**
     * Check that all object types that support DisplayProperties are orders correctly
     */
    @Test
    void order() {
        DataObjectClassTree dataObjectClassTree = ClassInspectionUtil.getDataObjectClassTree();
        orderLayerCheck(dataObjectClassTree.getClassTreeRoot(), 6, new HashMap<>());
    }

    /**
     * Check that all object types that support DisplayProperties are orders correctly for a specific class
     */
    private void orderLayerCheck(TreeNode<Class<? extends DataObject>> dataObjectClassTree, int layer, Map<Integer, String> pastMethods) {
        int layerStep = (int) Math.pow(10, layer);
        int parentLayerStep = (int) Math.pow(10, layer + 1);

        Map<Integer, String> currentMethods = new HashMap<>();
        Map<Integer, List<Integer>> pastOrderSets = new HashMap<>();

        // Generate all the available order sets
        for (Integer key : pastMethods.keySet()) {
            pastOrderSets.put(key / parentLayerStep, new ArrayList<>());
        }

        // For each method exposed at this layer
        for (Member member : new MemberClass(dataObjectClassTree.data).getVerbosityMembers(Integer.MAX_VALUE, false)) {

            // Find the listed Order
            DisplayProperties properties = member.getGetter().getAnnotation(DisplayProperties.class);
            assertNotNull(properties, "Method is missing DisplayProperties." + " Class:" + dataObjectClassTree.data.getSimpleName() + " Method:" + member.getGetter().getName());
            int order = properties.order();

            // Are there existing sets? (true for everything other the the first layer)
            if (pastMethods.size() != 0) {

                // Has this method been seen before? (is it overridden?)
                if (pastMethods.containsKey(order)) {
                    assertEquals(pastMethods.get(order), member.getName(), "A method had been overridden but its Order dose not match the parent." + " Class:" + dataObjectClassTree.data.getSimpleName() + " Method:" + member.getGetter().getName());
                } else {
                    int parentSet = order / parentLayerStep;
                    int orderInSet = (order - (parentSet * parentLayerStep)) / layerStep;

                    assertTrue(pastOrderSets.containsKey(parentSet), "Methods order value dose not fit into one of the ranges of the parent. Order was:" + order + " Class:" + dataObjectClassTree.data.getSimpleName() + " Method:" + member.getGetter().getName());
                    assertFalse(pastOrderSets.get(parentSet).contains(orderInSet), "Duplicate order value found." + " Class:" + dataObjectClassTree.data.getSimpleName() + " Method:" + member.getGetter().getName());
                    pastOrderSets.get(parentSet).add(orderInSet);

                }
            }
            currentMethods.put(order, member.getName());
        }

        // Is this the root layer?
        if (pastMethods.size() == 0) {
            List<Integer> baseOrder = new ArrayList<>(currentMethods.keySet());
            baseOrder.sort(Integer::compareTo);
            int start = baseOrder.get(0);
            for (int i = 1; i < baseOrder.size(); i++) {
                assertEquals(start + layerStep, baseOrder.get(i), "One of the order values is missing. Missing values is " + start + layerStep + " Class:" + dataObjectClassTree.data.getSimpleName());
                start = baseOrder.get(i);
            }
        } else {
            // Check each set independently
            for (Integer key : pastOrderSets.keySet()) {
                if (pastOrderSets.get(key).size() != 0) {
                    pastOrderSets.get(key).sort(Integer::compareTo);
                    int start = pastOrderSets.get(key).get(0);
                    for (int i = 1; i < pastOrderSets.get(key).size(); i++) {
                        assertEquals(start + 1, pastOrderSets.get(key).get(i), "One of the order values is missing. Missing values is " + start + layerStep + " Class:" + dataObjectClassTree.data.getSimpleName());
                        start = pastOrderSets.get(key).get(i);
                    }
                }
            }
        }

        for (TreeNode<Class<? extends DataObject>> child : dataObjectClassTree.children) {
            Map<Integer, String> newPast = new HashMap<>(pastMethods);
            newPast.putAll(currentMethods);
            orderLayerCheck(child, layer - 1, newPast);
        }
    }
}
