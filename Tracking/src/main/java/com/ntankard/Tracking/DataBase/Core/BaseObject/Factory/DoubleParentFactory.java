package com.ntankard.Tracking.DataBase.Core.BaseObject.Factory;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.ObjectFactory;

public class DoubleParentFactory<GeneratedType extends DataObject, GeneratorType extends DataObject, SecondaryGeneratorType extends DataObject> extends ObjectFactory<GeneratedType, GeneratorType> {

    /**
     * Interface to allow the generated object to be built
     */
    public interface GeneratedObjectConstructor<GeneratedType extends DataObject, GeneratorType extends DataObject, SecondaryGeneratorType extends DataObject> {

        /**
         * Constructor one of the generated objects based on the
         *
         * @param generator The object generating the new object
         * @return A constructed, but not added object
         */
        GeneratedType generate(GeneratorType generator, SecondaryGeneratorType secondaryGenerator);
    }

    /**
     * The other type that also generated the object.
     */
    private final Class<SecondaryGeneratorType> secondaryGeneratorType;

    /**
     * The object constructor
     */
    private final GeneratedObjectConstructor<GeneratedType, GeneratorType, SecondaryGeneratorType> constructor;

    /**
     * Constructor
     */
    public DoubleParentFactory(Class<GeneratedType> generatedType, Class<SecondaryGeneratorType> secondaryGeneratorType, GeneratedObjectConstructor<GeneratedType, GeneratorType, SecondaryGeneratorType> constructor) {
        super(generatedType);
        this.secondaryGeneratorType = secondaryGeneratorType;
        this.constructor = constructor;
    }

    /**
     * Constructor
     */
    public DoubleParentFactory(Class<GeneratedType> generatedType, Class<SecondaryGeneratorType> secondaryGeneratorType, GeneratedObjectConstructor<GeneratedType, GeneratorType, SecondaryGeneratorType> constructor, GeneratorMode mode) {
        super(generatedType, mode);
        this.secondaryGeneratorType = secondaryGeneratorType;
        this.constructor = constructor;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void generate(GeneratorType generator) {
        for (SecondaryGeneratorType secondaryGenerator : TrackingDatabase.get().get(secondaryGeneratorType)) {
            if (shouldBuild(new TwoParent_Children_Set<>(getGeneratedType(), generator, secondaryGenerator).get().size())) {
                constructor.generate(generator, secondaryGenerator).add();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Class<SecondaryGeneratorType> getSecondaryGeneratorType() {
        return secondaryGeneratorType;
    }
}
