package com.feyleya.servlet;

import com.feyleya.dto.RequestCurrencyDto;
import com.feyleya.dto.ResponseCurrencyDto;
import com.feyleya.entity.Currency;
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
import java.util.List;

import static com.feyleya.util.ValidationUtil.*;

@WebServlet("/currencies")
@Slf4j
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService service = CurrencyService.INSTANCE;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("GET /currencies getAll()");
        try {
            List<ResponseCurrencyDto> currencyDtoList = service.getAll();
            JsonResponseUtil.sendJsonGetResponse(resp, currencyDtoList);
            log.info("Successfully processed GET /currencies");
        } catch (Exception e) {
            log.error("GET /currencies - Internal server error", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        log.info("POST /currencies code = {}, name = {}, sign = {}", code, name, sign);

        validateName(name);
        validateCode(code);
        validateSign(sign);

        try {
            validateName(name);
            validateCode(code);
            validateSign(sign);

            RequestCurrencyDto requestCurrencyDto = new RequestCurrencyDto(name, code, sign);
            Currency currency = new Currency();
            currency.setFullName(name);
            currency.setCode(code);
            currency.setSign(sign);

            service.add(requestCurrencyDto);

            ResponseCurrencyDto response = service.findByCode(requestCurrencyDto);

            JsonResponseUtil.sendJsonPostResponse(resp, response);
            log.info("Successfully processed POST /currencies code = {}, name = {}, sign = {}", code, name, sign);
        } catch (InvalidDataException e) {
            ExceptionHandler.handleException(resp, e, "POST /currencies - Invalid data");
        } catch (CurrencyNotFoundException e) {
            ExceptionHandler.handleException(resp, e, "POST /currencies - Currency not found");
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, e, "POST /currencies - Internal server error");
        }
    }
}
