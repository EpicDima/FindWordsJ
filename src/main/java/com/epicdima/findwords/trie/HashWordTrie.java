package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class HashWordTrie implements WordTrie {
    @NonNull
    private final Node root = new Node();

    private HashWordTrie() {
    }

    @SuppressWarnings("unused") // used via MethodHandle
    @NonNull
    public static WordTrie createInstance(@NonNull String dictionaryPath) {
        WordTrie wordTrie = new HashWordTrie();
        WordTrie.fill(wordTrie, dictionaryPath);
        return wordTrie;
    }

    @SuppressWarnings("unused") // used via MethodHandle
    @NonNull
    public static WordTrie createInstance(@NonNull InputStream inputStream) {
        WordTrie wordTrie = new HashWordTrie();
        WordTrie.fill(wordTrie, inputStream);
        return wordTrie;
    }

    @Override
    public void insert(@NonNull final String word) {
        final int wordLength = word.codePointCount(0, word.length()) - 1;
        int index = 0;
        Node node = root;
        while (true) {
            int codePoint = word.codePointAt(index);
            Node tempNode = node.letters.get(codePoint);
            if (tempNode != null) {
                if (index < wordLength) {
                    node = tempNode;
                } else {
                    tempNode.setWord(true);
                    break;
                }
            } else {
                if (index < wordLength) {
                    Node newNode = new Node();
                    node.letters.put(codePoint, newNode);
                    node = newNode;
                } else {
                    node.letters.put(codePoint, new Node(true));
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
            node = node.letters.get(substring.codePointAt(index++));
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
            node = node.letters.get(word.codePointAt(index++));
        }
        return false;
    }

    private static final class Node {
        @NonNull
        private final Map<Integer, Node> letters = new HashMap<>();
        private boolean isWord;

        public Node(boolean isWord) {
            this.isWord = isWord;
        }

        public Node() {
            this(false);
        }

        public void setWord(boolean word) {
            isWord = word;
        }
    }
}
