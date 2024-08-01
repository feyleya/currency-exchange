package com.feyleya.dao;

import com.feyleya.dto.RequestExchangeRateDto;
import com.feyleya.entity.ExchangeRate;
import com.feyleya.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao implements Dao<ExchangeRate, RequestExchangeRateDto>{
    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();
    public static final String ADD_SQL = "INSERT INTO ExchangeRates(base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
    public static final String UPDATE_SQL = "UPDATE ExchangeRates SET rate = ? WHERE base_currency_id IN (SELECT id FROM Currencies WHERE code = ?) AND target_currency_id IN (SELECT id FROM Currencies WHERE code = ?)";
    public static final String GET_ALL_SQL = "SELECT * FROM ExchangeRates";
    public static final String GET_BY_ID_SQL = "SELECT * FROM ExchangeRates INNER JOIN Currencies C on ExchangeRates.base_currency_id = C.id INNER JOIN Currencies C2 on C2.id = ExchangeRates.target_currency_id WHERE C.code = ? AND C2.code = ?";

    private ExchangeRateDao() {

    }


    public List<ExchangeRate> getAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExchangeRate> getByCode(RequestExchangeRateDto request) {
        String baseCurrency = request.getBaseCurrency();
        String targetCurrency = request.getTargetCurrency();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
            preparedStatement.setString(1, baseCurrency.toUpperCase());
            preparedStatement.setString(2, targetCurrency.toUpperCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildExchangeRate(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(RequestExchangeRateDto request) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL)) {
            preparedStatement.setInt(1, Integer.parseInt(request.getBaseCurrency()));
            preparedStatement.setInt(2, Integer.parseInt(request.getTargetCurrency()));
            preparedStatement.setBigDecimal(3, request.getRate());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(RequestExchangeRateDto request) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setBigDecimal(1, request.getRate());
            preparedStatement.setString(2, request.getBaseCurrency().toUpperCase());
            preparedStatement.setString(3, request.getTargetCurrency().toUpperCase());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {

        return new ExchangeRate(resultSet.getInt("id"),
                resultSet.getInt("base_currency_id"),
                resultSet.getInt("target_currency_id"),
                resultSet.getBigDecimal("rate"));
    }

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }
}
