package com.wellstone.implspringoauth.account.DTO;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateDTO {
    private String currentPassword;
    private String updatePassword;

    @Size(min = 3, max = 30)
    private String name;

    @Size(max = 50)
    private String email;

    private String company;
}
