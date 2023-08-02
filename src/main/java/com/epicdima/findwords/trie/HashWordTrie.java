package com.epicdima.findwords.trie;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HashWordTrie implements WordTrie {
    private final Node root = new Node();

    private HashWordTrie() {
    }

    public static WordTrie createInstance(String dictionaryPath) {
        WordTrie wordTrie = new HashWordTrie();
        WordTrie.fill(wordTrie, dictionaryPath);
        return wordTrie;
    }

    public static WordTrie createInstance(InputStream inputStream) {
        WordTrie wordTrie = new HashWordTrie();
        WordTrie.fill(wordTrie, inputStream);
        return wordTrie;
    }

    @Override
    public void insert(final String word) {
        final int length = word.length() - 1;
        int index = 0;

        Node node = root;

        while (true) {
            char ch = word.charAt(index);

            Node tempNode = node.letters.get(ch);
            if (tempNode != null) {
                if (index < length) {
                    node = tempNode;
                } else {
                    tempNode.setWord(true);
                    break;
                }
            } else {
                if (index < length) {
                    Node newNode = new Node();
                    node.letters.put(ch, newNode);
                    node = newNode;
                } else {
                    node.letters.put(ch, new Node(true));
                    break;
                }
            }

            index++;
        }
    }

    @Override
    public boolean containsSubstring(final String substring) {
        final int length = substring.length();
        int index = 0;

        Node node = root;

        while (node != null) {
            if (index == length) {
                return true;
            }

            node = node.letters.get(substring.charAt(index++));
        }

        return false;
    }

    @Override
    public boolean containsWord(final String word) {
        final int length = word.length();
        int index = 0;

        Node node = root;

        while (node != null) {
            if (index == length) {
                return node.isWord;
            }

            node = node.letters.get(word.charAt(index++));
        }

        return false;
    }


    private static class Node {
        private final Map<Character, Node> letters = new HashMap<>();
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
