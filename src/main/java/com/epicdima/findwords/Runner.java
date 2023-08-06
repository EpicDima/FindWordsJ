package com.epicdima.findwords;

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

public class Runner {

    public static void main(String[] args) {
        List<ConsoleEnumType<WordTrieType>> consoleWordTrieTypes = getConsoleWordTrieTypes();
        List<ConsoleEnumType<SolverType>> consoleSolverTypes = getConsoleSolverTypes();

        String defaultWordTrieKey = consoleWordTrieTypes.get(0).getConsoleKey();
        String defaultSolverKey = consoleSolverTypes.get(0).getConsoleKey();

        if (args.length == 0) {
            printHelp(consoleWordTrieTypes, consoleSolverTypes, defaultWordTrieKey, defaultSolverKey);
            return;
        }

        Arguments arguments = Arguments.parse(defaultWordTrieKey, defaultSolverKey, args);

        WordTrie wordTrie = createWordTrie(consoleWordTrieTypes, arguments.wordTrieName, arguments.dictionaryPath);
        Solver solver = createSolver(consoleSolverTypes, arguments.solverName, arguments.linesSeparator, wordTrie);

        solver.solve(arguments.text, arguments.minLength, arguments.maxLength, arguments.fullMatch);

        printResult(solver);
    }

    private static void printHelp(
            List<ConsoleEnumType<WordTrieType>> consoleWordTrieTypes,
            List<ConsoleEnumType<SolverType>> consoleSolverTypes,
            String defaultWordTrieKey,
            String defaultSolverKey
    ) {
        System.out.println("The first required argument is text. A space is a character where there cannot be a letter.");
        System.out.println("-fm  boolean         - to find the full combination                        (default = false)");
        System.out.println("-min integer         - minimum number of characters for a word (inclusive) (default = 4)");
        System.out.println("-max integer         - maximum number of characters for a word (inclusive) (default = 10)");
        System.out.println("-ls  string          - string for dividing text into lines                 (default = /)");
        System.out.println("-dp  string          - path to the dictionary file                         (default = '' - for using built-in dictionary)");
        System.out.println("-wtr [" + ConsoleEnumType.getConsoleKeys(consoleWordTrieTypes) + "]      - string for selecting the word trie                  (default = " + defaultWordTrieKey + ")");
        System.out.println("(" + ConsoleEnumType.getConsoleKeysAndTypeClassNames(consoleWordTrieTypes) + ")");
        System.out.println("-s   [" + ConsoleEnumType.getConsoleKeys(consoleSolverTypes) + "] - string for selecting the solver                     (default = " + defaultSolverKey + ")");
        System.out.println("(" + ConsoleEnumType.getConsoleKeysAndTypeClassNames(consoleSolverTypes) + ")");
        System.out.println();
    }

    private static void printResult(Solver solver) {
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

    private static List<ConsoleEnumType<WordTrieType>> getConsoleWordTrieTypes() {
        return Arrays.stream(WordTrieType.values())
                .map((wordTrieType) -> new ConsoleEnumType<WordTrieType>() {

                    @Override
                    public WordTrieType getEnumType() {
                        return wordTrieType;
                    }

                    @Override
                    public String getTypeClassName() {
                        return wordTrieType.getWordTrieClass().getSimpleName();
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<ConsoleEnumType<SolverType>> getConsoleSolverTypes() {
        return Arrays.stream(SolverType.values())
                .map((solverType) -> new ConsoleEnumType<SolverType>() {

                    @Override
                    public SolverType getEnumType() {
                        return solverType;
                    }

                    @Override
                    public String getTypeClassName() {
                        return solverType.getSolverClass().getSimpleName();
                    }
                })
                .collect(Collectors.toList());

    }

    private static WordTrie createWordTrie(
            List<ConsoleEnumType<WordTrieType>> consoleWordTrieTypes,
            String key,
            String dictionaryPath
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

    private static Solver createSolver(
            List<ConsoleEnumType<SolverType>> consoleSolverTypes,
            String key,
            String linesSeparator,
            WordTrie trie
    ) {
        SolverType solverType = consoleSolverTypes.stream()
                .filter((consoleSolverType) -> consoleSolverType.getConsoleKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown solver key '" + key + "'"))
                .getEnumType();
        return solverType.createInstance(linesSeparator, trie);
    }

    private interface ConsoleEnumType<E extends Enum<E>> {

        E getEnumType();

        String getTypeClassName();

        default String getConsoleKey() {
            return getTypeClassName().chars()
                    .filter(Character::isUpperCase)
                    .map(Character::toLowerCase)
                    .mapToObj(Character::toString)
                    .collect(Collectors.joining());
        }

        static <E extends Enum<E>> String getConsoleKeys(List<ConsoleEnumType<E>> types) {
            return types.stream()
                    .map(ConsoleEnumType::getConsoleKey)
                    .collect(Collectors.joining("|"));
        }

        static <E extends Enum<E>> String getConsoleKeysAndTypeClassNames(List<ConsoleEnumType<E>> types) {
            return types.stream()
                    .map(value -> value.getConsoleKey() + ": " + value.getTypeClassName())
                    .collect(Collectors.joining(", "));
        }
    }

    private static class Arguments {
        public String text = "";
        public boolean fullMatch = false;
        public int minLength = 4;
        public int maxLength = 10;
        public String linesSeparator = "/";
        public String dictionaryPath = "";
        public String wordTrieName;
        public String solverName;

        Arguments(String wordTrieName, String solverName) {
            this.wordTrieName = wordTrieName;
            this.solverName = solverName;
        }

        private static Arguments parse(String defaultWordTrieKey, String defaultSolverKey, String[] args) {
            Arguments arguments = new Arguments(defaultWordTrieKey, defaultSolverKey);

            arguments.text = args[0];

            try {
                for (int i = 1; i < args.length - 1; i++) {
                    switch (args[i]) {
                        case "-fm":
                            arguments.fullMatch = Boolean.parseBoolean(args[i + 1]);
                            break;
                        case "-min":
                            arguments.minLength = Integer.parseInt(args[i + 1]);
                            break;
                        case "-max":
                            arguments.maxLength = Integer.parseInt(args[i + 1]);
                            break;
                        case "-ls":
                            arguments.linesSeparator = args[i + 1];
                            break;
                        case "-dp":
                            arguments.dictionaryPath = args[i + 1];
                            break;
                        case "-wtr":
                            arguments.wordTrieName = args[i + 1];
                        case "-s":
                            arguments.solverName = args[i + 1];
                            break;
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
                if (!lengthTemp.equals(lengths.get(0))) {
                    throw new IllegalArgumentException("Strings have a different number of characters");
                }
            }
        }
    }
}
