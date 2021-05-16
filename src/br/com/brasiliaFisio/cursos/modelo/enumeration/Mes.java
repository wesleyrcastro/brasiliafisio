package br.com.brasiliaFisio.cursos.modelo.enumeration;

public enum Mes {

	Janeiro("Janeiro", 1), Fevereiro("Fevereiro", 2), Março("Março", 3), Abril("Abril", 4), Maio("Maio", 5), Junho(
			"Junho", 6), Julho("Julho", 7), Agosto("Agosto", 8), Setembro("Setembro", 9), Outubro("Outubro", 10), Novembro(
			"Novembro", 11), Dezembro("Dezembro", 12);

	private Integer valor;

	private String descricao;

	private Mes(String descricao, Integer valor) {

		this.valor = valor;
		this.descricao = descricao;
	}

	public Integer getValor() {
		return valor;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
