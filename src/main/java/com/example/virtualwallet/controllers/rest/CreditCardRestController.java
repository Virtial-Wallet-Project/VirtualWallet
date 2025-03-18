package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.mappers.CardMapper;
import com.example.virtualwallet.DTOs.CardDto;
import com.example.virtualwallet.DTOs.CardUpdateDto;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.CreditCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cards")
@Tag(name = "Card Controller", description = "Different card related options. Create, Update or Delete a card.")
public class CreditCardRestController {

    private final CreditCardService cardService;
    private final CardMapper cardMapper;
    private final AuthenticationHelper authorizationHelper;

    @Autowired
    public CreditCardRestController(CreditCardService cardService, CardMapper cardMapper, AuthenticationHelper authorizationHelper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @Operation(summary = "Create a card.", description = "Creates a card after providing unique card information.")
    @PostMapping
    @SecurityRequirement(name = "authHeader")
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

    @Operation(summary = "Update a card.", description = "Updates a card's check number or card holder.")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "authHeader")
    public CardDto updateCard(@RequestHeader HttpHeaders headers, @Valid @RequestBody CardUpdateDto cardDto,
                              @PathVariable int id) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            cardDto.setId(id);
            CreditCard card = cardMapper.dtoToObjectForUpdate(cardDto);
            cardService.updateCard(user, card);
            return cardMapper.objectToDto(card);
            
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

    @Operation(summary = "Delete a card.", description = "Deletes a card after valid authentication.")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "authHeader")
    public void deleteCard(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            cardService.deleteCard(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
