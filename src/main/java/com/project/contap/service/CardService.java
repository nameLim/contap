package com.project.contap.service;

import com.project.contap.model.Card;
import com.project.contap.model.QCard;
import com.project.contap.repository.CardRepository;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final com.querydsl.jpa.impl.JPAQueryFactory jpaQueryFactory;
    @Autowired
    public CardService (CardRepository cardRepository,com.querydsl.jpa.impl.JPAQueryFactory jpaQueryFactory)
    {
        this.cardRepository = cardRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Card> getCards(Long userId) {
        QCard hu = QCard.card;
        QueryResults<Card> abc =  jpaQueryFactory
                .select(hu)
                .from(hu)
                .where(hu.user.id.eq(userId))
                .fetchResults();
        List<Card> ret = abc.getResults();
        return ret;
    }

}
