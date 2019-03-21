package com.wellstone.implspringoauth.account;

import com.wellstone.implspringoauth.account.DTO.*;
import com.wellstone.implspringoauth.common.ResponseData;
import com.wellstone.implspringoauth.common.ResponseDataType;
import com.wellstone.implspringoauth.exception.BadValidationException;
import com.wellstone.implspringoauth.exception.DuplicatedException;
import com.wellstone.implspringoauth.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
    AccountValidator accountValidator;

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity insertAccount(@RequestBody @Valid AccountInsertDTO insertDTO,
                                        BindingResult result) {
        if (result.hasErrors()) {
            throw new BadValidationException(result.getFieldErrors());
        }

        Account account = accountService.insertAccount(insertDTO);
        AccountResponseDTO responseDTO = modelMapper.map(account, AccountResponseDTO.class);

        URI createUri = linkTo(AccountController.class).slash(responseDTO.getIdx()).toUri();
        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("create account")
                .build();

        return ResponseEntity.created(createUri).body(responseData);
    }

    @GetMapping
    public ResponseEntity getAccountList(Pageable pageable,
                                         @ModelAttribute @Valid AccountQueryDTO queryDTO,
                                         BindingResult result) {
        if (result.hasErrors()) {
            throw new BadValidationException(result.getFieldErrors());
        }

        Page<Account> accountPage = accountService.getAccountList(pageable, queryDTO);
        List<AccountResponseDTO> content = accountPage.getContent()
                .stream()
                .map(account -> modelMapper.map(account, AccountResponseDTO.class))
                .collect(Collectors.toList());

        final PageImpl<AccountResponseDTO> responseDTOS = new PageImpl<>(content, pageable, accountPage.getTotalElements());

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTOS)
                .message("Get account list")
                .build();

        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping(value = "/{idx:[0-9]++}")
    public ResponseEntity getAccount(@PathVariable Long idx) {
        Account account = accountService.getAccount(idx);
        AccountResponseDTO responseDTO = modelMapper.map(account, AccountResponseDTO.class);

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("Get account by idx = " + idx)
                .build();

        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping(value = "/query")
    public ResponseEntity getAccountByQuery(@ModelAttribute @Valid AccountQueryDTO queryDTO,
                                            BindingResult result) {
        if (result.hasErrors()) {
            throw new BadValidationException(result.getFieldErrors());
        }

        accountValidator.queryValidate(queryDTO, result);
        if (result.hasErrors()) {
            throw new BadValidationException(result.getAllErrors());
        }

        Account account = accountService.getAccountByQuery(queryDTO);
        AccountResponseDTO responseDTO = modelMapper.map(account, AccountResponseDTO.class);

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("Get account by query")
                .build();

        return ResponseEntity.ok().body(responseData);
    }

    @PutMapping(value = "/{idx:[0-9]++}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateAccount(@PathVariable Long idx,
                                        @RequestBody @Valid AccountUpdateDTO updateDTO,
                                        BindingResult result){

        accountValidator.updateValidate(updateDTO, result);
        if (result.hasErrors()) {
            throw new BadValidationException(result.getAllErrors());
        }

        Account account = accountService.updateAccount(idx, updateDTO);
        AccountResponseDTO responseDTO = modelMapper.map(account, AccountResponseDTO.class);

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("Update account by idx = " + idx)
                .build();

        return ResponseEntity.ok().body(responseData);
    }

    @DeleteMapping(value ="/{idx:[0-9]++}")
    public ResponseEntity deleteAccount(@PathVariable Long idx){
        Account account = accountService.deleteAccount(idx);
        AccountResponseDTO responseDTO = modelMapper.map(account, AccountResponseDTO.class);

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("Delete account by idx = " + idx)
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
