CREATE TABLE users (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')) NOT NULL,
                       age INT CHECK (age > 0),
                       weight FLOAT CHECK (weight > 0),
                       height FLOAT CHECK (height > 0),
                       goal VARCHAR(50) CHECK (goal IN ('Похудение', 'Поддержание', 'Набор')),
                       daily_calories FLOAT
);

CREATE TABLE dishes (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        calories_per_serving FLOAT CHECK (calories_per_serving >= 0),
                        protein FLOAT CHECK (protein >= 0),
                        fats FLOAT CHECK (fats >= 0),
                        carbohydrates FLOAT CHECK (carbohydrates >= 0)
);

CREATE TABLE meals (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       user_id BIGINT REFERENCES users(id),
                       meal_date DATE NOT NULL,
                       dish_id BIGINT REFERENCES dishes(id),
                       servings INT CHECK (servings > 0)
);

CREATE INDEX idx_meals_user_date ON meals(user_id, meal_date);
CREATE INDEX idx_meals_dish ON meals(dish_id);

INSERT INTO users (name, email, password, gender, age, weight, height, goal, daily_calories) VALUES
                                                                                                 ('Иван Петров', 'ivan@example.com', '$2a$10$xJwL5v5Jz5UZJZo2VYQd3.Q9W7Rk6XcN1hOYV7sS7tLk5J2YHbW1K', 'MALE', 28, 75.5, 180.0, 'Поддержание', 2500),
                                                                                                 ('Мария Сидорова', 'maria@example.com', '$2a$10$YHjgP7rG7sS5V8pN1qXt0uWQkZJ9XcL2mO3VYbR4tUjK5L1MfW3D', 'FEMALE', 32, 62.0, 165.0, 'Похудение', 1800),
                                                                                                 ('Алекс Нейтральный', 'alex@example.com', '$2a$10$3mXp7qV9sR2tUjK1LfW3D.YHjgP7rG7sS5V8pN1qXt0uWQkZJ9XcL', 'OTHER', 25, 70.0, 170.0, 'Набор', 2200),
                                                                                                 ('Ольга Васильева', 'olga@example.com', '$2a$10$pN1qXt0uWQkZJ9XcL2mO3.VYbR4tUjK5L1MfW3DxJwL5v5Jz5UZJZo', 'FEMALE', 35, 58.0, 168.0, 'Похудение', 1700),
                                                                                                 ('Дмитрий Козлов', 'dmitry@example.com', '$2a$10$QkZJ9XcL2mO3VYbR4tUjK5L1MfW3DxJwL5v5Jz5UZJZo2VYQd3.Q9W7R', 'MALE', 40, 85.0, 182.0, 'Набор', 3000);
INSERT INTO dishes (name, calories_per_serving, protein, fats, carbohydrates) VALUES
                                                                                  ('Куриная грудка', 165, 31.0, 3.6, 0.0),
                                                                                  ('Салат Цезарь', 220, 10.0, 15.0, 10.0),
                                                                                  ('Рис отварной', 130, 2.7, 0.3, 28.0),
                                                                                  ('Брокколи', 55, 4.0, 0.6, 11.0),
                                                                                  ('Яблоко', 52, 0.3, 0.2, 14.0);

INSERT INTO meals (user_id, meal_date, dish_id, servings) VALUES
                                                              (1, '2023-10-01', 1, 1), -- Иван Иванов, Куриная грудка
                                                              (1, '2023-10-01', 3, 2), -- Иван Иванов, Рис отварной
                                                              (2, '2023-10-01', 2, 1), -- Мария Петрова, Салат Цезарь
                                                              (3, '2023-10-01', 1, 2), -- Алексей Сидоров, Куриная грудка
                                                              (4, '2023-10-01', 4, 1), -- Ольга Смирнова, Брокколи
                                                              (5, '2023-10-01', 5, 1); -- Дмитрий Кузнецов, Яблоко
