package br.com.brasiliaFisio.cursos.modelo.enumeration;

public enum Cargo {

	Funcionario("Funcionário"), Gerente("Gerente");

	private String descricao;

	Cargo(String descricao) {

		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
