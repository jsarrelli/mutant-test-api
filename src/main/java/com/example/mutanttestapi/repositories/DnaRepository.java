package com.example.mutanttestapi.repositories;

import com.example.mutanttestapi.models.DnaEntity;
import com.example.mutanttestapi.models.DnaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DnaRepository extends JpaRepository<DnaEntity, String> {

    long countByType(DnaType type);
}
