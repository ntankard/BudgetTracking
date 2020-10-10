package com.ntankard.dynamicGUI.DataObject;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.TrackingDatabase_Schema;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static com.ntankard.TestUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldContainerTest {

    @Test
    void add() {
        FieldContainer fieldContainer = Valid_Abstract_DataObjectC.getFieldContainer();
        DataField<?> toAdd = new Test_DataField("Test1");

        // Check valid add
        assertDoesNotThrow(() -> fieldContainer.add(toAdd));
        assertEquals(toAdd, fieldContainer.get("Test1"));
        assertTrue(fieldContainer.getList().contains(toAdd));

        // Check you cant double add
        assertThrows(IllegalArgumentException.class, () -> fieldContainer.add(toAdd));

        // Check you cant add with the same name
        DataField<?> toAdd2 = new Test_DataField("Test1");
        assertThrows(IllegalArgumentException.class, () -> fieldContainer.add(toAdd2));

        // Check you can add again after a failure
        DataField<?> toAdd3 = new Test_DataField("Test3");
        assertDoesNotThrow(() -> fieldContainer.add(toAdd3));
        assertEquals(toAdd3, fieldContainer.get("Test3"));
        assertTrue(fieldContainer.getList().contains(toAdd3));

        // Check you still cant double add
        assertThrows(IllegalArgumentException.class, () -> fieldContainer.add(toAdd3));

        // Check you cant add after finalization
        DataField<?> toAdd4 = new Test_DataField("Test4");
        fieldContainer.finaliseContainer(Valid_DataObjectD.class);
        assertThrows(IllegalStateException.class, () -> fieldContainer.add(toAdd4));
    }

    @Test
    void endLayer() {
        FieldContainer fieldContainer = new FieldContainer();

        // check a valid stack
        assertDoesNotThrow(() -> fieldContainer.endLayer(DataObject.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_DataObjectC.class));

        // Check that you can no double end
        assertThrows(IllegalArgumentException.class, () -> fieldContainer.endLayer(Valid_Abstract_DataObjectB.class));

        // Check that you can not end a solid object
        assertThrows(IllegalStateException.class, () -> fieldContainer.endLayer(Valid_DataObjectD.class));

        // check that you can not add after finalise
        FieldContainer fieldContainer2 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer2.endLayer(DataObject.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_DataObjectC.class));
        assertDoesNotThrow(() -> fieldContainer2.finaliseContainer(Valid_DataObjectD.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer2.endLayer(Valid_Abstract_DataObjectE.class));
    }

    @Test
    void finaliseContainer() {
        // Check valid sequence
        FieldContainer fieldContainer = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer.endLayer(DataObject.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_DataObjectC.class));
        assertDoesNotThrow(() -> fieldContainer.finaliseContainer(Valid_DataObjectD.class));

        // Check you cant skip a layer
        FieldContainer fieldContainer2 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer2.endLayer(DataObject.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_DataObjectB.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer2.finaliseContainer(Valid_DataObjectD.class));

        FieldContainer fieldContainer3 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer3.endLayer(DataObject.class));
        assertDoesNotThrow(() -> fieldContainer3.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> fieldContainer3.endLayer(Valid_Abstract_DataObjectC.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer3.finaliseContainer(Valid_DataObjectD.class));

        FieldContainer fieldContainer4 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer4.endLayer(DataObject.class));
        assertDoesNotThrow(() -> fieldContainer4.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> fieldContainer4.endLayer(Valid_Abstract_DataObjectC.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer4.finaliseContainer(Valid_DataObjectD.class));

        // Check you can't finalise an abstract layer
        FieldContainer fieldContainer5 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer5.endLayer(DataObject.class));
        assertDoesNotThrow(() -> fieldContainer5.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> fieldContainer5.endLayer(Valid_Abstract_DataObjectB.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer5.finaliseContainer(Valid_Abstract_DataObjectC.class));

        // Check you cant double run
        FieldContainer fieldContainer6 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer6.endLayer(DataObject.class));
        assertDoesNotThrow(() -> fieldContainer6.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> fieldContainer6.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> fieldContainer6.endLayer(Valid_Abstract_DataObjectC.class));
        assertDoesNotThrow(() -> fieldContainer6.finaliseContainer(Valid_DataObjectD.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer6.finaliseContainer(Valid_DataObjectD.class));
    }

    @Test
    void get() {
        // Check test objects
        FieldContainer fieldContainer = Valid_DataObjectD.getFieldContainer();
        for (DataField<?> field : fieldContainer.getList()) {
            assertEquals(field, fieldContainer.get(field.getIdentifierName()));
        }

        // Check all real objects
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            fieldContainer = TrackingDatabase_Schema.getFieldContainer(dClass);
            for (DataField<?> field : fieldContainer.getList()) {
                assertEquals(field, fieldContainer.get(field.getIdentifierName()));
            }
        }
    }

    @Test
    void getList() {
        // Check test objects
        FieldContainer fieldContainer = Valid_DataObjectD.getFieldContainer();
        assertEquals(fieldContainer.masterMap.size(), fieldContainer.getList().size());
        for (DataField<?> field : fieldContainer.getList()) {
            assertEquals(field, fieldContainer.masterMap.get(field.getIdentifierName()));
        }

        // Check all real objects
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            fieldContainer = TrackingDatabase_Schema.getFieldContainer(dClass);
            assertEquals(fieldContainer.masterMap.size(), fieldContainer.getList().size());
            for (DataField<?> field : fieldContainer.getList()) {
                assertEquals(field, fieldContainer.masterMap.get(field.getIdentifierName()));
            }
        }
    }

    @Test
    void getLast() {
        FieldContainer fieldContainer = new FieldContainer();
        assertNull(fieldContainer.getLast());

        for (int i = 0; i < 100; i++) {
            Test_DataField toAddA = new Test_DataField(i + "A1");
            Test_DataField toAddB = new Test_DataField(i + "B1");
            fieldContainer.add(toAddA);
            assertEquals(toAddA, fieldContainer.getLast());
            fieldContainer.add(i + "A1", toAddB);
            assertEquals(toAddB, fieldContainer.getLast());
        }

        fieldContainer.endLayer(DataObject.class);

        for (int i = 0; i < 100; i++) {
            Test_DataField toAddA = new Test_DataField(i + "A2");
            Test_DataField toAddB = new Test_DataField(i + "B2");
            fieldContainer.add(toAddA);
            assertEquals(toAddA, fieldContainer.getLast());
            fieldContainer.add(i + "A2", toAddB);
            assertEquals(toAddB, fieldContainer.getLast());
        }

        fieldContainer.endLayer(Valid_Abstract_DataObjectA.class);

        for (int i = 0; i < 100; i++) {
            Test_DataField toAddA = new Test_DataField(i + "A3");
            Test_DataField toAddB = new Test_DataField(i + "B3");
            fieldContainer.add(toAddA);
            assertEquals(toAddA, fieldContainer.getLast());
            fieldContainer.add(i + "A3", toAddB);
            assertEquals(toAddB, fieldContainer.getLast());
        }
    }

    @Test
    void testOrder() {
        // TODO TBD
    }

    @Test
    void testDataObjects() {
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            FieldContainer fieldContainer = TrackingDatabase_Schema.getFieldContainer(dClass);
            if (Modifier.isAbstract(dClass.getModifiers())) {
                assertEquals(dClass, fieldContainer.inheritedObjects.get(fieldContainer.inheritedObjects.size() - 1));
            } else {
                assertEquals(dClass, fieldContainer.solidObjectType);
                for (DataField<?> dataField : fieldContainer.getList()) {
                    assertEquals(dClass, dataField.getParentType());
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Test Objects ##################################################
    //------------------------------------------------------------------------------------------------------------------

    abstract static class Valid_Abstract_DataObjectA extends DataObject {
        public static FieldContainer getFieldContainer() {
            FieldContainer fieldContainer = DataObject.getFieldContainer();
            return fieldContainer.endLayer(Valid_Abstract_DataObjectA.class);
        }
    }

    abstract static class Valid_Abstract_DataObjectB extends Valid_Abstract_DataObjectA {
        public static FieldContainer getFieldContainer() {
            FieldContainer fieldContainer = Valid_Abstract_DataObjectA.getFieldContainer();
            return fieldContainer.endLayer(Valid_Abstract_DataObjectB.class);
        }
    }

    abstract static class Valid_Abstract_DataObjectC extends Valid_Abstract_DataObjectB {
        public static FieldContainer getFieldContainer() {
            FieldContainer fieldContainer = Valid_Abstract_DataObjectB.getFieldContainer();
            return fieldContainer.endLayer(Valid_Abstract_DataObjectC.class);
        }
    }

    static class Valid_DataObjectD extends Valid_Abstract_DataObjectC {
        public static FieldContainer getFieldContainer() {
            FieldContainer fieldContainer = Valid_Abstract_DataObjectC.getFieldContainer();
            return fieldContainer.finaliseContainer(Valid_DataObjectD.class);
        }
    }

    abstract static class Valid_Abstract_DataObjectE extends Valid_Abstract_DataObjectC {
    }

    static class Test_DataField extends DataField<Object> {
        private final String identifierName;

        public Test_DataField(String identifierName) {
            super(identifierName, Object.class);
            this.identifierName = identifierName;
        }

        @Override
        public String getIdentifierName() {
            return identifierName;
        }
    }
}
