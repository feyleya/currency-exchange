package com.feyleya;

import com.feyleya.dto.*;
import com.feyleya.entity.Currency;

import java.math.BigDecimal;

public final class ModelMapper {
    private ModelMapper() {
    }

    public static ResponseCurrencyDto currencyToResponseCurrencyDto(Currency currency) {
        return new ResponseCurrencyDto(
                currency.getId(),
                currency.getFullName(),
                currency.getCode(),
                currency.getSign());
    }

    public static RequestExchangeRateDto exchangeToRequest(RequestExchangeDto request, boolean isDirectExchangeRate) {
        RequestExchangeRateDto exchangeRateDto = new RequestExchangeRateDto();
        if (isDirectExchangeRate) {
            exchangeRateDto.setBaseCurrency(request.getBaseCurrency());
            exchangeRateDto.setTargetCurrency(request.getTargetCurrency());
        } else {
            exchangeRateDto.setBaseCurrency(request.getTargetCurrency());
            exchangeRateDto.setTargetCurrency(request.getBaseCurrency());
        }
        return exchangeRateDto;
    }

    public static ResponseExchangeDto exchangeToResponse(ResponseCurrencyDto baseCurrency, ResponseCurrencyDto targetCurrency, BigDecimal rate, BigDecimal amount) {
        ResponseExchangeDto response = new ResponseExchangeDto();
        response.setBaseCurrency(baseCurrency);
        response.setTargetCurrency(targetCurrency);
        response.setRate(rate);
        response.setAmount(amount);
        response.setConvertedAmount(rate.multiply(amount));
        return response;
    }
}
