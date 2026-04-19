package ma.gov.mini.projet.controller;

import ma.gov.mini.projet.dto.AuteurDTO;
import ma.gov.mini.projet.service.AuteurService;
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
@RequestMapping("/api/auteurs")
@CrossOrigin("*")
public class AuteurController {

    @Autowired
    private AuteurService auteurService;

    @GetMapping
    public ResponseEntity<List<AuteurDTO>> findAll() {
        return ResponseEntity.ok(auteurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuteurDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(auteurService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AuteurDTO> save(@RequestBody AuteurDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auteurService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuteurDTO> update(@PathVariable Long id, @RequestBody AuteurDTO dto) {
        return ResponseEntity.ok(auteurService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        auteurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
