package com.example.virtualwallet.service;

import com.example.virtualwallet.models.Card;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.UserRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
public class WithdrawServiceImpl implements WithdrawService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final SessionFactory sessionFactory;

    private final String MONEY_TRANSFER_API_URL = "http://localhost:8081/api/transfer/withdraw";

    @Autowired
    public WithdrawServiceImpl(UserRepository userRepository, RestTemplate restTemplate, SessionFactory sessionFactory) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.sessionFactory = sessionFactory;
    }

    //Might need some changes, non-final version
    @Override
    @Transactional
    public String depositMoney(User user, CreditCard userCard, double amount) {

        Card card = new Card();
        card.setCardNumber(userCard.getCardNumber());
        card.setCheckNumber(userCard.getCheckNumber());

        ResponseEntity<String> response = restTemplate.postForEntity(MONEY_TRANSFER_API_URL, card, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
                user.setBalance(user.getBalance() + amount);
                userRepository.updateUser(user);
                return "Deposit Successful! New Balance: " + user.getBalance();

        } else {
            return "Deposit Failed: " + response.getBody();
        }
    }
}
