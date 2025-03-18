package com.example.virtualwallet.services;

import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.helpers.PermissionHelpers;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.TransactionRepository;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.service.DepositServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DepositServiceImpl depositService;

    private User user;
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setBalance(100.0);
        user.setAccountVerified(true);

        creditCard = new CreditCard();
        creditCard.setCardId(1);
        creditCard.setCardNumber("1234567890123456");
        creditCard.setCheckNumber("123");
        creditCard.setCreatedBy(user);
    }

    @Test
    void depositMoney_SuccessfulDeposit() {
        double depositAmount = 50.0;
        ResponseEntity<String> successfulResponse = new ResponseEntity<>("Success", HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(successfulResponse);

        String result = depositService.depositMoney(user, creditCard, depositAmount);

        assertEquals("Deposit Successful! New Balance: 150.0", result);
        assertEquals(150.0, user.getBalance());

        verify(userRepository, times(1)).updateUser(user);
        verify(transactionRepository, times(1)).createTransaction(any(Transaction.class));
    }

    @Test
    void depositMoney_ThrowsInvalidOperationException_WhenAmountIsZero() {
        double depositAmount = 0.0;

        assertThrows(InvalidOperationException.class, () -> depositService.depositMoney(user, creditCard, depositAmount));

        verifyNoInteractions(restTemplate, userRepository, transactionRepository);
    }

    @Test
    void depositMoney_ThrowsInvalidOperationException_WhenAmountIsNegative() {
        double depositAmount = -10.0;

        assertThrows(InvalidOperationException.class, () -> depositService.depositMoney(user, creditCard, depositAmount));

        verifyNoInteractions(restTemplate, userRepository, transactionRepository);
    }

//    @Test
//    void depositMoney_FailsDueToInsufficientFunds() {
//        double depositAmount = 50.0;
//
//        // Mocking the RestTemplate to throw a BadRequest exception
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
//
//        // Calling the method
//        String result = depositService.depositMoney(user, creditCard, depositAmount);
//
//        // Verifying that the deposit failed and the balance remains the same
//        assertEquals("Deposit Failed! Insufficient funds!", result);
//        assertEquals(100.0, user.getBalance());  // Balance should remain unchanged
//
//        // Verifying that the updateUser and createTransaction methods are never called
//        verify(userRepository, never()).updateUser(any(User.class));
//        verify(transactionRepository, never()).createTransaction(any(Transaction.class));
//    }

    @Test
    void depositMoney_FailsDueToUnexpectedError() {
        double depositAmount = 50.0;

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        String result = depositService.depositMoney(user, creditCard, depositAmount);

        assertEquals("An unexpected error occurred. Please try again later.", result);
        assertEquals(100.0, user.getBalance());  // Balance should remain unchanged

        verify(userRepository, never()).updateUser(any(User.class));
        verify(transactionRepository, never()).createTransaction(any(Transaction.class));
    }
}
