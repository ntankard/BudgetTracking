package com.ntankard.coreObject.factory;

import com.ntankard.testUtil.ClassInspectionUtil;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.coreObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.coreObject.factory.ObjectFactory;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectFactoryTest {

    @Test
    void multipleParent() {
        // Check each class
        for (Class<? extends DataObject> aClass : ClassInspectionUtil.getSolidClasses()) {

            // For each class, check each factory
            DataObject_Schema container = TrackingDatabase_Schema.get().getClassSchema(aClass);
            for (ObjectFactory<?> factory : container.getObjectFactories()) {

                // If the factory is DoubleParentFactory
                if (DoubleParentFactory.class.isAssignableFrom(factory.getClass())) {
                    DoubleParentFactory<?, ?, ?> doubleParentFactory = ((DoubleParentFactory<?, ?, ?>) factory);

                    // Check the secondary generator type
                    DataObject_Schema containerToCheck = TrackingDatabase_Schema.get().getClassSchema(doubleParentFactory.getSecondaryGeneratorType());
                    boolean found = false;
                    for (ObjectFactory<?> factoryToCheck : containerToCheck.getObjectFactories()) {

                        // Check at least 1 of the factories also has the original class as secondary
                        if (DoubleParentFactory.class.isAssignableFrom(factoryToCheck.getClass())) {
                            DoubleParentFactory<?, ?, ?> doubleParentFactoryToCheck = ((DoubleParentFactory<?, ?, ?>) factoryToCheck);
                            if (doubleParentFactoryToCheck.getSecondaryGeneratorType().isAssignableFrom(aClass)) {
                                found = true;
                                break;
                            }
                        }
                    }

                    // Verify that ever instance of DoubleParentFactory has a matching DoubleParentFactory in its secondary generator class
                    assertTrue(found, "Class " + aClass.getSimpleName() + " implements a DoubleParentFactory bot the other class, " + doubleParentFactory.getSecondaryGeneratorType().getSimpleName() + " dose not also implement the factory");
                }
            }
        }
    }
}
