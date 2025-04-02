package resenkov.work.calorietracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import resenkov.work.calorietracker.dto.UserDTO;
import resenkov.work.calorietracker.entity.User;
import resenkov.work.calorietracker.model.DailyCaloriesCalculator;
import resenkov.work.calorietracker.repository.MealRepository;
import resenkov.work.calorietracker.repository.UserRepository;
import resenkov.work.calorietracker.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private DailyCaloriesCalculator caloriesCalculator;

    @InjectMocks
    private UserService userService;

    private UserDTO createTestUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setEmail("test@mail.com");
        dto.setName("Test User");
        dto.setPassword("password123");
        dto.setGender(User.Gender.valueOf("MALE"));
        dto.setAge(25);
        dto.setWeight(70.0);
        dto.setHeight(175.0);
        dto.setGoal(User.Goal.valueOf("Поддержание"));
        return dto;
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setName("Test User");
        user.setPassword("password123");
        user.setGender(User.Gender.MALE);
        user.setAge(25);
        user.setWeight(70.0);
        user.setHeight(175.0);
        user.setGoal(User.Goal.Поддержание);
        user.setDailyCalories(2500.0);
        return user;
    }

    @Test
    void registerUser_WithValidData_ShouldReturnUserDTO() {
        // Подготовка
        UserDTO inputDto = createTestUserDTO();
        User savedUser = createTestUser();

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(caloriesCalculator.calculateDailyCalories(any(User.class))).thenReturn(2500.0);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Вызов
        UserDTO result = userService.registerUser(inputDto);

        // Проверки
        assertEquals(1L, result.getId());
        assertEquals("test@mail.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnDTO() {
        User user = createTestUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);
        assertEquals("test@mail.com", result.getEmail());
        assertEquals("Test User", result.getName());
    }

    @Test
    void updateUser_WhenWeightChanges_ShouldRecalculateCalories() {
        User existingUser = createTestUser();
        existingUser.setDailyCalories(2000.0);

        UserDTO updateDto = new UserDTO();
        updateDto.setWeight(75.0); // Изменение веса

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(caloriesCalculator.calculateDailyCalories(existingUser)).thenReturn(2100.0);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserDTO result = userService.updateUser(1L, updateDto);

        assertEquals(75.0, result.getWeight());
        assertEquals(2100.0, result.getDailyCalories());
    }

    @Test
    void checkDailyCalories_WhenUnderLimit_ShouldReturnValidResult() {
        User user = createTestUser();
        user.setDailyCalories(2000.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.calculateDailyCalories(1L, LocalDate.now())).thenReturn(1500.0);

        UserService.CalorieCheckResult result = userService.checkDailyCalories(1L, LocalDate.now());

        assertTrue(result.isGoalAchieved());
        assertEquals(500.0, result.getRemainingCalories());
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(999L));
    }
}