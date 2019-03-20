package com.wellstone.implspringoauth.account.DTO;


import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.validation.constraints.Size;

@Component
public class AccountUpdateValidator {
    public void validate(AccountUpdateDTO updateDTO, BindingResult result) {

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
