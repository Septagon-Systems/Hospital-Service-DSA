package edu.ug.dcit308.hospital.app;

import edu.ug.dcit308.hospital.app.util.StudentParameterCalculator;
import edu.ug.dcit308.hospital.app.util.StudentParameterCalculator.StudentParameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentParameterCalculatorTest {

    @Test
    void testValidStudentIdCalculation() {
        StudentParameters params = StudentParameterCalculator.calculate("10987654");
        assertNotNull(params);
        assertEquals("10987654", params.indexNumber);
        assertTrue(params.priorityWeight >= 1 && params.priorityWeight <= 5);
        assertTrue(params.hashTableSize > 0);
        assertTrue(params.toString().contains("Algorithm Parameters for Student Index 10987654"));
    }

    @Test
    void testInvalidStudentIdHandling() {
        assertThrows(NumberFormatException.class, () -> StudentParameterCalculator.calculate(""));
    }
}
