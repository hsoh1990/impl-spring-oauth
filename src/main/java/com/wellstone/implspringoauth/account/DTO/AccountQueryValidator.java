package com.wellstone.implspringoauth.account.DTO;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Component
public class AccountQueryValidator {
    public void validate(AccountQueryDTO queryDTO, BindingResult result) {

        if (StringUtils.isEmpty(queryDTO.getAccountId()) && StringUtils.isEmpty(queryDTO.getEmail())) {
            String[] codes = {AccountQueryDTO.class.getSimpleName()};
            String[] arguments = {};
            ObjectError objectError = new ObjectError(
                    AccountQueryDTO.class.getSimpleName(),
                    codes,
                    arguments,
                    "Account ID, Email 중 하나는 존재 해야합니다.");
            result.addError(objectError);
        }
    }
}
