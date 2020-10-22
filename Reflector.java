package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Jake Kim
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
    }

    /**It returns whether it is reflecting.
     * @return boolean
     */
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw new EnigmaException("reflector has only one position");
        }
    }

    /**It returns error.
     * @param e it takes in integer
     * @return throw error
     */
    int convertBackward(int e) {
        throw new EnigmaException("Reflectors cannot go backwards");
    }



}
