package com.example.virtualwallet.repositories;

import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.models.CreditCard;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CreditCardRepositoryImpl implements CreditCardRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CreditCardRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<CreditCard> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM CreditCard ", CreditCard.class).list();
        }
    }

    @Override
    public CreditCard getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            CreditCard card = session.get(CreditCard.class, id);
            if (card == null) {
                throw new EntityNotFoundException("Card", id);
            }

            return card;
        }
    }

    @Override
    public CreditCard getByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<CreditCard> card = session.createQuery(
                    "From CreditCard Where createdBy.userId = :user_id", CreditCard.class);
            card.setParameter("user_id", userId);
            return card
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Card", "user ID", String.valueOf(userId)));
        }
    }

    @Override
    public CreditCard getByCardNumber(String cardNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<CreditCard> card = session.createQuery(
                    "From CreditCard Where cardNumber = :cardNumber", CreditCard.class);
            card.setParameter("cardNumber", cardNumber);
            return card
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Card", "card number", cardNumber));
        }
    }

    @Override
    public void createCard(CreditCard card) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(card);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateCard(CreditCard card) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(card);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteCard(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }


}
