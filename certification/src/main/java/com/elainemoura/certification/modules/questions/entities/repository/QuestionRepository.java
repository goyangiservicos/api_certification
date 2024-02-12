package com.elainemoura.certification.modules.questions.entities.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elainemoura.certification.modules.questions.entities.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, UUID> {
   List<QuestionEntity> findByTechnology(String technology);
   //pegou a tecnologia 
}
