package com.github.jummes.timedcommands.manager;

import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.timedcommands.timed.TimedCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class TimedCommandManager extends ModelManager<TimedCommand> {

    private List<TimedCommand> commands;

    public TimedCommandManager(Class<TimedCommand> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin, new HashMap<>());
        this.commands = database.loadObjects();
    }

    public TimedCommand getByName(String name) {
        return commands.stream().filter(timedCommand -> timedCommand.getName().equals(name)).findFirst().orElse(null);
    }
}
