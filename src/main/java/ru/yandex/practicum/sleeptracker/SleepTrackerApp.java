package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class SleepTrackerApp {
    public static final SleepStatLoader sleepStatLoader = new SleepStatLoader();
    public static SleepingMetricsAnalyzer sMa;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Путь к файлу с данными сна не найден!");
            return;
        }

        String sleepingFile = args[0];
        sMa = new SleepingMetricsAnalyzer(sleepStatLoader.load(sleepingFile));
        List<String> metrics = List.of(
                //Метрика 1: Общее количество сессий сна
                sMa.getTotalSessions(),
                //Метрика 2: Минимальная по длительности сессии сна
                sMa.shortestSession(),
                //Метрика 3: Максимальная по длительности сессии сна
                sMa.longestSession(),
                //Метрика 4: Средняя продолжительность сессии сна
                sMa.averageDuration(),
                //Метрика 5: Количество сессий с качеством BAD
                sMa.badSessionsCount(),
                //Метрика 6: Количество бессонных ночей
                sMa.unsleepNightCount(),
                //Метрика 7: Выявление хронотипа
                sMa.getChronotype()
        );

        System.out.println("\n");
        metrics.forEach(System.out::println);
    }
}