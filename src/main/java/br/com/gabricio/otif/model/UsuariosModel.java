package br.com.gabricio.otif.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class UsuariosModel implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String idUsuarioExterno;

	private String nome;

	@Indexed(unique = true)
	private String cpf;

	private String cnpj;

	private String senha;

	private List<PerfilEnum> perfis = new ArrayList<>();

	private LocalDateTime dataHoraCriacao;

	private StatusUsuario statusUsuario;

	private LocalDateTime datahoraAtualizacao;

	private String cpfUsuarioAlteracao;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis;
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.cpf;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
