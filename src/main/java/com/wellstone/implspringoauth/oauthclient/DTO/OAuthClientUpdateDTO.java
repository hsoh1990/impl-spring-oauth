package com.wellstone.implspringoauth.oauthclient.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthClientUpdateDTO {
    private String resourceIds;
    private String scope;
    private String authGrantTypes;
    private String webServiceRedirectUri;
    @Builder.Default
    private int accessTokenValidity = 0;
    @Builder.Default
    private int refreshTokenValidity = 0;
    private String additionalInfo;
}
