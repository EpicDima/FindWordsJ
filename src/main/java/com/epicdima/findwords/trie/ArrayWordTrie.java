package com.epicdima.findwords.trie;

import com.epicdima.findwords.base.WordTrie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Могут быть проблемы, так как при операции % может быть одинаковое число при конвертации символа (char) в int
 */
public class ArrayWordTrie implements WordTrie {
    private final Node root;
    private final int abcLength;

    private ArrayWordTrie(int abcLength) {
        this.abcLength = abcLength;
        this.root = new Node(abcLength);
    }

    public static WordTrie createInstance(String dictionaryPath) {
        try {
            return createInstance(new FileInputStream(dictionaryPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static WordTrie createInstance(InputStream inputStream) {
        List<String> words = new ArrayList<>();
        Set<Character> abc = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String word;
            while ((word = reader.readLine()) != null) {
                words.add(word);

                for (int i = 0; i < word.length(); i++) {
                    abc.add(word.charAt(i));
                }
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
    public void insert(final String word) {
        final int length = word.length() - 1;
        int index = 0;

        Node node = root;

        while (true) {
            int key = word.charAt(index) % abcLength;

            Node tempNode = node.nodes[key];
            if (tempNode != null) {
                if (index < length) {
                    node = tempNode;
                } else {
                    tempNode.setWord(true);
                    break;
                }
            } else {
                if (index < length) {
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
    public boolean containsSubstring(final String substring) {
        int index = 0;

        Node node = root;

        while (node != null) {
            if (index == substring.length()) {
                return true;
            }

            node = node.nodes[substring.charAt(index++) % abcLength];
        }

        return false;
    }

    @Override
    public boolean containsWord(final String word) {
        int index = 0;

        Node node = root;

        while (node != null) {
            if (index == word.length()) {
                return node.isWord;
            }

            node = node.nodes[word.charAt(index++) % abcLength];
        }

        return false;
    }


    private static class Node {
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
