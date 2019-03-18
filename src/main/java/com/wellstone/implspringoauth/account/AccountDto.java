package com.wellstone.implspringoauth.account;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@SuppressWarnings("squid:S1118")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AccountDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class AccountRegisterDto {
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
}
