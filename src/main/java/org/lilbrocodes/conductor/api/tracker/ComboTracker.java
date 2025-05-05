package org.lilbrocodes.conductor.api.tracker;

public class ComboTracker<X> extends AbstractTracker<X, java.lang.Integer> {
    @Override
    public void modifyTrackedValues() {}

    public void combo(X key) {
        put(key, get(key) + 1);
    }

    public void breakCombo(X key) {
        put(key, 0);
    }
}
