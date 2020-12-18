package com.github.jummes.timedcommands.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.timedcommands.TimedCommands;
import com.github.jummes.timedcommands.timed.TimedCommand;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimedCommandsStart extends AbstractCommand {

    public TimedCommandsStart(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        if (arguments.length < 1)
            return;
        TimedCommand command = TimedCommands.getInstance().getTimedCommandManager().getByName(arguments[0]);
        if (command == null) {
            sender.sendMessage(Libs.getLocale().get("commands.incorrect-usage"));
            return;
        }
        if (command.getPlaceholders().size() > arguments.length - 1) {
            sender.sendMessage(Libs.getLocale().get("commands.not-enough-arguments"));
            return;
        }
        List<String> args = new ArrayList<>();
        if (arguments.length > 1) {
            args = Lists.newArrayList(Arrays.copyOfRange(arguments, 1, arguments.length));
        }
        command.scheduleCommands(args);
    }

    @Override
    protected boolean isOnlyPlayer() {
        return false;
    }

    @Override
    protected Permission getPermission() {
        if (arguments.length < 1) {
            return new Permission("timedcommands.admin.help");
        }
        return new Permission("timedcommands.start." + arguments[0], PermissionDefault.OP);
    }
}
