package com.example.virtualwallet.repositories;

import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.models.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Transaction> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Transaction ", Transaction.class).list();
        }
    }

    @Override
    public Transaction getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.get(Transaction.class, id);
            if (transaction == null) {
                throw new EntityNotFoundException("Transaction", id);
            }
            return transaction;
        }
    }

    @Override
    public List<Transaction> getBySenderId(int senderId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> transactions = session.createQuery(
                    "From Transaction Where sender.userId = :sender_id", Transaction.class);
            transactions.setParameter("sender_id", senderId);
            return transactions.getResultList();
        }
    }

    @Override
    public List<Transaction> getByRecipientId(int recipientId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Transaction> transactions = session.createQuery(
                    "From Transaction Where recipient.userId = :recipient_id", Transaction.class);
            transactions.setParameter("recipient_id", recipientId);
            return transactions.getResultList();
        }
    }

    @Override
    public void createTransaction(Transaction transaction) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(transaction);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(transaction);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteTransaction(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }
}
