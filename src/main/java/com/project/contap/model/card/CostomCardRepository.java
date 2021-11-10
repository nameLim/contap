package com.project.contap.model.card;

import com.project.contap.model.card.dto.QCardDto;

import java.util.List;

public interface CostomCardRepository {
    List<QCardDto> findAllByUserId(Long userId);
}
