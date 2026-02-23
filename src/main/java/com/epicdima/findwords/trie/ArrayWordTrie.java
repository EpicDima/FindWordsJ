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

public final class ArrayWordTrie implements WordTrie {
    @NonNull
    private final Node root;
    private final int abcLength;

    private final int[] charMap;
    private final int minCodePoint;
    private final int maxCodePoint;

    private ArrayWordTrie(int abcLength, int[] charMap, int minCodePoint, int maxCodePoint) {
        this.abcLength = abcLength;
        this.charMap = charMap;
        this.minCodePoint = minCodePoint;
        this.maxCodePoint = maxCodePoint;
        this.root = new Node(abcLength);
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
            return new ArrayWordTrie(0, new int[0], 0, -1);
        }

        int mapSize = maxCodePoint - minCodePoint + 1;
        int[] charMap = new int[mapSize];
        Arrays.fill(charMap, -1);

        int index = 0;
        for (Integer codePoint : abc) {
            charMap[codePoint - minCodePoint] = index++;
        }

        WordTrie wordTrie = new ArrayWordTrie(abc.size(), charMap, minCodePoint, maxCodePoint);
        for (String word : words) {
            wordTrie.insert(word);
        }

        return wordTrie;
    }

    private int getIndexForCodePoint(int codePoint) {
        if (codePoint < minCodePoint || codePoint > maxCodePoint) {
            return -1;
        }
        return charMap[codePoint - minCodePoint];
    }

    @Override
    public WordTrie.Cursor cursor() {
        return new ArrayCursor(this, root);
    }

    @Override
    public void insert(@NonNull final String word) {
        final int wordLength = word.length();
        int index = 0;
        Node node = root;
        while (index < wordLength) {
            int codePoint = word.codePointAt(index);
            index += Character.charCount(codePoint);

            int key = getIndexForCodePoint(codePoint);
            if (key == -1) {
                continue;
            }

            Node tempNode = node.nodes[key];
            if (tempNode != null) {
                if (index < wordLength) {
                    node = tempNode;
                } else {
                    tempNode.setWord(true);
                    break;
                }
            } else {
                if (index < wordLength) {
                    Node newNode = new Node(abcLength);
                    node.nodes[key] = newNode;
                    node = newNode;
                } else {
                    node.nodes[key] = new Node(abcLength, true);
                    break;
                }
            }
        }
    }

    @Override
    public boolean containsSubstring(@NonNull final String substring) {
        final int substringLength = substring.length();
        if (substringLength == 0) return false;

        int index = 0;
        Node node = root;
        while (node != null && index < substringLength) {
            int codePoint = substring.codePointAt(index);
            index += Character.charCount(codePoint);

            int key = getIndexForCodePoint(codePoint);
            if (key == -1) return false;

            node = node.nodes[key];
            if (index == substringLength && node != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsWord(@NonNull final String word) {
        final int wordLength = word.length();
        if (wordLength == 0) return false;

        int index = 0;
        Node node = root;
        while (node != null && index < wordLength) {
            int codePoint = word.codePointAt(index);
            index += Character.charCount(codePoint);

            int key = getIndexForCodePoint(codePoint);
            if (key == -1) return false;

            node = node.nodes[key];
            if (index == wordLength && node != null) {
                return node.isWord;
            }
        }
        return false;
    }

    private static final class Node {
        @NonNull
        private final Node[] nodes;
        private boolean isWord;

        Node(int abcLength, boolean isWord) {
            this.isWord = isWord;
            nodes = new Node[abcLength];
        }

        Node(int abcLength) {
            this(abcLength, false);
        }

        public void setWord(boolean word) {
            isWord = word;
        }
    }

    private static class ArrayCursor implements WordTrie.Cursor {
        private final ArrayWordTrie trie;
        private final Node[] path = new Node[256];
        private int depth = 0;

        public ArrayCursor(ArrayWordTrie trie, Node root) {
            this.trie = trie;
            this.path[0] = root;
        }

        @Override
        public boolean push(int codePoint) {
            int key = trie.getIndexForCodePoint(codePoint);
            if (key == -1) {
                return false;
            }
            Node nextNode = path[depth].nodes[key];
            if (nextNode == null) {
                return false;
            }
            depth++;
            path[depth] = nextNode;
            return true;
        }

        @Override
        public void pop() {
            depth--;
        }

        @Override
        public boolean isWord() {
            return path[depth].isWord;
        }
    }
}
