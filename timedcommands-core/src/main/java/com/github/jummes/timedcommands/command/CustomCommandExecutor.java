package com.github.jummes.timedcommands.command;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String[] str = new String[1];
        str[0] = command.getName();
        new TimedCommandsStart(sender, "start", (String[]) ArrayUtils.addAll(str, args),
                sender instanceof Player).checkExecution();
        return false;
    }
}
