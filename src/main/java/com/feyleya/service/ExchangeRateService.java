package com.feyleya.service;

import com.feyleya.dao.CurrencyDao;
import com.feyleya.dao.ExchangeRateDao;
import com.feyleya.dto.*;
import com.feyleya.entity.Currency;
import com.feyleya.entity.ExchangeRate;
import com.feyleya.exception.DataNotFoundException;
import com.feyleya.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

public class ExchangeRateService {
    public static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public List<ResponseExchangeRateDto> getAll() {
        List<ExchangeRate> exchangeRates = exchangeRateDao.getAll();
        return exchangeRates.stream()
                .map(this::buildExchangeRateDto)
                .collect(toList());
    }

    public ResponseExchangeRateDto getByCode(RequestExchangeRateDto request) {
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.getByCode(request);
        if (exchangeRate.isPresent()) {
            return buildExchangeRateDto(exchangeRate.get());
        } else {
            throw new DataNotFoundException("Exchange rate not found for base currency " + request.getBaseCurrency() + " and target currency " + request.getTargetCurrency());
        }
    }

    public void add(RequestExchangeRateDto request) {
        RequestCurrencyDto base = new RequestCurrencyDto();
        RequestCurrencyDto target = new RequestCurrencyDto();

        base.setCode(request.getBaseCurrency());
        target.setCode(request.getTargetCurrency());

        Optional<Currency> baseCurrency = currencyDao.getByCode(base);
        Optional<Currency> targetCurrency = currencyDao.getByCode(target);

        if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
            request.setBaseCurrency(String.valueOf(baseCurrency.get().getId()));
            request.setTargetCurrency(String.valueOf(targetCurrency.get().getId()));
            exchangeRateDao.create(request);
        } else {
            throw new DataNotFoundException("Base or target currency not found");
        }
    }

    private ResponseExchangeRateDto buildExchangeRateDto(ExchangeRate exchangeRate) {
        Optional<Currency> baseCurrency = currencyDao.getById(exchangeRate.getBaseCurrencyId());
        Optional<Currency> targetCurrency = currencyDao.getById(exchangeRate.getTargetCurrencyId());

        return new ResponseExchangeRateDto(exchangeRate.getId(),
                ModelMapper.currencyToResponseCurrencyDto(baseCurrency.get()),
                ModelMapper.currencyToResponseCurrencyDto(targetCurrency.get()),
                exchangeRate.getRate());
    }


    public void update(RequestExchangeRateDto request) {
        if (exchangeRateDao.getByCode(request).isEmpty()) {
            throw new DataNotFoundException("Exchange rate not found for update");
        } else {
            exchangeRateDao.update(request);
        }
    }

    public ResponseExchangeDto exchange(RequestExchangeDto request) {
        RequestExchangeRateDto directRequest = ModelMapper.exchangeToRequest(request, true);
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.getByCode(directRequest);
        //1
        if (exchangeRate.isPresent()) {
            ResponseExchangeRateDto response = buildExchangeRateDto(exchangeRate.get());
            return ModelMapper.exchangeToResponse(response.getBaseCurrency(), response.getTargetCurrency(), response.getRate(), request.getAmount());
        }
        //2
        RequestExchangeRateDto reverseRequest = ModelMapper.exchangeToRequest(request, false);
        exchangeRate = exchangeRateDao.getByCode(reverseRequest);
        if (exchangeRate.isPresent()) {
            ResponseExchangeRateDto response = buildExchangeRateDto(exchangeRate.get());
            BigDecimal reverseRate = new BigDecimal("1").divide(response.getRate(), 5, RoundingMode.HALF_UP);
            return ModelMapper.exchangeToResponse(response.getTargetCurrency(), response.getBaseCurrency(), reverseRate, request.getAmount());
        }
        //3
        RequestExchangeRateDto baseUSD = ModelMapper.exchangeToRequest(request, false);
        RequestExchangeRateDto targetUSD = ModelMapper.exchangeToRequest(request, true);
        baseUSD.setBaseCurrency("USD");
        targetUSD.setBaseCurrency("USD");
        Optional<ExchangeRate> baseExchangeRate = exchangeRateDao.getByCode(baseUSD);
        Optional<ExchangeRate> targetExchangeRate = exchangeRateDao.getByCode(targetUSD);
        if (baseExchangeRate.isPresent() && targetExchangeRate.isPresent()) {
            ResponseExchangeRateDto baseResponseDto = buildExchangeRateDto(baseExchangeRate.get());
            ResponseExchangeRateDto targetResponseDto = buildExchangeRateDto(targetExchangeRate.get());
            BigDecimal rate = targetExchangeRate.get().getRate().divide(baseExchangeRate.get().getRate(), 5, RoundingMode.HALF_UP);
            return ModelMapper.exchangeToResponse(baseResponseDto.getTargetCurrency(), targetResponseDto.getTargetCurrency(), rate, request.getAmount());
        } else {
            throw new DataNotFoundException("Exchange can not be done due to missing base or target exchange rates");

        }
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}
