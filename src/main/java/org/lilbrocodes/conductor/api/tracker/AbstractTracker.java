package org.lilbrocodes.conductor.api.tracker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractTracker<X, Y> {
    private final Map<X, Y> tracker = new HashMap<>();

    public abstract void modifyTrackedValues();

    public void tick(Set<X> existing) {
        for (X key : existing) {
            tracker.putIfAbsent(key, null);
        }

        tracker.keySet().removeIf(key -> !existing.contains(key));

        modifyTrackedValues();
    }

    public Y get(X key) {
        return tracker.get(key);
    }

    public void put(X key, Y value) {
        tracker.put(key, value);
    }

    public Map<X, Y> getTrackedItems() {
        return tracker;
    }
}
