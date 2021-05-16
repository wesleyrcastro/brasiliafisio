package br.com.brasiliaFisio.cursos.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.brasiliaFisio.cursos.modelo.enumeration.NomeDoBanco;

@Entity
@Audited
@AuditTable(value = "CHEQUE_AUD")
@Table(name = "CHEQUE")
public class Cheque implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;

	@NotEmpty
	@Length(min = 3, max = 45, message = "O nome do proprietário deve ter entre {min} e {max} caracteres")
	@Column(name = "PROPRIETARIO_CHEQUE", length = 45, nullable = false)
	private String proprietarioCheque;

	@NotEmpty
	@Length(min = 1, message = "O número do cheque deve ter no mínimo {min} caracteres")
	@Column(name = "NUMERO_CHEQUE", length = 20, nullable = false)
	private String numCheque;

	@NotNull(message = "Valor obrigatório")
	@DecimalMin(value = "0.01")
	@Column(name = "VALOR", nullable = false, columnDefinition = "numeric(10,2)")
	private BigDecimal valor;

	@NotNull(message = "Data entrega obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_ENTREGA", nullable = false)
	private Calendar dataEntrega = Calendar.getInstance();

	@NotNull(message = "Data depósito obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_DEPOSITO", nullable = false)
	private Calendar dataDeposito = Calendar.getInstance();

	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	@Column(name = "OBS")
	private String obs;

	@NotNull(message = "Nome do banco obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name = "ID_NOME_BANCO", length = 20, nullable = false)
	private NomeDoBanco nomeDoBancoENumero;

	@NotEmpty
	@Length(min = 3, message = "O número da agência deve ter no mínimo {min} caracteres")
	@Column(name = "AGENCIA", length = 15, nullable = false)
	private String agencia;

	@NotEmpty
	@Length(min = 3, message = "O número da conta deve ter no mínimo {min} caracteres")
	@Column(name = "NUMERO_CONTA", length = 20, nullable = false)
	private String numConta;

	@NotNull(message = "Aluno obrigatório")
	@ManyToOne
	@JoinColumn(name = "ID_ALUNO", referencedColumnName = "ID", nullable = false)
	private Aluno aluno;

	@NotNull(message = "Curso obrigatório")
	@ManyToOne
	@JoinColumn(name = "ID_CURSO", referencedColumnName = "ID", nullable = false)
	private Curso curso;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProprietarioCheque() {
		return proprietarioCheque;
	}

	public void setProprietarioCheque(String proprietarioCheque) {
		this.proprietarioCheque = proprietarioCheque;
	}

	public String getNumCheque() {
		return numCheque;
	}

	public void setNumCheque(String numCheque) {
		this.numCheque = numCheque;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Calendar getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Calendar dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public Calendar getDataDeposito() {
		return dataDeposito;
	}

	public void setDataDeposito(Calendar dataDeposito) {
		this.dataDeposito = dataDeposito;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public NomeDoBanco getNomeDoBancoENumero() {
		return nomeDoBancoENumero;
	}

	public void setNomeDoBancoENumero(NomeDoBanco nomeDoBancoENumero) {
		this.nomeDoBancoENumero = nomeDoBancoENumero;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getNumConta() {
		return numConta;
	}

	public void setNumConta(String numConta) {
		this.numConta = numConta;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

}
