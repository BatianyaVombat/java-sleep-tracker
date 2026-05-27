package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepStatLoader {
    private final DateTimeFormatter DATE_TIME_FORMATER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public List<SleepingSession> load(String path) {
        try (Stream<String> lines = Files.lines(Path.of(path), StandardCharsets.UTF_8)) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(this::parseLine)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Не смогли найти файл со статистикой сна: " + path, e);
        }
    }

    //самостоятельно расчленяем строку на составляющие
    private Optional<SleepingSession> parseLine(String line) {
        String trimLine = line.trim();
        String[] tempArray = trimLine.split(";");

        if (tempArray.length != 3) {
            return Optional.empty();
        }

        String start = tempArray[0];
        String end = tempArray[1];
        String quality = tempArray[2];

        try {
            LocalDateTime dtStart = LocalDateTime.parse(start, DATE_TIME_FORMATER);
            LocalDateTime dtEnd = LocalDateTime.parse(end, DATE_TIME_FORMATER);
            SleepQuality sleepQuality = SleepQuality.valueOf(quality);
            return Optional.of(new SleepingSession(dtStart, dtEnd, sleepQuality));

        } catch (DateTimeParseException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
