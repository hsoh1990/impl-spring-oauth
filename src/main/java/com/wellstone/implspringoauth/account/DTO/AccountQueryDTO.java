package com.wellstone.implspringoauth.account.DTO;


import com.wellstone.implspringoauth.common.FieldRule;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountQueryDTO {
    @Size(min = 3, max = 30)
    @FieldRule(group = "notAllNull", rule = FieldRule.Rule.NOT_ALL_NULL)
    private String accountId;

    private String name;

    @Size(max = 50)
    @FieldRule(group = "notAllNull", rule = FieldRule.Rule.NOT_ALL_NULL)
    private String email;

    @Column(name = "company")
    private String company;
}
