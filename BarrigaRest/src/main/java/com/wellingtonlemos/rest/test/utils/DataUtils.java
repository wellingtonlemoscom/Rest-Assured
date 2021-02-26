package com.wellingtonlemos.rest.test.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {
	
	public static String getdataDiferenciaDias(Integer qntDias) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, qntDias);
		return getDataFormatada(cal.getTime());
	}
	
	public static String getDataFormatada(Date data) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}
	
}
