package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ScholarshipCalculatorTest {

    private static final double EPS = 1e-9;

    private final ScholarshipCalculator calc = new ScholarshipCalculator();

    // ---------- Хэсэг A: happy path ----------

    @Test
    void letterGrade_score95_returnsA() {
        assertEquals("A", calc.letterGrade(95));
    }

    @Test
    void letterGrade_score85_returnsB() {
        assertEquals("B", calc.letterGrade(85));
    }

    @Test
    void gpa_scores95And85_returns3point5() {
        assertEquals(3.5, calc.gpa(new double[] {95, 85}), EPS);
    }

    @Test
    void scholarship_gpa39Credits16NoViolation_returns100() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
    }

    @Test
    void scholarship_gpa35Credits12NoViolation_returns70() {
        assertEquals(70, calc.scholarship(3.5, 12, false));
    }

    // ---------- Хэсэг B: branch 100% ----------

    // letterGrade
    @Test
    void letterGrade_score75_returnsC() {
        assertEquals("C", calc.letterGrade(75));
    }

    @Test
    void letterGrade_score65_returnsD() {
        assertEquals("D", calc.letterGrade(65));
    }

    @Test
    void letterGrade_score30_returnsF() {
        assertEquals("F", calc.letterGrade(30));
    }

    @Test
    void letterGrade_negativeScore_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1));
    }

    @Test
    void letterGrade_scoreAbove100_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101));
    }

    // gpa
    @Test
    void gpa_nullScores_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
    }

    @Test
    void gpa_emptyScores_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[0]));
    }

    @Test
    void gpa_scoresCDandF_returns1() {
        // C=2, D=1, F=0 (switch-ийн default салаа) -> 3/3 = 1.0
        assertEquals(1.0, calc.gpa(new double[] {75, 65, 30}), EPS);
    }

    // scholarship
    @Test
    void scholarship_negativeGpa_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(-0.1, 15, false));
    }

    @Test
    void scholarship_gpaAbove4_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.1, 15, false));
    }

    @Test
    void scholarship_negativeCredits_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(3.5, -1, false));
    }

    @Test
    void scholarship_hasViolation_returns0() {
        assertEquals(0, calc.scholarship(3.9, 16, true));
    }

    @Test
    void scholarship_gpa39ButCredits14_returns70() {
        // голч >= 3.8 хангасан ч кредит < 15 -> 100 биш, дараагийн нөхцөл 70
        assertEquals(70, calc.scholarship(3.9, 14, false));
    }

    @Test
    void scholarship_gpa35ButCredits11_returns50() {
        // голч >= 3.4 хангасан ч кредит < 12 -> 70 биш, 50
        assertEquals(50, calc.scholarship(3.5, 11, false));
    }

    @Test
    void scholarship_gpa32Credits10_returns50() {
        assertEquals(50, calc.scholarship(3.2, 10, false));
    }

    @Test
    void scholarship_gpa25Credits20_returns0() {
        assertEquals(0, calc.scholarship(2.5, 20, false));
    }

    // ---------- Хэсэг C: gpa() доторх давталтын зааг (0 / 1 / 2+) ----------

    @Test
    void gpa_loopZeroIterations_emptyOrNullScores_throws() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[0]), "empty"),
                () -> assertThrows(IllegalArgumentException.class, () -> calc.gpa(null), "null"));
    }

    @Test
    void gpa_loopOneIteration_singleScore95_returns4() {
        assertEquals(4.0, calc.gpa(new double[] {95}), EPS);
    }

    @Test
    void gpa_loopManyIterations_scores95_85_75_65_returns2point5() {
        assertEquals(2.5, calc.gpa(new double[] {95, 85, 75, 65}), EPS);
    }

    // ---------- Хэсэг D: letterGrade-ийн зааг утгууд (спецификацийн хил бүр) ----------

    @ParameterizedTest(name = "letterGrade({0}) = {1}")
    @CsvSource({
            "60,    D",
            "59.99, F",
            "69.99, D",
            "0,     F",
            "100,   A",
            "89.99, B",
            "90,    A"
    })
    void letterGrade_boundaryScores_matchSpec(double score, String expected) {
        assertEquals(expected, calc.letterGrade(score));
    }
}
