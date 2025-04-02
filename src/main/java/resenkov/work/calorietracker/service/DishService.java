package resenkov.work.calorietracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import resenkov.work.calorietracker.dto.DishDTO;
import resenkov.work.calorietracker.entity.Dish;
import resenkov.work.calorietracker.exception.ResourceAlreadyExistsException;
import resenkov.work.calorietracker.exception.ResourceNotFoundException;
import resenkov.work.calorietracker.mapping.DishMapper;
import resenkov.work.calorietracker.repository.DishRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;

    public DishDTO create(DishDTO dto) {
        if (dishRepository.existsByName(dto.getName())) {
            throw new ResourceAlreadyExistsException("Блюдо с таким названием уже существует");
        }
        Dish dish = DishMapper.toEntity(dto);
        return DishMapper.toDTO(dishRepository.save(dish));
    }

    public DishDTO getById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Блюдо не найдено"));
        return DishMapper.toDTO(dish);
    }

    public List<DishDTO> getAll() {
        return dishRepository.findAll().stream()
                .map(DishMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DishDTO update(Long id, DishDTO dto) {
        Dish existing = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Блюдо не найдено"));
        if (!existing.getName().equals(dto.getName()) &&
                dishRepository.existsByName(dto.getName())) {
            throw new ResourceAlreadyExistsException("Блюдо с таким названием уже существует");
        }
        existing.setName(dto.getName());
        existing.setCaloriesPerServing(dto.getCaloriesPerServing());
        existing.setProtein(dto.getProtein());
        existing.setFats(dto.getFats());
        existing.setCarbohydrates(dto.getCarbohydrates());
        return DishMapper.toDTO(dishRepository.save(existing));
    }

    public void delete(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Блюдо не найдено");
        }
        dishRepository.deleteById(id);
    }
}