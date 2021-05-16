package br.com.brasiliaFisio.cursos.modelo;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity
@Audited
@AuditTable(value = "TURMA_AUD")
@Table(name = "TURMA")
public class Turma implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;

	@NotNull(message = "Data início obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INICIO", nullable = false)
	private Calendar dataInicio = Calendar.getInstance();

	@NotNull(message = "Data fim obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FIM", nullable = false)
	private Calendar dataFim = Calendar.getInstance();

	@NotNull(message = "Curso obrigatório")
	@ManyToOne
	@JoinColumn(name = "ID_CURSO", referencedColumnName = "ID", nullable = false)
	private Curso curso;

	@NotNull(message = "Aluno obrigatório")
	@ManyToOne
	@JoinColumn(name = "ID_ALUNO", referencedColumnName = "ID", nullable = false)
	private Aluno aluno;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Calendar dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Calendar getDataFim() {
		return dataFim;
	}

	public void setDataFim(Calendar dataFim) {
		this.dataFim = dataFim;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

}
