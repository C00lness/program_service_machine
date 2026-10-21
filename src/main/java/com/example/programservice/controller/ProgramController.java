package com.example.programservice.controller;

import com.example.programservice.kafka.ProgramEventProducer;
import com.example.programservice.model.Program;
import com.example.programservice.repository.ProgramRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramRepository programRepository;
    private final ProgramEventProducer programEventProducer;
    private final SimpMessagingTemplate messagingTemplate;

    public ProgramController(ProgramRepository programRepository, ProgramEventProducer programEventProducer, SimpMessagingTemplate messagingTemplate) {
        this.programRepository = programRepository;
        this.programEventProducer = programEventProducer;
        this.messagingTemplate = messagingTemplate;
    }

    // GET /api/programs — получить все программы
    @GetMapping
    public List<Program> getAll() {
        return programRepository.findAll();
    }

    // GET /api/programs/{id} — получить программу по ID
    @GetMapping("/{id}")
    public ResponseEntity<Program> getById(@PathVariable Long id) {
        return programRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/programs — создать новую программу
    @PostMapping
    public Program create(@RequestBody Program program) {
        Program saved = programRepository.save(program);
        messagingTemplate.convertAndSend("/topic/programs", saved);
        try{
            programEventProducer.sendProgramCreatedEvent(saved.getId(), saved.getName(), saved.getCreatedBy());
        } catch (Exception e) {
            System.err.println("Has not send Kafka message, error: " + e.getMessage());
        }

        return saved;
    }

    // PUT /api/programs/{id} — обновить программу
    @PutMapping("/{id}")
    public ResponseEntity<Program> update(@PathVariable Long id, @RequestBody Program updated) {
        return programRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setDescription(updated.getDescription());
                    existing.setMetalType(updated.getMetalType());
                    existing.setThickness(updated.getThickness());
                    existing.setLength(updated.getLength());
                    existing.setQuantity(updated.getQuantity());
                    existing.setStatus(updated.getStatus());
                    return ResponseEntity.ok(programRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/programs/{id} — удалить программу
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (programRepository.existsById(id)) {
            programRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/programs/status/{status} — получить программы по статусу
    @GetMapping("/status/{status}")
    public List<Program> getByStatus(@PathVariable String status) {
        return programRepository.findByStatus(status);
    }
}
