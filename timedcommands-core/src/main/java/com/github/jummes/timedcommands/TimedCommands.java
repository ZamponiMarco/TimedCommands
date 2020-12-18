package com.github.jummes.timedcommands;

import com.github.jummes.libs.command.PluginCommandExecutor;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.localization.PluginLocale;
import com.github.jummes.timedcommands.command.CustomCommandExecutor;
import com.github.jummes.timedcommands.command.HelpCommand;
import com.github.jummes.timedcommands.command.TimedCommandsList;
import com.github.jummes.timedcommands.command.TimedCommandsStart;
import com.github.jummes.timedcommands.delay.TimeDelay;
import com.github.jummes.timedcommands.manager.ScheduledCommandManager;
import com.github.jummes.timedcommands.manager.TimedCommandManager;
import com.github.jummes.timedcommands.placeholder.Placeholder;
import com.github.jummes.timedcommands.scheduled.ScheduledCommand;
import com.github.jummes.timedcommands.timed.SingleCommand;
import com.github.jummes.timedcommands.timed.TimedCommand;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Map;

public class TimedCommands extends JavaPlugin {

    static {
        Libs.registerSerializables();

        ConfigurationSerialization.registerClass(TimedCommand.class);
        ConfigurationSerialization.registerClass(SingleCommand.class);
        ConfigurationSerialization.registerClass(Placeholder.class);
        ConfigurationSerialization.registerClass(TimeDelay.class);
    }

    @Getter
    private TimedCommandManager timedCommandManager;

    @Getter
    private ScheduledCommandManager scheduledCommandManager;

    private SimpleCommandMap scm;

    public static TimedCommands getInstance() {
        return getPlugin(TimedCommands.class);
    }

    @Override
    public void onEnable() {
        setUpFolder();
        Libs.initializeLibrary(this);
        Libs.getLocale().registerLocaleFiles(this, Lists.newArrayList("en-US"), getConfig().getString("locale"));
        registerCommandMap();
        timedCommandManager = new TimedCommandManager(TimedCommand.class, "yaml", this);
        scheduledCommandManager = new ScheduledCommandManager(ScheduledCommand.class, "yaml", this);
        PluginCommandExecutor ex = new PluginCommandExecutor(HelpCommand.class, "help");
        ex.registerCommand("list", TimedCommandsList.class);
        ex.registerCommand("start", TimedCommandsStart.class);
        getCommand("tc").setExecutor(ex);
    }


    private void setUpFolder() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveDefaultConfig();
        }
    }

    @SneakyThrows
    private void registerCommandMap() {
        SimplePluginManager spm = (SimplePluginManager) getServer().getPluginManager();
        scm = (SimpleCommandMap) FieldUtils.readField(spm, "commandMap", true);
    }

    public void registerCommand(String command) {
        scm.register(getDescription().getName(), getPluginCommand(command));
        getCommand(command).setExecutor(new CustomCommandExecutor());
    }

    @SneakyThrows
    public void unregisterCommand(String command) {
        Map<String, Command> map = (Map<String, Command>) FieldUtils.
                readField(scm, "knownCommands", true);
        map.remove(command);
    }

    @SneakyThrows
    private PluginCommand getPluginCommand(String name) {
        PluginCommand command;
        Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        c.setAccessible(true);
        command = c.newInstance(name, this);
        return command;
    }
}
