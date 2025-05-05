package org.lilbrocodes.conductor.api.registry;

import org.lilbrocodes.conductor.api.identifier.Identifier;

import java.util.HashMap;
import java.util.Map;

public class Registry<T> {
    private final Map<Identifier, T> registry = new HashMap<>();

    public void register(Identifier id, T item) {
        if (!this.registry.containsKey(id)) {
            this.registry.put(id, item);
        }
    }

    public Map<Identifier, T> getRegisteredItems() {
        return registry;
    }
}
