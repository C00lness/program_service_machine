package com.example.programservice.repository;

import com.example.programservice.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findByStatus(String status);
    List<Program> findByCreatedBy(String createdBy);
}
