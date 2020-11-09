package bearmaps;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;


public class MyTrieSet implements TrieSet61BL {
    TrieNode root;

    MyTrieSet() {
        root = new TrieNode('\u0000', false);
    }

    @Override
    public void clear() {
        root = new TrieNode('\u0000', false);
        return;
    }

    @Override
    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            return false;
        }
        TrieNode curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            // tests if subsequent letter has such a character in the tree
            if (!curr.map.containsKey(c)) {
                return false;
            }
            curr = curr.map.get(c);
        }
        // tests if those set of letters is considered a word
        return curr.isKey;
    }

    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        TrieNode curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new TrieNode(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
        return;
    }

    public List<String> keysWithPrefix(String prefix) {
        List<String> keys = new LinkedList<>();
        if (prefix == null || prefix.length() < 0) {
            return null;
        }

        TrieNode curr = root;
        for (int i = 0, n = prefix.length(); i < n; i++) {
            char c = prefix.charAt(i);
            if (!curr.map.containsKey(c)) {
                return null;
            }
            curr = curr.map.get(c);
        }
        if (curr.isKey) {
            keys.add(prefix);
        }
        prefix = prefix.substring(0, prefix.length() - 1);
        return prefixHelper(curr, prefix, keys);
    }

    public List<String> prefixHelper(TrieNode node, String prefix, List<String> keys) {
        if (node == null) {
            return keys;
        }
        if (node.isKey) {
            keys.add(prefix + node.character);
        }
        for (Map.Entry<Character, TrieNode> entry : node.map.entrySet()) {
            prefixHelper(entry.getValue(), prefix + node.character, keys);
        }
        return keys;
    }

    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

    private class TrieNode {
        HashMap<Character, TrieNode> map;
        boolean isKey;
        char character;

        TrieNode(char c, boolean isWord) {
            character = c;
            isKey = isWord;
            map = new HashMap<>();
        }
    }
}
