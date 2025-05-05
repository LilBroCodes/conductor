package org.lilbrocodes.conductor.api.config.command;

import org.lilbrocodes.commander.api.argument.ParameterType;
import org.lilbrocodes.commander.api.argument.TypedParameter;
import org.lilbrocodes.commander.api.executor.CommandActionNode;
import org.lilbrocodes.commander.api.executor.CommandHybridNode;
import org.lilbrocodes.commander.api.util.StaticChatUtil;

import java.util.List;
import java.util.function.Function;

/**
 * Represents a configurable value as a command node with a "set" subcommand.
 * @param <T> The type of the config value.
 */
public class ConfigValueCommand<T> extends CommandHybridNode {
    private T value;
    private PostSetExecutor executor;
    private final Function<String, String> messagePrefix;

    public ConfigValueCommand(String name, ParameterType type, T value) {
        this(name, type, value, s -> "§6§lConfig§r "); // default prefix
    }

    public ConfigValueCommand(String name, ParameterType type, T value, Function<String, String> messagePrefix) {
        super(
                name,
                "Shows the current value of " + name,
                messagePrefix.apply("")
        );
        this.value = value;
        this.messagePrefix = messagePrefix;

        this.addExecutor((sender, args) -> {
            sender.sendMessage(String.format("%sThe value of '%s' is %s", messagePrefix.apply(""), name, this.value));
        });

        CommandActionNode set = new CommandActionNode(
                "set",
                "Sets the value of this config",
                messagePrefix.apply(""),
                List.of(new TypedParameter("value", type)),
                (sender, args) -> {
                    if (args.get(0) != null) {
                        this.value = (T) args.get(0);
                        StaticChatUtil.info(
                                sender,
                                messagePrefix.apply(""),
                                String.format("Value of '%s' set to '%s'.", this.name, this.value)
                        );
                    }

                    if (executor != null) {
                        executor.valueChanged(args.get(0));
                    }
                }
        );

        addChild(set);
    }

    public void addPostSetTrigger(PostSetExecutor executor) {
        this.executor = executor;
    }

    public interface PostSetExecutor {
        void valueChanged(Object newValue);
    }
}
