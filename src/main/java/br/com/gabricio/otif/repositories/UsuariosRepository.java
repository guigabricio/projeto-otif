package br.com.gabricio.otif.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.gabricio.otif.model.PerfilEnum;
import br.com.gabricio.otif.model.StatusUsuario;
import br.com.gabricio.otif.model.UsuariosModel;

@Repository
public interface UsuariosRepository extends MongoRepository<UsuariosModel, String> {

    public boolean existsById(String id);
    
    public void deleteByCpf(String cpf);

    public boolean existsByCpf(String cpf);

    public Optional<UsuariosModel> findByCpf(String cpf);
    
    public Optional<UsuariosModel> findByCpfAndSenha(String cpf, String senha);
    
    List<UsuariosModel> findByPerfisAndStatusUsuario(PerfilEnum perfil, StatusUsuario statusUsuario);
    
    List<UsuariosModel> findByStatusUsuario(StatusUsuario statusUsuario);
    
}
