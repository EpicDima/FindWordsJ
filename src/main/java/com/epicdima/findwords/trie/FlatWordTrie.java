package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FlatWordTrie implements WordTrie {
    private final int[] charMap;
    private final int minCodePoint;
    private final int maxCodePoint;
    private final int nodeSize;

    // Flattened trie structure. Each node takes `nodeSize` elements:
    // tree[offset] == 1 if it's the end of a word, 0 otherwise
    // tree[offset + 1 + charIndex] == offset of the child node
    private int[] tree;
    private int nextNodeOffset;

    private FlatWordTrie(int abcLength, int[] charMap, int minCodePoint, int maxCodePoint) {
        this.charMap = charMap;
        this.minCodePoint = minCodePoint;
        this.maxCodePoint = maxCodePoint;
        this.nodeSize = abcLength + 1;

        // Initial capacity for 1000 nodes
        this.tree = new int[this.nodeSize * 1000];

        // Root is always at offset 0
        this.nextNodeOffset = this.nodeSize;
    }

    @SuppressWarnings("unused") // used via MethodHandle
    @NonNull
    public static WordTrie createInstance(@NonNull String dictionaryPath) {
        try {
            return createInstance(new FileInputStream(dictionaryPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused") // used via MethodHandle
    @NonNull
    public static WordTrie createInstance(@NonNull InputStream inputStream) {
        List<String> words = new ArrayList<>();
        Set<Integer> abc = new HashSet<>();

        int minCodePoint = Integer.MAX_VALUE;
        int maxCodePoint = Integer.MIN_VALUE;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String word;
            while ((word = reader.readLine()) != null) {
                words.add(word);
                for (int i = 0; i < word.length(); ) {
                    int codePoint = word.codePointAt(i);
                    abc.add(codePoint);
                    if (codePoint < minCodePoint) minCodePoint = codePoint;
                    if (codePoint > maxCodePoint) maxCodePoint = codePoint;
                    i += Character.charCount(codePoint);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (words.isEmpty()) {
            return new FlatWordTrie(0, new int[0], 0, -1);
        }

        int mapSize = maxCodePoint - minCodePoint + 1;
        int[] charMap = new int[mapSize];
        Arrays.fill(charMap, -1);

        int index = 0;
        for (Integer codePoint : abc) {
            charMap[codePoint - minCodePoint] = index++;
        }

        FlatWordTrie wordTrie = new FlatWordTrie(abc.size(), charMap, minCodePoint, maxCodePoint);
        for (String word : words) {
            wordTrie.insert(word);
        }

        // Memory optimization: cut off unused array tail
        wordTrie.trimToSize();

        return wordTrie;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > tree.length) {
            int newCapacity = Math.max(tree.length * 2, minCapacity);
            tree = Arrays.copyOf(tree, newCapacity);
        }
    }

    private void trimToSize() {
        if (tree.length > nextNodeOffset) {
            tree = Arrays.copyOf(tree, nextNodeOffset);
        }
    }

    private int getIndexForCodePoint(int codePoint) {
        if (codePoint < minCodePoint || codePoint > maxCodePoint) {
            return -1;
        }
        return charMap[codePoint - minCodePoint];
    }

    @Override
    public void insert(@NonNull final String word) {
        final int wordLength = word.length();
        int offset = 0;
        int index = 0;

        while (index < wordLength) {
            int codePoint = word.codePointAt(index);
            index += Character.charCount(codePoint);

            int key = getIndexForCodePoint(codePoint);
            if (key == -1) continue;

            int childOffsetIndex = offset + 1 + key;
            int nextOffset = tree[childOffsetIndex];

            if (nextOffset == 0) { // No node found
                nextOffset = nextNodeOffset;
                ensureCapacity(nextNodeOffset + nodeSize);
                nextNodeOffset += nodeSize;
                tree[childOffsetIndex] = nextOffset;
            }
            offset = nextOffset;
        }

        tree[offset] = 1; // isWord = true
    }

    @Override
    public boolean containsSubstring(@NonNull final String substring) {
        final int substringLength = substring.length();
        if (substringLength == 0) return false;

        int offset = 0;
        int index = 0;

        while (index < substringLength) {
            int codePoint = substring.codePointAt(index);
            index += Character.charCount(codePoint);

            int key = getIndexForCodePoint(codePoint);
            if (key == -1) return false;

            offset = tree[offset + 1 + key];
            if (offset == 0) return false;
        }
        return true;
    }

    @Override
    public boolean containsWord(@NonNull final String word) {
        final int wordLength = word.length();
        if (wordLength == 0) return false;

        int offset = 0;
        int index = 0;

        while (index < wordLength) {
            int codePoint = word.codePointAt(index);
            index += Character.charCount(codePoint);

            int key = getIndexForCodePoint(codePoint);
            if (key == -1) return false;

            offset = tree[offset + 1 + key];
            if (offset == 0) return false;
        }
        return tree[offset] == 1;
    }
}
