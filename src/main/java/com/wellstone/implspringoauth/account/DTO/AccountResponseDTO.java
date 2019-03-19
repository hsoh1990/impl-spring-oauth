package com.wellstone.implspringoauth.account.DTO;

import com.wellstone.implspringoauth.account.AccountRole;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private Long idx;
    private String accountId;
    private String name;
    private String email;
    private String company;
    private Date joinDate;
    private Set<AccountRole> roles;
}
