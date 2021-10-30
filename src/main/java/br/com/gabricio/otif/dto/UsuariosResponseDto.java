package br.com.gabricio.otif.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.gabricio.otif.configuration.converters.TrataDataSerializer;
import br.com.gabricio.otif.model.PerfilEnum;
import br.com.gabricio.otif.model.StatusUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosResponseDto {

    private String id;

    private String nome;

    private String cpf;

    private String cnpj;

    private PerfilEnum perfil;
    
    @JsonSerialize(using = TrataDataSerializer.class)
    private LocalDateTime dataHoraCriacao;    
    
    private StatusUsuario statusUsuario;
    
    @JsonSerialize(using = TrataDataSerializer.class)
    private LocalDateTime dataHoraAtualizacao;
    
    private String cpfUsuarioAlteracao;

}
