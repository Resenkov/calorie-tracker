package resenkov.work.calorietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import resenkov.work.calorietracker.entity.Dish;

import java.util.List;


@Repository
public interface DishRepository extends JpaRepository<Dish , Long> {
    List<Dish> findByNameContainingIgnoreCase(String name);

    @Query("SELECT d FROM Dish d ORDER BY d.caloriesPerServing ASC")
    List<Dish> findAllOrderByCaloriesAsc();

    @Query("SELECT d FROM Dish d WHERE d.protein >= :minProtein ORDER BY d.protein DESC")
    List<Dish> findByProteinGreaterThanOrderByProteinDesc(double minProtein);

    @Query("SELECT DISTINCT d FROM Dish d JOIN d.meals m WHERE m.user.id = :userId")
    List<Dish> findDishesEatenByUser(Long userId);
}
