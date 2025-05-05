package org.lilbrocodes.conductor.api.config.command;

import org.lilbrocodes.commander.api.argument.ParameterType;
import org.lilbrocodes.commander.api.argument.TypedParameter;
import org.lilbrocodes.commander.api.executor.CommandActionNode;
import org.lilbrocodes.commander.api.executor.CommandHybridNode;
import org.lilbrocodes.commander.api.util.StaticChatUtil;

import java.util.List;
import static org.lilbrocodes.conductor.api.config.command.ConfigGenerator.prefixText;
import static org.lilbrocodes.conductor.api.config.command.ConfigGenerator.prefixMessage;

/**
 * Represents a configurable value as a command node with a "set" subcommand.
 * @param <T> The type of the config value.
 */
public class ConfigValueCommand<T> extends CommandHybridNode {
    private T value;
    private PostSetExecutor executor;

    public ConfigValueCommand(String name, ParameterType type, T value, String pluginName) {
        super(
                name,
                "Shows the current value of " + name,
                prefixText("Config", pluginName)
        );
        this.value = value;

        this.addExecutor((sender, args) -> {
            sender.sendMessage(prefixMessage(String.format("The value of '%s' is %s", name, this.value), pluginName));
        });

        CommandActionNode set = new CommandActionNode(
                "set",
                "Sets the value of this config",
                prefixText("Config", pluginName),
                List.of(new TypedParameter("value", type)),
                (sender, args) -> {
                    if (args.get(0) != null) {
                        this.value = (T) args.get(0);
                        StaticChatUtil.info(
                                sender,
                                prefixText("Config", pluginName),
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
