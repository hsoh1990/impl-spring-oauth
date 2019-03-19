package com.wellstone.implspringoauth.account;

import com.wellstone.implspringoauth.account.DTO.AccountQueryValidator;
import com.wellstone.implspringoauth.account.DTO.AccountRegisterDTO;
import com.wellstone.implspringoauth.account.DTO.AccountResponseDTO;
import com.wellstone.implspringoauth.account.DTO.AccountQueryDTO;
import com.wellstone.implspringoauth.common.ResponseData;
import com.wellstone.implspringoauth.common.ResponseDataType;
import com.wellstone.implspringoauth.exception.BadValidationException;
import com.wellstone.implspringoauth.exception.DuplicatedException;
import com.wellstone.implspringoauth.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountQueryValidator accountQueryValidator;

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registerAccount(@RequestBody @Valid AccountRegisterDTO registerDTO,
                                          BindingResult result) {
        if (result.hasErrors()) {
            throw new BadValidationException(result.getFieldErrors());
        }

        Account account = accountService.saveAccount(registerDTO);
        AccountResponseDTO responseDTO = modelMapper.map(account, AccountResponseDTO.class);

        URI createUri = linkTo(AccountController.class).slash(responseDTO.getIdx()).toUri();
        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("create account")
                .build();

        return ResponseEntity.created(createUri).body(responseData);
    }

    @GetMapping(value = "/{idx:[0-9]++}")
    public ResponseEntity getAccount(@PathVariable Long idx) {
        Account account = accountService.getAccount(idx);
        AccountResponseDTO responseDTO = modelMapper.map(account, AccountResponseDTO.class);

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("Get account by idx")
                .build();

        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping(value = "/query")
    public ResponseEntity getAccountByQuery(@ModelAttribute @Valid AccountQueryDTO searchDTO,
                                            BindingResult result) {
        if (result.hasErrors()) {
            throw new BadValidationException(result.getFieldErrors());
        }

        accountQueryValidator.validate(searchDTO, result);
        if (result.hasErrors()) {
            throw new BadValidationException(result.getAllErrors());
        }

        Account account = accountService.getAccountByQuery(searchDTO);
        AccountResponseDTO responseDTO = modelMapper.map(account, AccountResponseDTO.class);

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("Get account by query")
                .build();

        return ResponseEntity.ok().body(responseData);
    }

    /**
     * Exception handler
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity NotFoundExceptionHandler(NotFoundException e) {
        log.error("Account Not found exception");
        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.FAILED)
                .result(e.getInformation())
                .build();

        return ResponseEntity.badRequest().body(responseData);
    }

    @ExceptionHandler(value = DuplicatedException.class)
    public ResponseEntity DuplicatedExceptionHandler(DuplicatedException e) {
        log.error("Account Duplicated exception");
        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.FAILED)
                .result(e.getInformation())
                .build();

        return ResponseEntity.badRequest().body(responseData);
    }

    @ExceptionHandler(value = BadValidationException.class)
    public ResponseEntity BadValidationExceptionHandler(BadValidationException e) {
        log.error("Account Validation exception");
        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.FAILED)
                .result(e.getInformation())
                .build();

        return ResponseEntity.badRequest().body(responseData);
    }
}
