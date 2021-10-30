package br.com.gabricio.otif.model;

import org.springframework.security.core.GrantedAuthority;

public enum PerfilEnum implements GrantedAuthority {

	ADMINISTRADOR, GUARITA, MOTORISTA;
	
	@Override
	public String getAuthority() {
		return name();
	}
	
}
