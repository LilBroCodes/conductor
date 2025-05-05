package org.lilbrocodes.conductor.api.tracker;

import java.util.Map;
import java.util.Set;

public class CooldownTracker<T> extends AbstractTracker<T, java.lang.Integer> {
    private final BooleanTracker<T> rechargeTracker = new BooleanTracker<>();

    @Override
    public void modifyTrackedValues() {
        for (Map.Entry<T, Integer> entry : getTrackedItems().entrySet()) {
            if (rechargeTracker.get(entry.getKey())) {
                if (entry.getValue() > 0) put(entry.getKey(), entry.getValue() - 1);
                else if (entry.getValue() < 0) put(entry.getKey(), 0);

                if (entry.getValue() == 0) rechargeTracker.put(entry.getKey(), false);
            }
        }
    }

    @Override
    public void tick(Set<T> existing) {
        super.tick(existing);
        rechargeTracker.tick(existing);
    }

    public void recharge(T key) {
        rechargeTracker.put(key, true);
    }
}
