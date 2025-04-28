package resenkov.work.calorietracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resenkov.work.calorietracker.dto.UserDTO;
import resenkov.work.calorietracker.service.UserService;

import java.time.LocalDate;



@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO dto) {
        UserDTO createdUser = userService.registerUser(dto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/daily-calories/{id}")
    public ResponseEntity<Double> getDailyCalories(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user.getDailyCalories());
    }


    /**
    Метод для проверки лимита дневной нормы калорий.
     **/

    @GetMapping("/{userId}/calories/check/{date}")
    public ResponseEntity<UserService.CalorieCheckResult> checkCalories(
            @PathVariable Long userId,
            @PathVariable
            @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate date) {

        return ResponseEntity.ok(
                userService.checkDailyCalories(userId, date)
        );
    }
}