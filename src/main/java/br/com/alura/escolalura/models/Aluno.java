package br.com.alura.escolalura.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

public class Aluno {
	private ObjectId id;//gerado automaticamente pelo MongoDB
	  private String nome;
	  
	  @DateTimeFormat(pattern = "yyyy-MM-dd")
	 
	  private Date dataNascimento;
	  private Curso curso;
	  public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	public List<Nota> getNotas() {
		  if(notas == null) {
		    notas = new ArrayList<Nota>();
		  }
		  return notas;
	}
	
	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}
	
	
	public List<Habilidade> getHabilidades() {
		//se o atributo de habilidades do aluno for nulo? Teremos uma exceção por que estaremos tentando adicionar uma habilidade ao nulo
		
		if(habilidades == null){
		    habilidades = new ArrayList<Habilidade>();
		  }
		  return habilidades;
		}
	
	
	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}
	private List<Nota> notas;
	  private List<Habilidade> habilidades;
	public Aluno criaId() {
		setId(new ObjectId());
		  return this;
	}


	public Aluno adiciona(Aluno aluno, Habilidade habilidade) {
		  List<Habilidade> habilidades = aluno.getHabilidades();//obter as habilidades ja cadastradas
		  habilidades.add(habilidade);//adicionar a nova habilidade
		  aluno.setHabilidades(habilidades);
		  return aluno;
		}
	
	public Aluno adicionar(Aluno aluno, Nota nota) {
	  List<Nota> notas = aluno.getNotas();
	  notas.add(nota);
	  aluno.setNotas(notas);
	  return aluno;
	}
	
	public Contato getContato() {
		return contato;
	}
	public void setContato(Contato contato) {
		this.contato = contato;
	}
	private Contato contato;
	
}
