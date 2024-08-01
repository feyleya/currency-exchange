CREATE TABLE IF NOT EXISTS ExchangeRates(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id INTEGER NOT NULL,
    target_currency_id INTEGER NOT NULL,
    rate DECIMAL(6) NOT NULL,
    FOREIGN KEY (base_currency_id) REFERENCES Currencies(id) ON DELETE CASCADE,
    FOREIGN KEY (target_currency_id) REFERENCES Currencies(id) ON DELETE CASCADE,
    UNIQUE (base_currency_id, target_currency_id)
);