package com.ntankard.dynamicGUI.DataObject;

import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static com.ntankard.testUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataObjectSchemaTest {

    @Test
    void add() {
        DataObject_Schema dataObjectSchema = Valid_Abstract_DataObjectC.getFieldContainer();
        DataField_Schema<?> toAdd = new Test_DataField("Test1");

        // Check valid add
        assertDoesNotThrow(() -> dataObjectSchema.add(toAdd));
        assertEquals(toAdd, dataObjectSchema.get("Test1"));
        assertTrue(dataObjectSchema.getList().contains(toAdd));

        // Check you cant double add
        assertThrows(IllegalArgumentException.class, () -> dataObjectSchema.add(toAdd));

        // Check you cant add with the same name
        DataField_Schema<?> toAdd2 = new Test_DataField("Test1");
        assertThrows(IllegalArgumentException.class, () -> dataObjectSchema.add(toAdd2));

        // Check you can add again after a failure
        DataField_Schema<?> toAdd3 = new Test_DataField("Test3");
        assertDoesNotThrow(() -> dataObjectSchema.add(toAdd3));
        assertEquals(toAdd3, dataObjectSchema.get("Test3"));
        assertTrue(dataObjectSchema.getList().contains(toAdd3));

        // Check you still cant double add
        assertThrows(IllegalArgumentException.class, () -> dataObjectSchema.add(toAdd3));

        // Check you cant add after finalization
        DataField_Schema<?> toAdd4 = new Test_DataField("Test4");
        dataObjectSchema.finaliseContainer(Valid_DataObjectD.class);
        assertThrows(IllegalStateException.class, () -> dataObjectSchema.add(toAdd4));
    }

    @Test
    void endLayer() {
        DataObject_Schema dataObjectSchema = new DataObject_Schema();

        // check a valid stack
        assertDoesNotThrow(() -> dataObjectSchema.endLayer(DataObject.class));
        assertDoesNotThrow(() -> dataObjectSchema.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> dataObjectSchema.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> dataObjectSchema.endLayer(Valid_Abstract_DataObjectC.class));

        // Check that you can no double end
        assertThrows(IllegalArgumentException.class, () -> dataObjectSchema.endLayer(Valid_Abstract_DataObjectB.class));

        // Check that you can not end a solid object
        assertThrows(IllegalStateException.class, () -> dataObjectSchema.endLayer(Valid_DataObjectD.class));

        // check that you can not add after finalise
        DataObject_Schema dataObjectSchema2 = new DataObject_Schema();
        assertDoesNotThrow(() -> dataObjectSchema2.endLayer(DataObject.class));
        assertDoesNotThrow(() -> dataObjectSchema2.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> dataObjectSchema2.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> dataObjectSchema2.endLayer(Valid_Abstract_DataObjectC.class));
        assertDoesNotThrow(() -> dataObjectSchema2.finaliseContainer(Valid_DataObjectD.class));
        assertThrows(IllegalStateException.class, () -> dataObjectSchema2.endLayer(Valid_Abstract_DataObjectE.class));
    }

    @Test
    void finaliseContainer() {
        // Check valid sequence
        DataObject_Schema dataObjectSchema = new DataObject_Schema();
        assertDoesNotThrow(() -> dataObjectSchema.endLayer(DataObject.class));
        assertDoesNotThrow(() -> dataObjectSchema.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> dataObjectSchema.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> dataObjectSchema.endLayer(Valid_Abstract_DataObjectC.class));
        assertDoesNotThrow(() -> dataObjectSchema.finaliseContainer(Valid_DataObjectD.class));

        // Check you cant skip a layer
        DataObject_Schema dataObjectSchema2 = new DataObject_Schema();
        assertDoesNotThrow(() -> dataObjectSchema2.endLayer(DataObject.class));
        assertDoesNotThrow(() -> dataObjectSchema2.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> dataObjectSchema2.endLayer(Valid_Abstract_DataObjectB.class));
        assertThrows(IllegalStateException.class, () -> dataObjectSchema2.finaliseContainer(Valid_DataObjectD.class));

        DataObject_Schema dataObjectSchema3 = new DataObject_Schema();
        assertDoesNotThrow(() -> dataObjectSchema3.endLayer(DataObject.class));
        assertDoesNotThrow(() -> dataObjectSchema3.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> dataObjectSchema3.endLayer(Valid_Abstract_DataObjectC.class));
        assertThrows(IllegalStateException.class, () -> dataObjectSchema3.finaliseContainer(Valid_DataObjectD.class));

        DataObject_Schema dataObjectSchema4 = new DataObject_Schema();
        assertDoesNotThrow(() -> dataObjectSchema4.endLayer(DataObject.class));
        assertDoesNotThrow(() -> dataObjectSchema4.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> dataObjectSchema4.endLayer(Valid_Abstract_DataObjectC.class));
        assertThrows(IllegalStateException.class, () -> dataObjectSchema4.finaliseContainer(Valid_DataObjectD.class));

        // Check you can't finalise an abstract layer
        DataObject_Schema dataObjectSchema5 = new DataObject_Schema();
        assertDoesNotThrow(() -> dataObjectSchema5.endLayer(DataObject.class));
        assertDoesNotThrow(() -> dataObjectSchema5.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> dataObjectSchema5.endLayer(Valid_Abstract_DataObjectB.class));
        assertThrows(IllegalStateException.class, () -> dataObjectSchema5.finaliseContainer(Valid_Abstract_DataObjectC.class));

        // Check you cant double run
        DataObject_Schema dataObjectSchema6 = new DataObject_Schema();
        assertDoesNotThrow(() -> dataObjectSchema6.endLayer(DataObject.class));
        assertDoesNotThrow(() -> dataObjectSchema6.endLayer(Valid_Abstract_DataObjectA.class));
        assertDoesNotThrow(() -> dataObjectSchema6.endLayer(Valid_Abstract_DataObjectB.class));
        assertDoesNotThrow(() -> dataObjectSchema6.endLayer(Valid_Abstract_DataObjectC.class));
        assertDoesNotThrow(() -> dataObjectSchema6.finaliseContainer(Valid_DataObjectD.class));
        assertThrows(IllegalStateException.class, () -> dataObjectSchema6.finaliseContainer(Valid_DataObjectD.class));
    }

    @Test
    void get() {
        // Check test objects
        DataObject_Schema dataObjectSchema = Valid_DataObjectD.getFieldContainer();
        for (DataField_Schema<?> field : dataObjectSchema.getList()) {
            assertEquals(field, dataObjectSchema.get(field.getIdentifierName()));
        }

        // Check all real objects
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            dataObjectSchema = TrackingDatabase_Schema.getFieldContainer(dClass);
            for (DataField_Schema<?> field : dataObjectSchema.getList()) {
                assertEquals(field, dataObjectSchema.get(field.getIdentifierName()));
            }
        }
    }

    @Test
    void getList() {
        // Check test objects
        DataObject_Schema dataObjectSchema = Valid_DataObjectD.getFieldContainer();
        assertEquals(dataObjectSchema.masterMap.size(), dataObjectSchema.getList().size());
        for (DataField_Schema<?> field : dataObjectSchema.getList()) {
            assertEquals(field, dataObjectSchema.masterMap.get(field.getIdentifierName()));
        }

        // Check all real objects
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            dataObjectSchema = TrackingDatabase_Schema.getFieldContainer(dClass);
            assertEquals(dataObjectSchema.masterMap.size(), dataObjectSchema.getList().size());
            for (DataField_Schema<?> field : dataObjectSchema.getList()) {
                assertEquals(field, dataObjectSchema.masterMap.get(field.getIdentifierName()));
            }
        }
    }

    @Test
    void getLast() {
        DataObject_Schema dataObjectSchema = new DataObject_Schema();
        assertNull(dataObjectSchema.getLast());

        for (int i = 0; i < 100; i++) {
            Test_DataField toAddA = new Test_DataField(i + "A1");
            Test_DataField toAddB = new Test_DataField(i + "B1");
            dataObjectSchema.add(toAddA);
            assertEquals(toAddA, dataObjectSchema.getLast());
            dataObjectSchema.add(i + "A1", toAddB);
            assertEquals(toAddB, dataObjectSchema.getLast());
        }

        dataObjectSchema.endLayer(DataObject.class);

        for (int i = 0; i < 100; i++) {
            Test_DataField toAddA = new Test_DataField(i + "A2");
            Test_DataField toAddB = new Test_DataField(i + "B2");
            dataObjectSchema.add(toAddA);
            assertEquals(toAddA, dataObjectSchema.getLast());
            dataObjectSchema.add(i + "A2", toAddB);
            assertEquals(toAddB, dataObjectSchema.getLast());
        }

        dataObjectSchema.endLayer(Valid_Abstract_DataObjectA.class);

        for (int i = 0; i < 100; i++) {
            Test_DataField toAddA = new Test_DataField(i + "A3");
            Test_DataField toAddB = new Test_DataField(i + "B3");
            dataObjectSchema.add(toAddA);
            assertEquals(toAddA, dataObjectSchema.getLast());
            dataObjectSchema.add(i + "A3", toAddB);
            assertEquals(toAddB, dataObjectSchema.getLast());
        }
    }

    @Test
    void testOrder() {
        // TODO TBD
    }

    @Test
    void testDataObjects() {
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            DataObject_Schema dataObjectSchema = TrackingDatabase_Schema.getFieldContainer(dClass);
            if (Modifier.isAbstract(dClass.getModifiers())) {
                assertEquals(dClass, dataObjectSchema.inheritedObjects.get(dataObjectSchema.inheritedObjects.size() - 1));
            } else {
                assertEquals(dClass, dataObjectSchema.solidObjectType);
                for (DataField_Schema<?> dataFieldSchema : dataObjectSchema.getList()) {
                    assertEquals(dClass, dataFieldSchema.getParentType());
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Test Objects ##################################################
    //------------------------------------------------------------------------------------------------------------------

    abstract static class Valid_Abstract_DataObjectA extends DataObject {
        public static DataObject_Schema getFieldContainer() {
            DataObject_Schema dataObjectSchema = DataObject.getFieldContainer();
            return dataObjectSchema.endLayer(Valid_Abstract_DataObjectA.class);
        }
    }

    abstract static class Valid_Abstract_DataObjectB extends Valid_Abstract_DataObjectA {
        public static DataObject_Schema getFieldContainer() {
            DataObject_Schema dataObjectSchema = Valid_Abstract_DataObjectA.getFieldContainer();
            return dataObjectSchema.endLayer(Valid_Abstract_DataObjectB.class);
        }
    }

    abstract static class Valid_Abstract_DataObjectC extends Valid_Abstract_DataObjectB {
        public static DataObject_Schema getFieldContainer() {
            DataObject_Schema dataObjectSchema = Valid_Abstract_DataObjectB.getFieldContainer();
            return dataObjectSchema.endLayer(Valid_Abstract_DataObjectC.class);
        }
    }

    static class Valid_DataObjectD extends Valid_Abstract_DataObjectC {
        public static DataObject_Schema getFieldContainer() {
            DataObject_Schema dataObjectSchema = Valid_Abstract_DataObjectC.getFieldContainer();
            return dataObjectSchema.finaliseContainer(Valid_DataObjectD.class);
        }
    }

    abstract static class Valid_Abstract_DataObjectE extends Valid_Abstract_DataObjectC {
    }

    static class Test_DataField extends DataField_Schema<Object> {
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
