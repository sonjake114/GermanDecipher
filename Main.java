package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Jake Kim
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);
        _freshConfig = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        int count = 1;
        Machine finMec = addItToMachine();
        String ms = "";
        while (this._input.hasNextLine()) {
            ms = this._input.nextLine();
            if (ms.equals("")) {
                printMessageLine(ms);
            } else if (ms.substring(0, 1).equals("*")) {
                count++;
                String setString = ms;
                String newinput  = setString.substring(2);
                String [] nra = newinput.split(" ");
                if (nra.length <= finMec.numRotors()) {
                    throw new EnigmaException("Rotor numbers do not match");
                }
                checkRotorSetting(nra, finMec.numRotors(),
                        finMec.numPawls(), newinput);
                String [] newcopiedArray = new String [finMec.numRotors()];
                System.arraycopy(nra, 0, newcopiedArray, 0, finMec.numRotors());
                finMec.insertRotors(newcopiedArray, finMec.numPawls());
                if (nra[finMec.numRotors()].substring(0, 1).equals("(")) {
                    finMec.setRotors("AAAA");
                } else {
                    finMec.setRotors(nra[finMec.numRotors()]);
                }
                Permutation npbp;
                if (setString.indexOf("(") != -1) {
                    String npc = setString.substring(setString.indexOf("("));
                    npbp = new Permutation(npc, this._alphabet);
                } else {
                    npbp = new Permutation("", this._alphabet);
                }
                finMec.setPlugboard(npbp);
            } else {
                printMessageLine(finMec.convert(ms));
            }
            count++;
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     *      *  file _config and apply it to the messages in _input, sending the
     *      *  results to _output.
     * @return Final Machine
     */
    public Machine addItToMachine() {
        Machine cm = readConfig(); String line1 = _input.nextLine();
        if (line1.charAt(0) != '*') {
            throw new EnigmaException("Bad input");
        }
        String lws = line1.substring(2);
        String [] eachRotorArray = lws.split(" ");
        if (eachRotorArray.length <=  cm.numRotors()) {
            throw new EnigmaException("Rotor numbers do not match");
        }
        checkRotorSetting(eachRotorArray, cm.numRotors(), cm.numPawls(), lws);
        cm.insertRotors(eachRotorArray, cm.numPawls());
        if (eachRotorArray
                [cm.numRotors()].substring(0, 1).equals("(")) {
            cm.setRotors("AAAA");
        } else {
            cm.setRotors(eachRotorArray
                    [cm.numRotors()]);
        }
        Permutation pbp;
        if (lws.indexOf("(") != -1) {
            String plugboardCycle = lws.substring(lws.indexOf("("));
            pbp = new Permutation(plugboardCycle, this._alphabet);
        } else {
            pbp = new Permutation("", this._alphabet);
        }
        cm.setPlugboard(pbp);
        return cm;
    }

    /**
     * It checks the validity of rotor.
     * @param inputs input of array.
     * @param rotorcount number of rotors
     * @param movable number of movable
     * @param lws linewithoutstar
     */
    public void checkRotorSetting(String [] inputs,
                                  int rotorcount, int movable, String lws) {
        boolean isReflectorThere = false;
        for (int i = 0; i < this.reflector.size(); i += 1) {
            if (inputs[0].equals(this.reflector.get(i).name())) {
                isReflectorThere = true;
            }
        }
        if (!isReflectorThere) {
            throw new EnigmaException("Reflector is not at index 0");
        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(this._config.nextLine());
            int tr = this._config.nextInt();
            int movableRotors = this._config.nextInt();
            String [][] currentRotors = getRotorSpec();
            ArrayList<Rotor> inputRotors = new ArrayList<Rotor>();
            for (int i = 0; i < currentRotors.length; i++) {
                inputRotors.add(readRotor(currentRotors[i]));
            }
            return new Machine(_alphabet, tr, movableRotors, inputRotors);
        } catch (NoSuchElementException excp) {
            throw new EnigmaException("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config.
     * @param motorConfig it takes in all the motors.
     * */
    private Rotor readRotor(String [] motorConfig) {
        try {
            String name = motorConfig[0].trim();
            String cycles = motorConfig[2].trim();
            Permutation inputPerm = new Permutation(cycles, this._alphabet);
            if (motorConfig[1].charAt(0) == 'M') {
                String notches = motorConfig[1].substring(1);
                MovingRotor move = new MovingRotor(name, inputPerm, notches);
                this.movableRotor.add(move);
                return move;
            } else if (motorConfig[1].charAt(0) == 'N') {
                FixedRotor fixed = new FixedRotor(name, inputPerm);
                this.fixedRotor.add(fixed);
                return fixed;
            } else {
                Reflector reflect = new Reflector(name, inputPerm);
                this.reflector.add(reflect);
                return reflect;
            }
        } catch (NoSuchElementException excp) {
            throw new EnigmaException("bad rotor description");
        }
    }

    /**
     * It returns the 2d array of all the rotors specs.
     * @return out.
     */
    public String [][] getRotorSpec() {
        this._config.nextLine();
        int numberRemainLine = countLine();
        this._freshConfig.nextLine();
        this._freshConfig.nextLine();
        String [][] out = new String [numberRemainLine][3];
        int i = 0;
        while (this._freshConfig.hasNextLine()) {
            String [] temp = new String [3];
            String tl = this._freshConfig.nextLine().trim();
            if (!tl.substring(0, 1).equals("(")) {
                String firstTwo = tl.substring(0, tl.indexOf("(") - 1);
                String last = tl.substring(tl.indexOf("("));
                last = last.trim();
                String first = firstTwo.substring(0, firstTwo.indexOf(" "));
                first = first.trim();
                String two = firstTwo.substring(firstTwo.indexOf(" ") + 1);
                two = two.trim();
                temp[0] = first;
                temp[1] = two;
                temp[2] = last;
                out[i] = temp;
                i += 1;
            } else {
                out[i - 1][2] = out[i - 1][2] + " " + tl;
            }
        }
        return out;
    }

    /**
     * It counts the number of remaining lines.
     * @return num
     */
    public int countLine() {
        int num = 0;
        while (this._config.hasNextLine()) {
            if (!this._config.nextLine().trim().substring(0, 1).equals("(")) {
                num += 1;
            }
        }
        return num;
    }


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String out = " ";
        int num = 0;
        for (int i = 0; i < msg.length(); i++) {
            out += msg.charAt(i);
            num++;
            if (num % 5 == 0 && i != msg.length() - 1) {
                out += " ";
            }
        }
        System.out.println(out.trim());
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** Fresh line reader. */
    private Scanner _freshConfig;

    /** Arraylist of all the rotors. */
    private ArrayList<Rotor> movableRotor = new ArrayList<Rotor>();

    /** Arraylist of fixed.*/
    private ArrayList<Rotor> fixedRotor = new ArrayList<Rotor>();

    /** ArrayList of reflectors.*/
    private ArrayList<Rotor> reflector = new ArrayList<Rotor>();

    /** File for encoded/decoded messages. */
    private PrintStream _output;

}
