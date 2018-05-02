INSERT INTO client (birth_date, first_name, last_name, passport_number, passport_series, patronymic) VALUES
  ('1960-01-01', 'Иван', 'Иванов', '770000', '8800', 'Иванович'),
  ('1990-03-03', 'Петр', 'Петров', '777777', '8800', 'Петрович'),
  ('1980-02-02', 'Иван', 'Петров', '774567', '7700', 'Петрович'),
  ('2000-04-04', 'Иван', 'Иванов', '776547', '7700', 'Петрович');

INSERT INTO contract (APARTMENT, CALC_DATE, CITY, CONTRACT_DATE, CONTRACT_NUMBER, COUNTRY, PERIOD_END, PERIOD_START,
                      PREMIUM, PROPERTY_TYPE, REGION, SQUARE, STREET, SUM, YEAR_BUILT, CLIENT_ID)
VALUES
  (1, CURRENT_DATE(), 'Москва', CURRENT_DATE(), 111111, 'Россия', '2019-05-01', CURRENT_DATE(), 48.62, 'квартира',
      'Московская', 111.0, 'Ленина', 11, 1961, 1);