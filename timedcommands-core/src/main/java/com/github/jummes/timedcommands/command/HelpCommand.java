package com.github.jummes.timedcommands.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.util.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class HelpCommand extends AbstractCommand {
    public HelpCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        sender.sendMessage(MessageUtils.color("&c&lTimedCommands &6&lHelp\n" +
                "&c/tc help &7to display the help message.\n" +
                "&c/tc start &6[name] <args> &7to start a timed command. &6[name] &7is the timed command name, " +
                "&6<args> &7are the args you want to input\n" +
                "&c/tc list &7Show the timed commands GUI."));
    }

    @Override
    protected boolean isOnlyPlayer() {
        return false;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("timedcommands.admin.help");
    }
}
