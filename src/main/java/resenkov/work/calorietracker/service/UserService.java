package resenkov.work.calorietracker.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import resenkov.work.calorietracker.dto.UserDTO;
import resenkov.work.calorietracker.entity.User;
import resenkov.work.calorietracker.exception.UserNotFoundException;
import resenkov.work.calorietracker.mapping.UserMapper;
import resenkov.work.calorietracker.model.DailyCaloriesCalculator;
import resenkov.work.calorietracker.repository.MealRepository;
import resenkov.work.calorietracker.repository.UserRepository;

import java.time.LocalDate;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DailyCaloriesCalculator caloriesCalculator;
    private final MealRepository mealRepository;

    public UserDTO registerUser(UserDTO registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email уже занят");
        }
        User user = UserMapper.toEntity(registrationDto);
        user.setDailyCalories(caloriesCalculator.calculateDailyCalories(user));
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден по ID: " + userId));
        return UserMapper.toDTO(user);
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        if (userDTO.getName() != null) existingUser.setName(userDTO.getName());
        if (userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getAge() != null) existingUser.setAge(userDTO.getAge());
        if (userDTO.getWeight() != 0) existingUser.setWeight(userDTO.getWeight());
        if (userDTO.getHeight() != 0) existingUser.setHeight(userDTO.getHeight());
        if (userDTO.getGoal() != null) existingUser.setGoal(userDTO.getGoal());

        if (userDTO.getWeight() != 0 || userDTO.getHeight() != 0
                || userDTO.getAge() != null || userDTO.getGoal() != null) {
            existingUser.setDailyCalories(caloriesCalculator.calculateDailyCalories(existingUser));
        }

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toDTO(updatedUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    public CalorieCheckResult checkDailyCalories(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Double consumedCalories = mealRepository.calculateDailyCalories(userId, date);
        if (consumedCalories == null) consumedCalories = 0.0;

        boolean isGoalAchieved = consumedCalories <= user.getDailyCalories();
        double remaining = user.getDailyCalories() - consumedCalories;

        return new CalorieCheckResult(
                date,
                consumedCalories,
                user.getDailyCalories(),
                isGoalAchieved,
                remaining
        );
    }
    @Data
    @AllArgsConstructor
    public static class CalorieCheckResult {
        private LocalDate date;
        private double caloriesConsumed;
        private double dailyGoal;
        private boolean isGoalAchieved;
        private double remainingCalories;
    }
}
