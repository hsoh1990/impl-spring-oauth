package com.wellstone.implspringoauth.account;

import com.wellstone.implspringoauth.account.DTO.AccountRegisterDTO;
import com.wellstone.implspringoauth.common.BaseControllerTest;
import com.wellstone.implspringoauth.common.ResponseDataType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    Account account;

    @Before
    public void setUp() throws Exception {
        AccountRegisterDTO registerDto = AccountRegisterDTO.builder()
                .accountId("test")
                .password("testpass")
                .name("test")
                .email("test@test.io")
                .company("ymtech")
                .build();

        this.account = accountService.saveAccount(registerDto);
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
}