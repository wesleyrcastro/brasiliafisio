package br.com.brasiliaFisio.cursos.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Audited
@AuditTable(value = "DINHEIRO_AUD")
@Table(name = "DINHEIRO")
public class Dinheiro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;

	@NotNull(message = "Data obrigatótio")
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	private Calendar data = Calendar.getInstance();

	@DecimalMin(value = "0.01")
	@NotNull(message = "Valor obrigatório")
	@Column(name = "VALOR", nullable = false, columnDefinition = "numeric(10,2)")
	private BigDecimal valor;

	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	@Column(name = "OBS")
	private String obs;

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

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
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
