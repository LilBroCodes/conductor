package org.lilbrocodes.conductor.api.tracker;

import java.lang.Boolean;

public class BooleanTracker<T> extends AbstractTracker<T, Boolean> {
    public BooleanTracker() {
        setDefault(false);
    }

    @Override
    public void modifyTrackedValues() {

    }

    public void flip(T key) {
        put(key, Boolean.FALSE.equals(get(key)));
    }
}
