package br.com.brasiliaFisio.cursos.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class DAOLoginBloqueado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public boolean existe(String ip) {
		Query query = em.createQuery("select count(l) from LoginBloqueado l where l.ip = :ip", Long.class);
		query.setParameter("ip", ip);
		Integer count = ((Long) query.getSingleResult()).intValue();
		return count > 0;
	}
}