package com.wellstone.implspringoauth.oauthclient.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthClientInsertDTO {
    @NotBlank
    private String resourceIds;
    @NotBlank
    private String scope;
    @NotBlank
    private String authGrantTypes;
    private String webServiceRedirectUri;
    @Builder.Default
    private String authorities = "ROLE_YOUR_CLIENT";
    @Builder.Default
    private int accessTokenValidity = 36000;
    @Builder.Default
    private int refreshTokenValidity = 2592000;
    private String additionalInfo;
    private String autoapprove;

    @NotBlank
    private String name;
    @NotNull
    private Long accountIdx;
}
