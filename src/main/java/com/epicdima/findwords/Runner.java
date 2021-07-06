package com.epicdima.findwords;

import com.epicdima.findwords.base.Solver;
import com.epicdima.findwords.base.WordAndMask;
import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.solver.DefaultSolver;
import com.epicdima.findwords.solver.FastSolver;
import com.epicdima.findwords.solver.ForkJoinSolver;
import com.epicdima.findwords.solver.MultiThreadedSolver;
import com.epicdima.findwords.trie.ArrayWordTrie;
import com.epicdima.findwords.trie.HashWordTrie;
import com.epicdima.findwords.utils.Utils;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Runner {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        Arguments arguments = Arguments.parse(args);

        WordTrie trie = WordTrieType.getByKey(arguments.wordTrieName).createInstance(arguments.dictionaryPath);
        Solver solver = SolverType.getByKey(arguments.solverName).createInstance(arguments.linesSeparator, trie);

        solver.solve(arguments.text, arguments.minLength, arguments.maxLength, arguments.fullMatch);

        printResult(solver);
    }

    private static void printHelp() {
        System.out.println("The first required argument is text");
        System.out.println("-fm  boolean         - to find the full combination                        (default = false)");
        System.out.println("-min integer         - minimum number of characters for a word (inclusive) (default = 4)");
        System.out.println("-max integer         - maximum number of characters for a word (inclusive) (default = 10)");
        System.out.println("-ls  string          - string for dividing text into lines                 (default = /)");
        System.out.println("-dp  string          - path to the dictionary file                         (default = '' - for using built-in dictionary)");
        System.out.println("-wtr [" + getKeys(WordTrieType.values()) + "]      - string for selecting the word trie                  (default = " + WordTrieType.values()[0].key + ")");
        System.out.println("(" + getKeysAndNames(WordTrieType.values()) + ")");
        System.out.println("-s   [" + getKeys(SolverType.values()) + "] - string for selecting the solver                     (default = " + SolverType.values()[0].key + ")");
        System.out.println("(" + getKeysAndNames(SolverType.values()) + ")");
        System.out.println();
    }

    private static String getKeys(EnumType[] values) {
        return Arrays.stream(values)
                .map(EnumType::getKey)
                .collect(Collectors.joining("|"));
    }

    private static String getKeysAndNames(EnumType[] values) {
        return Arrays.stream(values)
                .map(value -> value.getKey() + ": " + value.getClassName())
                .collect(Collectors.joining(", "));
    }

    private static void printResult(Solver solver) {
        System.out.println(System.lineSeparator());
        System.out.println("Found words:");
        System.out.println(solver.getWords()
                .stream()
                .map(wordAndMask -> wordAndMask.word)
                .collect(Collectors.joining(System.lineSeparator())));

        List<List<WordAndMask>> fullMatches = solver.getFullMatches();

        if (!fullMatches.isEmpty()) {
            System.out.println(System.lineSeparator());
            System.out.println("Complete combinations:");
            System.out.println(fullMatches
                    .stream()
                    .map(array -> array
                            .stream()
                            .map(wordAndMask -> wordAndMask.word)
                            .collect(Collectors.joining(" ")))
                    .collect(Collectors.joining(System.lineSeparator())));
        }
    }


    private enum WordTrieType implements EnumType {
        HASH("hash", HashWordTrie.class),
        ARRAY("arr", ArrayWordTrie.class);

        public final String key;
        public final Class<?> cls;

        WordTrieType(String key, Class<?> cls) {
            this.key = key;
            this.cls = cls;
        }

        public static WordTrieType getByKey(String key) {
            for (WordTrieType type : values()) {
                if (type.getKey().equals(key)) {
                    return type;
                }
            }

            throw new IllegalArgumentException("Unknown word trie key '" + key + "'");
        }

        public WordTrie createInstance(String dictionaryPath) {
            try {
                if (dictionaryPath.isEmpty()) {
                    MethodType methodType = MethodType.methodType(WordTrie.class, InputStream.class);
                    MethodHandle createInstance = MethodHandles.publicLookup().findStatic(cls, "createInstance", methodType);
                    return (WordTrie) createInstance.invokeExact(getClass().getResourceAsStream(Utils.DEFAULT_DICTIONARY));
                } else {
                    MethodType methodType = MethodType.methodType(WordTrie.class, String.class);
                    MethodHandle createInstance = MethodHandles.publicLookup().findStatic(cls, "createInstance", methodType);
                    return (WordTrie) createInstance.invokeExact(dictionaryPath);
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getClassName() {
            return cls.getSimpleName();
        }
    }


    private enum SolverType implements EnumType {
        DEFAULT("ds", DefaultSolver.class),
        FAST("fs", FastSolver.class),
        MULTITHREADED("mts", MultiThreadedSolver.class),
        FORKJOIN("fjs", ForkJoinSolver.class);

        public final String key;
        public final Class<?> cls;

        SolverType(String key, Class<?> cls) {
            this.key = key;
            this.cls = cls;
        }

        public static SolverType getByKey(String key) {
            for (SolverType type : values()) {
                if (type.getKey().equals(key)) {
                    return type;
                }
            }

            throw new IllegalArgumentException("Unknown solver key '" + key + "'");
        }

        public Solver createInstance(String linesSeparator, WordTrie trie) {
            try {
                MethodType methodType = MethodType.methodType(void.class, String.class, WordTrie.class);
                MethodHandle constructor = MethodHandles.publicLookup().findConstructor(cls, methodType);
                return (Solver) constructor.invokeExact(linesSeparator, trie);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getClassName() {
            return cls.getSimpleName();
        }
    }


    private interface EnumType {

        String getKey();

        String getClassName();
    }

    private static class Arguments {
        public String text = "";
        public boolean fullMatch = false;
        public int minLength = 4;
        public int maxLength = 10;
        public String linesSeparator = "/";
        public String dictionaryPath = "";
        public String wordTrieName = WordTrieType.values()[0].key;
        public String solverName = SolverType.values()[0].key;

        private static Arguments parse(String[] args) {
            Arguments arguments = new Arguments();

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
                    .collect(Collectors.toList());

            for (Integer lengthTemp : lengths) {
                if (!lengthTemp.equals(lengths.get(0))) {
                    throw new IllegalArgumentException("Strings have a different number of characters");
                }
            }
        }
    }
}
