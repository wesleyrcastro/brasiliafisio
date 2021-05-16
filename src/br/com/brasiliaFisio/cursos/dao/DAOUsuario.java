package br.com.brasiliaFisio.cursos.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.brasiliaFisio.cursos.modelo.Usuario;
import br.com.brasiliaFisio.cursos.util.security.TransformaStringSHA512;

public class DAOUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public List<Usuario> buscaPorNome(String nome) {

		String jpql = "SELECT u FROM Usuario u WHERE UPPER (u.nome) LIKE UPPER (:pUsuario) ORDER BY u.nome";
		TypedQuery<Usuario> query = this.em.createQuery(jpql, Usuario.class);
		query.setParameter("pUsuario", "%" + nome + "%");

		return query.getResultList();
	}

	public Usuario existe(Usuario usuario) {
		// Verifica se e a mesma senha e usuario cadastrado.
		// "Caixa alta ou caixa baixa".
		Query query = em.createNativeQuery(
				"SELECT * FROM USUARIO WHERE BINARY login = :pLogin AND BINARY senha = :pSenha", Usuario.class);
		query.setParameter("pLogin", usuario.getLogin());
		query.setParameter("pSenha", TransformaStringSHA512.sha512(usuario.getSenha()));
		try {
			return (Usuario) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

}
