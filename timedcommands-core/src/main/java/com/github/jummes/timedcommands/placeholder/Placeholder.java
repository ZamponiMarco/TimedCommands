package com.github.jummes.timedcommands.placeholder;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Placeholder implements Model {

    private static final String PLACEHOLDER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0=";

    @Serializable(headTexture = PLACEHOLDER_HEAD, description = "gui.placeholder.placeholder")
    private String placeholder;

    public Placeholder() {
        this("player");
    }

    public static Placeholder deserialize(Map<String, Object> map) {
        String placeholder = (String) map.get("placeholder");
        return new Placeholder(placeholder);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(PLACEHOLDER_HEAD),
                "&9&lName » &6" + placeholder,
                Libs.getLocale().getList("gui.placeholder.description"));
    }
}
