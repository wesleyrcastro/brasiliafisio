package br.com.brasiliaFisio.cursos.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.br.CPF;

import br.com.brasiliaFisio.cursos.modelo.enumeration.Estado;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Sexo;

@Entity
@Audited
@AuditTable(value = "ALUNO_AUD")
@Table(name = "ALUNO")
public class Aluno implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;

	@NotBlank(message="Nome obrigatório")
	@Length(min = 3, max = 45, message = "O nome deve ter entre {min} e {max} caracteres")
	@Column(name = "NOME", length = 45, nullable = false)
	private String nome;

	@NotEmpty(message="CPF obrigatório")
	@CPF(message = "Informe um CPF válido")
	@Column(name = "CPF", length = 15, nullable = false)
	private String cpf;

	@NotBlank(message="RG obrigatório")
	@Length(min = 3, message = "O RG deve ter no mínimo {min} números")
	@Column(name = "IDENTIDADE", length = 20, nullable = false)
	private String identidade;

	@NotBlank(message="Orgão expeditor obrigatório")
	@Length(min = 2, message = "O orgão expeditor deve ter no mínimo {min} caracteres")
	@Column(name = "ORGAO_EXPEDITOR", length = 10, nullable = false)
	private String orgaoExp;

	@NotEmpty(message="E-mail obrigatório")
	@Email
	@Length(max = 45, message = "O e-mail deve ter no máximo {max} caracteres")
	@Column(name = "EMAIL", length = 50, nullable = false)
	private String email;

	@NotNull(message = "Data de nascimento obrigatótio")
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_NASCIMENTO", nullable = false)
	private Date dataNasc;

	@NotNull(message = "Sexo obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name = "ID_SEXO", length = 10, nullable = false)
	private Sexo sexo;

	@Column(name = "TEL_RESIDENCIAL", length = 17)
	private String telResidencial;

	@NotEmpty(message="Tel. Celular obrigatório")
	@Column(name = "TEL_CELULAR", length = 17, nullable = false)
	private String telCelular;

	@NotEmpty(message="CEP obrigatório")
	@Column(name = "CEP", length = 12, nullable = false)
	private String cep;

	@NotBlank(message="Logradouro obrigatório")
	@Length(min = 3, max = 45, message = "O endereço deve ter entre {min} e {max} caracteres")
	@Column(name = "ENDERECO", length = 45, nullable = false)
	private String endereco;

	@Length(max = 45, message = "O complemento deve ter no máximo {max} caracteres")
	@Column(name = "COMPLEMENTO", length = 45)
	private String complemento;

	@NotBlank(message="Bairro obrigatório")
	@Length(min = 3, max = 45, message = "O bairro deve ter entre {min} e {max} caracteres")
	@Column(name = "BAIRRO", length = 45, nullable = false)
	private String bairro;

	@NotBlank(message="Cidade obrigatório")
	@Length(min = 3, max = 25, message = "A cidade deve ter entre {min} e {max} caracteres")
	@Column(name = "CIDADE", length = 25, nullable = false)
	private String cidade;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Estado obrigatório")
	@Column(name = "ID_ESTADO", length = 3, nullable = false)
	private Estado estado;
	

	@Length(max = 25, message = "O pais deve ter no máximo {max} caracteres")
	@Column(name = "PAIS", length = 25)
	private String pais;

	@Length(max = 45, message = "A ocupação deve ter no máximo {max} caracteres")
	@Column(name = "OCUPACAO", length = 45)
	private String ocupacao;

	@Length(max = 30, message = "O Nº do registro profissional deve ter no máximo {max} caracteres")
	@Column(name = "NUM_REGISTRO_PROFISSIONAL", length = 30)
	private String numRegistroProfissional;

	@NotBlank(message="Senha obrigatório")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Length(min = 8, max = 20, message = "A senha deve ter entre {min} e {max} caracteres!")
	@Column(name = "SENHA", length = 200, nullable = false)
	private String senha;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getIdentidade() {
		return identidade;
	}

	public void setIdentidade(String identidade) {
		this.identidade = identidade;
	}

	public String getOrgaoExp() {
		return orgaoExp;
	}

	public void setOrgaoExp(String orgaoExp) {
		this.orgaoExp = orgaoExp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public String getTelResidencial() {
		return telResidencial;
	}

	public void setTelResidencial(String telResidencial) {
		this.telResidencial = telResidencial;
	}

	public String getTelCelular() {
		return telCelular;
	}

	public void setTelCelular(String telCelular) {
		this.telCelular = telCelular;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(String ocupacao) {
		this.ocupacao = ocupacao;
	}

	public String getNumRegistroProfissional() {
		return numRegistroProfissional;
	}

	public void setNumRegistroProfissional(String numRegistroProfissional) {
		this.numRegistroProfissional = numRegistroProfissional;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Aluno [id=" + id + ", nome=" + nome + "]";
	}

}