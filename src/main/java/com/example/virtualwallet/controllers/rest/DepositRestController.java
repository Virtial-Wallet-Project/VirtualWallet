package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.CreditCardService;
import com.example.virtualwallet.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/wallet")
public class DepositRestController {

    private final DepositService depositService;
    private final AuthenticationHelper authorizationHelper;
    private final CreditCardService creditCardService;


    @Autowired
    public DepositRestController(DepositService withdrawService, AuthenticationHelper authorizationHelper, CreditCardService creditCardService) {
        this.depositService = withdrawService;
        this.authorizationHelper = authorizationHelper;
        this.creditCardService = creditCardService;
    }

    @PostMapping("/deposit/{cardNumber}")
    public ResponseEntity<String> depositMoney(@RequestHeader HttpHeaders headers, @RequestParam Double amount,
                                               @PathVariable long cardNumber) {
        try {
            User user = authorizationHelper.tryGetUser(headers);
            CreditCard card = creditCardService.getByCardNumber(String.valueOf(cardNumber));

            String response = depositService.depositMoney(user, card, amount);

            if (response.contains("Deposit Successful")) {
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.badRequest().body(response);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
