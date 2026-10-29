package com.example.programservice.service;

import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    /**
     * Считает общую площадь металла (в кв. метрах).
     * Формула: (длина в мм * количество деталей) / 1000.
     * Добавляем 5% на отходы.
     */
    public double calculateTotalArea(double length, int quantity) {
        if (length <= 0 || quantity <= 0) throw new IllegalArgumentException("Длина и количество должны быть больше нуля");

        double baseArea = (length * quantity) / 1000.0;
        return baseArea * 1.05; // + 5% на отходы
    }
}
