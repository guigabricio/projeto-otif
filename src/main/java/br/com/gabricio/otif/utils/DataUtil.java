/**
 * Autor: gabricio
 * Criado em: 2020-10-24
 */
package br.com.gabricio.otif.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;


public class DataUtil {

	public static final DateTimeFormatter DDMMYYY_HHMMSS = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public static final DateTimeFormatter DDMMYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static final DateTimeFormatter DDMMYYYY_PADRAO = new DateTimeFormatterBuilder()
			.appendPattern("dd/MM/yyyy")
			.optionalStart()
			.appendPattern(" HH:mm:ss")
			.optionalEnd()
			.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
			.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			.toFormatter();


	public static final DateTimeFormatter MMMYY = DateTimeFormatter.ofPattern("MMM/yy");   

	public static LocalDateTime converteStringToLocalDateTime(String dataString, DateTimeFormatter dateTimeFormatter) {
		if (dataString != null && !"".equals(dataString)) {				
			return LocalDateTime.parse(dataString, dateTimeFormatter);
		}
		return null;
	}
	
	public static boolean ehValido(String dataString, DateTimeFormatter dateTimeFormatter) {
		try {
			LocalDateTime.parse(dataString, dateTimeFormatter);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String imprimeData(LocalDateTime date, DateTimeFormatter formato) {
		try {
			return formato.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static boolean pertenceAoLimite(String horaInicio, String horaLimite) throws ParseException {
		SimpleDateFormat formatador = new SimpleDateFormat("HH:mm");

		Date timeIni = formatador.parse(horaInicio);
		Date timeEnd = formatador.parse(horaLimite);

		Calendar calendar = Calendar.getInstance();
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);

		Date minhaHora = formatador.parse(hours + ":" + minutes);

		return (minhaHora.after(timeIni) || minhaHora.equals(timeIni)) && 
				(minhaHora.before(timeEnd) || minhaHora.equals(timeEnd));		
	}

}
