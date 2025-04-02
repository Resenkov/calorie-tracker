package resenkov.work.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resenkov.work.calorietracker.dto.MealCreateDTO;
import resenkov.work.calorietracker.dto.MealItemDTO;
import resenkov.work.calorietracker.dto.MealItemResponseDTO;
import resenkov.work.calorietracker.dto.MealResponseDTO;
import resenkov.work.calorietracker.entity.Dish;
import resenkov.work.calorietracker.entity.Meal;
import resenkov.work.calorietracker.entity.User;
import resenkov.work.calorietracker.exception.ResourceNotFoundException;
import resenkov.work.calorietracker.repository.DishRepository;
import resenkov.work.calorietracker.repository.MealRepository;
import resenkov.work.calorietracker.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    public MealResponseDTO createMeal(MealCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        List<Meal> meals = dto.getMealItems().stream()
                .map(item -> createMealItem(user, dto.getMealDate(), item))
                .toList();

        mealRepository.saveAll(meals);

        return buildResponseDTO(dto.getMealDate(), meals);
    }

    private Meal createMealItem(User user, LocalDate date, MealItemDTO item) {
        Dish dish = dishRepository.findById(item.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Блюдо не найдено"));
        Meal meal = new Meal();
        meal.setUser(user);
        meal.setMealDate(date);
        meal.setDish(dish);
        meal.setServings(item.getServings());
        return meal;
    }

    private MealResponseDTO buildResponseDTO(LocalDate date, List<Meal> meals) {
        MealResponseDTO response = new MealResponseDTO();
        response.setMealDate(date);
        List<MealItemResponseDTO> items = meals.stream()
                .map(this::convertToItemDTO)
                .toList();

        response.setItems(items);

        response.setTotalCalories(items.stream()
                .mapToDouble(MealItemResponseDTO::getCalories)
                .sum());

        response.setTotalFats(items.stream()
                .mapToDouble(MealItemResponseDTO::getFats)
                .sum());

        response.setTotalProtein(items.stream()
                .mapToDouble(MealItemResponseDTO::getProtein)
                .sum());

        response.setTotalCarbohydrates(items.stream()
                .mapToDouble(MealItemResponseDTO::getCarbohydrates)
                .sum());
        return response;
    }

    public List<MealResponseDTO> getMealsByUserAndDate(Long userId, LocalDate date) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }
        List<Meal> meals = mealRepository.findByUserIdAndMealDate(userId, date);

        Map<LocalDate, List<Meal>> mealsByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getMealDate));
        return mealsByDate.entrySet().stream()
                .map(entry -> {
                    MealResponseDTO dto = new MealResponseDTO();
                    dto.setMealDate(entry.getKey());

                    List<MealItemResponseDTO> items = entry.getValue().stream()
                            .map(this::convertToItemDTO)
                            .toList();
                    dto.setItems(items);

                    dto.setTotalCalories(calculateTotal(items, MealItemResponseDTO::getCalories));
                    dto.setTotalProtein(calculateTotal(items, MealItemResponseDTO::getProtein));
                    dto.setTotalFats(calculateTotal(items, MealItemResponseDTO::getFats));
                    dto.setTotalCarbohydrates(calculateTotal(items, MealItemResponseDTO::getCarbohydrates));

                    return dto;
                })
                .toList();
    }

    private double calculateTotal(List<MealItemResponseDTO> items, ToDoubleFunction<MealItemResponseDTO> mapper) {
        return items.stream()
                .mapToDouble(mapper)
                .sum();
    }

    public MealItemResponseDTO convertToItemDTO(Meal meal) {
        MealItemResponseDTO dto = new MealItemResponseDTO();
        dto.setDishId(meal.getDish().getId());
        dto.setDishName(meal.getDish().getName());
        dto.setServings(meal.getServings());
        dto.setCalories(meal.getTotalCalories());
        dto.setProtein(meal.getTotalProtein());
        dto.setFats(meal.getTotalFats());
        dto.setCarbohydrates(meal.getTotalCarbohydrates());
        return dto;
    }
}