package com.epicdima.findwords.base;

import com.epicdima.findwords.utils.Matrices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SolverTest {
    protected final String linesSeparator = "\n";

    private final Solver solver = createSolver();

    protected abstract Solver createSolver();

    @Test
    public void test1() {
        String text = "при\nтев\nдуб";

        solver.solve(text, 1, 10, false);

        List<CharSequence> words = solver.getWords().stream().map(wordAndMask -> wordAndMask.word).collect(Collectors.toList());

        Assertions.assertTrue(words.contains("привет"));
        Assertions.assertTrue(words.contains("дуб"));
    }

    @Test
    public void test2() {
        String text = Matrices.MATRIX_8_X_8;

        solver.solve(text, 4, 100, false);

        CharSequence[] correctWords = new CharSequence[]{"араб", "арго", "барий", "баул", "брег", "враг", "герб", "гербарий", "глад", "гладиатор", "гоми", "гора", "готовка", "гусар", "домысел", "досыл", "дружба", "дружба", "ивка", "игра", "кагор", "капо", "карп", "катушка", "кашка", "лоск", "лось", "лотус", "мотовка", "овраг", "опак", "оптом", "осушка", "подготовка", "проигрыш", "пуск", "пута", "пушка", "ракша", "рота", "скво", "слот", "соль", "сота", "соус", "суша", "сушка", "тарт", "ташка", "толь", "тоска", "туша", "тушка", "усол", "ушат", "ушка", "шато",};

        List<CharSequence> words = solver.getWords()
                .stream()
                .map(wordAndMask -> wordAndMask.word)
                .sorted()
                .collect(Collectors.toList());

        Assertions.assertEquals(correctWords.length, words.size());

        for (int i = 0; i < correctWords.length; i++) {
            Assertions.assertEquals(correctWords[i], words.get(i));
        }
    }

    @Test
    public void test3() {
        String text = Matrices.MATRIX_8_X_8;

        solver.solve(text, 4, 100, true);

        CharSequence[] correctFullMatches = new CharSequence[]{
                "гербарий гладиатор гусар домысел дружба катушка лось подготовка проигрыш",
                "гербарий гладиатор гусар домысел дружба катушка подготовка проигрыш соль"
        };

        List<CharSequence> fullMatches = solver.getFullMatches()
                .stream()
                .map(fullMatch -> fullMatch
                        .stream()
                        .map(wordAndMask -> wordAndMask.word)
                        .sorted()
                        .collect(Collectors.joining(" ")))
                .sorted()
                .collect(Collectors.toList());

        Assertions.assertEquals(correctFullMatches.length, fullMatches.size());

        for (int i = 0; i < correctFullMatches.length; i++) {
            Assertions.assertEquals(correctFullMatches[i], fullMatches.get(i));
        }
    }
}
