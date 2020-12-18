package com.github.jummes.timedcommands.timed;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.timedcommands.TimedCommands;
import com.github.jummes.timedcommands.delay.TimeDelay;
import com.github.jummes.timedcommands.placeholder.Placeholder;
import com.github.jummes.timedcommands.scheduled.ScheduledCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@CustomClickable(customCollectionClickConsumer = "getCustomConsumer")
public class SingleCommand implements Model {

    private static final String DELAY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";
    private static final String COMMAND_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0YzIxZDE3YWQ2MzYzODdlYTNjNzM2YmZmNmFkZTg5NzMxN2UxMzc0Y2Q1ZDliMWMxNWU2ZTg5NTM0MzIifX19";

    @Serializable(headTexture = DELAY_HEAD, description = "gui.single-command.delay")
    private TimeDelay delay;
    @Serializable(headTexture = COMMAND_HEAD, description = "gui.single-command.command")
    private String command;

    public SingleCommand() {
        this(new TimeDelay(), "");
    }

    public static SingleCommand deserialize(Map<String, Object> map) {
        TimeDelay delay = (TimeDelay) map.get("delay");
        String command = (String) map.get("command");
        return new SingleCommand(delay, command);
    }

    public void scheduleCommand(List<Placeholder> placeholders, List<String> arguments) {
        String command = renderCommand(placeholders, arguments);
        TimedCommands.getInstance().getScheduledCommandManager().
                scheduleCommand(new ScheduledCommand(command, delay.getDate()));
    }

    private String renderCommand(List<Placeholder> placeholders, List<String> arguments) {
        String base = command;
        for (int i = 0; i < placeholders.size(); i++) {
            Placeholder placeholder = placeholders.get(i);
            String argument = arguments.get(i);
            argument = argument.replaceAll("_", " ");
            base = base.replaceAll("%" + placeholder.getPlaceholder() + "%", argument);
        }
        return base;
    }

    @Override
    public ItemStack getGUIItem() {
        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.color("&7&oExecute the command after:"));
        lore.add(delay.toString());
        lore.addAll(Libs.getLocale().getList("gui.single-command.description"));
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(COMMAND_HEAD),
                "&9&lCommand » &6" + command,
                lore);
    }


    public void getCustomConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath path,
                                  Field field, InventoryClickEvent e) throws IllegalAccessException {
        if (e.getClick().equals(ClickType.LEFT)) {
            path.addModel(this);
            e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, parent, path).getInventory());
        } else if (e.getClick().equals(ClickType.RIGHT)) {
            ((Collection<SingleCommand>) FieldUtils.readField(field,
                    path.getLast() != null ? path.getLast() : path.getModelManager(), true)).remove(this);
            path.addModel(this);
            path.deleteModel();
            path.popModel();
            onRemoval();
            e.getWhoClicked().openInventory(parent.getInventory());
        } else if ((e.getClick().equals(ClickType.NUMBER_KEY))) {
            switch (e.getHotbarButton()) {
                case 0:
                    delay.setSeconds(delay.getSeconds() - 1);
                    break;
                case 1:
                    delay.setSeconds(delay.getSeconds() + 1);
                    break;
                case 2:
                    delay.setMinutes(delay.getMinutes() - 1);
                    break;
                case 3:
                    delay.setMinutes(delay.getMinutes() + 1);
                    break;
                case 4:
                    delay.setHours(delay.getHours() - 1);
                    break;
                case 5:
                    delay.setHours(delay.getHours() + 1);
                    break;
                case 6:
                    delay.setDays(delay.getDays() - 1);
                    break;
                case 7:
                    delay.setDays(delay.getDays() + 1);
                    break;
            }
            path.saveModel();
            e.getWhoClicked().openInventory(parent.getInventory());
        }
    }
}
