package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SleepTrackerAppTest {
    private SleepingMetricsAnalyzer sMa;
    private List<SleepingSession> sessions;

    @BeforeEach
    void setUp() {
        sessions = new ArrayList<>();
        sMa = new SleepingMetricsAnalyzer(sessions);
    }

    //вспомогательные метод
    private SleepingSession createSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        return new SleepingSession(start, end, quality);
    }

    //Определение максимальной длительности longestSession()
    @Test
    @Order(1)
    @DisplayName("Тест 1: Самая длинная сессия сна - longestSession()")
    public void shouldReturnSessionWithMaxDuration() {
        sessions.add(createSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 1, 0), SleepQuality.GOOD));
        sessions.add(createSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 5, 0), SleepQuality.NORMAL));

        Assertions.assertEquals("Самая долгая сессия: 6 час(-ов) 0 минут(-ы)", sMa.longestSession());
    }

    //Определение минимальной длительности shortestSession()
    @Test
    @Order(2)
    @DisplayName("Тест 2: Самая короткая сессия сна - shortestSession()")
    public void shouldReturnSessionWithMinDuration() {
        sessions.add(createSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                LocalDateTime.of(2025, 10, 2, 1, 0), SleepQuality.GOOD));
        sessions.add(createSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 5, 0), SleepQuality.NORMAL));
        sessions.add(createSession(LocalDateTime.of(2025, 10, 3, 14, 10),
                LocalDateTime.of(2025, 10, 3, 15, 0), SleepQuality.NORMAL));

        Assertions.assertEquals("Самая короткая сессия: 50 минут(-ы)", sMa.shortestSession());
    }

    //Определение средней длительности averageDuration()
    @Test
    @Order(3)
    @DisplayName("Тест 3: Средняя продолжительность сна - averageDuration()")
    public void shouldReturnAverageSessionsDuration() {
        sessions.add(createSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 1, 0), SleepQuality.GOOD));
        sessions.add(createSession(LocalDateTime.of(2025, 10, 2, 2, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD));

        //(120 + 300) / 2 = 210 минут = 3 часа 30 минут
        Assertions.assertEquals("Средняя продолжительность: 3 час(-ов) 30 минут(-ы)", sMa.averageDuration());
    }

    //Если в списке нет записей getTotalSessions() / longestSession() / shortestSession() / averageDuration()
    @Test
    @Order(4)
    @DisplayName("Тест 4: Проверяем поведение методов если сессий сна нет")
    public void shouldReturnAnMessageWhenListIsEmpty() {
        Assertions.assertEquals("Записано 0 сессий сна", sMa.getTotalSessions());
        Assertions.assertEquals("Самая долгая сессия: нет данных", sMa.longestSession());
        Assertions.assertEquals("Самая короткая сессия: менее минуты", sMa.shortestSession());
        Assertions.assertEquals("Средняя продолжительность: менее минуты", sMa.averageDuration());
    }

    //Подсчёт сессий с плохим качеством (badSessionsCount)
    @Test
    @Order(5)
    @DisplayName("Тест 5: Подсчёт сессий с качеством BAD - смешанный список")
    public void shouldReturnBadSessionsCount() {
        sessions.add(createSession(LocalDateTime.of(2026, 5, 27, 22, 0),
                LocalDateTime.of(2026, 5, 28, 6, 0), SleepQuality.GOOD));
        sessions.add(createSession(LocalDateTime.of(2025, 5, 23, 23, 0),
                LocalDateTime.of(2026, 5, 24, 3, 0), SleepQuality.BAD));
        sessions.add(createSession(LocalDateTime.of(2025, 5, 28, 14, 10),
                LocalDateTime.of(2026, 5, 28, 15, 0), SleepQuality.BAD));

        Assertions.assertEquals("Количество сессий с плохим качеством сна: 2", sMa.badSessionsCount());
    }

    @Test
    @Order(6)
    @DisplayName("Тест 6: Подсчёт сессий с качеством BAD - смешанный список сессий качества NORMAL и GOOD")
    public void shouldReturnZeroBadSessions() {
        sessions.add(createSession(LocalDateTime.of(2026, 1, 1, 22, 0),
                LocalDateTime.of(2026, 1, 2, 6, 0), SleepQuality.GOOD));
        sessions.add(createSession(LocalDateTime.of(2026, 5, 27, 23, 0),
                LocalDateTime.of(2026, 5, 28, 5, 0), SleepQuality.NORMAL));

        Assertions.assertEquals("Количество сессий с плохим качеством сна: 0", sMa.badSessionsCount());
    }

    //Бессонные ночи (не попадающие в 00:00–06:00)
    @Test
    @Order(7)
    @DisplayName("Тест 7: Определение бессонной ночи. Единственная сессия — бессонная (дневной сон)")
    public void shouldReturnTrueIfSessionIsSleepless() {
        SleepingSession session = createSession(LocalDateTime.of(2025, 10, 3, 14, 10),
                LocalDateTime.of(2025, 10, 3, 15, 0), SleepQuality.BAD);

        Assertions.assertTrue(session.isSleeplessNight());
    }

    @Test
    @Order(8)
    @DisplayName("Тест 8: Определение бессонной ночи. Единственная сессия — не бессонная (ночной сон)")
    public void shouldReturnFalseIfSessionIsSleepy() {
        SleepingSession session = createSession(LocalDateTime.of(2026, 5, 27, 23, 15),
                LocalDateTime.of(2026, 5, 28, 7, 30), SleepQuality.GOOD);

        Assertions.assertFalse(session.isSleeplessNight());
    }

    @Test
    @Order(9)
    @DisplayName("Тест 9: Сессия попадает в тайминг, но дата засыпания/просыпания одна и та же")
    public void shouldReturnTrueBecauseSessionAtSameDay() {
        SleepingSession session = createSession(LocalDateTime.of(2026, 5, 27, 0, 0),
                LocalDateTime.of(2026, 5, 27, 3, 0), SleepQuality.BAD);

        Assertions.assertTrue(session.isSleeplessNight());
    }

    @Test
    @Order(10)
    @DisplayName("Тест 10: Подсчёт количества бессонных ночей. Смешанный список сессий")
    public void shouldReturnCurrentCountOfSleeplessNight() {
        sessions.add(createSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 5, 0), SleepQuality.NORMAL));
        sessions.add(createSession(LocalDateTime.of(2025, 10, 3, 14, 0),
                LocalDateTime.of(2025, 10, 3, 15, 30), SleepQuality.BAD));
        sessions.add(createSession(LocalDateTime.of(2025, 10, 5, 22, 0),
                LocalDateTime.of(2025, 10, 6, 8, 0), SleepQuality.GOOD));
        sessions.add(createSession(LocalDateTime.of(2025, 10, 6, 10, 0),
                LocalDateTime.of(2025, 10, 6, 11, 0), SleepQuality.BAD));

        Assertions.assertEquals("Количество бессонных ночей: 2", sMa.unsleepNightCount());
    }

    @Test
    @Order(11)
    @DisplayName("Тест 11: Подсчёт количества бессонных ночей. Список сессий пуст")
    public void shouldReturnMessageIfListIsEmpty() {
        Assertions.assertEquals("Количество бессонных ночей: 0", sMa.unsleepNightCount());
    }


    //Определение хронотипа Жаворонок/Голубь/Сова
    @Test
    @Order(12)
    @DisplayName("Тест 12: Определение хронотипа. Однозначный Жаворонок")
    public void shouldReturnDefinitelyAnEarlyBird() {
        sessions.add(createSession(LocalDateTime.of(2026, 5, 25, 21, 0),
                LocalDateTime.of(2026, 5, 26, 6, 0), SleepQuality.GOOD));

        Assertions.assertEquals("Ваш хронотип - Жаворонок", sMa.getChronotype());
    }

    @Test
    @Order(13)
    @DisplayName("Тест 13: Определение хронотипа. Однозначная Сова")
    public void shouldReturnDefinitelyAnOwl() {
        sessions.add(createSession(LocalDateTime.of(2026, 5, 25, 23, 30),
                LocalDateTime.of(2026, 5, 26, 10, 0), SleepQuality.GOOD));

        Assertions.assertEquals("Ваш хронотип - Сова", sMa.getChronotype());
    }

    @Test
    @Order(14)
    @DisplayName("Тест 14: Определение хронотипа. Однозначный Голубь")
    public void shouldReturnDefinitelyPigeon() {
        sessions.add(createSession(LocalDateTime.of(2026, 5, 28, 22, 30),
                LocalDateTime.of(2026, 5, 29, 8, 0), SleepQuality.GOOD));

        Assertions.assertEquals("Ваш хронотип - Голубь", sMa.getChronotype());
    }

    @Test
    @Order(15)
    @DisplayName("Тест 15: Поведение метода определения хронотипа. Данных нет")
    public void shouldReturnMessageIfIsEmptySessionsList() {
        Assertions.assertEquals("Недостаточно данных", sMa.getChronotype());
    }
}