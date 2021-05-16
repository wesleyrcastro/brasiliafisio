package br.com.brasiliaFisio.cursos.modelo.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.brasiliaFisio.cursos.modelo.Curso;

public class ValorPorCursoDinheiro implements Serializable {

	private static final long serialVersionUID = 1L;

	private final BigDecimal valor;
	private final Curso curso;

	public ValorPorCursoDinheiro(BigDecimal valor, Curso curso) {
		this.valor = valor;
		this.curso = curso;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Curso getCurso() {
		return curso;
	}

}