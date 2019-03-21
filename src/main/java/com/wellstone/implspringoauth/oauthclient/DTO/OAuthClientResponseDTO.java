package com.wellstone.implspringoauth.oauthclient.DTO;

import com.wellstone.implspringoauth.account.DTO.AccountResponseDTO;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthClientResponseDTO {
    private String clientId;
    private String resourceIds;
    private String clientSecret;
    private String scope;
    private String authGrantTypes;
    private String webServiceRedirectUri;
    private String authorities;
    private int accessTokenValidity;
    private int refreshTokenValidity;
    private String additionalInfo;
    private String autoapprove;

    //custom attribute
    private String name;
    AccountResponseDTO account;
}
