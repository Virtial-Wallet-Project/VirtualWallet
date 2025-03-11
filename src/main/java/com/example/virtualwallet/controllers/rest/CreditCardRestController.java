package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.helpers.CardMapper;
import com.example.virtualwallet.models.CardDto;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.CreditCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cards")
public class CreditCardRestController {

    private final CreditCardService cardService;
    private final CardMapper cardMapper;
    private final AuthenticationHelper authorizationHelper;

    public CreditCardRestController(CreditCardService cardService, CardMapper cardMapper, AuthenticationHelper authorizationHelper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @PostMapping
    public CardDto createCard(@RequestHeader HttpHeaders headers, @Valid @RequestBody CardDto cardDto) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            CreditCard card = cardMapper.dtoToObject(cardDto);
            cardService.createCard(user, card);
            return cardDto;

        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping
    public CardDto updateCard(@RequestHeader HttpHeaders headers, @Valid @RequestBody CardDto cardDto) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            CreditCard card = cardMapper.dtoToObjectForUpdate(cardDto, user);
            cardService.updateCard(user, card);
            return cardDto;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping
    public void deleteCard(@RequestHeader HttpHeaders headers) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            cardService.deleteCard(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
