package br.com.alura.escolalura.repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import br.com.alura.escolalura.codecs.AlunoCodec;
import br.com.alura.escolalura.models.Aluno;

@Repository
public class AlunoRepository {

	MongoDatabase bancoDeDados;
	
	MongoCollection<Aluno> alunos;
	
	MongoClient cliente;
	
	public void salvar(Aluno aluno){
	    
	    criarConexao();
	    
	    
	    //validação para não criar alunos duplicados
	    if(aluno.getId() == null){
	        alunos.insertOne(aluno);
	      }else{
	        alunos.updateOne(Filters.eq("_id", aluno.getId()), new Document("$set", aluno));
	      }
	    
	    fecharConexao();
	  }
	
	public List<Aluno> obterTodosAlunos(){
		  criarConexao();		  

		  MongoCursor<Aluno> resultados = alunos.find().iterator();

		  List<Aluno> alunosEncontrados = popularAlunos(resultados);

		  fecharConexao();
		  
		  return alunosEncontrados;
		}

	private void criarConexao() {
		Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
		  AlunoCodec alunoCodec = new AlunoCodec(codec);

		  CodecRegistry registro = CodecRegistries.fromRegistries(
		      MongoClient.getDefaultCodecRegistry(), 
		      CodecRegistries.fromCodecs(alunoCodec));

		  MongoClientOptions options = MongoClientOptions.builder().codecRegistry(registro).build();

		  this.cliente = new MongoClient("localhost:27017", options);
		  this.bancoDeDados = cliente.getDatabase("test");
		  
		  this.alunos = bancoDeDados.getCollection("alunos", Aluno.class);//estamos deixando o tipo explícito, e queremos uma coleção de Aluno. é preciso criar este codec para que o Mongo consiga traduzir um documento "aluno", para um objeto aluno
		
		  
		  
	}

	public Aluno obterAlunoPor(String id) {
		criarConexao();
		  //MongoCollection<Aluno> alunos = this.bancoDeDados.getCollection("alunos", Aluno.class);
		  //além de fazer a busca com o filtro, estamos retornando apenas seu primeiro resultado.
		  Aluno aluno = alunos.find(Filters.eq("_id", new ObjectId(id))).first();
		  
		  fecharConexao();
		  
		  return aluno;
	}

	public List<Aluno> pesquisarPor(String nome) {
		criarConexao();

		//Estamos filtrando a coleção com base no nome recebido como argumento, a partir do qual recuperamos um iterador.

		  MongoCursor<Aluno> resultados = alunos.find(Filters.eq("nome", nome), Aluno.class).iterator();
		  
		  
		  List<Aluno> alunos = popularAlunos(resultados);

		  fecharConexao();

		  return alunos;
	}
	
	private List<Aluno> popularAlunos(MongoCursor<Aluno> resultados) {
		  List<Aluno> alunos = new ArrayList<>();

		  while(resultados.hasNext()) {
		    alunos.add(resultados.next());
		  }
		  return alunos;
		}
	
	private void fecharConexao() {
		  this.cliente.close();
		}

	public List<Aluno> pesquisarPor(String classificacao, Double nota) {
		criarConexao();
		  //MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("alunos", Aluno.class);
		  MongoCursor<Aluno> resultados = null;

		  if(classificacao.equals("reprovados")) {
		    resultados = alunos.find(Filters.lt("notas", nota)).iterator();
		  }else if(classificacao.equals("aprovados")) {
		    resultados = alunos.find(Filters.gte("notas", nota)).iterator();
		  }

		  List<Aluno> alunos = popularAlunos(resultados);

		  fecharConexao();

		  return alunos;
	}

	public List<Aluno> pesquisaPorGeolocalizacao(Aluno aluno) {
	    criarConexao();
	    //MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("alunos", Aluno.class);
	    alunos.createIndex(Indexes.geo2dsphere("contato"));

	    List<Double> coordinates = aluno.getContato().getCoordinates();
	    Point pontoReferencia = new Point(new Position(coordinates.get(0), coordinates.get(1)));

	    MongoCursor<Aluno> resultados = alunos.find(Filters.nearSphere("contato", 
	    		pontoReferencia, 2000.0, 0.0))
	    		.limit(2) //limitamos o número de alunos próximos a 2  
	    		.skip(1) //e pulamos o primeiro deles, já que aluno mais próximo de você é você mesmo!
	    		.iterator();
	    List<Aluno> alunos = popularAlunos(resultados);

	    fecharConexao();
	    return alunos;
	}


	
}
