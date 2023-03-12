package main.java.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TaskValidator {
    private Map<LocalDateTime, Boolean> timePeriods;
    private final static int DAY_OF_YEAR = 365;

    public TaskValidator() {
        this.timePeriods = createMap();
    }

    private Map<LocalDateTime, Boolean> createMap() {
        Map<LocalDateTime, Boolean> map = new HashMap<>();
        for (int i = 0; i < DAY_OF_YEAR; i++) {
            LocalDateTime date = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 0, 0).plusDays(i);
            LocalDateTime currentDate = date;
            while (currentDate.isBefore(date.plusDays(1))) {
                map.put(currentDate, true);
                currentDate = currentDate.plusMinutes(15);
            }
        }

        return map;
    }


    public boolean checkTask(Task task) {
        if (!(task instanceof Epic)) {
            if (checkInterval(task.getStartTime(), task.getEndTime())) {
                setMap(task.getStartTime(), task.getEndTime());
                return true;
            }
            return false;
        }

        return true;
    }

    private boolean checkInterval(LocalDateTime startTime, LocalDateTime endTime) {
        while (startTime.isBefore(endTime)) {
            if (!timePeriods.get(startTime)) {
                return false;
            }
            startTime = startTime.plusMinutes(15);
        }
        return true;
    }

    private void setMap(LocalDateTime startTime, LocalDateTime endTime) {
        while (startTime.isBefore(endTime)) {
            timePeriods.put(startTime, !timePeriods.get(startTime));
            startTime = startTime.plusMinutes(15);
        }
    }

    public void releaseInterval(Task task) {
        if (!(task instanceof Epic)) {
            setMap(task.getStartTime(), task.getEndTime());
        }
    }

    public void clear() {
        timePeriods = createMap();
    }
}
