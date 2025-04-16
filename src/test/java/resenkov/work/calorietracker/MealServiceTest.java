package resenkov.work.calorietracker;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import resenkov.work.calorietracker.dto.*;
import resenkov.work.calorietracker.entity.*;
import resenkov.work.calorietracker.exception.ResourceNotFoundException;
import resenkov.work.calorietracker.repository.*;
import resenkov.work.calorietracker.service.MealService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private MealService mealService;

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        return user;
    }

    private Dish createTestDish() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Test Dish");
        dish.setCaloriesPerServing(200.0);
        dish.setProtein(10.0);
        dish.setFats(5.0);
        dish.setCarbohydrates(30.0);
        return dish;
    }

    private MealCreateDTO createTestMealCreateDTO() {
        MealCreateDTO dto = new MealCreateDTO();
        dto.setUserId(1L);
        dto.setMealDate(LocalDate.now());

        MealItemDTO item = new MealItemDTO();
        item.setDishId(1L);
        item.setServings(2);
        dto.setMealItems(List.of(item));

        return dto;
    }

    @Test
    void createMeal_WithValidData_ShouldReturnResponseDTO() {
        MealCreateDTO dto = createTestMealCreateDTO();
        User user = createTestUser();
        Dish dish = createTestDish();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(mealRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        MealResponseDTO result = mealService.createMeal(dto);

        assertNotNull(result);
        assertEquals(dto.getMealDate(), result.getMealDate());
        assertEquals(1, result.getItems().size());

        MealItemResponseDTO item = result.getItems().get(0);
        assertEquals(400.0, item.getCalories()); // 200 калорий * 2 порции
        assertEquals(20.0, item.getProtein());
        assertEquals(10.0, item.getFats());
        assertEquals(60.0, item.getCarbohydrates());

        assertEquals(400.0, result.getTotalCalories());
    }

    @Test
    void createMeal_WhenUserNotFound_ShouldThrowException() {
        MealCreateDTO dto = createTestMealCreateDTO();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mealService.createMeal(dto));
    }

    @Test
    void createMeal_WhenDishNotFound_ShouldThrowException() {
        MealCreateDTO dto = createTestMealCreateDTO();
        when(userRepository.findById(1L)).thenReturn(Optional.of(createTestUser()));
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mealService.createMeal(dto));
    }


    @Test
    void getMealsByUserAndDate_WithValidData_ShouldReturnResponseDTO() {
        LocalDate date = LocalDate.now();
        User user = createTestUser();
        Dish dish = createTestDish();

        Meal meal = new Meal();
        meal.setUser(user);
        meal.setMealDate(date);
        meal.setDish(dish);
        meal.setServings(3);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(mealRepository.findByUserIdAndMealDate(1L, date)).thenReturn(List.of(meal));


        List<MealResponseDTO> result = mealService.getMealsByUserAndDate(1L, date);

        assertEquals(1, result.size());
        MealResponseDTO dto = result.get(0);
        assertEquals(date, dto.getMealDate());

        assertEquals(600.0, dto.getTotalCalories()); // 200 калорий * 3 порции
        assertEquals(30.0, dto.getTotalProtein());
        assertEquals(15.0, dto.getTotalFats());
        assertEquals(90.0, dto.getTotalCarbohydrates());
    }

    @Test
    void getMealsByUserAndDate_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,
                () -> mealService.getMealsByUserAndDate(1L, LocalDate.now()));
    }

    @Test
    void getMealsByUserAndDate_WithMultipleMeals_ShouldCalculateTotalsCorrectly() {
        LocalDate date = LocalDate.now();
        User user = createTestUser();
        Dish dish1 = createTestDish();
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setCaloriesPerServing(300.0);
        dish2.setProtein(15.0);
        dish2.setFats(10.0);
        dish2.setCarbohydrates(40.0);

        Meal meal1 = new Meal();
        meal1.setUser(user);
        meal1.setMealDate(date);
        meal1.setDish(dish1);
        meal1.setServings(2); // 400 калорий

        Meal meal2 = new Meal();
        meal2.setUser(user);
        meal2.setMealDate(date);
        meal2.setDish(dish2);
        meal2.setServings(1); // 300 калорий

        when(userRepository.existsById(1L)).thenReturn(true);
        when(mealRepository.findByUserIdAndMealDate(1L, date)).thenReturn(List.of(meal1, meal2));

        List<MealResponseDTO> result = mealService.getMealsByUserAndDate(1L, date);

        assertEquals(1, result.size());
        MealResponseDTO dto = result.get(0);
        assertEquals(700.0, dto.getTotalCalories()); // 400 + 300
        assertEquals(35.0, dto.getTotalProtein()); // 20 + 15
        assertEquals(20.0, dto.getTotalFats()); // 10 + 10
        assertEquals(100.0, dto.getTotalCarbohydrates()); // 60 + 40
    }

    @Test
    void convertToItemDTO_ShouldConvertCorrectly() {
        Meal meal = new Meal();
        Dish dish = createTestDish();
        meal.setDish(dish);
        meal.setServings(2);

        MealItemResponseDTO dto = mealService.convertToItemDTO(meal);

        assertEquals(dish.getId(), dto.getDishId());
        assertEquals(dish.getName(), dto.getDishName());
        assertEquals(2, dto.getServings());
        assertEquals(400.0, dto.getCalories());
        assertEquals(20.0, dto.getProtein());
        assertEquals(10.0, dto.getFats());
        assertEquals(60.0, dto.getCarbohydrates());
    }
}