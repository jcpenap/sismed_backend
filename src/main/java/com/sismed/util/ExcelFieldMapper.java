package com.sismed.util;


import com.sismed.dto.ExcelField;
import com.sismed.util.enums.FieldType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ExcelFieldMapper {


	public static <T> List<T> getPojos(List<ExcelField[]> excelFields, Class<T> clazz) {

		List<T> list = new ArrayList<>();
		excelFields.forEach(evc -> {

			T t = null;

			try {
				t = clazz.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}

			Class<? extends Object> classz = t.getClass();

			for (int i = 0; i < evc.length; i++) {

				for (Field field : classz.getDeclaredFields()) {
					field.setAccessible(true);

					if (evc[i].getPojoAttribute().equalsIgnoreCase(field.getName())) {

						try {
							if (evc[i].getExcelValue() != null) {
								if (FieldType.STRING.getValue().equalsIgnoreCase(evc[i].getExcelColType())) {
									String value = evc[i].getExcelValue() != "" ? evc[i].getExcelValue() : null;
									field.set(t, value);
								} else if (FieldType.DOUBLE.getValue().equalsIgnoreCase(evc[i].getExcelColType())) {
									field.set(t, Double.valueOf(evc[i].getExcelValue()));
								} else if (FieldType.INTEGER.getValue().equalsIgnoreCase(evc[i].getExcelColType())) {
									field.set(t, Double.valueOf(evc[i].getExcelValue()).intValue());
								} else if (FieldType.DATE.getValue().equalsIgnoreCase(evc[i].getExcelColType())) {
									SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
									Date date = formatter.parse(evc[i].getExcelValue());
									field.set(t, date);
								}
							}
						} catch (IllegalArgumentException | IllegalAccessException | ParseException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}

			list.add(t);
		});

		return list;
	}

}
