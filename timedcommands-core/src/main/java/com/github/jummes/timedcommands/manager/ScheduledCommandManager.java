package com.github.jummes.timedcommands.manager;

import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.timedcommands.TimedCommands;
import com.github.jummes.timedcommands.scheduled.ScheduledCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduledCommandManager extends ModelManager<ScheduledCommand> {

    private List<ScheduledCommand> commands;

    public ScheduledCommandManager(Class<ScheduledCommand> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin, new HashMap<>());
        commands = database.loadObjects();
        Bukkit.getScheduler().runTaskTimer(TimedCommands.getInstance(), () -> {
            List<ScheduledCommand> toExecute = commands.stream().filter(command ->
                    command.compareTo(new ScheduledCommand("", new Date())) < 0).collect(Collectors.toList());
            toExecute.forEach(this::executeCommand);
        }, 0, 5);
    }


    public void scheduleCommand(ScheduledCommand command) {
        commands.add(command);
        saveModel(command);
    }

    private void executeCommand(ScheduledCommand command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.getCommand());
        deleteModel(command);
    }
}
