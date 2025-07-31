public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        Deque<Character> palindromeDeque = new LinkedListDeque();
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            palindromeDeque.addLast(letter);
        }
        return palindromeDeque;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> charDeque = this.wordToDeque(word);
        for (int i = 0; i < charDeque.size() / 2; i++) {
            if (!(charDeque.get(i) == (charDeque.get(charDeque.size() - 1 - i)))) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> charDeque = this.wordToDeque(word);
        for (int i = 0; i < charDeque.size() / 2; i++) {
            if (!(cc.equalChars(charDeque.get(i), charDeque.get(charDeque.size() - 1 - i)))) {
                return false;
            }
        }
        return true;
    }


}