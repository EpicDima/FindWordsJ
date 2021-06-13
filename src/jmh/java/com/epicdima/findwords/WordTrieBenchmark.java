package com.epicdima.findwords;

import com.epicdima.findwords.base.WordTrie;
import com.epicdima.findwords.trie.ArrayWordTrie;
import com.epicdima.findwords.trie.HashWordTrie;
import com.epicdima.findwords.utils.Utils;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 3, time = 5)
@State(Scope.Benchmark)
public class WordTrieBenchmark {
    private final WordTrie hashWordTrie = HashWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);
    private final WordTrie arrayWordTrie = ArrayWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);

    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Benchmark
    public WordTrie initHash() {
        return HashWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);
    }

    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Benchmark
    public WordTrie initArray() {
        return ArrayWordTrie.createInstance(Utils.DEFAULT_DICTIONARY);
    }

    @Benchmark
    public void insertHash() {
        hashWordTrie.insert("привилегированность");
    }

    @Benchmark
    public void insertArray() {
        arrayWordTrie.insert("привилегированность");
    }

    @Benchmark
    public boolean containsWordHash() {
        return hashWordTrie.containsWord("привилегированность");
    }

    @Benchmark
    public boolean containsWordArray() {
        return arrayWordTrie.containsWord("привилегированность");
    }

    @Benchmark
    public boolean containsSubstringHash() {
        return hashWordTrie.containsSubstring("привилегированность");
    }

    @Benchmark
    public boolean containsSubstringArray() {
        return arrayWordTrie.containsSubstring("привилегированность");
    }
}
