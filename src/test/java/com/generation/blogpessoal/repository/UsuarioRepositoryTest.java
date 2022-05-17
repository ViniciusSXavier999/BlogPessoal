package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;

//indicando que a classe UsuarioRepositoryTest é uma classe de test, que vai rodar em uma porta 
//aleatoria a cada teste realizado
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

//cria uma instancia de testes, que define que o ciclo de vida do teste vai respeitar o 
//ciclo de vida da classe(será executado e resetado após o uso)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	// injeção de dependência do repository
	@Autowired
	private UsuarioRepository repository;           //começo

	// A anotação @BeforeAll indica que o método deve ser executado
	// uma única vez antes de todos os métodos da classe, para criar
	// algumas pré-condições necessárias para todos os testes (criar
	// objetos, por exemplo)

	@BeforeAll
	void start() {
		repository.save(new Usuario(0l, "vini", "xavier@gmail.com", "12345", "https://i.imgur.com/FETvs2O.jpg"
));// independente do tanto de usuario
																						// que formos criar e cadastrar,
																						// o padrão ira ser 0L, que
																						// significa id long.
		repository.save(new Usuario(0l, "Lebron", "king11@gmail.com", "54321", "https://i.imgur.com/FETvs2O.jpg"));

		repository.save(new Usuario(0l, "Kobe", "mamba021@gmail.com", "oine", "https://i.imgur.com/FETvs2O.jpg"  //meio
));
		repository.save(new Usuario(0l, "Pool", "nicpole@gmail.com", "12345", "https://i.imgur.com/FETvs2O.jpg"
));
	}

	@Test
	@DisplayName("Teste que retorna 1 usuario")
	public void retornaUmUsuario() {
		Optional<Usuario> usuario = repository.findByUsuario("xavier@gmail.com");
		assertTrue(usuario.get().getUsuario().equals("xavier@gmail.com"));

	}
	
	@Test
	@DisplayName("Retorna 3 usuarios")
	public void deveRetornarTresUsuarios() {

		/**
		 *  Executa o método findAllByNomeContainingIgnoreCase para buscar todos os usuarios cujo nome contenha
		 *  a palavra "Silva"
		 */
		List<Usuario> listaDeUsuarios = repository.findAllByNomeContainingIgnoreCase("Souza");

		/**
		 * Verifica se a afirmação: "É verdade que a busca retornou 3 usuarios, cujo nome possua a palavra Silva" 
		 * é verdadeira
		 * Se for verdadeira, o teste passa, senão o teste falha.
		 */
		assertEquals(3, listaDeUsuarios.size());

		/**
		 *  Verifica se a afirmação: "É verdade que a busca retornou na primeira posição da Lista o usuario 
		 * João da Silva" é verdadeira
		 * Se for verdadeira, o teste passa, senão o teste falha.
		 */
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Vinicius de Souza"));

		/**
		 *  Verifica se a afirmação: "É verdade que a busca retornou na segunda posição da Lista a usuaria 
		 * Manuela da Silva" é verdadeira
		 * Se for verdadeira, o teste passa, senão o teste falha.
		 */
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Nicole de Souza"));

		/**
		 *  Verifica se a afirmação: "É verdade que a busca retornou na primeira posição da Lista a usuaria 
		 * Adriana da Silva" é verdadeira
		 * Se for verdadeira, o teste passa, senão o teste falha.
		 */
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Heliane de Souza"));
		
	}
	

	
	//deleta todo o banco de dados e reseta ele, e deixa ele limpo para os proximos testes.
	@AfterAll
	public void end() {
		repository.deleteAll();                    //fim
	}


}
