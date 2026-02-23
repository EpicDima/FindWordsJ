package com.epicdima.findwords.trie;

import androidx.annotation.NonNull;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public final class SetWordTrie implements WordTrie {
    @NonNull
    private final Set<String> words = new HashSet<>();
    @NonNull
    private final Set<String> substrings = new HashSet<>();

    @SuppressWarnings("unused") // used via MethodHandle
    @NonNull
    public static WordTrie createInstance(@NonNull String dictionaryPath) {
        WordTrie wordTrie = new SetWordTrie();
        WordTrie.fill(wordTrie, dictionaryPath);
        return wordTrie;
    }

    @SuppressWarnings("unused") // used via MethodHandle
    @NonNull
    public static WordTrie createInstance(@NonNull InputStream inputStream) {
        WordTrie wordTrie = new SetWordTrie();
        WordTrie.fill(wordTrie, inputStream);
        return wordTrie;
    }

    @Override
    public WordTrie.Cursor cursor() {
        return new SetCursor(this);
    }

    @Override
    public void insert(@NonNull String word) {
        words.add(word);
        final StringBuilder stringBuilder = new StringBuilder(word.length());
        for (int i = 0; i < word.length(); ) {
            int codePoint = word.codePointAt(i);
            stringBuilder.appendCodePoint(codePoint);
            substrings.add(stringBuilder.toString());
            i += Character.charCount(codePoint);
        }
    }

    @Override
    public boolean containsSubstring(@NonNull String substring) {
        return substrings.contains(substring);
    }

    @Override
    public boolean containsWord(@NonNull String word) {
        return words.contains(word);
    }

    private static class SetCursor implements WordTrie.Cursor {
        private final SetWordTrie trie;
        private final StringBuilder sb = new StringBuilder(256);

        public SetCursor(SetWordTrie trie) {
            this.trie = trie;
        }

        @Override
        public boolean push(int codePoint) {
            sb.appendCodePoint(codePoint);
            if (trie.substrings.contains(sb.toString())) {
                return true;
            } else {
                sb.setLength(sb.length() - Character.charCount(codePoint));
                return false;
            }
        }

        @Override
        public void pop() {
            int length = sb.length();
            if (length > 1 && Character.isSurrogatePair(sb.charAt(length - 2), sb.charAt(length - 1))) {
                sb.setLength(length - 2);
            } else {
                sb.setLength(length - 1);
            }
        }

        @Override
        public boolean isWord() {
            return trie.words.contains(sb.toString());
        }
    }
}
