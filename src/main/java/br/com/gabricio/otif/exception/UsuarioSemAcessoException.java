package br.com.gabricio.otif.exception;

public class UsuarioSemAcessoException extends OtifException {

	private static final long serialVersionUID = 1L;

	public UsuarioSemAcessoException(String message) {
        super(message);
    }
}
