package ma.gov.mini.projet.controller;

import ma.gov.mini.projet.dto.EditeurDTO;
import ma.gov.mini.projet.service.EditeurService;
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
@RequestMapping("/api/editeurs")
@CrossOrigin("*")
public class EditeurController {

    @Autowired
    private EditeurService editeurService;

    @GetMapping
    public ResponseEntity<List<EditeurDTO>> findAll() {
        return ResponseEntity.ok(editeurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditeurDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(editeurService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EditeurDTO> save(@RequestBody EditeurDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(editeurService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditeurDTO> update(@PathVariable Long id, @RequestBody EditeurDTO dto) {
        return ResponseEntity.ok(editeurService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        editeurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
