package com.github.jummes.timedcommands.scheduled;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ScheduledCommand implements Comparable<ScheduledCommand>, Model {

    @Serializable
    private String command;
    @Serializable
    private Date date;

    public static ScheduledCommand deserialize(Map<String, Object> map) {
        String command = (String) map.get("command");
        Date date = (Date) map.get("date");
        return new ScheduledCommand(command, date);
    }

    @Override
    public int compareTo(ScheduledCommand o) {
        return date.compareTo(o.date);
    }
}
