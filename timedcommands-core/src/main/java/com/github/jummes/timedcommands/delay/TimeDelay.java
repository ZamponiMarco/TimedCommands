package com.github.jummes.timedcommands.delay;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@Getter
public class TimeDelay implements Model {

    private static final String SECONDS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==";
    private static final String MINUTES_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y4MzM0MTUxYzIzNGY0MTY0NzExM2JlM2VhZGYyODdkMTgxNzExNWJhYzk0NDVmZmJiYmU5Y2IyYjI4NGIwIn19fQ==";
    private static final String HOURS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThkMGM2N2JhYTcyNWExZTQxYmFiMzA4MDU3NzdlNzMyYmIyYWU2ZTkzNGY4NzM1Y2M0MDc4NzVkNjRjY2IifX19";
    private static final String DAYS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE2YTdkMWI3NjI5MTkzOTQyZjhlMTQ2YzZkMWQyZGIxOTFkMjdmODExZDIxZTI5YTJlNGNmYmFiZGEwODgifX19";

    @Serializable(headTexture = SECONDS_HEAD, description = "gui.time-delay.seconds")
    private int seconds;
    @Serializable(headTexture = MINUTES_HEAD, description = "gui.time-delay.minutes")
    private int minutes;
    @Serializable(headTexture = HOURS_HEAD, description = "gui.time-delay.hours")
    private int hours;
    @Serializable(headTexture = DAYS_HEAD, description = "gui.time-delay.days")
    private int days;

    public TimeDelay() {
        this(0, 0, 0, 0);
    }

    public static TimeDelay deserialize(Map<String, Object> map) {
        int seconds = (int) map.get("seconds");
        int minutes = (int) map.get("minutes");
        int hours = (int) map.get("hours");
        int days = (int) map.get("days");
        return new TimeDelay(seconds, minutes, hours, days);
    }

    public Date getDate() {
        return new Date(new Date().getTime() / 1000 * 1000 +
                seconds * 1_000 +
                minutes * 60 * 1_000 +
                hours * 60 * 60 * 1_000 +
                days * 24 * 60 * 60 * 1000);
    }

    @Override
    public String toString() {
        return MessageUtils.color(String.format("&6&l%d &cs &4- &6&l%d &cm &4- &6&l%d &ch &4- &6&l%d &cd", seconds,
                minutes, hours, days));
    }

    public void setSeconds(int seconds) {
        this.seconds = Math.max(seconds, 0) % 60;
        this.minutes = minutes + Math.max(seconds, 0) / 60;
    }

    public void setMinutes(int minutes) {
        this.minutes = Math.max(minutes, 0) % 60;
        this.hours = hours + Math.max(minutes, 0) / 60;
    }

    public void setHours(int hours) {
        this.hours = Math.max(hours, 0) % 24;
        this.days = days + Math.max(hours, 0) / 24;
    }

    public void setDays(int days) {
        this.days = Math.max(days, 0);
    }
}
