package com.wellstone.implspringoauth.account;

import com.wellstone.implspringoauth.account.DTO.AccountRegisterDTO;
import com.wellstone.implspringoauth.account.DTO.AccountUpdateDTO;
import com.wellstone.implspringoauth.common.BaseControllerTest;
import com.wellstone.implspringoauth.common.ResponseDataType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    private Account account;

    @Before
    public void setUp() throws Exception {
        AccountRegisterDTO registerDto = AccountRegisterDTO.builder()
                .accountId("test")
                .password("testpass")
                .name("test")
                .email("test@test.io")
                .company("ymtech")
                .build();

        this.account = accountService.insertAccount(registerDto);
    }

    @Test
    public void registerAccount() throws Exception {
        //Given
        AccountRegisterDTO registerDto = AccountRegisterDTO.builder()
                .accountId("hsoh")
                .password("hsohpass")
                .name("hsoh")
                .email("hsoh@test.io")
                .company("ymtech")
                .build();

        //When
        ResultActions resultActions = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(registerDto)));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.idx").exists())
                .andExpect(jsonPath("$.result.password").doesNotExist())
                .andExpect(jsonPath("$.result.accountId").value(registerDto.getAccountId()))
                .andExpect(jsonPath("$.result.name").value(registerDto.getName()))
                .andExpect(jsonPath("$.result.email").value(registerDto.getEmail()))
                .andExpect(jsonPath("$.result.company").value(registerDto.getCompany()));
    }

    @Test
    public void getAccountList() throws Exception {
        //Given
        AccountRegisterDTO registerDto = AccountRegisterDTO.builder()
                .accountId("hsoh")
                .password("hsohpass")
                .name("hsoh")
                .email("hsoh@test.io")
                .company("ymtech")
                .build();
        Account savedAccount = accountService.insertAccount(registerDto);

        //When
        ResultActions resultActions = mockMvc.perform(get("/api/accounts", this.account.getIdx())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .param("page", "0")
                .param("size", "3")
                .param("sort", "idx,DESC")
//                .param("accountId", savedAccount.getAccountId())
//                .param("name", savedAccount.getName())
//                .param("email", savedAccount.getEmail())
                .param("company", savedAccount.getCompany()));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.totalPages").value(1))
                .andExpect(jsonPath("$.result.totalElements").value(2))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.content[0].idx").exists())
                .andExpect(jsonPath("$.result.content[0].password").doesNotExist())
                .andExpect(jsonPath("$.result.content[0].accountId").value(savedAccount.getAccountId()))
                .andExpect(jsonPath("$.result.content[0].name").value(savedAccount.getName()))
                .andExpect(jsonPath("$.result.content[0].email").value(savedAccount.getEmail()))
                .andExpect(jsonPath("$.result.content[0].company").value(savedAccount.getCompany()))
        ;
    }

    @Test
    public void getAccount() throws Exception {
        //Given

        //When
        ResultActions resultActions = mockMvc.perform(get("/api/accounts/{idx}", this.account.getIdx())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.idx").exists())
                .andExpect(jsonPath("$.result.password").doesNotExist())
                .andExpect(jsonPath("$.result.accountId").value(this.account.getAccountId()))
                .andExpect(jsonPath("$.result.name").value(this.account.getName()))
                .andExpect(jsonPath("$.result.email").value(this.account.getEmail()))
                .andExpect(jsonPath("$.result.company").value(this.account.getCompany()));
    }

    @Test
    public void getAccountQuery() throws Exception {
        //Given

        //When
        ResultActions resultActions = mockMvc.perform(get("/api/accounts/query")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .param("email", this.account.getEmail())
                .param("accountId", this.account.getAccountId()));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.idx").exists())
                .andExpect(jsonPath("$.result.password").doesNotExist())
                .andExpect(jsonPath("$.result.accountId").value(this.account.getAccountId()))
                .andExpect(jsonPath("$.result.name").value(this.account.getName()))
                .andExpect(jsonPath("$.result.email").value(this.account.getEmail()))
                .andExpect(jsonPath("$.result.company").value(this.account.getCompany()));
    }

    @Test
    public void updateAccount() throws Exception {
        //Given
        Account account = accountService.getAccount((long) 1);
        AccountUpdateDTO updateDTO = AccountUpdateDTO.builder()
                .currentPassword("admin")
                .updatePassword(("admin2"))
                .name("update")
                .email("update@test.io")
                .company("ymtech")
                .build();

        //When
        // TODO this.account.getIdx()사용시 unsupportedoperationexception 발생..
        ResultActions resultActions = mockMvc.perform(put("/api/accounts/{idx}", account.getIdx())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(updateDTO)));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.idx").exists())
                .andExpect(jsonPath("$.result.password").doesNotExist())
                .andExpect(jsonPath("$.result.name").value(updateDTO.getName()))
                .andExpect(jsonPath("$.result.email").value(updateDTO.getEmail()))
                .andExpect(jsonPath("$.result.company").value(updateDTO.getCompany()));
    }

    @Test
    public void deleteAccount() throws Exception {
        //Given

        //When
        // TODO this.account.getIdx()사용시 unsupportedoperationexception 발생..
        ResultActions resultActions = mockMvc.perform(delete("/api/accounts/{idx}", this.account.getIdx())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.idx").exists())
                .andExpect(jsonPath("$.result.password").doesNotExist())
                .andExpect(jsonPath("$.result.name").value(this.account.getName()))
                .andExpect(jsonPath("$.result.email").value(this.account.getEmail()))
                .andExpect(jsonPath("$.result.company").value(this.account.getCompany()));
    }
}