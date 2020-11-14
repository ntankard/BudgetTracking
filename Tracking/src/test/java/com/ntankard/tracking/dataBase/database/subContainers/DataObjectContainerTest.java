package com.ntankard.tracking.dataBase.database.subContainers;

import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.subContainers.DataObjectContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class DataObjectContainerTest {

    @Test
    void testErrorCheck() {
        DataObjectContainer dataObjectContainer = new DataObjectContainer();

        dataObjectContainer.add(new Layer1_DataObject_New(1));
        assertThrows(RuntimeException.class, () -> {
            dataObjectContainer.add(new Layer1_DataObject_New(1));
        });

        assertThrows(RuntimeException.class, () -> {
            dataObjectContainer.add(new Layer2_DataObject_New(1));
        });
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    void testAdd() {
        DataObjectContainer dataObjectContainer = new DataObjectContainer();

        Layer1_DataObject_New layer1 = new Layer1_DataObject_New(1);
        dataObjectContainer.add(layer1);
        assertTrue(dataObjectContainer.get().contains(layer1));
        assertTrue(dataObjectContainer.get(Layer1_DataObject_New.class).contains(layer1));
        assertFalse(dataObjectContainer.get(Layer2_DataObject_New.class).contains(layer1));
        assertFalse(dataObjectContainer.get(Layer3_DataObject_New.class).contains(layer1));
        assertFalse(dataObjectContainer.get(Layer1_ALT_DataObject_New.class).contains(layer1));

        Layer2_DataObject_New layer2 = new Layer2_DataObject_New(2);
        dataObjectContainer.add(layer2);
        assertTrue(dataObjectContainer.get().contains(layer2));
        assertTrue(dataObjectContainer.get(Layer1_DataObject_New.class).contains(layer2));
        assertTrue(dataObjectContainer.get(Layer2_DataObject_New.class).contains(layer2));
        assertFalse(dataObjectContainer.get(Layer3_DataObject_New.class).contains(layer2));
        assertFalse(dataObjectContainer.get(Layer1_ALT_DataObject_New.class).contains(layer2));

        Layer3_DataObject_New layer3 = new Layer3_DataObject_New(3);
        dataObjectContainer.add(layer3);
        assertTrue(dataObjectContainer.get().contains(layer3));
        assertTrue(dataObjectContainer.get(Layer1_DataObject_New.class).contains(layer3));
        assertTrue(dataObjectContainer.get(Layer2_DataObject_New.class).contains(layer3));
        assertTrue(dataObjectContainer.get(Layer3_DataObject_New.class).contains(layer3));
        assertFalse(dataObjectContainer.get(Layer1_ALT_DataObject_New.class).contains(layer3));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    void testAddRemove() {
        int testSize = 1000;

        DataObjectContainer dataObjectContainer = new DataObjectContainer();
        List<DataObject> all = new ArrayList<>();

        // Generate random objects
        DataObject dataObject_new;
        for (int i = 0; i < testSize; i++) {
            switch (new Random().nextInt((3) + 1)) {
                case 0:
                    dataObject_new = new Layer1_DataObject_New(dataObjectContainer.getNextId());
                    all.add(dataObject_new);
                    dataObjectContainer.add(dataObject_new);
                    break;
                case 1:
                    dataObject_new = new Layer2_DataObject_New(dataObjectContainer.getNextId());
                    all.add(dataObject_new);
                    dataObjectContainer.add(dataObject_new);
                    break;
                case 2:
                    dataObject_new = new Layer3_DataObject_New(dataObjectContainer.getNextId());
                    all.add(dataObject_new);
                    dataObjectContainer.add(dataObject_new);
                    break;
                case 3:
                    dataObject_new = new Layer1_ALT_DataObject_New(dataObjectContainer.getNextId());
                    all.add(dataObject_new);
                    dataObjectContainer.add(dataObject_new);
                    break;
            }
        }

        // Check that they are accessible at the correct layer
        for (int i = 0; i < testSize; i++) {
            DataObject toTest = all.get(i);
            assertTrue(dataObjectContainer.get().contains(toTest));
            if (toTest.getClass().equals(Layer1_DataObject_New.class)) {
                assertTrue(dataObjectContainer.get(Layer1_DataObject_New.class).contains(toTest));
                assertFalse(dataObjectContainer.get(Layer2_DataObject_New.class).contains(toTest));
                assertFalse(dataObjectContainer.get(Layer3_DataObject_New.class).contains(toTest));
                assertFalse(dataObjectContainer.get(Layer1_ALT_DataObject_New.class).contains(toTest));

                assertEquals(dataObjectContainer.get(Layer1_DataObject_New.class, toTest.getId()), toTest);
                assertNotEquals(dataObjectContainer.get(Layer2_DataObject_New.class, toTest.getId()), toTest);
                assertNotEquals(dataObjectContainer.get(Layer3_DataObject_New.class, toTest.getId()), toTest);
                assertNotEquals(dataObjectContainer.get(Layer1_ALT_DataObject_New.class, toTest.getId()), toTest);
            } else if (toTest.getClass().equals(Layer2_DataObject_New.class)) {
                assertTrue(dataObjectContainer.get(Layer1_DataObject_New.class).contains(toTest));
                assertTrue(dataObjectContainer.get(Layer2_DataObject_New.class).contains(toTest));
                assertFalse(dataObjectContainer.get(Layer3_DataObject_New.class).contains(toTest));
                assertFalse(dataObjectContainer.get(Layer1_ALT_DataObject_New.class).contains(toTest));

                assertEquals(dataObjectContainer.get(Layer1_DataObject_New.class, toTest.getId()), toTest);
                assertEquals(dataObjectContainer.get(Layer2_DataObject_New.class, toTest.getId()), toTest);
                assertNotEquals(dataObjectContainer.get(Layer3_DataObject_New.class, toTest.getId()), toTest);
                assertNotEquals(dataObjectContainer.get(Layer1_ALT_DataObject_New.class, toTest.getId()), toTest);
            } else if (toTest.getClass().equals(Layer3_DataObject_New.class)) {
                assertTrue(dataObjectContainer.get(Layer1_DataObject_New.class).contains(toTest));
                assertTrue(dataObjectContainer.get(Layer2_DataObject_New.class).contains(toTest));
                assertTrue(dataObjectContainer.get(Layer3_DataObject_New.class).contains(toTest));
                assertFalse(dataObjectContainer.get(Layer1_ALT_DataObject_New.class).contains(toTest));

                assertEquals(dataObjectContainer.get(Layer1_DataObject_New.class, toTest.getId()), toTest);
                assertEquals(dataObjectContainer.get(Layer2_DataObject_New.class, toTest.getId()), toTest);
                assertEquals(dataObjectContainer.get(Layer3_DataObject_New.class, toTest.getId()), toTest);
                assertNotEquals(dataObjectContainer.get(Layer1_ALT_DataObject_New.class, toTest.getId()), toTest);
            } else if (toTest.getClass().equals(Layer1_ALT_DataObject_New.class)) {
                assertFalse(dataObjectContainer.get(Layer1_DataObject_New.class).contains(toTest));
                assertFalse(dataObjectContainer.get(Layer2_DataObject_New.class).contains(toTest));
                assertFalse(dataObjectContainer.get(Layer3_DataObject_New.class).contains(toTest));
                assertTrue(dataObjectContainer.get(Layer1_ALT_DataObject_New.class).contains(toTest));

                assertNotEquals(dataObjectContainer.get(Layer1_DataObject_New.class, toTest.getId()), toTest);
                assertNotEquals(dataObjectContainer.get(Layer2_DataObject_New.class, toTest.getId()), toTest);
                assertNotEquals(dataObjectContainer.get(Layer3_DataObject_New.class, toTest.getId()), toTest);
                assertEquals(dataObjectContainer.get(Layer1_ALT_DataObject_New.class, toTest.getId()), toTest);
            }
        }

        assertEquals(all.size(), dataObjectContainer.get().size());

        // Check that remove works
        for (int i = 0; i < testSize; i++) {
            DataObject toTest = all.get(i);

            assertTrue(dataObjectContainer.get().contains(toTest));
            dataObjectContainer.remove(toTest);
            assertFalse(dataObjectContainer.get().contains(toTest));

            assertFalse(dataObjectContainer.get(Layer1_DataObject_New.class).contains(toTest));
            assertFalse(dataObjectContainer.get(Layer2_DataObject_New.class).contains(toTest));
            assertFalse(dataObjectContainer.get(Layer3_DataObject_New.class).contains(toTest));
            assertFalse(dataObjectContainer.get(Layer1_ALT_DataObject_New.class).contains(toTest));

            assertNull(dataObjectContainer.get(Layer1_DataObject_New.class, toTest.getId()));
            assertNull(dataObjectContainer.get(Layer2_DataObject_New.class, toTest.getId()));
            assertNull(dataObjectContainer.get(Layer3_DataObject_New.class, toTest.getId()));
            assertNull(dataObjectContainer.get(Layer1_ALT_DataObject_New.class, toTest.getId()));
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Special Objects #################################################
    //------------------------------------------------------------------------------------------------------------------

    private static class Layer1_ALT_DataObject_New extends DataObject {
        private final Integer id;

        Layer1_ALT_DataObject_New(Integer id) {
            super();
            this.id = id;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public List<DataObject> getParents() {
            return null;
        }
    }

    private static class Layer1_DataObject_New extends DataObject {
        private final Integer id;

        Layer1_DataObject_New(Integer id) {
            super();
            this.id = id;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public List<DataObject> getParents() {
            return null;
        }
    }

    private static class Layer2_DataObject_New extends Layer1_DataObject_New {
        Layer2_DataObject_New(Integer id) {
            super(id);
        }
    }

    private static class Layer3_DataObject_New extends Layer2_DataObject_New {
        Layer3_DataObject_New(Integer id) {
            super(id);
        }
    }
}
