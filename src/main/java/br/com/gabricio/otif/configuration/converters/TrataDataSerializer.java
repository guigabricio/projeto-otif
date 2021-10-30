/**
 * Autor: gabricio
 * Criado em: 2020-10-24
 */
package br.com.gabricio.otif.configuration.converters;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class TrataDataSerializer extends StdSerializer<LocalDateTime> {
	 
	private static final long serialVersionUID = 1L;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");    
    
    public TrataDataSerializer() {
        this(null);
    }
 
    public TrataDataSerializer(Class<LocalDateTime> t) {
        super(t);
    }

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(formatter.format(value));
	}
}