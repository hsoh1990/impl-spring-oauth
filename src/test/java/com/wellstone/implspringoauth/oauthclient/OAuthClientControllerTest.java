package com.wellstone.implspringoauth.oauthclient;

import com.wellstone.implspringoauth.account.Account;
import com.wellstone.implspringoauth.account.AccountService;
import com.wellstone.implspringoauth.account.DTO.AccountInsertDTO;
import com.wellstone.implspringoauth.common.BaseControllerTest;
import com.wellstone.implspringoauth.common.ResponseDataType;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientInsertDTO;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientUpdateDTO;
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

public class OAuthClientControllerTest extends BaseControllerTest {
    @Autowired
    AccountService accountService;

    @Autowired
    OAuthClientService oAuthClientService;

    private Account account;

    private OAuthClient oAuthClient;

    @Before
    public void setUp() throws Exception {
        AccountInsertDTO registerDto = AccountInsertDTO.builder()
                .accountId("test")
                .password("testpass")
                .name("test")
                .email("test@test.io")
                .company("ymtech")
                .build();
        this.account = accountService.insertAccount(registerDto);

        OAuthClientInsertDTO insertDTO = OAuthClientInsertDTO.builder()
                .resourceIds("test")
                .scope("read,write")
                .authGrantTypes("password,refresh_token")
                .additionalInfo("test oauth client")
                .name("testClient")
                .accountIdx(this.account.getIdx())
                .build();
        this.oAuthClient = oAuthClientService.insertOAuthClient(insertDTO);
    }

    @Test
    public void insertOAuthClient() throws Exception {
        //Given
        OAuthClientInsertDTO insertDTO = OAuthClientInsertDTO.builder()
                .resourceIds("oauth")
                .scope("read,write")
                .authGrantTypes("password,refresh_token")
                .additionalInfo("test oauth client")
                .name("insertTestClient")
                .accountIdx(this.account.getIdx())
                .build();

        //When
        ResultActions resultActions = mockMvc.perform(post("/api/oauth/clients")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .content(objectMapper.writeValueAsString(insertDTO)));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.resourceIds").value(insertDTO.getResourceIds()))
                .andExpect(jsonPath("$.result.clientId").exists())
                .andExpect(jsonPath("$.result.clientSecret").exists())
                .andExpect(jsonPath("$.result.scope").value(insertDTO.getScope()))
                .andExpect(jsonPath("$.result.authorities").value(insertDTO.getAuthorities()))
                .andExpect(jsonPath("$.result.name").value(insertDTO.getName()))
                .andExpect(jsonPath("$.result.account").exists());
   }

    @Test
    public void getOAuthClientList() throws Exception {
        //Given

        //When
        ResultActions resultActions = mockMvc.perform(get("/api/oauth/clients", this.account.getIdx())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .param("page", "0")
                .param("size", "3")
                .param("sort", "account_idx,DESC")
                .param("accountIdx", oAuthClient.getAccount().getIdx().toString()));

        //Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.totalPages").value(1))
                .andExpect(jsonPath("$.result.totalElements").value(1))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.content[0].name").value(oAuthClient.getName()))
                .andExpect(jsonPath("$.result.content[0].resourceIds").value(oAuthClient.getResourceIds()))
                .andExpect(jsonPath("$.result.content[0].scope").value(oAuthClient.getScope()))
                .andExpect(jsonPath("$.result.content[0].authGrantTypes").value(oAuthClient.getAuthGrantTypes()))
                .andExpect(jsonPath("$.result.content[0].account").exists())
        ;
    }

    @Test
    public void getOAuthClient() throws Exception {
        //Given

        //When
        ResultActions resultActions = mockMvc.perform(get("/api/oauth/clients/{clientId}", this.oAuthClient.getClientId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken()));
        //Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.name").value(oAuthClient.getName()))
                .andExpect(jsonPath("$.result.resourceIds").value(oAuthClient.getResourceIds()))
                .andExpect(jsonPath("$.result.scope").value(oAuthClient.getScope()))
                .andExpect(jsonPath("$.result.authGrantTypes").value(oAuthClient.getAuthGrantTypes()))
                .andExpect(jsonPath("$.result.account").exists())
        ;
    }

    @Test
    public void updateOAuthClient() throws Exception {
        //Given
        OAuthClientUpdateDTO updateDTO = OAuthClientUpdateDTO.builder()
                .resourceIds("updateTest")
                .scope("read")
                .authGrantTypes("password")
                .webServiceRedirectUri("http://www.test.io")
                .accessTokenValidity(30000)
                .refreshTokenValidity(60000)
                .additionalInfo("update test oauth client")
                .build();

        //When
        ResultActions resultActions = mockMvc.perform(put("/api/oauth/clients/{clientId}", this.oAuthClient.getClientId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .content(objectMapper.writeValueAsString(updateDTO)));
        //Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.resourceIds").value(updateDTO.getResourceIds()))
                .andExpect(jsonPath("$.result.scope").value(updateDTO.getScope()))
                .andExpect(jsonPath("$.result.authGrantTypes").value(updateDTO.getAuthGrantTypes()))
                .andExpect(jsonPath("$.result.webServiceRedirectUri").value(updateDTO.getWebServiceRedirectUri()))
                .andExpect(jsonPath("$.result.accessTokenValidity").value(updateDTO.getAccessTokenValidity()))
                .andExpect(jsonPath("$.result.refreshTokenValidity").value(updateDTO.getRefreshTokenValidity()))
                .andExpect(jsonPath("$.result.additionalInfo").value(updateDTO.getAdditionalInfo()))
                .andExpect(jsonPath("$.result.account").exists())
        ;
    }

    @Test
    public void deleteOAuthClient() throws Exception {
        //Given

        //When
        ResultActions resultActions = mockMvc.perform(delete("/api/oauth/clients/{clientId}", this.oAuthClient.getClientId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken()));
        //Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(ResponseDataType.SUCCESS.toString()))
                .andExpect(jsonPath("$.result.resourceIds").value(this.oAuthClient.getResourceIds()))
                .andExpect(jsonPath("$.result.scope").value(this.oAuthClient.getScope()))
                .andExpect(jsonPath("$.result.authGrantTypes").value(this.oAuthClient.getAuthGrantTypes()))
                .andExpect(jsonPath("$.result.webServiceRedirectUri").value(this.oAuthClient.getWebServiceRedirectUri()))
                .andExpect(jsonPath("$.result.accessTokenValidity").value(this.oAuthClient.getAccessTokenValidity()))
                .andExpect(jsonPath("$.result.refreshTokenValidity").value(this.oAuthClient.getRefreshTokenValidity()))
                .andExpect(jsonPath("$.result.additionalInfo").value(this.oAuthClient.getAdditionalInfo()))
                .andExpect(jsonPath("$.result.account").exists())
        ;
    }
}