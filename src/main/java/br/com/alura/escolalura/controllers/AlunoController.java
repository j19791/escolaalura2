package br.com.alura.escolalura.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.alura.escolalura.models.Aluno;
import br.com.alura.escolalura.repositories.AlunoRepository;



@Controller
public class AlunoController {
	
	@Autowired
	private AlunoRepository repositorio;
	


	@GetMapping("aluno/cadastrar")
	public String cadastrar(Model model) {
		
	model.addAttribute("aluno", new Aluno());//enviaremos o objeto aluno para ser utilizado na view formulário.
		
		return "aluno/cadastrar";
		
	}
	
	
	@PostMapping("/aluno/salvar") //metodo recebera requisições do tipo post
	public String salvar(@ModelAttribute Aluno aluno){//para recebermos o objeto aluno enviado pelo formulário
	  System.out.println(aluno);
		repositorio.salvar(aluno);
	  return "redirect:/";//redirecionará  a pagina principal da app
	}
	
	
	@GetMapping("/aluno/listar")
	public String listar(Model model){//model para enviar objetos para a view 
	  List<Aluno> alunos = repositorio.obterTodosAlunos();
	  model.addAttribute("alunos", alunos);
	  return "aluno/listar";
	}
	
	@GetMapping("/aluno/visualizar/{id}")
	public String visualizar(@PathVariable String id, Model model) {
	  Aluno aluno = repositorio.obterAlunoPor(id);
	  model.addAttribute("aluno", aluno);

	  return "aluno/visualizar";
	}
	
}
