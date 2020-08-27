package com.ntankard.Tracking.DataBase.Core.BaseObject.Factory;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.dynamicGUI.CoreObject.Factory.ObjectFactory;

public class SingleParentFactory<GeneratedType extends DataObject, GeneratorType extends DataObject> extends ObjectFactory<GeneratedType, GeneratorType> {

    /**
     * Interface to allow the generated object to be built
     */
    public interface GeneratedObjectConstructor<GeneratedType extends DataObject, GeneratorType extends DataObject> {

        /**
         * Constructor one of the generated objects based on the
         *
         * @param generator The object generating the new object
         * @return A constructed, but not added object
         */
        GeneratedType generate(GeneratorType generator);
    }

    /**
     * The object constructor
     */
    private final GeneratedObjectConstructor<GeneratedType, GeneratorType> constructor;

    /**
     * Constructor
     */
    public SingleParentFactory(Class<GeneratedType> generatedType, GeneratedObjectConstructor<GeneratedType, GeneratorType> constructor) {
        super(generatedType);
        this.constructor = constructor;
    }

    /**
     * Constructor
     */
    public SingleParentFactory(Class<GeneratedType> generatedType, GeneratedObjectConstructor<GeneratedType, GeneratorType> constructor, GeneratorMode mode) {
        super(generatedType, mode);
        this.constructor = constructor;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void generate(GeneratorType generator) {
        if (shouldBuild(generator.getChildren(getGeneratedType()).size())) {
            constructor.generate(generator).add();
        }
    }
}
