package com.wellstone.implspringoauth.account.DTO;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterDTO {
    @NotBlank
    @Size(min = 3, max = 30)
    private String accountId;

    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String email;

    private String company;
}
