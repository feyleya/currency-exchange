package com.feyleya.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ExceptionHandler {
    public static void handleException(HttpServletResponse response, Exception e, String logMessage) {
        try {
            if (e instanceof InvalidDataException) {
                logErrorAndSend(response, HttpServletResponse.SC_BAD_REQUEST, e, logMessage);
            } else if (e instanceof CurrencyNotFoundException) {
                logErrorAndSend(response, HttpServletResponse.SC_NOT_FOUND, e, logMessage);
            } else if (e instanceof ExchangeRateException) {
                logErrorAndSend(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, logMessage);
            } else if (e instanceof DataNotFoundException) {
                logErrorAndSend(response, HttpServletResponse.SC_NOT_FOUND, e, logMessage);
            } else if (e instanceof DatabaseFileNotFoundException) {
                logErrorAndSend(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, logMessage);
            } else {
                logErrorAndSend(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, "Unexpected error");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void logErrorAndSend(HttpServletResponse response, int statusCode, Exception e, String logMessage) throws IOException {
        log.error(logMessage, e);
        response.sendError(statusCode, e.getMessage());
    }
}
