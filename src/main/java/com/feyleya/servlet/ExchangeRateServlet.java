package com.feyleya.servlet;

import com.feyleya.dto.RequestExchangeRateDto;
import com.feyleya.dto.ResponseExchangeRateDto;
import com.feyleya.exception.ExceptionHandler;
import com.feyleya.service.ExchangeRateService;
import com.feyleya.util.JsonResponseUtil;
import com.feyleya.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
@Slf4j
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService service = ExchangeRateService.INSTANCE;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getMethod().equalsIgnoreCase("PATCH")) {
                doPatch(req, resp);
            } else {
                super.service(req, resp);
            }
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, e, "Service method - Error processing request");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo().substring(1);

            RequestExchangeRateDto requestDto = new RequestExchangeRateDto();
            requestDto.setBaseCurrency(path.substring(0, 3));
            requestDto.setTargetCurrency(path.substring(3, 6));

            ResponseExchangeRateDto responseExchangeRateDto = service.getByCode(requestDto);

            JsonResponseUtil.sendJsonGetResponse(resp, responseExchangeRateDto);
            log.info("Successfully processed GET /exchangeRate/{} from={}, to={}", path, requestDto.getBaseCurrency(), requestDto.getTargetCurrency());
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, e, "GET /exchangeRate - Error retrieving exchange rate");
        }

    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ValidationUtil.validatePath(req.getPathInfo());
            String path = req.getPathInfo().substring(1);
            BufferedReader reader = req.getReader();
            String rate = null;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("rate=")) {
                    int index = line.indexOf("rate=");
                    rate = line.substring(index + 5);
                }
            }
            ValidationUtil.validateRate(rate);
            RequestExchangeRateDto request = new RequestExchangeRateDto();
            request.setBaseCurrency(path.substring(0, 3));
            request.setTargetCurrency(path.substring(3, 6));
            request.setRate(new BigDecimal(rate));
            ValidationUtil.validateCode(request.getBaseCurrency());
            ValidationUtil.validateCode(request.getTargetCurrency());
            service.update(request);
            log.info("Successfully processed PATCH /exchangeRate/{} rate={}", path, rate);
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, e, "PATCH /exchangeRate - Error updating exchange rate");
        }
    }
}
