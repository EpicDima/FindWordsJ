package com.epicdima.findwords;

import com.epicdima.findwords.trie.WordTrie;
import com.epicdima.findwords.trie.WordTrieType;
import com.epicdima.findwords.utils.BenchmarkUtils;
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
@Warmup(iterations = 1, time = 4)
@Measurement(iterations = 3, time = 4)
@State(Scope.Benchmark)
public class WordTrieBenchmark {

    private static final String LONG_EXISTING_WORD = "привилегированность";
    private static final String LONG_UNEXISTING_WORD = "привилегированностъ";

    private static final String SHORT_EXISTING_WORD = "привет";
    private static final String SHORT_UNEXISTING_WORD = "приветъ";

    private static final String FULL_UNKNOWN_WORD = "яяяяяяяя";
    private static final String PARTIAL_UNKNOWN_WORD = "привилегированностъ";

    @Param({
            "HASH",
            "ARRAY",
            "SET",
    })
    public String wordTrieTypeName;

    private WordTrieType wordTrieType;

    private WordTrie wordTrie;

    @Setup
    public void setup() throws Throwable {
        wordTrieType = WordTrieType.valueOf(wordTrieTypeName);
        wordTrie = wordTrieType.createInstance(BenchmarkUtils.DEFAULT_DICTIONARY);
    }

    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Benchmark
    public Object init() {
        return wordTrieType.createInstance(BenchmarkUtils.DEFAULT_DICTIONARY);
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
