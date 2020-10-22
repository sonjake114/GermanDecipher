package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Jake Kim
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }

    /**
     * @return Default to false for fixedrotor.
     */
    boolean rotates() {
        return false;
    }

    /**
     * @return Default to false for fixedrotor
     */
    boolean reflecting() {
        return false;
    }
}
