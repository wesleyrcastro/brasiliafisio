package br.com.brasiliaFisio.cursos.modelo.enumeration;

public enum NomeDoBanco {

	Banco_do_Brasil("Banco do Brasil - 001"), Caixa_Economica("Caixa Econômica - 104"), HSBC("HSBC - 399"), BRB(
			"BRB - 070"), Itau("Itaú - 341"), Santander("Santander - 033"), Bradesco("Bradesco - 237"), Unicred("Unicred - 091-4");

	private String descricao;

	NomeDoBanco(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
