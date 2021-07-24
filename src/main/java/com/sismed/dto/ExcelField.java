package com.sismed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelField {

	private String excelHeader;
	private int excelIndex;
	private String excelColType;
	private String excelValue;
	private String pojoAttribute;

}
