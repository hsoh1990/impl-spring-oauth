package com.wellstone.implspringoauth.account;

import com.wellstone.implspringoauth.exception.BadValidationException;
import org.modelmapper.internal.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registerAccount(@RequestBody @Valid AccountDto.AccountRegisterDto registerDto,
                                          BindingResult result) throws URISyntaxException {

        if(result.hasErrors()){
            throw new BadValidationException(result.getFieldErrors());
        }

        return ResponseEntity.created(new URI("http://localhost:8081")).body(registerDto);
    }

    @GetMapping(value = "/{accountId}")
    public ResponseEntity getAccount(@PathVariable String accountId) {
        Account account = accountService.getAccount(accountId);
        return ResponseEntity.ok().body(account);
    }
}
