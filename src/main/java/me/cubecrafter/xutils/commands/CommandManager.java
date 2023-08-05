package me.cubecrafter.xutils.commands;

import lombok.Getter;
import lombok.Setter;
import me.cubecrafter.xutils.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public final class CommandManager {

    private static CommandManager instance;

    private final Map<String, CommandWrapper> commands = new HashMap<>();
    private final CommandMap commandMap;

    private String permissionMessage;
    private String unknownCommandMessage;

    private CommandManager() {
        this.commandMap = (CommandMap) ReflectionUtil.getFieldValue(Bukkit.getServer().getClass(), "commandMap", Bukkit.getServer());
    }

    public CommandWrapper register(CommandWrapper command) {
        commands.put(command.getName(), command);
        commandMap.register(command.getPlugin().getName(), command);
        return command;
    }

    public CommandWrapper getCommand(String name) {
        return commands.get(name);
    }

    public static CommandManager get() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

}
