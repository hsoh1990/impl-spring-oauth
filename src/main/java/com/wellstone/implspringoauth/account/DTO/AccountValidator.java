package com.wellstone.implspringoauth.account.DTO;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Component
public class AccountValidator {
    public void queryValidate(AccountQueryDTO queryDTO, BindingResult result) {

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

    public void updateValidate(AccountUpdateDTO updateDTO, BindingResult result) {

        if (!StringUtils.isEmpty(updateDTO.getUpdatePassword())){
            if(StringUtils.isEmpty(updateDTO.getCurrentPassword())){
                String[] codes = {AccountUpdateDTO.class.getSimpleName()};
                String[] arguments = {};
                ObjectError objectError = new ObjectError(
                        AccountQueryDTO.class.getSimpleName(),
                        codes,
                        arguments,
                        "Password를 변경하려면 currentPassword가 존재 해야합니다.");
                result.addError(objectError);
            }
        }
    }
}
