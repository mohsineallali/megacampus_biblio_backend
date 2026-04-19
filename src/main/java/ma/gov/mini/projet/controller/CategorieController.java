package ma.gov.mini.projet.controller;

import ma.gov.mini.projet.dto.CategorieDTO;
import ma.gov.mini.projet.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategorieController {

    @Autowired
    private CategorieService categorieService;

    @GetMapping
    public ResponseEntity<List<CategorieDTO>> findAll() {
        return ResponseEntity.ok(categorieService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategorieDTO> save(@RequestBody CategorieDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categorieService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategorieDTO> update(@PathVariable Long id, @RequestBody CategorieDTO dto) {
        return ResponseEntity.ok(categorieService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categorieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
