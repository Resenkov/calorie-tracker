package resenkov.work.calorietracker.mapping;

import resenkov.work.calorietracker.dto.DishDTO;

import resenkov.work.calorietracker.entity.Dish;

public class DishMapper {
    public static DishDTO toDTO(Dish dish) {
        DishDTO dto = new DishDTO();
        dto.setName(dish.getName());
        dto.setId(dto.getId());
        dto.setFats(dto.getFats());
        dto.setProtein(dto.getProtein());
        dto.setCarbohydrates(dto.getCarbohydrates());
        dto.setCaloriesPerServing(dto.getCaloriesPerServing());
        return dto;
    }

    public static Dish toEntity(DishDTO dto) {
        Dish entity = new Dish();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFats(dto.getFats());
        entity.setCarbohydrates(dto.getCarbohydrates());
        entity.setProtein(dto.getProtein());
        entity.setCaloriesPerServing(dto.getCaloriesPerServing());
        return entity;
    }
}
