package com.wellstone.implspringoauth.oauthclient.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthClientQueryDTO {
    private String name;
    private Long accountIdx;
    private String accountId;
}
