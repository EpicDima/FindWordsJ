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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ArrayWordTrie implements WordTrie {
    @NonNull
    private final Node root;
    private final int abcLength;

    private ArrayWordTrie(int abcLength) {
        this.abcLength = abcLength;
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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String word;
            while ((word = reader.readLine()) != null) {
                words.add(word);
                word.codePoints().forEach(abc::add);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WordTrie wordTrie = new ArrayWordTrie(abc.size());
        for (String word : words) {
            wordTrie.insert(word);
        }

        return wordTrie;
    }

    @Override
    public void insert(@NonNull final String word) {
        final int wordLength = word.codePointCount(0, word.length()) - 1;
        int index = 0;
        Node node = root;
        while (true) {
            int key = word.codePointAt(index) % abcLength;
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
            index++;
        }
    }

    @Override
    public boolean containsSubstring(@NonNull final String substring) {
        final int substringLength = substring.codePointCount(0, substring.length());
        int index = 0;
        Node node = root;
        while (node != null) {
            if (index == substringLength) {
                return true;
            }
            node = node.nodes[substring.codePointAt(index++) % abcLength];
        }
        return false;
    }

    @Override
    public boolean containsWord(@NonNull final String word) {
        final int wordLength = word.codePointCount(0, word.length());
        int index = 0;
        Node node = root;
        while (node != null) {
            if (index == wordLength) {
                return node.isWord;
            }
            node = node.nodes[word.codePointAt(index++) % abcLength];
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
}
