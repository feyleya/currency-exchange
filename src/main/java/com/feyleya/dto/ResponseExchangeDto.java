package com.feyleya.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ResponseExchangeDto {
    private ResponseCurrencyDto baseCurrency;
    private ResponseCurrencyDto targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ResponseExchangeDto() {
    }

    public ResponseCurrencyDto getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(ResponseCurrencyDto baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public ResponseCurrencyDto getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(ResponseCurrencyDto targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseExchangeDto that = (ResponseExchangeDto) o;
        return Objects.equals(baseCurrency, that.baseCurrency) && Objects.equals(targetCurrency, that.targetCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, targetCurrency);
    }
}
