package com.epicdima.findwords;

import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.utils.Utils;
import org.openjdk.jmh.annotations.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 5, time = 5)
@State(Scope.Benchmark)
public class WordTrieBenchmark {
    private static final String LONG_WORD = "привилегированность";
    private static final String SHORT_WORD = "привет";
    private static final String UNKNOWN_WORD = "яяяяяяя";

    @Param({
            "com.epicdima.findwords.trie.HashWordTrie",
            "com.epicdima.findwords.trie.ArrayWordTrie",
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
    public void insertLong() {
        wordTrie.insert(LONG_WORD);
    }

    @Benchmark
    public void insertShort() {
        wordTrie.insert(SHORT_WORD);
    }

    @Benchmark
    public boolean containsWordLong() {
        return wordTrie.containsWord(LONG_WORD);
    }

    @Benchmark
    public boolean containsWordShort() {
        return wordTrie.containsWord(SHORT_WORD);
    }

    @Benchmark
    public boolean notContainsWord() {
        return wordTrie.containsWord(UNKNOWN_WORD);
    }

    @Benchmark
    public boolean containsSubstringLong() {
        return wordTrie.containsSubstring(LONG_WORD);
    }

    @Benchmark
    public boolean containsSubstringShort() {
        return wordTrie.containsSubstring(SHORT_WORD);
    }

    @Benchmark
    public boolean notContainsSubstring() {
        return wordTrie.containsSubstring(UNKNOWN_WORD);
    }
}
