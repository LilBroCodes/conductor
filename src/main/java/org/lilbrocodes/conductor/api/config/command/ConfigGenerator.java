package org.lilbrocodes.conductor.api.config.command;

import org.lilbrocodes.commander.api.argument.ParameterType;
import org.lilbrocodes.commander.api.executor.CommandActionNode;
import org.lilbrocodes.commander.api.executor.CommandGroupNode;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

public class ConfigGenerator {

    /**
     * Generates a command group for editing, saving, and loading a config object.
     *
     * @param instance                The config instance with public/private fields to expose as editable.
     * @param saveCallback            Runnable to call when the "save" subcommand is executed.
     * @param loadCallback            Runnable to call when the "load" subcommand is executed.
     * @param messagePrefixProvider   Function that takes a context string and returns a formatted plugin prefix.
     * @return A CommandGroupNode with `edit`, `save`, and `load` subcommands.
     */
    public static CommandGroupNode generateConfigCommand(
            Object instance,
            Runnable saveCallback,
            Runnable loadCallback,
            Function<String, String> messagePrefixProvider
    ) {
        CommandGroupNode root = new CommandGroupNode(
                "config",
                "Configuration command",
                messagePrefixProvider.apply("")
        );

        Class<?> clazz = instance.getClass();
        CommandGroupNode edit = new CommandGroupNode(
                "edit",
                "Edit configuration",
                messagePrefixProvider.apply("Config")
        );

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(instance);

                ParameterType type = mapType(field.getType());
                if (type == null) continue;

                ConfigValueCommand<?> command = new ConfigValueCommand<>(
                        field.getName(),
                        type,
                        value
                );

                command.addPostSetTrigger(newValue -> {
                    try {
                        field.set(instance, newValue);
                    } catch (IllegalAccessException ignored) {
                    }
                });

                edit.addChild(command);
            } catch (IllegalAccessException ignored) {
            }
        }

        CommandActionNode save = new CommandActionNode(
                "save",
                "Saves the configuration",
                messagePrefixProvider.apply("Config"),
                List.of(),
                (sender, args) -> {
                    saveCallback.run();
                    sender.sendMessage(messagePrefixProvider.apply("Config saved successfully."));
                }
        );

        CommandActionNode load = new CommandActionNode(
                "load",
                "Loads the configuration",
                messagePrefixProvider.apply("Config"),
                List.of(),
                (sender, args) -> {
                    loadCallback.run();
                    sender.sendMessage(messagePrefixProvider.apply("Config loaded successfully."));
                }
        );

        root.addChild(edit);
        root.addChild(save);
        root.addChild(load);
        return root;
    }

    /**
     * Maps Java types to command parameter types.
     *
     * Extend this if you support more types.
     */
    private static ParameterType mapType(Class<?> type) {
        if (type == String.class) return ParameterType.GREEDY_STRING;
        if (type == short.class || type == Short.class) return ParameterType.SHORT;
        if (type == int.class || type == Integer.class) return ParameterType.INT;
        if (type == long.class || type == Long.class) return ParameterType.LONG;
        if (type == boolean.class || type == Boolean.class) return ParameterType.BOOL;
        if (type == double.class || type == Double.class) return ParameterType.DOUBLE;
        if (type == float.class || type == Float.class) return ParameterType.FLOAT;
        return null; // unsupported type
    }
}
