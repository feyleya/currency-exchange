package com.feyleya.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.feyleya.dto.ResponseCurrencyDto;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonResponseUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void sendJsonGetResponse(HttpServletResponse resp, Object responseObject) throws IOException {
        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        String jsonResponse = objectWriter.writeValueAsString(responseObject);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonResponse);
    }

    public static void sendJsonPostResponse(HttpServletResponse resp, ResponseCurrencyDto response) throws IOException {
        String jsonResponse = mapper.writeValueAsString(response);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(jsonResponse);
    }
}
