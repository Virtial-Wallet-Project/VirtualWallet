package org.example.moneytransfer.controllers;

import org.example.moneytransfer.models.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/transfer")
public class MoneyWithdraw {

    @PostMapping("/withdraw")
    public ResponseEntity<String> transferMoney (@RequestBody Card card) {
        Random random = new Random();
        int result = random.nextInt(10) + 1;

        if (result > 3) {
            return ResponseEntity.ok("Success!");
        } else {
            return ResponseEntity.badRequest().body("Insufficient funds!");
        }
    }
}
