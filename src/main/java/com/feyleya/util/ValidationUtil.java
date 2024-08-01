package com.feyleya.util;

import com.feyleya.exception.InvalidDataException;

import java.math.BigDecimal;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static void validateCode(String code) {
        if (code == null || code.length() != 3) {
            throw new InvalidDataException("Currency code is not a valid");
        }
    }

    public static void validateName(String name) {
        if (name == null) {
            throw new InvalidDataException("Name is not a valid");
        }
    }

    public static void validateSign(String sign) {
        if (sign == null) {
            throw new InvalidDataException("Sign is not a valid");
        }
    }

    public static void validateRate(String rate) {
        if (rate == null) {
            throw new InvalidDataException("Rate is missing or empty");
        }
        try {
            BigDecimal check = new BigDecimal(rate);
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Rate is not a valid");
        }
    }

    public static void validatePath(String path) {
        if (path == null || path.isEmpty()) {
            throw new InvalidDataException("Path is not a valid");
        }
    }
}
