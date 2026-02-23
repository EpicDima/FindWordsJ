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
    public WordTrie.Cursor cursor() {
        return new HashCursor(root);
    }

    @Override
    public void insert(@NonNull final String word) {
        final int wordLength = word.length();
        int index = 0;
        Node node = root;
        while (index < wordLength) {
            int codePoint = word.codePointAt(index);
            index += Character.charCount(codePoint);
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
        }
    }

    @Override
    public boolean containsSubstring(@NonNull final String substring) {
        final int substringLength = substring.length();
        int index = 0;
        Node node = root;
        while (node != null && index < substringLength) {
            int codePoint = substring.codePointAt(index);
            index += Character.charCount(codePoint);
            node = node.letters.get(codePoint);
            if (index == substringLength && node != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsWord(@NonNull final String word) {
        final int wordLength = word.length();
        int index = 0;
        Node node = root;
        while (node != null && index < wordLength) {
            int codePoint = word.codePointAt(index);
            index += Character.charCount(codePoint);
            node = node.letters.get(codePoint);
            if (index == wordLength && node != null) {
                return node.isWord;
            }
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

    private static class HashCursor implements WordTrie.Cursor {
        private final Node[] path = new Node[256];
        private int depth = 0;

        public HashCursor(Node root) {
            this.path[0] = root;
        }

        @Override
        public boolean push(int codePoint) {
            Node nextNode = path[depth].letters.get(codePoint);
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
