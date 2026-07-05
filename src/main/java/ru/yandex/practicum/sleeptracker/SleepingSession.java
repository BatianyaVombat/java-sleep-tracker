package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SleepingSession {
    private final LocalDateTime sessionStart;
    private final LocalDateTime sessionEnd;
    private final SleepQuality sleepQuality;

    public SleepingSession(LocalDateTime sessionStart, LocalDateTime sessionFinish, SleepQuality sleepQuality) {
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionFinish;
        this.sleepQuality = sleepQuality;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public LocalDateTime getSessionEnd() {
        return sessionEnd;
    }

    public SleepQuality getSleepQuality() {
        return sleepQuality;
    }

    public Duration getDuration() {
        return Duration.between(sessionStart, sessionEnd);
    }

    protected boolean isSleeplessNight() {
        LocalDate startDate = this.sessionStart.toLocalDate();
        LocalDateTime startNight = startDate.plusDays(1).atStartOfDay();
        LocalDateTime endOfNight = startNight.plusHours(6);

        boolean intersection = this.sessionStart.isBefore(endOfNight) && this.sessionEnd.isAfter(startNight);

        return !intersection;
    }
}
