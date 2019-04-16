package br.com.alura.escolalura.repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import br.com.alura.escolalura.codecs.AlunoCodec;
import br.com.alura.escolalura.models.Aluno;

@Repository
public class AlunoRepository {

	MongoDatabase bancoDeDados;
	
	MongoCollection<Aluno> alunos;
	
	public void salvar(Aluno aluno){
	    
	    criarConexao();
	    
	    alunos.insertOne(aluno);//persiste o novo aluno
	  }
	
	public List<Aluno> obterTodosAlunos(){
		  criarConexao();		  

		  MongoCursor<Aluno> resultado = alunos.find().iterator();

		  List<Aluno> alunosEncontrados = new ArrayList<>();
		  while(resultado.hasNext()){
		    Aluno aluno = resultado.next();
		    alunosEncontrados.add(aluno);
		  }

		  return alunosEncontrados;
		}

	private void criarConexao() {
		Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
		  AlunoCodec alunoCodec = new AlunoCodec(codec);

		  CodecRegistry registro = CodecRegistries.fromRegistries(
		      MongoClient.getDefaultCodecRegistry(), 
		      CodecRegistries.fromCodecs(alunoCodec));

		  MongoClientOptions options = MongoClientOptions.builder().codecRegistry(registro).build();

		  MongoClient cliente = new MongoClient("localhost:27017", options);
		  this.bancoDeDados = cliente.getDatabase("test");
		  
		  this.alunos = bancoDeDados.getCollection("alunos", Aluno.class);//estamos deixando o tipo explícito, e queremos uma coleção de Aluno. é preciso criar este codec para que o Mongo consiga traduzir um documento "aluno", para um objeto aluno
		
	}
	
}
