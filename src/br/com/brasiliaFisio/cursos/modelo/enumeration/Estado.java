package br.com.brasiliaFisio.cursos.modelo.enumeration;

public enum Estado {

	AL("AL"), AM("AM"), AP("AP"), BA("BA"), CE("CE"), DF("DF"), ES("ES"), GO("GO"), MA("MA"), MG("MG"), MS("MS"), MT(
			"MT"), PA("PA"), PB("PB"), PE("PE"), PI("PI"), PR("PR"), RJ("RJ"), RN("RN"), RR("RR"), RO("RO"), RS("RS"), SC(
			"SC"), SE("SE"), SP("SP"), TO("TO");

	private String descricao;

	Estado(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
