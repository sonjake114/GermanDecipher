package enigma;

import static enigma.EnigmaException.*;
import java.util.ArrayList;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jake Kim
 */
class Permutation {


    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        this._alphabet = alphabet;
        this._cycles = cycles;
    }

    /**
     * It makes cycles by breaking it.
     * @param cycle it takes in string of cycles.
     * @return a it returns the arraylist.
     */
    public static ArrayList<String> makeCycle(String cycle) {
        String [] copyArray;
        ArrayList<String> a = new ArrayList<String>();
        String copy = cycle;
        copy = copy.trim();
        if (copy.length() != 0) {
            if (copy.contains(" ")) {
                copy = copy.substring(1, copy.length() - 1);
                copy = copy.replaceAll(" ", "");
                copyArray = copy.split("\\)\\(");

            } else {
                copy = copy.replaceAll("\\(", "");
                copy = copy.replaceAll("\\)", "");
                copyArray = new String [] {copy};
            }
            for (int i = 0; i < copyArray.length; i++) {
                a.add(copyArray[i]);
            }
        }
        return a;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        this._cycles += cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int begin = this.wrap(p);
        char beginPlace = this._alphabet.toChar(begin);
        char placeAfter = permute(beginPlace);
        int placeOut = this._alphabet.toInt(placeAfter);
        return placeOut;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int begin = this.wrap(c);
        char beginPlace = this._alphabet.toChar(begin);
        char placeAfter = invert(beginPlace);
        int placeOut = this._alphabet.toInt(placeAfter);
        return placeOut;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        ArrayList<String> fake = makeCycle(this._cycles);
        char finalchar = p;
        if (this._cycles.equals("")) {
            return finalchar;
        } else {
            for (String b : fake) {
                if (b.indexOf(p) != -1) {
                    if (b.indexOf(p) == b.length() - 1) {
                        finalchar = b.charAt(0);
                    } else {
                        finalchar = b.charAt(b.indexOf(p) + 1);
                    }

                }
            }
        }
        return finalchar;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        ArrayList<String> fake = makeCycle(this._cycles);
        char finalchar = c;
        if (this._cycles.equals("")) {
            return finalchar;
        } else {
            for (String b : fake) {
                if (b.indexOf(c) != -1) {
                    if (b.indexOf(c) == 0) {
                        finalchar = b.charAt(b.length() - 1);
                    } else {
                        finalchar = b.charAt(b.indexOf(c) - 1);
                    }

                }
            }
        }
        return finalchar;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        ArrayList<String> a = makeCycle(this._cycles);
        for (String b: a) {
            if (b.length() == 1) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** String of _cycles. */
    private String _cycles;
}
