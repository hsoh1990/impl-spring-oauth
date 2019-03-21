package com.wellstone.implspringoauth.common;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class FieldRuleValidator {
    private FieldRuleValidator() {
    }

    public static void validate(Object object, BindingResult result) {
        Map<String, List<Field>> groupingField = new HashMap<>();
        Class<?> objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();

        Arrays.stream(fields)
                //1. DTO field 뽑아오기
                .filter(field -> field.getAnnotation(FieldRule.class) != null)
                //2. Group 끼리 모으기
                .forEach(field -> {
                    FieldRule annotation = field.getAnnotation(FieldRule.class);
                    String group = annotation.group();
                    if ("Null".equals(group)) {
                        group = UUID.randomUUID().toString();
                    }
                    List<Field> findFields = groupingField.computeIfAbsent(group, k -> new ArrayList<>());
                    findFields.add(field);
                });

        // 3. groupingField 돌면서 확인 rule 후 BindingResult 값을 채움
        for (String key : groupingField.keySet()) {
            List<Field> fieldsList = groupingField.get(key);
            List<Object> values = new ArrayList<>();

            //TODO order 높은 값으로 rule 정의
            FieldRule.Rule rule = fieldsList.get(0).getAnnotation(FieldRule.class).rule();

            // 4. Field 값을 꺼내서 리스트로 변경
            boolean accessible = false;
            for (Field field : fieldsList) {
                try {
                    accessible = field.canAccess(object);
                    field.setAccessible(true);
                    Object o = field.get(object);
                    values.add(o);
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(accessible);
                }
            }

            // 5. Rule 에 따른 validation 핸들링
            if (rule.equals(FieldRule.Rule.NOT_ALL_NULL)) {
                handleNotAllNull(object, result, values);
            } else if (rule.equals(FieldRule.Rule.EXIST_OTHERS)) {
                handleExistAnotherOne(object, result, values);
            }


        }
    }

    // 리스트중 전체가 null 이 아니면서 하나라도 null 이면 에러
    private static void handleNotAllNull(Object object, BindingResult result,List<Object> values) {
        if(values.isEmpty()) return;
        boolean valid = values.stream().anyMatch(Objects::nonNull);
        if (!valid) {
            String[] codes = {object.getClass().getSimpleName(), FieldRule.Rule.NOT_ALL_NULL.toString()};
            String[] arguments = {};
            ObjectError objectError = new ObjectError(
                    object.getClass().getSimpleName(),
                    codes,
                    arguments,
                    "한개의 이상의 필드에는 값이 있어야 합니다.");
            result.addError(objectError);
        }
    }

    // 리스트중 1개 값만 null 이 아니면 에러
    private static void handleExistAnotherOne(Object object, BindingResult result, List<Object> values) {
        if(values.isEmpty()) return;
        List<Object> collect = values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (collect.size() == 1) {
            String[] codes = {object.getClass().getSimpleName(),  FieldRule.Rule.EXIST_OTHERS.toString()};
            String[] arguments = {};
            ObjectError objectError = new ObjectError(
                    object.getClass().getSimpleName(),
                    codes,
                    arguments,
                    "한개의 필드에는 값이 있면 다른 필드 값도 필요합니다");
            result.addError(objectError);
        }
    }


}
