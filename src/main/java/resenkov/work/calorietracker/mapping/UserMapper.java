package resenkov.work.calorietracker.mapping;

import resenkov.work.calorietracker.entity.User;
import resenkov.work.calorietracker.dto.UserDTO;

public class UserMapper {
    public static UserDTO toDTO(User entity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setAge(entity.getAge());
        userDTO.setId(entity.getId());
        userDTO.setName(entity.getName());
        userDTO.setEmail(entity.getEmail());
        userDTO.setGoal(entity.getGoal());
        userDTO.setHeight(entity.getHeight());
        userDTO.setWeight(entity.getWeight());
        userDTO.setPassword(entity.getPassword());
        userDTO.setDailyCalories(entity.getDailyCalories());
        userDTO.setGender(entity.getGender());
        return userDTO;
    }

    public static User toEntity(UserDTO dto) {
        User entity = new User();
        entity.setId(dto.getId());
        entity.setAge(dto.getAge());
        entity.setGoal(dto.getGoal());
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setHeight(dto.getHeight());
        entity.setWeight(dto.getWeight());
        entity.setDailyCalories(dto.getDailyCalories());
        entity.setPassword(dto.getPassword());
        entity.setGender(dto.getGender());
        return entity;
    }
}
