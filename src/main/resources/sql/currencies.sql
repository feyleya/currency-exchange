CREATE TABLE Currencies(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code VARCHAR(3),
    full_name VARCHAR(40),
    sign VARCHAR(1),
    UNIQUE(code)
);