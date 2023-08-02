package com.epicdima.findwords;

import com.epicdima.findwords.trie.WordTrie;
import com.epicdima.findwords.utils.Utils;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 5, time = 5)
@State(Scope.Benchmark)
public class WordTrieBenchmark {

    private static final String LONG_EXISTING_WORD = "привилегированность";
    private static final String LONG_UNEXISTING_WORD = "привилегированностъ";

    private static final String SHORT_EXISTING_WORD = "привет";
    private static final String SHORT_UNEXISTING_WORD = "приветъ";

    private static final String FULL_UNKNOWN_WORD = "яяяяяяяя";
    private static final String PARTIAL_UNKNOWN_WORD = "привилегированностъ";

    @Param({
            "com.epicdima.findwords.trie.HashWordTrie",
            "com.epicdima.findwords.trie.ArrayWordTrie",
            "com.epicdima.findwords.trie.SetWordTrie",
    })
    public String wordTrieClass;

    private MethodHandle createInstanceMethod;
    private WordTrie wordTrie;

    @Setup
    public void setup() throws Throwable {
        createInstanceMethod = MethodHandles.publicLookup()
                .findStatic(Class.forName(wordTrieClass), "createInstance",
                        MethodType.methodType(WordTrie.class, String.class));

        wordTrie = (WordTrie) createInstanceMethod.invoke(Utils.DEFAULT_DICTIONARY);
    }

    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Benchmark
    public Object init() throws Throwable {
        return createInstanceMethod.invoke(Utils.DEFAULT_DICTIONARY);
    }

    @Benchmark
    public void insertExistingLongWord() {
        wordTrie.insert(LONG_EXISTING_WORD);
    }

    @Benchmark
    public void insertUnexistingLongWord() {
        wordTrie.insert(LONG_UNEXISTING_WORD);
    }

    @Benchmark
    public void insertExistingShortWord() {
        wordTrie.insert(SHORT_EXISTING_WORD);
    }

    @Benchmark
    public void insertUnexistingShortWord() {
        wordTrie.insert(SHORT_UNEXISTING_WORD);
    }

    @Benchmark
    public boolean containsExistingLongWord() {
        return wordTrie.containsWord(LONG_EXISTING_WORD);
    }

    @Benchmark
    public boolean notContainsUnexistingLongWord() {
        return wordTrie.containsWord(LONG_UNEXISTING_WORD);
    }

    @Benchmark
    public boolean containsExistingShortWord() {
        return wordTrie.containsWord(SHORT_EXISTING_WORD);
    }

    @Benchmark
    public boolean notContainsUnexistingShortWord() {
        return wordTrie.containsWord(SHORT_UNEXISTING_WORD);
    }

    @Benchmark
    public boolean notContainsFullUnknownWord() {
        return wordTrie.containsWord(FULL_UNKNOWN_WORD);
    }

    @Benchmark
    public boolean notContainsPartialUnknownWord() {
        return wordTrie.containsWord(PARTIAL_UNKNOWN_WORD);
    }

    @Benchmark
    public boolean containsSubstringExistingLongWord() {
        return wordTrie.containsSubstring(LONG_EXISTING_WORD);
    }

    @Benchmark
    public boolean notContainsSubstringUnexistingLongWord() {
        return wordTrie.containsSubstring(LONG_UNEXISTING_WORD);
    }

    @Benchmark
    public boolean containsSubstringExistingShortWord() {
        return wordTrie.containsSubstring(SHORT_EXISTING_WORD);
    }

    @Benchmark
    public boolean notContainsSubstringUnexistingShortWord() {
        return wordTrie.containsSubstring(SHORT_UNEXISTING_WORD);
    }

    @Benchmark
    public boolean notContainsSubstringFullUnknownWord() {
        return wordTrie.containsSubstring(FULL_UNKNOWN_WORD);
    }

    @Benchmark
    public boolean notContainsSubstringPartialUnknownWord() {
        return wordTrie.containsSubstring(PARTIAL_UNKNOWN_WORD);
    }
}
