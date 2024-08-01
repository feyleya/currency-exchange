package com.feyleya.servlet;

import com.feyleya.dto.RequestCurrencyDto;
import com.feyleya.dto.ResponseCurrencyDto;
import com.feyleya.exception.CurrencyNotFoundException;
import com.feyleya.exception.ExceptionHandler;
import com.feyleya.exception.InvalidDataException;
import com.feyleya.service.CurrencyService;
import com.feyleya.util.JsonResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.feyleya.util.ValidationUtil.*;

@WebServlet("/currency/*")
@Slf4j
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService service = CurrencyService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currencyCode = req.getPathInfo().substring(1);
        log.info("GET /currency/{}", currencyCode);
        try {
            validatePath(currencyCode);
            RequestCurrencyDto request = new RequestCurrencyDto();
            validateCode(currencyCode);
            request.setCode(currencyCode);

            ResponseCurrencyDto response = service.findByCode(request);

            JsonResponseUtil.sendJsonGetResponse(resp, response);
            log.info("Successfully processed GET /currency/{}", currencyCode);
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, e, "GET /currency - Invalid data: {}");
        }
    }
}
