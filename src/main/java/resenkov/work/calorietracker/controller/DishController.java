package resenkov.work.calorietracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resenkov.work.calorietracker.dto.DishDTO;
import resenkov.work.calorietracker.service.DishService;

import java.util.List;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;

    @PostMapping("/create")
    public ResponseEntity<DishDTO> create(@RequestBody @Valid DishDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dishService.create(dto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DishDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<DishDTO>> getAll() {
        return ResponseEntity.ok(dishService.getAll());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<DishDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid DishDTO dto) {
        return ResponseEntity.ok(dishService.update(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}