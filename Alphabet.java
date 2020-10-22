package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Jake Kim
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        this.alphabetPart = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return this.alphabetPart.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        return this.alphabetPart.indexOf(ch) != -1;

    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return this.alphabetPart.charAt(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return this.alphabetPart.indexOf(ch);
    }

    /** representing alphabet part.
     */
    private String alphabetPart;

}
