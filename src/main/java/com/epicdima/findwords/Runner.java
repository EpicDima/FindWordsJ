package com.epicdima.findwords;

import androidx.annotation.NonNull;
import com.epicdima.findwords.mask.MaskType;
import com.epicdima.findwords.solver.Solver;
import com.epicdima.findwords.solver.SolverType;
import com.epicdima.findwords.solver.WordAndMask;
import com.epicdima.findwords.trie.WordTrie;
import com.epicdima.findwords.trie.WordTrieType;
import com.epicdima.findwords.utils.Utils;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Runner {

    public static void main(@NonNull String[] args) {
        List<ConsoleEnumType<MaskType>> consoleMaskTypes = getConsoleMaskTypes();
        List<ConsoleEnumType<WordTrieType>> consoleWordTrieTypes = getConsoleWordTrieTypes();
        List<ConsoleEnumType<SolverType>> consoleSolverTypes = getConsoleSolverTypes();

        String defaultMaskTypeKey = consoleMaskTypes.getFirst().getConsoleKey();
        String defaultWordTrieKey = consoleWordTrieTypes.getFirst().getConsoleKey();
        String defaultSolverKey = consoleSolverTypes.getFirst().getConsoleKey();

        if (args.length == 0) {
            printHelp(consoleMaskTypes, consoleWordTrieTypes, consoleSolverTypes, defaultMaskTypeKey, defaultWordTrieKey, defaultSolverKey);
            return;
        }

        Arguments arguments = Arguments.parse(defaultMaskTypeKey, defaultWordTrieKey, defaultSolverKey, args);

        MaskType maskType = createMaskType(consoleMaskTypes, arguments.maskTypeName);
        WordTrie wordTrie = createWordTrie(consoleWordTrieTypes, arguments.wordTrieName, arguments.dictionaryPath);
        Solver solver = createSolver(consoleSolverTypes, arguments.solverName, arguments.linesSeparator, maskType, wordTrie);

        solver.solve(arguments.text, arguments.minLength, arguments.maxLength, arguments.fullMatch);

        printResult(solver);
    }

    private static void printHelp(
            @NonNull List<ConsoleEnumType<MaskType>> consoleMaskTypes,
            @NonNull List<ConsoleEnumType<WordTrieType>> consoleWordTrieTypes,
            @NonNull List<ConsoleEnumType<SolverType>> consoleSolverTypes,
            @NonNull String defaultMaskTypeKey,
            @NonNull String defaultWordTrieKey,
            @NonNull String defaultSolverKey
    ) {
        System.out.println("The first required argument is text. A space is a character where there cannot be a letter.");
        System.out.println("-fm  boolean         - to find the full combination                        (default = false)");
        System.out.println("-min integer         - minimum number of characters for a word (inclusive) (default = 4)");
        System.out.println("-max integer         - maximum number of characters for a word (inclusive) (default = 10)");
        System.out.println("-ls  string          - string for dividing text into lines                 (default = /)");
        System.out.println("-dp  string          - path to the dictionary file                         (default = '' - for using built-in dictionary)");
        System.out.println("-mt  [" + ConsoleEnumType.getConsoleKeys(consoleMaskTypes) + "]      - string for selecting the mask type                  (default = " + defaultMaskTypeKey + ")");
        System.out.println("(" + ConsoleEnumType.getConsoleKeysAndTypeClassNames(consoleMaskTypes) + ")");
        System.out.println("-wtr [" + ConsoleEnumType.getConsoleKeys(consoleWordTrieTypes) + "]      - string for selecting the word trie                  (default = " + defaultWordTrieKey + ")");
        System.out.println("(" + ConsoleEnumType.getConsoleKeysAndTypeClassNames(consoleWordTrieTypes) + ")");
        System.out.println("-s   [" + ConsoleEnumType.getConsoleKeys(consoleSolverTypes) + "] - string for selecting the solver                     (default = " + defaultSolverKey + ")");
        System.out.println("(" + ConsoleEnumType.getConsoleKeysAndTypeClassNames(consoleSolverTypes) + ")");
        System.out.println();
    }

    private static void printResult(@NonNull Solver solver) {
        System.out.println(System.lineSeparator());
        System.out.println("Found words:");
        System.out.println(solver.getWords()
                .stream()
                .map(WordAndMask::word)
                .collect(Collectors.joining(System.lineSeparator())));

        List<List<WordAndMask>> fullMatches = solver.getFullMatches();

        if (!fullMatches.isEmpty()) {
            System.out.println(System.lineSeparator());
            System.out.println("Complete combinations:");
            System.out.println(fullMatches
                    .stream()
                    .map(array -> array
                            .stream()
                            .map(WordAndMask::word)
                            .collect(Collectors.joining(" ")))
                    .collect(Collectors.joining(System.lineSeparator())));
        }
    }

    @NonNull
    private static List<ConsoleEnumType<MaskType>> getConsoleMaskTypes() {
        return Arrays.stream(MaskType.values())
                .map((wordTrieType) -> new ConsoleEnumType<MaskType>() {

                    @NonNull
                    @Override
                    public MaskType getEnumType() {
                        return wordTrieType;
                    }

                    @NonNull
                    @Override
                    public String getTypeClassName() {
                        return wordTrieType.getMaskClass().getSimpleName();
                    }
                })
                .collect(Collectors.toList());
    }

    @NonNull
    private static List<ConsoleEnumType<WordTrieType>> getConsoleWordTrieTypes() {
        return Arrays.stream(WordTrieType.values())
                .map((wordTrieType) -> new ConsoleEnumType<WordTrieType>() {

                    @NonNull
                    @Override
                    public WordTrieType getEnumType() {
                        return wordTrieType;
                    }

                    @NonNull
                    @Override
                    public String getTypeClassName() {
                        return wordTrieType.getWordTrieClass().getSimpleName();
                    }
                })
                .collect(Collectors.toList());
    }

    @NonNull
    private static List<ConsoleEnumType<SolverType>> getConsoleSolverTypes() {
        return Arrays.stream(SolverType.values())
                .map((solverType) -> new ConsoleEnumType<SolverType>() {

                    @NonNull
                    @Override
                    public SolverType getEnumType() {
                        return solverType;
                    }

                    @NonNull
                    @Override
                    public String getTypeClassName() {
                        return solverType.getSolverClass().getSimpleName();
                    }
                })
                .collect(Collectors.toList());

    }

    @NonNull
    private static MaskType createMaskType(
            @NonNull List<ConsoleEnumType<MaskType>> consoleMaskTypes,
            @NonNull String key
    ) {
        return consoleMaskTypes.stream()
                .filter((consoleSolverType) -> consoleSolverType.getConsoleKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown mask type key '" + key + "'"))
                .getEnumType();
    }

    @NonNull
    private static WordTrie createWordTrie(
            @NonNull List<ConsoleEnumType<WordTrieType>> consoleWordTrieTypes,
            @NonNull String key,
            @NonNull String dictionaryPath
    ) {
        WordTrieType wordTrieType = consoleWordTrieTypes.stream()
                .filter((consoleWordTrieType) -> consoleWordTrieType.getConsoleKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown word trie key '" + key + "'"))
                .getEnumType();
        if (dictionaryPath.isEmpty()) {
            return wordTrieType.createInstance(WordTrieType.class.getResourceAsStream(Utils.DEFAULT_DICTIONARY));
        } else {
            return wordTrieType.createInstance(dictionaryPath);
        }
    }

    @NonNull
    private static Solver createSolver(
            @NonNull List<ConsoleEnumType<SolverType>> consoleSolverTypes,
            @NonNull String key,
            @NonNull String linesSeparator,
            @NonNull MaskType maskType,
            @NonNull WordTrie trie
    ) {
        SolverType solverType = consoleSolverTypes.stream()
                .filter((consoleSolverType) -> consoleSolverType.getConsoleKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown solver key '" + key + "'"))
                .getEnumType();
        return solverType.createInstance(linesSeparator, maskType, trie);
    }

    private interface ConsoleEnumType<E extends Enum<E>> {

        @NonNull
        E getEnumType();

        @NonNull
        String getTypeClassName();

        @NonNull
        default String getConsoleKey() {
            return getTypeClassName().chars()
                    .filter(Character::isUpperCase)
                    .map(Character::toLowerCase)
                    .mapToObj(Character::toString)
                    .collect(Collectors.joining());
        }

        @NonNull
        static <E extends Enum<E>> String getConsoleKeys(@NonNull List<ConsoleEnumType<E>> types) {
            return types.stream()
                    .map(ConsoleEnumType::getConsoleKey)
                    .collect(Collectors.joining("|"));
        }

        @NonNull
        static <E extends Enum<E>> String getConsoleKeysAndTypeClassNames(@NonNull List<ConsoleEnumType<E>> types) {
            return types.stream()
                    .map(value -> value.getConsoleKey() + ": " + value.getTypeClassName())
                    .collect(Collectors.joining(", "));
        }
    }

    private static final class Arguments {
        @NonNull
        public String text = "";
        public boolean fullMatch = false;
        public int minLength = 4;
        public int maxLength = 10;
        @NonNull
        public String linesSeparator = "/";
        @NonNull
        public String dictionaryPath = "";
        @NonNull
        public String maskTypeName;
        @NonNull
        public String wordTrieName;
        @NonNull
        public String solverName;

        Arguments(@NonNull String maskTypeName, @NonNull String wordTrieName, @NonNull String solverName) {
            this.maskTypeName = maskTypeName;
            this.wordTrieName = wordTrieName;
            this.solverName = solverName;
        }

        @NonNull
        private static Arguments parse(@NonNull String defaultMaskTypeKey, @NonNull String defaultWordTrieKey, @NonNull String defaultSolverKey, @NonNull String[] args) {
            Arguments arguments = new Arguments(defaultMaskTypeKey, defaultWordTrieKey, defaultSolverKey);
            arguments.text = args[0];
            try {
                for (int i = 1; i < args.length - 1; i++) {
                    switch (args[i]) {
                        case "-fm" -> arguments.fullMatch = Boolean.parseBoolean(args[i + 1]);
                        case "-min" -> arguments.minLength = Integer.parseInt(args[i + 1]);
                        case "-max" -> arguments.maxLength = Integer.parseInt(args[i + 1]);
                        case "-ls" -> arguments.linesSeparator = args[i + 1];
                        case "-dp" -> arguments.dictionaryPath = args[i + 1];
                        case "-mt" -> arguments.maskTypeName = args[i + 1];
                        case "-wtr" -> arguments.wordTrieName = args[i + 1];
                        case "-s" -> arguments.solverName = args[i + 1];
                    }
                }
            } catch (Exception e) {
                System.out.println("Incorrect arguments");
            }
            arguments.validate();
            return arguments;
        }

        private void validate() {
            if (minLength < 1) {
                throw new IllegalArgumentException("The minimum word length must be greater than zero");
            }
            if (maxLength < 1) {
                throw new IllegalArgumentException("The maximum word length must be greater than zero");
            }
            if (minLength > maxLength) {
                throw new IllegalArgumentException("The minimum word length must be less than or equal to the maximum");
            }

            text = text.trim();
            if (text.isEmpty()) {
                throw new IllegalArgumentException("The text should not be empty");
            }

            List<Integer> lengths = Arrays.stream(text.split(Pattern.quote(linesSeparator)))
                    .map(String::length)
                    .toList();

            for (Integer lengthTemp : lengths) {
                if (!lengthTemp.equals(lengths.getFirst())) {
                    throw new IllegalArgumentException("Strings have a different number of characters");
                }
            }
        }
    }
}
