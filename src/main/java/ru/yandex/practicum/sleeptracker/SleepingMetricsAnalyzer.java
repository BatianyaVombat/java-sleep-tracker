package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SleepingMetricsAnalyzer {
    private final List<SleepingSession> sessions;

    public SleepingMetricsAnalyzer(List<SleepingSession> sessions) {
        if (sessions == null) {
            throw new IllegalArgumentException("Список сессий не может быть null!");
        }

        this.sessions = sessions;
    }

    //--------------------Получение общего количества сессий сна--------------------
    public String getTotalSessions() {
        return "Записано " + sessions.size() + " сессий сна";
    }

    //--------------------Получение сессии с min длительностью--------------------
    private long getShortestSession(List<SleepingSession> list) {
        return list.stream()
                .mapToLong(s -> s.getDuration().toMinutes())
                .min()
                .orElse(0);
    }

    public String shortestSession() {
        return "Самая короткая сессия: " + timeFormatting(getShortestSession(sessions));
    }

    //--------------------Получение сессии с max длительностью--------------------
    private Optional<SleepingSession> getLongestSession(List<SleepingSession> list) {
        return list.stream()
                .max(Comparator.comparingLong(s -> s.getDuration().toMinutes()));
    }

    public String longestSession() {
        return getLongestSession(sessions)
                .map(s -> "Самая долгая сессия: " + timeFormatting(s.getDuration().toMinutes()))
                .orElse("Самая долгая сессия: нет данных");
    }

    //---------------Получение средней продолжительности сессии в мин.---------------
    private long getAverageDuration(List<SleepingSession> list) {
        double fractional = list.stream()
                .mapToDouble(s -> s.getDuration().toMinutes())
                .average()
                .orElse(0.0);

        //округляю до целых минут
        return Math.round(fractional);
    }

    public String averageDuration() {
        return "Средняя продолжительность: " + timeFormatting(getAverageDuration(sessions));
    }

    //---------------Получение количества сессий с качеством BAD---------------
    private long getBadSessionsCount(List<SleepingSession> list) {
        return list.stream()
                .filter(s -> s.getSleepQuality().equals(SleepQuality.BAD))
                .count();
    }

    public String badSessionsCount() {
        return "Количество сессий с плохим качеством сна: " + getBadSessionsCount(sessions);
    }

    //---------------Получение количества бессонных ночей---------------
    private long getUnsleepingNightCount(List<SleepingSession> list) {
        return list.stream()
                .filter(SleepingSession::isSleeplessNight)
                .count();
    }

    public String unsleepNightCount() {
        return "Количество бессонных ночей: " + getUnsleepingNightCount(sessions);
    }

    //-------------------Выявление хронотипа личности-------------------
    public String getChronotype() {
        if (sessions.isEmpty()) {
            return "Недостаточно данных";
        }

        //среднее время начала сессии в секундах
        double avrStartTimeInSec = sessions.stream()
                .mapToDouble(s -> s.getSessionStart().toLocalTime().toSecondOfDay())
                .average()
                .orElse(0.0);

        //среднее время окончания сессии в секундах
        double avrEndTimeInSec = sessions.stream()
                .mapToDouble(s -> s.getSessionEnd().toLocalTime().toSecondOfDay())
                .average()
                .orElse(0.0);

        //создание объектов на основе округлённого количества секунд с начала дня
        LocalTime avrStartTime = LocalTime.ofSecondOfDay(Math.round(avrStartTimeInSec));
        LocalTime avrEndTime = LocalTime.ofSecondOfDay(Math.round(avrEndTimeInSec));

        if (avrStartTime.isAfter(LocalTime.of(23, 0)) &&
                avrEndTime.isAfter(LocalTime.of(9, 0))) {
            return "Ваш хронотип - Сова";
        } else if (avrStartTime.isBefore(LocalTime.of(22, 0)) &&
                avrEndTime.isBefore(LocalTime.of(7, 0))) {
            return "Ваш хронотип - Жаворонок";
        } else {
            return "Ваш хронотип - Голубь";
        }
    }

    //форматирование вывода времени
    private static String timeFormatting(long sessionMinutes) {
        long hours = sessionMinutes / 60;
        long minutes = sessionMinutes % 60;

        if (hours == 0 && minutes == 0) {
            return "менее минуты";
        } else if (hours == 0) {
            return String.format("%d минут(-ы)", minutes);
        } else {
            return String.format("%d час(-ов) %d минут(-ы)", hours, minutes);
        }
    }
}