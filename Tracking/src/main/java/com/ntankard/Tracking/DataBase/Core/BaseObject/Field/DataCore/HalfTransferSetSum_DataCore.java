package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataCore;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.CoreObject.Field.Listener.FieldChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Value;

public class HalfTransferSetSum_DataCore extends DataCore<Double> implements FieldChangeListener<List<HalfTransfer>> {

    /**
     * Interface to allow the value to be modified from just the sum
     */
    public interface Calculator {

        /**
         * Based on the rolling sum perform whatever conversion are required to get the correct value. All calculations should be static
         *
         * @param rollingSum The sum of the set
         * @return The actual value to use
         */
        Double calculate(Double rollingSum);
    }

    /**
     * The source of the set
     */
    private final DataField<List<HalfTransfer>> setField;

    /**
     * Listener for changes in the value of any of the transfers
     */
    private final FieldChangeListener<Double> valueFieldChangeListener;

    /**
     * Listener for changes in the currency of any of the transfers
     */
    private final FieldChangeListener<Currency> currencyFieldChangeListener;

    /**
     * All objects seen from the set
     */
    private final List<HalfTransfer> knowObjects = new ArrayList<>();

    /**
     * The current sum
     */
    private Double rollingSum = 0.0;

    /**
     * The calculator to be run on the rolling sum before returning it
     */
    private final Calculator calculator;

    /**
     * Constructor
     */
    public HalfTransferSetSum_DataCore(DataField<List<HalfTransfer>> setField) {
        this(setField, null);
    }

    /**
     * Constructor
     */
    public HalfTransferSetSum_DataCore(DataField<List<HalfTransfer>> setField, Calculator calculator) {
        this.setField = setField;
        this.calculator = calculator;

        valueFieldChangeListener = (field, oldValue, newValue) -> {
            Currency currency = ((HalfTransfer) field.getContainer()).getCurrency();
            Double pastValue = get();
            rollingSum -= currency.getToPrimary() * oldValue;
            rollingSum += currency.getToPrimary() * newValue;

            for (FieldChangeListener<Double> fieldChangeListener : getDataField().getFieldChangeListeners()) {
                fieldChangeListener.valueChanged(getDataField(), pastValue, get());
            }
        };

        currencyFieldChangeListener = (field, oldValue, newValue) -> {
            Double value = ((HalfTransfer) field.getContainer()).getValue();
            Double pastValue = get();
            rollingSum -= oldValue.getToPrimary() * value;
            rollingSum += newValue.getToPrimary() * value;

            for (FieldChangeListener<Double> fieldChangeListener : getDataField().getFieldChangeListeners()) {
                fieldChangeListener.valueChanged(getDataField(), pastValue, get());
            }
        };
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void attachToField(DataField<Double> dataField) {
        super.attachToField(dataField);
        setField.addChangeListener(this);
        for (HalfTransfer halfTransfer : setField.get()) {
            valueChanged(setField, null, Collections.singletonList(halfTransfer));
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void detachFromField(DataField<Double> field) {
        if (knowObjects.size() != 0) {
            throw new UnsupportedOperationException(); // Cant delete and object with this core atm
        }
        setField.removeChangeListener(this);
        super.detachFromField(field);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void valueChanged(DataField<List<HalfTransfer>> field, List<HalfTransfer> oldValue, List<HalfTransfer> newValue) {
        if (oldValue == null && newValue != null) {
            for (HalfTransfer halfTransfer : newValue) {
                if (knowObjects.contains(halfTransfer)) {
                    throw new IllegalStateException();
                }

                halfTransfer.<Double>getField(HalfTransfer_Value).addChangeListener(valueFieldChangeListener);
                halfTransfer.<Currency>getField(HalfTransfer_Currency).addChangeListener(currencyFieldChangeListener);
                valueFieldChangeListener.valueChanged(halfTransfer.getField(HalfTransfer_Value), 0.0, halfTransfer.getValue());
                knowObjects.add(halfTransfer);
            }
        } else if (oldValue != null && newValue == null) {
            for (HalfTransfer halfTransfer : oldValue) {
                if (!knowObjects.contains(halfTransfer)) {
                    throw new IllegalStateException();
                }

                valueFieldChangeListener.valueChanged(halfTransfer.getField(HalfTransfer_Value), halfTransfer.getValue(), 0.0);
                halfTransfer.<Double>getField(HalfTransfer_Value).removeChangeListener(valueFieldChangeListener);
                halfTransfer.<Currency>getField(HalfTransfer_Currency).removeChangeListener(currencyFieldChangeListener);
                knowObjects.remove(halfTransfer);
            }
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double get() {
        if (calculator != null) {
            return calculator.calculate(rollingSum);
        }
        return rollingSum;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void set(Double toSet) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void initialSet(Double toSet) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canEdit() {
        return false;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canInitialSet() {
        return false;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean doseSupportChangeListeners() {
        return true;
    }
}
