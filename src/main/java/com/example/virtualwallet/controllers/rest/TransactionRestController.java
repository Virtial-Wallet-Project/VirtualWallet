package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.helpers.TransactionMapper;
import com.example.virtualwallet.models.FilterTransactionOptions;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.TransactionDto;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.TransactionService;
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
public class TransactionRestController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TransactionRestController(TransactionService transactionService, TransactionMapper transactionMapper, AuthenticationHelper authenticationHelper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<TransactionDto> getAll (@RequestParam(required = false) Integer userId,
                                        @RequestParam(required = false) Integer senderId,
                                        @RequestParam(required = false) Integer recipientId,
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
                    userId, senderId, recipientId, startDate, endDate, sortBy, orderBy
            );
        } else {
            filterTransactionOptions = new FilterTransactionOptions(
                    user.getUserId(), null, null, startDate, endDate, sortBy, orderBy
            );
        }
        return transactionMapper.transactionsToDtoOut(transactionService.getAll(filterTransactionOptions, page, size, user));
    }

    @GetMapping("{id}")
    public TransactionDto getById (@RequestHeader HttpHeaders headers, @PathVariable int id) {
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

    @PostMapping("{recipientId}")
    public TransactionDto createTransaction (@RequestHeader HttpHeaders headers, @Valid @RequestBody TransactionDto transactionDto,
                                             @PathVariable int recipientId) {
        try {
            User sender = authenticationHelper.tryGetUser(headers);
            Transaction transaction = transactionMapper.fromDtoToObject(transactionDto);
            transactionService.createTransaction(sender, recipientId, transaction);

            return transactionDto;

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
