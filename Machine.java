package enigma;

import java.util.Collection;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Jake Kim
 */
class Machine {
    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        this._numRotors = numRotors;
        this._pawls = pawls;
        this._allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return this._numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return this._pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting.
     * @param rotors uses these parameters to insert rotors
     * @param movable uses these parameters to insert rotors
     */

    void insertRotors(String[] rotors, int movable) {
        this._usedRotors = new ArrayList<Rotor>();
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor r : _allRotors) {
                if (r.name().toUpperCase().equals(rotors[i].toUpperCase())) {
                    this._usedRotors.add(r);
                }
            }
        }
        int numofmove = 0;
        for (Rotor r : _usedRotors) {
            if (r.rotates()) {
                numofmove++;
            }
        }
        if (numofmove != movable) {
            throw new EnigmaException("not correct number of rotors");
        }
        for (Rotor r : _usedRotors) {
            if (r == null) {
                throw new EnigmaException("misnamed rotor");
            }
        }

        for (int i = 0; i < _usedRotors.size(); i++) {
            for (int j = i + 1; j < _usedRotors.size(); j++) {
                if (_usedRotors.get(i).name()
                        == _usedRotors.get(i + 1).name()) {
                    throw new EnigmaException("no duplicates");
                }
            }
        }

        if (this._usedRotors.size() != numRotors()) {
            int dif = numRotors() - _usedRotors.size();
            throw new EnigmaException(dif
                    + " " + "rotors are not from configuration");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < this._usedRotors.size(); i++) {
            this._usedRotors.get(i).set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        this._plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean [] checkBefore = new boolean [this._usedRotors.size()];
        checkBefore[this._usedRotors.size() - 1] = true;
        for (int k = this._usedRotors.size() - 2; k >= 0; k--) {
            if (this._usedRotors.get(k + 1).atNotch()
                    && this._usedRotors.get(k).rotates()) {
                checkBefore[k] = true;
                checkBefore[k + 1] = true;
            }
        }
        for (int i = 0; i < checkBefore.length; i++) {
            if (checkBefore[i]) {
                this._usedRotors.get(i).advance();
            }
        }

        int convertOut = this._plugboard.permute(c);

        for (int i = this._usedRotors.size() - 1; i >= 0; i -= 1) {
            convertOut = this._usedRotors.get(i).convertForward(convertOut);
        }

        for (int i = 1; i < this._usedRotors.size(); i++) {
            convertOut = this._usedRotors.get(i).convertBackward(convertOut);
        }

        convertOut = this._plugboard.permute((convertOut));
        return convertOut;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String copyMsg = msg;
        copyMsg = copyMsg.replaceAll(" ", "");
        String [] copyArray = copyMsg.split("");
        int [] copyArrayIndex = new int [copyArray.length];
        for (int i = 0; i < copyArrayIndex.length; i++) {
            copyArrayIndex[i] = _alphabet.toInt(copyArray[i].charAt(0));
        }
        char [] finalArray = new char [copyArray.length];
        for (int i = 0; i < copyArray.length; i++) {
            copyArrayIndex[i] = this.convert(copyArrayIndex[i]);
            finalArray[i] = _alphabet.toChar(copyArrayIndex[i]);
        }
        String out = "";
        for (int i = 0; i < finalArray.length; i++) {
            out += Character.toString(finalArray[i]);
        }
        return out;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** number of rotors. */
    private int _numRotors;
    /** number of pawls. */
    private int _pawls;
    /** collection of all rotors possible. */
    private Collection<Rotor> _allRotors;
    /** permutation that is used. */
    private Permutation _plugboard;
    /** ArrayList of all the used rotors. */
    private ArrayList<Rotor> _usedRotors;

}
