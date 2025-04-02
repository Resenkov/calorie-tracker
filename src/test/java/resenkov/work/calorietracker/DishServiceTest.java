package resenkov.work.calorietracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import resenkov.work.calorietracker.dto.DishDTO;
import resenkov.work.calorietracker.entity.Dish;
import resenkov.work.calorietracker.exception.ResourceAlreadyExistsException;
import resenkov.work.calorietracker.exception.ResourceNotFoundException;
import resenkov.work.calorietracker.mapping.DishMapper;
import resenkov.work.calorietracker.repository.DishRepository;
import resenkov.work.calorietracker.service.DishService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishMapper dishMapper;

    @InjectMocks
    private DishService dishService;

    private DishDTO createTestDishDTO() {
        DishDTO dto = new DishDTO();
        dto.setId(1L);
        dto.setName("Test Dish");
        dto.setCaloriesPerServing(200.0);
        dto.setProtein(10.0);
        dto.setFats(5.0);
        dto.setCarbohydrates(30.0);
        return dto;
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

    @Test
    void create_WithNewDish_ShouldReturnSavedDishDTO() {
        // Arrange
        DishDTO inputDto = createTestDishDTO();
        Dish dishToSave = createTestDish();
        Dish savedDish = createTestDish();
        DishDTO expectedDto = createTestDishDTO();

        when(dishMapper.toEntity(inputDto)).thenReturn(dishToSave);
        when(dishRepository.existsByName("Test Dish")).thenReturn(false);
        when(dishRepository.save(dishToSave)).thenReturn(savedDish);
        when(dishMapper.toDTO(savedDish)).thenReturn(expectedDto);

        // Act
        DishDTO result = dishService.create(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Dish", result.getName());
        assertEquals(200.0, result.getCaloriesPerServing());
        verify(dishRepository).save(dishToSave);
    }

    @Test
    void create_WithExistingName_ShouldThrowException() {
        DishDTO inputDto = createTestDishDTO();
        when(dishRepository.existsByName("Test Dish")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> dishService.create(inputDto));
        verify(dishRepository, never()).save(any());
    }

    @Test
    void getById_WithExistingDish_ShouldReturnDishDTO() {
        Dish dish = createTestDish();
        DishDTO expectedDto = createTestDishDTO();

        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishMapper.toDTO(dish)).thenReturn(expectedDto);

        DishDTO result = dishService.getById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Test Dish", result.getName());
        assertEquals(200.0, result.getCaloriesPerServing());
    }

    @Test
    void getById_WithNonExistingDish_ShouldThrowException() {
        when(dishRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dishService.getById(999L));
    }

    @Test
    void getAll_ShouldReturnListOfDishDTOs() {
        Dish dish1 = createTestDish();
        Dish dish2 = createTestDish();
        dish2.setId(2L);
        dish2.setName("Another Dish");

        DishDTO dto1 = createTestDishDTO();
        DishDTO dto2 = createTestDishDTO();
        dto2.setId(2L);
        dto2.setName("Another Dish");

        when(dishRepository.findAll()).thenReturn(List.of(dish1, dish2));
        when(dishMapper.toDTO(dish1)).thenReturn(dto1);
        when(dishMapper.toDTO(dish2)).thenReturn(dto2);

        List<DishDTO> result = dishService.getAll();

        assertEquals(2, result.size());
        assertEquals("Test Dish", result.get(0).getName());
        assertEquals("Another Dish", result.get(1).getName());
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedDishDTO() {
        Dish existingDish = createTestDish();
        DishDTO updateDto = createTestDishDTO();
        updateDto.setName("Updated Dish");
        updateDto.setCaloriesPerServing(250.0);

        Dish updatedDish = createTestDish();
        updatedDish.setName("Updated Dish");
        updatedDish.setCaloriesPerServing(250.0);

        DishDTO expectedDto = createTestDishDTO();
        expectedDto.setName("Updated Dish");
        expectedDto.setCaloriesPerServing(250.0);

        when(dishRepository.findById(1L)).thenReturn(Optional.of(existingDish));
        when(dishRepository.existsByName("Updated Dish")).thenReturn(false);
        when(dishRepository.save(existingDish)).thenReturn(updatedDish);
        when(dishMapper.toDTO(updatedDish)).thenReturn(expectedDto);

        DishDTO result = dishService.update(1L, updateDto);

        assertEquals("Updated Dish", result.getName());
        assertEquals(250.0, result.getCaloriesPerServing());
        verify(dishRepository).save(existingDish);
    }

    @Test
    void update_WithExistingName_ShouldThrowException() {
        Dish existingDish = createTestDish();
        DishDTO updateDto = createTestDishDTO();
        updateDto.setName("Existing Dish");

        when(dishRepository.findById(1L)).thenReturn(Optional.of(existingDish));
        when(dishRepository.existsByName("Existing Dish")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> dishService.update(1L, updateDto));
        verify(dishRepository, never()).save(any());
    }

    @Test
    void update_WithNonExistingDish_ShouldThrowException() {
        DishDTO updateDto = createTestDishDTO();
        when(dishRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> dishService.update(999L, updateDto));
        verify(dishRepository, never()).save(any());
    }

    @Test
    void delete_WithExistingDish_ShouldCallRepository() {
        when(dishRepository.existsById(1L)).thenReturn(true);
        doNothing().when(dishRepository).deleteById(1L);

        dishService.delete(1L);

        verify(dishRepository).deleteById(1L);
    }

    @Test
    void delete_WithNonExistingDish_ShouldThrowException() {
        when(dishRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> dishService.delete(999L));
        verify(dishRepository, never()).deleteById(any());
    }
}