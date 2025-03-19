package com.example.virtualwallet.services;

import static com.example.virtualwallet.Helpers.createMockUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.filtering.FilterTransactionOptions;
import com.example.virtualwallet.filtering.FilterUserOptions;
import com.example.virtualwallet.helpers.PermissionHelpers;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.TransactionRepository;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.service.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User sender;
    private User recipient;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        sender = new User(1, "senderUser", "password", "sender@example.com", "1234567890", 100.0, false, false);
        sender.setAccountVerified(true);
        recipient = new User(2, "recipientUser", "password", "recipient@example.com", "0987654321", 50.0, false, false);
        transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setSender(sender);
        transaction.setRecipient(recipient);
        transaction.setAmount(20.0);
    }

    @Test
    public void getAll_ShouldReturnAllUsers_WhenValid() {
        FilterTransactionOptions filterTransactionOptions = new FilterTransactionOptions("1", "sender", "recipient",
                LocalDateTime.now(), LocalDateTime.now(), "sortBy", "sortOrder");
        int page = 1;
        int size = 2;

        User user = createMockUser();
        user.setAdmin(true);

        List<Transaction> transactionList = transactionService.getAll(filterTransactionOptions, page, size, user);

        Assertions.assertEquals(transactionService.getAll(filterTransactionOptions, page, size, user).size(), transactionList.size());
    }

    @Test
    void createTransaction_Success() {
        when(userRepository.getById(recipient.getUserId())).thenReturn(recipient);
        doNothing().when(transactionRepository).createTransaction(transaction);
        doNothing().when(userRepository).updateUser(any(User.class));

        Transaction result = transactionService.createTransaction(sender, recipient.getUserId(), transaction);

        assertEquals(transaction, result);
        assertEquals(80.0, sender.getBalance());
        assertEquals(70.0, recipient.getBalance());
        verify(transactionRepository, times(1)).createTransaction(transaction);
        verify(userRepository, times(2)).updateUser(any(User.class));
    }

    @Test
    void createTransaction_FailsForSelfTransfer() {
        when(userRepository.getById(sender.getUserId())).thenReturn(sender);

        assertThrows(InvalidOperationException.class, () ->
                        transactionService.createTransaction(sender, sender.getUserId(), transaction),
                "You cannot send money to yourself!");
    }

    @Test
    void createTransaction_FailsForInsufficientFunds() {
        transaction.setAmount(200.0);
        when(userRepository.getById(recipient.getUserId())).thenReturn(recipient);

        assertThrows(InvalidOperationException.class, () ->
                        transactionService.createTransaction(sender, recipient.getUserId(), transaction),
                "Invalid operation! Insufficient funds!");
    }

    @Test
    void createTransaction_FailsForInvalidAmount() {
        transaction.setAmount(0);

        assertThrows(InvalidOperationException.class, () ->
                        transactionService.createTransaction(sender, recipient.getUserId(), transaction));
    }

    @Test
    void getById_AdminUser_Success() {
        sender.setAdmin(true);
        when(transactionRepository.getById(transaction.getTransactionId())).thenReturn(transaction);

        Transaction result = transactionService.getById(sender, transaction.getTransactionId());

        assertEquals(transaction, result);
    }

    @Test
    void getById_NonAdminUser_Failure() {
        sender.setAdmin(false);
        assertThrows(UnauthorizedOperationException.class, () ->
                transactionService.getById(sender, transaction.getTransactionId()));
    }
}
