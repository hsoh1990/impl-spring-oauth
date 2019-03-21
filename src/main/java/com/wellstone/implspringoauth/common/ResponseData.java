package com.wellstone.implspringoauth.common;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData {
	private String message;
	private ResponseDataType type;
	private Object result;
	private Object searchData;
	@Builder.Default
	private Date time = new Date();
}
