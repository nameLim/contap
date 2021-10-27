package com.project.contap.service;

import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.Card;
import com.project.contap.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardService {

//    private final CardRepository cardRepository;
//
//    public Page<Card> main(Pageable pageable) {
//        return cardRepository.findAllByOrderByModifiedDtDesc(pageable);
//    }
//
//    public Card getCards(Long id) throws ContapException {
//        return cardRepository.findById(id).orElseThrow(
//                () -> new ContapException(ErrorCode.CARD_NOT_FOUND)
//        );
//    }
}