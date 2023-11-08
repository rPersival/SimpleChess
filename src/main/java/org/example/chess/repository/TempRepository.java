package org.example.chess.repository;

import org.example.chess.model.entity.TempEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempRepository extends JpaRepository<TempEntity, Long> {
}
