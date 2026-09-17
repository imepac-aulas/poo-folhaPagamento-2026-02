package br.edu.imepac.dao;

import java.util.List;
import java.util.Optional;

/**
 * Contrato genérico de acesso a dados, comum a todas as entidades.
 * Nenhuma implementação existe ainda (nem em memória, nem em banco) —
 * é só a abstração que a camada de serviço depende, para poder ser
 * desenvolvida antes da camada de persistência estar pronta.
 *
 * @param <T>  tipo da entidade
 * @param <ID> tipo do identificador da entidade
 */
public interface DAO<T, ID> {

    T salvar(T entidade);

    Optional<T> buscarPorId(ID id);

    List<T> listarTodos();

    void excluir(ID id);
}
