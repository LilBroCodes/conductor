package org.lilbrocodes.conductor.api.tracker;

import java.util.Map;

public class CooldownTracker<X> extends AbstractTracker<X, java.lang.Integer> {
    @Override
    public void modifyTrackedValues() {
        for (Map.Entry<X, Integer> entry : getTrackedItems().entrySet()) {
            if (entry.getValue() > 0) put(entry.getKey(), entry.getValue() - 1);
            else if (entry.getValue() < 0) put(entry.getKey(), 0);
        }
    }
}
