package resenkov.work.calorietracker.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import resenkov.work.calorietracker.entity.User;


import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@Email(message = "Введите правильный формат почты!") @NotBlank(message = "Email обязателен для заполнения!") String email);
}
