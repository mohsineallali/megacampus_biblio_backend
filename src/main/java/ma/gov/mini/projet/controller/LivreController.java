package ma.gov.mini.projet.controller;

import ma.gov.mini.projet.dto.LivreDTO;
import ma.gov.mini.projet.service.LivreService;
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
@RequestMapping("/api/livres")
@CrossOrigin("*")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @GetMapping
    public ResponseEntity<List<LivreDTO>> findAll() {
        return ResponseEntity.ok(livreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(livreService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LivreDTO> save(@RequestBody LivreDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livreService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivreDTO> update(@PathVariable Long id, @RequestBody LivreDTO dto) {
        return ResponseEntity.ok(livreService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        livreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
