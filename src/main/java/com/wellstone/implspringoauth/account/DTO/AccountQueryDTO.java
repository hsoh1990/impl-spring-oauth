package com.wellstone.implspringoauth.account.DTO;


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
    private String accountId;

    private String name;

    @Size(max = 50)
    private String email;

    @Column(name = "company")
    private String company;
}
