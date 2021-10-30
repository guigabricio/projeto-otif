package br.com.gabricio.otif.exception;

public class UsuarioJaEstaAtivadoException extends OtifException {
	private static final long serialVersionUID = 1L;

	public UsuarioJaEstaAtivadoException(String message) {
        super(message);
    }
}
