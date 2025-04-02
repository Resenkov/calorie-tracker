CREATE TABLE users (
                       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,  -- Храним хеш пароля, не сам пароль!
                       age INT CHECK (age > 0),
                       weight DECIMAL CHECK (weight > 0),
                       height DECIMAL CHECK (height > 0),
                       goal VARCHAR(50) CHECK (goal IN ('Похудение', 'Поддержание', 'Набор массы')),
                       daily_calories DECIMAL
);

CREATE TABLE dishes (
                        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        calories_per_serving DECIMAL CHECK (calories_per_serving >= 0),
                        protein DECIMAL CHECK (protein >= 0),
                        fats DECIMAL CHECK (fats >= 0),
                        carbohydrates DECIMAL CHECK (carbohydrates >= 0)
);

CREATE TABLE meals (
                       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       user_id INT REFERENCES users(id),
                       meal_date DATE NOT NULL,
                       dish_id INT REFERENCES dishes(id),
                       servings INT CHECK (servings > 0)
);

CREATE INDEX idx_meals_user_date ON meals(user_id, meal_date);
CREATE INDEX idx_meals_dish ON meals(dish_id);

INSERT INTO users (name, email, password, age, weight, height, goal, daily_calories) VALUES
                                                                                         ('Иван Петров', 'ivan@example.com', '$2a$10$xJwL5v5Jz5UZJZo2VYQd3.Q9W7Rk6XcN1hOYV7sS7tLk5J2YHbW1K', 28, 75.5, 180.0, 'Поддержание', 2500),
                                                                                         ('Мария Сидорова', 'maria@example.com', '$2a$10$YHjgP7rG7sS5V8pN1qXt0uWQkZJ9XcL2mO3VYbR4tUjK5L1MfW3D', 32, 62.0, 165.0, 'Похудение', 1800),
                                                                                         ('Алексей Иванов', 'alex@example.com', '$2a$10$3mXp7qV9sR2tUjK1LfW3D.YHjgP7rG7sS5V8pN1qXt0uWQkZJ9XcL', 25, 80.0, 175.0, 'Набор массы', 3200),
                                                                                         ('Елена Кузнецова', 'elena@example.com', '$2a$10$pN1qXt0uWQkZJ9XcL2mO3.VYbR4tUjK5L1MfW3DxJwL5v5Jz5UZJZo', 40, 68.0, 170.0, 'Поддержание', 2100),
                                                                                         ('Дмитрий Смирнов', 'dmitry@example.com', '$2a$10$QkZJ9XcL2mO3VYbR4tUjK5L1MfW3DxJwL5v5Jz5UZJZo2VYQd3.Q9W7R', 22, 70.0, 178.0, 'Похудение', 2300);

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
