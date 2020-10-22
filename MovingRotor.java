package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Jake Kim
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this.notch = notches;
        this._permutation = perm;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean reflecting() {
        return false;
    }

    /**
     * It returns whether a rotor is atNotch.
     * @return return boolean
     */
    boolean atNotch() {
        String [] notches = this.notch.split("");
        for (int i = 0; i < notches.length; i++) {
            if (notches[i].equals(this.alphabet().toChar(setting()) + "")) {
                return true;
            }
        }
        return false;


    }

    @Override
    void advance() {
        super.set(this._permutation.wrap(super.setting() + 1));
    }

    /**
     * string notch.
     */
    private String notch;

    /**
     * Permutation permutation.
     */
    private Permutation _permutation;

}
