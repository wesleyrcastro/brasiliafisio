package br.com.brasiliaFisio.cursos.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;

public class DAOFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Produces
	public DAO createDAO(InjectionPoint injectionPoint, EntityManager em) {
		ParameterizedType type = (ParameterizedType) injectionPoint.getType();
		Class classe = (Class) type.getActualTypeArguments()[0];
		return new DAO(classe, em);

	}

}
