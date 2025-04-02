package resenkov.work.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import resenkov.work.calorietracker.dto.MealItemResponseDTO;
import resenkov.work.calorietracker.dto.MealResponseDTO;
import resenkov.work.calorietracker.dto.NutritionHistoryDTO;
import resenkov.work.calorietracker.entity.Dish;
import resenkov.work.calorietracker.entity.Meal;
import resenkov.work.calorietracker.repository.MealRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionHistoryService {
    private final MealRepository mealRepository;


    public List<NutritionHistoryDTO> getNutritionHistory(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Meal> meals = mealRepository.findByUserIdAndDateRange(userId, startDate, endDate);

        return meals.stream()
                .collect(Collectors.groupingBy(Meal::getMealDate))
                .entrySet().stream()
                .map(entry -> createDailyNutritionReport(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(NutritionHistoryDTO::getDate).reversed())
                .toList();
    }

    private NutritionHistoryDTO createDailyNutritionReport(LocalDate date, List<Meal> dailyMeals) {
        NutritionHistoryDTO dto = new NutritionHistoryDTO();
        dto.setDate(date);

        dto.setTotalCalories(calculateTotal(dailyMeals, Meal::getTotalCalories));
        dto.setTotalProtein(calculateTotal(dailyMeals, Meal::getTotalProtein));
        dto.setTotalFats(calculateTotal(dailyMeals, Meal::getTotalFats));
        dto.setTotalCarbohydrates(calculateTotal(dailyMeals, Meal::getTotalCarbohydrates));

        dto.setMeals(dailyMeals.stream()
                .map(this::convertToMealResponse)
                .toList());

        return dto;
    }

    private MealResponseDTO convertToMealResponse(Meal meal) {
        Dish dish = meal.getDish();

        MealItemResponseDTO itemDto = new MealItemResponseDTO();
        itemDto.setDishId(dish.getId());
        itemDto.setDishName(dish.getName());
        itemDto.setServings(meal.getServings());
        itemDto.setCalories(meal.getTotalCalories());
        itemDto.setProtein(meal.getTotalProtein());
        itemDto.setFats(meal.getTotalFats());
        itemDto.setCarbohydrates(meal.getTotalCarbohydrates());

        MealResponseDTO mealDto = new MealResponseDTO();
        mealDto.setId(meal.getId());
        mealDto.setMealDate(meal.getMealDate());
        mealDto.setItems(List.of(itemDto));
        mealDto.setTotalCalories(meal.getTotalCalories());
        mealDto.setTotalProtein(meal.getTotalProtein());
        mealDto.setTotalFats(meal.getTotalFats());
        mealDto.setTotalCarbohydrates(meal.getTotalCarbohydrates());

        return mealDto;
    }

    private Double calculateTotal(List<Meal> meals, Function<Meal, Double> mapper) {
        return meals.stream()
                .map(mapper)
                .reduce(0.0, Double::sum);
    }
}