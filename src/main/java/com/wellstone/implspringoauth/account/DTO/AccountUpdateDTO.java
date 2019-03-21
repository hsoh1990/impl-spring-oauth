package com.wellstone.implspringoauth.account.DTO;


import com.wellstone.implspringoauth.common.FieldRule;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateDTO {
    @FieldRule(group = "password", rule = FieldRule.Rule.EXIST_OTHERS)
    private String currentPassword;
    @FieldRule(group = "password", rule = FieldRule.Rule.EXIST_OTHERS)
    private String updatePassword;

    @Size(min = 3, max = 30)
    private String name;

    @Size(max = 50)
    private String email;

    private String company;
}
