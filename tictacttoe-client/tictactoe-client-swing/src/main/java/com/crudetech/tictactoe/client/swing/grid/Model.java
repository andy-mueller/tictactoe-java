package com.crudetech.tictactoe.client.swing.grid;

import com.crudetech.event.Event;
import com.crudetech.event.EventObject;
import com.crudetech.event.EventSupport;

import static com.crudetech.matcher.Verify.verifyThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class Model<T> {
    private T modelObject;
    private final EventSupport<ChangedEventObject<Model<T>>> changedEvent = new EventSupport<>();

    public Model(T modelObject) {
        this.modelObject = modelObject;
    }

    public Model() {
        this(null);
    }

    public T getModelObject() {
        return modelObject;
    }

    public void setModelObject(T grid) {
        verifyThat(grid, is((notNullValue())));
        this.modelObject = grid;
        changedEvent.fireEvent(new ChangedEventObject<>(this));
    }

    public Event<ChangedEventObject<Model<T>>> changed() {
        return changedEvent;
    }

    public static class ChangedEventObject<TModel extends Model<?>> extends EventObject<TModel> {
        ChangedEventObject(TModel changedModel) {
            super(changedModel);
        }
    }
}
