package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;


/** The suite of all JUnit tests for the Permutation class.
 *  @author Jake Kim
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkPermutation() {
        String l = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        perm = new Permutation(l, UPPER);
        assertEquals('L', perm.permute('E'));
        assertEquals('A', perm.permute('U'));
        assertEquals('S', perm.permute('S'));
        assertEquals('C', perm.permute('Y'));
        assertEquals('J', perm.permute('Z'));

    }

    @Test
    public void checkInversion() {
        String l = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        perm = new Permutation(l, UPPER);
        assertEquals('A', perm.invert('E'));
        assertEquals('R', perm.invert('U'));
        assertEquals('S', perm.invert('S'));
        assertEquals('O', perm.invert('Y'));
        assertEquals('J', perm.permute('Z'));

    }

    @Test
    public void checkPermutationNumber() {
        String l = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        perm = new Permutation(l, UPPER);
        assertEquals(4, perm.permute(0));
        assertEquals(7, perm.permute(15));
        assertEquals(2, perm.permute(50));

        Alphabet A = new Alphabet("ABCDEFGHIJK");
        perm = new Permutation(l, A);
        assertEquals(4, perm.permute(0));
        assertEquals(3, perm.permute(6));
    }

    @Test
    public void checkInversionNumber() {
        String l = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        perm = new Permutation(l, UPPER);
        assertEquals(20, perm.invert(0));
        assertEquals(19, perm.invert(15));
        assertEquals(14, perm.invert(50));

        Alphabet A = new Alphabet("ABCDEFGHIJK");
        perm = new Permutation(l, A);
        assertEquals(3, perm.invert(5));
        assertEquals(5, perm.invert(6));
    }







}
