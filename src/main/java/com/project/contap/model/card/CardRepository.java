package com.project.contap.model.card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> ,CostomCardRepository{
}
