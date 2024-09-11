// Authors: denan895, donan928

import java.util.*;

public class FindingAnagrams {

    // method which returns the list of best anagrams
    public static String findBestAnagram(String word, List<String> dictionary) {
        List<String> bestAnagram = new ArrayList<>(); // list of best anagrams
        Set<String> seenAnagrams = new HashSet<>(); // handles duplicate anagrams
        // start of recursive call to findBestAnagram
        findBestAnagramRecurisvely(word, new ArrayList<>(dictionary), new ArrayList<>(), bestAnagram, seenAnagrams);
        bestAnagram.sort(Comparator.comparingInt(String::length).reversed().thenComparing(String::compareTo));
        return String.join(" ", bestAnagram);
    }

    // recursive method to find best anagram
    private static void findBestAnagramRecurisvely(String target, List<String> dictionary, List<String> currAnagram,
            List<String> bestAnagram, Set<String> seenAnagrams) {
        // if target word has all letters removed
        if (target.isEmpty()) {
            List<String> sortedCurrentAnagram = new ArrayList<>(currAnagram);
            sortedCurrentAnagram.sort(String::compareTo);
            String currentAnagramKey = String.join(" ", sortedCurrentAnagram);
            // checks if current anagram has been found earlier
            if (!seenAnagrams.contains(currentAnagramKey)) {
                seenAnagrams.add(currentAnagramKey);
                // finding any better anagrams available
                if (isBetterAnagram(currAnagram, bestAnagram)) {
                    bestAnagram.clear();
                    bestAnagram.addAll(currAnagram);
                }
            }
            return;
        }

        // compares the leftover letters from earlier calls to the method against words
        // from dictionary
        for (String newWord : new ArrayList<>(dictionary)) {
            if (containsAllLetters(target, newWord)) {
                List<String> updatedAnagram = new ArrayList<>(currAnagram);
                updatedAnagram.add(newWord);
                String updatedRemainingWord = removeLetters(target, newWord);
                findBestAnagramRecurisvely(updatedRemainingWord, dictionary, updatedAnagram, bestAnagram, seenAnagrams);
            }
        }
    }

    // method which compares anagrams to find the best available one
    private static boolean isBetterAnagram(List<String> currAnagram, List<String> bestAnagram) {
        if (bestAnagram.isEmpty()) {
            return true;
        }

        // sort both lists by length then lexicographically
        List<String> currCopy = new ArrayList<>(currAnagram);
        List<String> bestCopy = new ArrayList<>(bestAnagram);
        currCopy.sort(Comparator.comparingInt(String::length).reversed().thenComparing(String::compareTo));
        bestCopy.sort(Comparator.comparingInt(String::length).reversed().thenComparing(String::compareTo));

        for (int i = 0; i < Math.min(currCopy.size(), bestCopy.size()); i++) {
            // compare sorted lists by length
            int lengthCmp = Integer.compare(currCopy.get(i).length(),
                    bestCopy.get(i).length());
            if (lengthCmp != 0) {
                return lengthCmp > 0;
            }
            // compare sorted lists lexicographically
            int lexicalCmp = currCopy.get(i).compareTo(bestCopy.get(i));
            if (lexicalCmp != 0) {
                return lexicalCmp < 0;
            }
        }

        return currAnagram.size() < bestAnagram.size();
    }

    // method that checks if an anagram for a word matches all letters
    private static boolean containsAllLetters(String target, String newWord) {
        int[] count = new int[26];
        for (char c : target.toCharArray()) {
            // assuming only using words with lowercase letters
            if (c >= 'a' && c <= 'z') {
                count[c - 'a']++;
            }
        }
        for (char c : newWord.toCharArray()) {
            if (count[c - 'a'] == 0) {
                return false;
            }
            count[c - 'a']--;
        }
        return true;
    }

    // method which removes letters from a string based on another
    private static String removeLetters(String target, String newWord) {
        StringBuilder sb = new StringBuilder(target);
        for (char c : newWord.toCharArray()) {
            int index = sb.indexOf(String.valueOf(c));
            if (index != -1) {
                sb.deleteCharAt(index);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<String> dict = new ArrayList<String>();
        boolean isDictionary = false;
        // reading lines in from file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase().trim();
            if (line.isEmpty()) {
                // finding the blank line after the target words
                isDictionary = true;
                continue;
            }
            if (isDictionary) {
                dict.add(line);
            } else {
                words.add(line);
            }
        }

        for (String word : words) {
            String bestAnagram = findBestAnagram(word, dict);
            System.out.println(word + ": " + bestAnagram);
        }

        scanner.close();
    }
}
