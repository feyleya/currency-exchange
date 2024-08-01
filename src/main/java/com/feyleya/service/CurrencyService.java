package com.feyleya.service;

import com.feyleya.dao.CurrencyDao;
import com.feyleya.dto.RequestCurrencyDto;
import com.feyleya.dto.ResponseCurrencyDto;
import com.feyleya.entity.Currency;
import com.feyleya.exception.CurrencyNotFoundException;
import com.feyleya.exception.DataNotFoundException;
import com.feyleya.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    public static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public List<ResponseCurrencyDto> getAll() {
        List<Currency> currencies = currencyDao.getAll();
        List<ResponseCurrencyDto> result = new ArrayList<>();
        if (currencies.isEmpty()) {
            throw new DataNotFoundException("No currencies found");
        } else {
            for (Currency currency : currencies) {
                ResponseCurrencyDto responseCurrencyDto = ModelMapper.currencyToResponseCurrencyDto(currency);
                result.add(responseCurrencyDto);
            }
        }
        return result;
    }

    public void add(RequestCurrencyDto requestMap) {
        if (currencyDao.getByCode(requestMap).isPresent()) {
            throw new RuntimeException();
        } else {
            currencyDao.create(requestMap);
        }
    }

    public ResponseCurrencyDto findByCode(RequestCurrencyDto request) {
        Optional<Currency> currency = currencyDao.getByCode(request);
        if (currency.isEmpty()) {
            throw new CurrencyNotFoundException("Currency not found for code: " + request.getCode());
        } else {
            return ModelMapper.currencyToResponseCurrencyDto(currency.get());
        }
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
