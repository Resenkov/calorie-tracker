package resenkov.work.calorietracker.model;
import org.springframework.stereotype.Component;
import resenkov.work.calorietracker.entity.User;
@Component
public class DailyCaloriesCalculator {

    /**
     * Расчёт дневной нормы калорий по формуле Харриса-Бенедикта.
     */
    public double calculateDailyCalories(User user) {
        double bmr = calculateBMR(user);

        return switch (user.getGoal()) {
            case Похудение -> bmr * 0.85;  // Дефицит 15%
            case Поддержание -> bmr;       // Без изменений
            case Набор -> bmr * 1.15; // Профицит 15%
        };
    }

    /**
     * Расчёт базового метаболизма (BMR) с учётом пола.
     */
    private double calculateBMR(User user) {
        if (user.getGender() == User.Gender.MALE) {
            return 88.362
                    + (13.397 * user.getWeight())
                    + (4.799 * user.getHeight())
                    - (5.677 * user.getAge());
        } else {
            return 447.593
                    + (9.247 * user.getWeight())
                    + (3.098 * user.getHeight())
                    - (4.330 * user.getAge());
        }
    }
}