package com.feyleya.servlet;

import com.feyleya.dto.RequestExchangeDto;
import com.feyleya.dto.ResponseExchangeDto;
import com.feyleya.exception.CurrencyNotFoundException;
import com.feyleya.exception.ExceptionHandler;
import com.feyleya.exception.InvalidDataException;
import com.feyleya.service.ExchangeRateService;
import com.feyleya.util.JsonResponseUtil;
import com.feyleya.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.ErrorHandler;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
@Slf4j
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRateService service = ExchangeRateService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        log.info("GET /exchange from={}, to={}, amount={}", from, to, amount);

        try {
            ValidationUtil.validateCode(from);
            ValidationUtil.validateCode(to);
            ValidationUtil.validateRate(amount);

            RequestExchangeDto request = new RequestExchangeDto(from, to, new BigDecimal(amount));
            ResponseExchangeDto exchange = service.exchange(request);

            JsonResponseUtil.sendJsonGetResponse(resp, exchange);
            log.info("Successfully processed GET /exchange from={}, to={}, amount={}", from, to, amount);
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, e, "GET /exchange - Error processing exchange");
        }
    }
}
