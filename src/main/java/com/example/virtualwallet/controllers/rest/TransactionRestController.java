package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.DTOs.TransactionDto;
import com.example.virtualwallet.DTOs.TransactionDtoOut;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.filtering.FilterTransactionOptions;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.mappers.TransactionMapper;
import com.example.virtualwallet.models.*;
import com.example.virtualwallet.service.TransactionService;
import com.example.virtualwallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller", description = "Create a transaction and see all your transactions filtered.")
public class TransactionRestController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final TransactionMapper transactionMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TransactionRestController(TransactionService transactionService, UserService userService, TransactionMapper transactionMapper, AuthenticationHelper authenticationHelper) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.transactionMapper = transactionMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @Operation(summary = "Transaction history.", description = "See your transaction history filtered. If admin, check all users' transaction history.")
    @GetMapping
    @SecurityRequirement(name = "authHeader")
    public List<TransactionDtoOut> getAll (@RequestParam(required = false) String userId,
                                           @RequestParam(required = false) String senderUsername,
                                           @RequestParam(required = false) String recipientUsername,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss") LocalDateTime startDate,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss") LocalDateTime endDate,
                                           @RequestParam(required = false) String sortBy,
                                           @RequestParam(required = false) String orderBy,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestHeader HttpHeaders headers) {

        User user = authenticationHelper.tryGetUser(headers);
        FilterTransactionOptions filterTransactionOptions;

        if (user.isAdmin()) {
            filterTransactionOptions = new FilterTransactionOptions(
                    userId, senderUsername, recipientUsername, startDate, endDate, sortBy, orderBy
            );
        } else {
            filterTransactionOptions = new FilterTransactionOptions(
                    String.valueOf(user.getUserId()), senderUsername, recipientUsername, startDate, endDate, sortBy, orderBy
            );
        }
        return transactionMapper.transactionsToDtoOut(transactionService.getAll(filterTransactionOptions, page, size, user));
    }

    @Operation(summary = "Get transaction by ID.", description = "Admin exclusive. Get a specific transaction by its ID.")
    @GetMapping("{id}")
    @SecurityRequirement(name = "authHeader")
    public TransactionDtoOut getById (@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Transaction transaction = transactionService.getById(user, id);
            return transactionMapper.fromObjectToDto(transaction);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Create Transaction", description = "Send money to a user by choosing them by their username, email or phone number.")
    @PostMapping("{recipientId}")
    @SecurityRequirement(name = "authHeader")
    public TransactionDtoOut createTransaction (@RequestHeader HttpHeaders headers, @Valid @RequestBody TransactionDto transactionDto,
                                                @PathVariable int recipientId) {
        try {
            User sender = authenticationHelper.tryGetUser(headers);
            User recipient = userService.getUserById(recipientId);
            Transaction transaction = transactionMapper.fromDtoToObject(transactionDto);
            transaction.setSender(sender);
            transaction.setRecipient(recipient);
            transactionService.createTransaction(sender, recipientId, transaction);

            return transactionMapper.fromObjectToDto(transaction);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
