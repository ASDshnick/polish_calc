package ru.yarsu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private final Calculator calc = new Calculator();

    // --- Базовые операции ---
    @Test
    void testAddition() {
        assertEquals(5.0, calc.evaluate("2 + 3"));
    }

    @Test
    void testSubtraction() {
        assertEquals(-1.0, calc.evaluate("2 - 3"));
    }

    @Test
    void testMultiplication() {
        assertEquals(6.0, calc.evaluate("2 * 3"));
    }

    @Test
    void testDivision() {
        assertEquals(2.0, calc.evaluate("6 / 3"));
    }

    @Test
    void testModulus() {
        assertEquals(1.0, calc.evaluate("5 % 2"));
    }

    @Test
    void testPower() {
        assertEquals(8.0, calc.evaluate("2 ^ 3"));
    }

    // --- Приоритет и ассоциативность ---
    @Test
    void testPrecedence() {
        assertEquals(14.0, calc.evaluate("2 + 3 * 4"));          // 2 + (3*4)
        assertEquals(20.0, calc.evaluate("(2 + 3) * 4"));         // (2+3)*4
        assertEquals(16.0, calc.evaluate("2 ^ 3 * 2"));           // (2^3)*2, не 2^(3*2)
        assertEquals(64.0, calc.evaluate("2 ^ (3 * 2)"));        // 2^(6)
    }

    @Test
    void testRightAssociativityPower() {
        assertEquals(256.0, calc.evaluate("2 ^ 2 ^ 3"));          // 2^(2^3) = 2^8 = 256
    }

    // --- Скобки ---
    @Test
    void testNestedBrackets() {
        assertEquals(15.0, calc.evaluate("((1 + 2) * (3 + 2))"));
        assertEquals(-11.0, calc.evaluate("1 + (2 - (3 + 4) * 2)"));
    }

    // --- Десятичные числа ---
    @Test
    void testDecimals() {
        assertEquals(2.5, calc.evaluate("5 / 2"), 1e-10);
        assertEquals(1.25, calc.evaluate("2.5 / 2"), 1e-10);
        assertEquals(0.3333333333333333, calc.evaluate("1 / 3"), 1e-15);
    }

    // --- Ошибки ---
    @Test
    void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> calc.evaluate("5 / 0"));
    }

    @Test
    void testModuloByZero() {
        assertThrows(ArithmeticException.class, () -> calc.evaluate("5 % 0"));
    }

    @Test
    void testInvalidSymbol() {
        assertThrows(IllegalArgumentException.class, () -> calc.evaluate("2 $ 3"));
    }

    @Test
    void testMismatchedBrackets() {
        assertThrows(IllegalArgumentException.class, () -> calc.evaluate("2 + (3 * 4"));
        assertThrows(IllegalArgumentException.class, () -> calc.evaluate("2 + )3 * 4("));
    }

    @Test
    void testEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> calc.evaluate(""));
    }

    // --- RPN преобразование (внутренняя проверка) ---
    @Test
    void testRPNTransform() {
        assertEquals("2 3 +", calc.transformToRPN("2 + 3"));
        assertEquals("2 3 4 * +", calc.transformToRPN("2 + 3 * 4"));
        assertEquals("2 3 + 4 *", calc.transformToRPN("(2 + 3) * 4"));
        assertEquals("2 3 2 ^ ^", calc.transformToRPN("2 ^ 3 ^ 2")); // правоассоц.
    }
}