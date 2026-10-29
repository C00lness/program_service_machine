package com.example.programservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculationServiceTest {

    private final CalculationService calculationService = new CalculationService();

    // 1. Параметризованный тест с контрольными примерами
    @ParameterizedTest(name = "Длина: {0} мм, Кол-во: {1} шт -> Ожидаем: {2} кв.м.")
    @CsvSource({
            "1000, 10, 10.5",     // 10 * 1.05 = 10.5
            "500,  4,  2.1",      // 2 * 1.05 = 2.1
            "2500, 2,  5.25"      // 5 * 1.05 = 5.25
    })
    void calculateTotalArea_ControlExamples(double length, int quantity, double expectedArea) {
        double actualArea = calculationService.calculateTotalArea(length, quantity);

        // Проверяем с погрешностью (delta) 0.001, так как работаем с double
        assertEquals(expectedArea, actualArea, 0.001, "Площадь рассчитана неверно");
    }

    // 2. Негативный тест (проверка исключения)
    @Test
    void calculateTotalArea_ThrowsException_WhenQuantityIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculationService.calculateTotalArea(1000, -5);
        });
    }
}
