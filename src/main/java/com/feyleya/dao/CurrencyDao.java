package com.feyleya.dao;

import com.feyleya.dto.RequestCurrencyDto;
import com.feyleya.entity.Currency;
import com.feyleya.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao<Currency, RequestCurrencyDto> {
    private static final CurrencyDao INSTANCE = new CurrencyDao();
    public static final String GET_ALL_SQL = "SELECT * FROM Currencies";
    public static final String GET_BY_CODE_SQL = "SELECT * FROM Currencies WHERE code = ?";
    public static final String GET_BY_ID_SQL = "SELECT * FROM Currencies WHERE id = ?";
    public static final String ADD_SQL = "INSERT INTO Currencies (code, full_name, sign) VALUES (?, ?, ?)";
    public static final String DELETE_SQL = "DELETE FROM Currencies WHERE id = ?";


    private CurrencyDao() {
    }

    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt("id"));
                currency.setSign(resultSet.getString("sign"));
                currency.setCode(resultSet.getString("code"));
                currency.setFullName(resultSet.getString("full_name"));
                currencies.add(currency);
            }
            return currencies;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currency> getByCode(RequestCurrencyDto request) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_CODE_SQL)) {
            preparedStatement.setObject(1, request.getCode());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Currency getCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setFullName(resultSet.getString("full_name"));
        currency.setSign(resultSet.getString("sign"));
        return currency;
    }

    public Optional<Currency> getById(Integer id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(RequestCurrencyDto request) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL)) {
            preparedStatement.setString(1, request.getCode());
            preparedStatement.setString(2, request.getName());
            preparedStatement.setString(3, request.getSign());

            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void delete(int id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }
}
