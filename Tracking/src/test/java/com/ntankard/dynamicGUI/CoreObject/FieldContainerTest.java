package com.ntankard.dynamicGUI.CoreObject;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static com.ntankard.TestUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldContainerTest {

    @Test
    void add() {
        FieldContainer fieldContainer = Valid_Abstract_CoreObjectC.getFieldContainer();
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
        fieldContainer.finaliseContainer(Valid_CoreObjectD.class);
        assertThrows(IllegalStateException.class, () -> fieldContainer.add(toAdd4));
    }

    @Test
    void endLayer() {
        FieldContainer fieldContainer = new FieldContainer();

        // check a valid stack
        assertDoesNotThrow(() -> fieldContainer.endLayer(CoreObject.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_CoreObjectA.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_CoreObjectB.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_CoreObjectC.class));

        // Check that you can no double end
        assertThrows(IllegalArgumentException.class, () -> fieldContainer.endLayer(Valid_Abstract_CoreObjectB.class));

        // Check that you can not end a solid object
        assertThrows(IllegalStateException.class, () -> fieldContainer.endLayer(Valid_CoreObjectD.class));

        // check that you can not add after finalise
        FieldContainer fieldContainer2 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer2.endLayer(CoreObject.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_CoreObjectA.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_CoreObjectB.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_CoreObjectC.class));
        assertDoesNotThrow(() -> fieldContainer2.finaliseContainer(Valid_CoreObjectD.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer2.endLayer(Valid_Abstract_CoreObjectE.class));
    }

    @Test
    void finaliseContainer() {
        // Check valid sequence
        FieldContainer fieldContainer = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer.endLayer(CoreObject.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_CoreObjectA.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_CoreObjectB.class));
        assertDoesNotThrow(() -> fieldContainer.endLayer(Valid_Abstract_CoreObjectC.class));
        assertDoesNotThrow(() -> fieldContainer.finaliseContainer(Valid_CoreObjectD.class));

        // Check you cant skip a layer
        FieldContainer fieldContainer2 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer2.endLayer(CoreObject.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_CoreObjectA.class));
        assertDoesNotThrow(() -> fieldContainer2.endLayer(Valid_Abstract_CoreObjectB.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer2.finaliseContainer(Valid_CoreObjectD.class));

        FieldContainer fieldContainer3 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer3.endLayer(CoreObject.class));
        assertDoesNotThrow(() -> fieldContainer3.endLayer(Valid_Abstract_CoreObjectA.class));
        assertDoesNotThrow(() -> fieldContainer3.endLayer(Valid_Abstract_CoreObjectC.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer3.finaliseContainer(Valid_CoreObjectD.class));

        FieldContainer fieldContainer4 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer4.endLayer(CoreObject.class));
        assertDoesNotThrow(() -> fieldContainer4.endLayer(Valid_Abstract_CoreObjectB.class));
        assertDoesNotThrow(() -> fieldContainer4.endLayer(Valid_Abstract_CoreObjectC.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer4.finaliseContainer(Valid_CoreObjectD.class));

        // Check you can't finalise an abstract layer
        FieldContainer fieldContainer5 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer5.endLayer(CoreObject.class));
        assertDoesNotThrow(() -> fieldContainer5.endLayer(Valid_Abstract_CoreObjectA.class));
        assertDoesNotThrow(() -> fieldContainer5.endLayer(Valid_Abstract_CoreObjectB.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer5.finaliseContainer(Valid_Abstract_CoreObjectC.class));

        // Check you cant double run
        FieldContainer fieldContainer6 = new FieldContainer();
        assertDoesNotThrow(() -> fieldContainer6.endLayer(CoreObject.class));
        assertDoesNotThrow(() -> fieldContainer6.endLayer(Valid_Abstract_CoreObjectA.class));
        assertDoesNotThrow(() -> fieldContainer6.endLayer(Valid_Abstract_CoreObjectB.class));
        assertDoesNotThrow(() -> fieldContainer6.endLayer(Valid_Abstract_CoreObjectC.class));
        assertDoesNotThrow(() -> fieldContainer6.finaliseContainer(Valid_CoreObjectD.class));
        assertThrows(IllegalStateException.class, () -> fieldContainer6.finaliseContainer(Valid_CoreObjectD.class));
    }

    @Test
    void get() {
        // Check test objects
        FieldContainer fieldContainer = Valid_CoreObjectD.getFieldContainer();
        for (DataField<?> field : fieldContainer.getList()) {
            assertEquals(field, fieldContainer.get(field.getIdentifierName()));
        }

        // Check all real objects
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            fieldContainer = CoreObject.getFieldContainer(dClass);
            for (DataField<?> field : fieldContainer.getList()) {
                assertEquals(field, fieldContainer.get(field.getIdentifierName()));
            }
        }
    }

    @Test
    void getList() {
        // Check test objects
        FieldContainer fieldContainer = Valid_CoreObjectD.getFieldContainer();
        assertEquals(fieldContainer.masterMap.size(), fieldContainer.getList().size());
        for (DataField<?> field : fieldContainer.getList()) {
            assertEquals(field, fieldContainer.masterMap.get(field.getIdentifierName()));
        }

        // Check all real objects
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            fieldContainer = CoreObject.getFieldContainer(dClass);
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

        fieldContainer.endLayer(CoreObject.class);

        for (int i = 0; i < 100; i++) {
            Test_DataField toAddA = new Test_DataField(i + "A2");
            Test_DataField toAddB = new Test_DataField(i + "B2");
            fieldContainer.add(toAddA);
            assertEquals(toAddA, fieldContainer.getLast());
            fieldContainer.add(i + "A2", toAddB);
            assertEquals(toAddB, fieldContainer.getLast());
        }

        fieldContainer.endLayer(Valid_Abstract_CoreObjectA.class);

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
    void testCoreObjects() {
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            FieldContainer fieldContainer = CoreObject.getFieldContainer(dClass);
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

    abstract static class Valid_Abstract_CoreObjectA extends CoreObject {
        public static FieldContainer getFieldContainer() {
            FieldContainer fieldContainer = CoreObject.getFieldContainer();
            return fieldContainer.endLayer(Valid_Abstract_CoreObjectA.class);
        }
    }

    abstract static class Valid_Abstract_CoreObjectB extends Valid_Abstract_CoreObjectA {
        public static FieldContainer getFieldContainer() {
            FieldContainer fieldContainer = Valid_Abstract_CoreObjectA.getFieldContainer();
            return fieldContainer.endLayer(Valid_Abstract_CoreObjectB.class);
        }
    }

    abstract static class Valid_Abstract_CoreObjectC extends Valid_Abstract_CoreObjectB {
        public static FieldContainer getFieldContainer() {
            FieldContainer fieldContainer = Valid_Abstract_CoreObjectB.getFieldContainer();
            return fieldContainer.endLayer(Valid_Abstract_CoreObjectC.class);
        }
    }

    static class Valid_CoreObjectD extends Valid_Abstract_CoreObjectC {
        public static FieldContainer getFieldContainer() {
            FieldContainer fieldContainer = Valid_Abstract_CoreObjectC.getFieldContainer();
            return fieldContainer.finaliseContainer(Valid_CoreObjectD.class);
        }
    }

    abstract static class Valid_Abstract_CoreObjectE extends Valid_Abstract_CoreObjectC {
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
