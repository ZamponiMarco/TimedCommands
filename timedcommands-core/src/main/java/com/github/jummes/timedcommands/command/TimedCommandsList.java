package com.github.jummes.timedcommands.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.gui.model.ModelCollectionInventoryHolder;
import com.github.jummes.timedcommands.TimedCommands;
import com.github.jummes.timedcommands.timed.TimedCommand;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class TimedCommandsList extends AbstractCommand {
    public TimedCommandsList(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @SneakyThrows
    @Override
    protected void execute() {
        Player p = (Player) sender;
        p.openInventory(new ModelCollectionInventoryHolder<TimedCommand>(TimedCommands.getInstance(),
                TimedCommands.getInstance().getTimedCommandManager(), "commands").getInventory());
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("timedcommands.admin.list");
    }
}
