package com.example.virtualwallet.repositories;

import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.filtering.FilterTransactionOptions;
import com.example.virtualwallet.models.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final SessionFactory sessionFactory;
    private final UserRepository userRepository;

    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory, UserRepository userRepository) {
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
    }

    @Override
    public List<Transaction> getAll(FilterTransactionOptions filterOptions, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new InvalidOperationException("Page and size should be positive numbers!");
        }

        try (Session session = sessionFactory.openSession()) {
            StringBuilder sb = new StringBuilder("FROM Transaction t ");

            sb.append("LEFT JOIN FETCH t.sender s ");
            sb.append("LEFT JOIN FETCH t.recipient r ");

            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getUserId().ifPresent(userId -> {
                filters.add("(s.userId = :userId OR r.userId = :userId)");
                params.put("userId", userId);
            });

            filterOptions.getSender().ifPresent(sender -> {
                filters.add("s.username = :sender");
                params.put("sender", sender);
            });

            filterOptions.getRecipient().ifPresent(recipient -> {
                filters.add("r.username = :recipient");
                params.put("recipient", recipient);
            });

            filterOptions.getStartDate().ifPresent(startDate -> {
                filters.add("t.transactionDate >= :startDate");
                params.put("startDate", startDate);
            });

            filterOptions.getEndDate().ifPresent(endDate -> {
                filters.add("t.transactionDate <= :endDate");
                params.put("endDate", endDate);
            });

            if (!filters.isEmpty()) {
                sb.append(" WHERE ").append(String.join(" AND ", filters));
            }

            sb.append(createOrderBy(filterOptions));

            Query<Transaction> query = session.createQuery(sb.toString(), Transaction.class);
            params.forEach(query::setParameter);
            query.setFirstResult(page * size);
            query.setMaxResults(size);

            return query.list();
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

    private String createOrderBy(FilterTransactionOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = switch (filterOptions.getSortBy().get()) {
            case "amount" -> "t.amount";
            default -> "t.transactionDate";
        };

        orderBy = String.format(" ORDER BY %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() &&
                filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s DESC", orderBy);
        }

        return orderBy;
    }
}
