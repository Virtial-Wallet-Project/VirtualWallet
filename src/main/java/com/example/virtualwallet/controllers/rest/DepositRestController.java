package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.CreditCardService;
import com.example.virtualwallet.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/deposit")
    public ResponseEntity<String> depositMoney(@RequestHeader HttpHeaders headers, @RequestParam Double amount) {

        User user = authorizationHelper.tryGetUser(headers);
        CreditCard card = creditCardService.getByUserId(user.getUserId());

        String response = depositService.depositMoney(user, card, amount);

        if (response.contains("Deposit Successful")) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}
