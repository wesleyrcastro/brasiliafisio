package br.com.brasiliaFisio.cursos.modelo;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import br.com.brasiliaFisio.cursos.modelo.enumeration.Indicacao;

@Entity
@Audited
@AuditTable(value = "INSCRICAO_AUD")
@Table(name = "INSCRICAO")
public class Inscricao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;

	@NotNull(message = "Termo obrigatório")
	@Column(name = "TERMO", nullable = false)
	private Boolean termo;

	@NotNull(message = "Aluno obrigatório")
	@ManyToOne
	@JoinColumn(name = "ID_ALUNO", referencedColumnName = "ID", nullable = false)
	private Aluno aluno;

	@NotNull(message = "Data obrigatótio")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA", nullable = false)
	private Calendar data = Calendar.getInstance();

	@NotNull(message = "Curso obrigatório")
	@ManyToOne
	@JoinColumn(name = "ID_CURSO", referencedColumnName = "ID", nullable = false)
	private Curso curso;

	@NotNull(message = "Como soube do curso. Obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name = "ID_INDICACAO", length = 25)
	private Indicacao indicacao;

	@Length(max = 45, message = "Outros deve ter no máximo {max} caracteres")
	@Column(name = "OUTROS", length = 45)
	private String outros;

	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	@Column(name = "OBS_ALUNO")
	private String obsAluno;

	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	@Column(name = "OBS_BRASILIAFISIO")
	private String obsBrasiliafisio;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getTermo() {
		return termo;
	}

	public void setTermo(Boolean termo) {
		this.termo = termo;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Indicacao getIndicacao() {
		return indicacao;
	}

	public void setIndicacao(Indicacao indicacao) {
		this.indicacao = indicacao;
	}

	public String getOutros() {
		return outros;
	}

	public void setOutros(String outros) {
		this.outros = outros;
	}

	public String getObsAluno() {
		return obsAluno;
	}

	public void setObsAluno(String obsAluno) {
		this.obsAluno = obsAluno;
	}

	public String getObsBrasiliafisio() {
		return obsBrasiliafisio;
	}

	public void setObsBrasiliafisio(String obsBrasiliafisio) {
		this.obsBrasiliafisio = obsBrasiliafisio;
	}

}
