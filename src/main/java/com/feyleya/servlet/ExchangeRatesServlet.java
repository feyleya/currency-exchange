package com.feyleya.servlet;

import com.feyleya.dto.RequestExchangeRateDto;
import com.feyleya.dto.ResponseExchangeRateDto;
import com.feyleya.exception.ExceptionHandler;
import com.feyleya.exception.ExchangeRateException;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.feyleya.util.JsonResponseUtil.*;

@WebServlet("/exchangeRates")
@Slf4j
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateService service = ExchangeRateService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /exchangeRates");
        try {
            List<ResponseExchangeRateDto> exchangeRateDtoList = service.getAll();
            sendJsonGetResponse(resp, exchangeRateDtoList);
            log.info("Successfully processed GET /exchangeRates");
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, e, "GET /exchangeRates - Error retrieving exchange rates");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String base = req.getParameter("baseCurrencyCode");
        String target = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");
        log.info("POST /exchangeRates base={}, target={}, rate={}", base, target, rate);
        try {
            ValidationUtil.validateCode(base);
            ValidationUtil.validateCode(target);
            ValidationUtil.validateRate(rate);

            RequestExchangeRateDto exchangeRateDto = new RequestExchangeRateDto(base, target, new BigDecimal(rate));
            service.add(exchangeRateDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            log.info("Successfully processed POST /exchangeRates base={}, target={}, rate={}", base, target, rate);
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, e, "POST /exchangeRates - Invalid data: {}");
        }
    }
}

