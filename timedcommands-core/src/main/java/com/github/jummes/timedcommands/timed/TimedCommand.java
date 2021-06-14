package com.github.jummes.timedcommands.timed;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.timedcommands.TimedCommands;
import com.github.jummes.timedcommands.placeholder.Placeholder;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TimedCommand implements Model {

    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjI4ZDk4Y2U0N2ZiNzdmOGI2MDRhNzY2ZGRkMjU0OTIzMjU2NGY5NTYyMjVjNTlmM2UzYjdiODczYTU4YzQifX19";
    private static final String PLACEHOLDER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0=";
    private static final String COMMANDS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0YzIxZDE3YWQ2MzYzODdlYTNjNzM2YmZmNmFkZTg5NzMxN2UxMzc0Y2Q1ZDliMWMxNWU2ZTg5NTM0MzIifX19";

    @Serializable(headTexture = NAME_HEAD, description = "gui.timed-command.name")
    private String name;
    @Serializable(headTexture = PLACEHOLDER_HEAD, description = "gui.timed-command.placeholders")
    private List<Placeholder> placeholders;
    @Serializable(headTexture = COMMANDS_HEAD, description = "gui.timed-command.commands")
    private List<SingleCommand> commands;

    public TimedCommand() {
        this(RandomStringUtils.randomAlphabetic(6), Lists.newArrayList(), Lists.newArrayList());
    }

    public TimedCommand(String name, List<Placeholder> placeholders, List<SingleCommand> commands) {
        this.name = name;
        this.placeholders = placeholders;
        this.commands = commands;
        TimedCommands.getInstance().registerCommand(name);
    }

    public static TimedCommand deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        List<Placeholder> placeholders = (List<Placeholder>) map.get("placeholders");
        List<SingleCommand> commands = (List<SingleCommand>) map.get("commands");
        return new TimedCommand(name, placeholders, commands);
    }

    public void scheduleCommands(List<String> arguments) {
        commands.forEach(command -> command.scheduleCommand(placeholders, arguments));
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(COMMANDS_HEAD),
                "&9&lName » &6" + name, Libs.getLocale().getList("gui.timed-command.description"));
    }

    @Override
    public Object beforeModify(Field field, Object value) {
        TimedCommands.getInstance().unregisterCommand(name);
        return value;
    }

    @Override
    public void onModify(Field field) {
        TimedCommands.getInstance().registerCommand(name);
    }
}
