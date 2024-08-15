package com.epicdima.findwords.solver;

import androidx.annotation.NonNull;
import com.epicdima.findwords.utils.Matrices;
import com.epicdima.findwords.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SolverTest {
    @NonNull
    protected final String dictionaryPath = TestUtils.DEFAULT_DICTIONARY;
    @NonNull
    protected final String linesSeparator = "\n";

    @NonNull
    private final Solver solver = createSolver();

    @NonNull
    protected abstract Solver createSolver();

    @Test
    public final void test1() {
        String text = "при\nтев\nдуб";

        solver.solve(text, 1, 10, false);

        List<CharSequence> words = solver.getWords().stream().map(WordAndMask::word).collect(Collectors.toList());

        Assertions.assertTrue(words.contains("привет"));
        Assertions.assertTrue(words.contains("дуб"));
    }

    @Test
    public final void test11() {
        String text = "при\nтев\nдуб";

        solver.solve(text, 3, 6, true);

        List<CharSequence> words = solver.getWords().stream().map(WordAndMask::word).collect(Collectors.toList());

        List<CharSequence> fullMatches = solver.getFullMatches()
                .stream()
                .map(fullMatch -> fullMatch
                        .stream()
                        .map(WordAndMask::word)
                        .sorted()
                        .collect(Collectors.joining(" ")))
                .sorted()
                .collect(Collectors.toList());

        Assertions.assertTrue(words.contains("привет"));
        Assertions.assertTrue(words.contains("дуб"));

        Assertions.assertFalse(fullMatches.isEmpty());
    }

    @Test
    public final void test2() {
        String text = Matrices.MATRIX_8_X_8;

        solver.solve(text, 4, 100, false);

        CharSequence[] correctWords = new CharSequence[]{"араб", "арго", "барий", "баул", "брег", "враг", "герб", "гербарий", "глад", "гладиатор", "гоми", "гора", "готовка", "гусар", "домысел", "досыл", "дружба", "дружба", "ивка", "игра", "кагор", "капо", "карп", "катушка", "кашка", "лоск", "лось", "лотус", "мотовка", "овраг", "опак", "оптом", "осел", "осушка", "подготовка", "проигрыш", "пуск", "пута", "пушка", "ракша", "рота", "скво", "слот", "соль", "сота", "соус", "суша", "сушка", "тарт", "ташка", "толь", "тоска", "туша", "тушка", "усол", "ушат", "ушка", "шато"};

        List<CharSequence> words = solver.getWords()
                .stream()
                .map(WordAndMask::word)
                .sorted()
                .collect(Collectors.toList());

        Assertions.assertEquals(correctWords.length, words.size());

        for (int i = 0; i < correctWords.length; i++) {
            Assertions.assertEquals(correctWords[i], words.get(i));
        }
    }

    @Test
    public final void test3() {
        String text = Matrices.MATRIX_8_X_8;

        solver.solve(text, 4, 100, true);

        CharSequence[] correctFullMatches = new CharSequence[]{
                "гербарий гладиатор гусар домысел дружба катушка лось подготовка проигрыш",
                "гербарий гладиатор гусар домысел дружба катушка подготовка проигрыш соль"
        };

        List<CharSequence> correctFullMatchesList = Arrays.stream(correctFullMatches)
                .map(fullMatch -> Arrays.stream(fullMatch.toString().split(" "))
                        .sorted()
                        .collect(Collectors.joining(" ")))
                .sorted()
                .map(s -> (CharSequence) s)
                .toList();

        List<CharSequence> fullMatches = solver.getFullMatches()
                .stream()
                .map(fullMatch -> fullMatch
                        .stream()
                        .map(WordAndMask::word)
                        .sorted()
                        .collect(Collectors.joining(" ")))
                .sorted()
                .collect(Collectors.toList());

        Assertions.assertEquals(correctFullMatchesList.size(), fullMatches.size());

        for (int i = 0; i < correctFullMatchesList.size(); i++) {
            Assertions.assertEquals(correctFullMatchesList.get(i), fullMatches.get(i));
        }
    }

    @Test
    public final void test4() {
        String text = Matrices.MATRIX_19_X_4;

        solver.solve(text, 4, 10, true);

        CharSequence[] correctFullMatches = new CharSequence[]{
                "тара анид блин дюна аура керн анид анод копа банк ларь лава кант вена ласа алла иена ласт авар",
                "тара анид блин дюна аура керн дина анод копа банк ларь лава кант вена ласа алла иена ласт авар",
                "тара анид блин дюна аура керн копа банк ларь лава кант вена диод ласа алла иена ласт анна авар",
                "анид блин дюна аура керн анид анод копа банк ларь лава кант вена ласа алла иена арат ласт авар",
                "анид блин дюна аура керн дина анод копа банк ларь лава кант вена ласа алла иена арат ласт авар",
                "анид блин дюна аура керн копа банк ларь лава кант вена диод ласа алла иена арат ласт анна авар",
                "тара блин дюна аура керн анид анод копа банк ларь лава кант дина вена ласа алла иена ласт авар",
                "тара блин дюна аура керн дина анод копа банк ларь лава кант дина вена ласа алла иена ласт авар",
                "тара блин дюна аура керн копа банк ларь лава кант дина вена диод ласа алла иена ласт анна авар",
                "анид блин дюна керн анид анод арат копа банк ларь лава кант аура вена ласа алла иена ласт авар",
                "анид блин дюна керн анид анод копа банк тара ларь лава кант аура вена ласа алла иена ласт авар",
                "анид блин дюна керн дина анод арат копа банк ларь лава кант аура вена ласа алла иена ласт авар",
                "анид блин дюна керн дина анод копа банк тара ларь лава кант аура вена ласа алла иена ласт авар",
                "анид блин дюна керн арат копа банк ларь лава кант аура вена диод ласа алла иена ласт анна авар",
                "анид блин дюна керн копа банк тара ларь лава кант аура вена диод ласа алла иена ласт анна авар",
                "блин дюна аура керн анид анод копа банк ларь лава кант дина вена ласа алла иена арат ласт авар",
                "блин дюна аура керн дина анод копа банк ларь лава кант дина вена ласа алла иена арат ласт авар",
                "блин дюна аура керн копа банк ларь лава кант дина вена диод ласа алла иена арат ласт анна авар",
                "блин дюна керн анид анод арат копа банк ларь лава кант дина аура вена ласа алла иена ласт авар",
                "блин дюна керн анид анод копа банк тара ларь лава кант дина аура вена ласа алла иена ласт авар",
                "блин дюна керн дина анод арат копа банк ларь лава кант дина аура вена ласа алла иена ласт авар",
                "блин дюна керн дина анод копа банк тара ларь лава кант дина аура вена ласа алла иена ласт авар",
                "блин дюна керн арат копа банк ларь лава кант дина аура вена диод ласа алла иена ласт анна авар",
                "блин дюна керн копа банк тара ларь лава кант дина аура вена диод ласа алла иена ласт анна авар"
        };
        List<CharSequence> correctFullMatchesList = Arrays.stream(correctFullMatches)
                .map(fullMatch -> Arrays.stream(fullMatch.toString().split(" "))
                        .sorted()
                        .collect(Collectors.joining(" ")))
                .sorted()
                .map(s -> (CharSequence) s)
                .toList();

        List<CharSequence> fullMatches = solver.getFullMatches()
                .stream()
                .map(fullMatch -> fullMatch
                        .stream()
                        .map(WordAndMask::word)
                        .sorted()
                        .collect(Collectors.joining(" ")))
                .sorted()
                .collect(Collectors.toList());

        Assertions.assertEquals(correctFullMatchesList.size(), fullMatches.size());

        for (int i = 0; i < correctFullMatchesList.size(); i++) {
            Assertions.assertEquals(correctFullMatchesList.get(i), fullMatches.get(i));
        }
    }

    @Test
    public final void test5() {
        String text = Matrices.MATRIX_10_X_10;

        solver.solve(text, 4, 100, true);

        List<CharSequence> words = solver.getWords().stream().map(WordAndMask::word).collect(Collectors.toList());

        List<CharSequence> fullMatches = solver.getFullMatches()
                .stream()
                .map(fullMatch -> fullMatch
                        .stream()
                        .map(WordAndMask::word)
                        .sorted()
                        .collect(Collectors.joining(" ")))
                .sorted()
                .collect(Collectors.toList());

        Assertions.assertEquals(143, words.size());

        Assertions.assertEquals(3, fullMatches.size());
    }
}
