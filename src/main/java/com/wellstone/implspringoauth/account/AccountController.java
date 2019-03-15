package com.wellstone.implspringoauth.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping(value = "/{accountId}")
    public ResponseEntity getAccount(@PathVariable String accountId) {
        Account account = accountService.getAccount(accountId);
        return ResponseEntity.ok().body(account);
    }
}
