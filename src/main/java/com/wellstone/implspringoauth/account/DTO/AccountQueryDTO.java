package com.wellstone.implspringoauth.account.DTO;


import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountQueryDTO {
//    @Group(name="a", validate="oneof")
//    @Member(group="a")
    @Size(min = 3, max = 30)
    private String accountId;

//    @Member(group="a")
    @Size(max = 50)
    private String email;

}
