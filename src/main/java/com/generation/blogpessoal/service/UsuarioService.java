package com.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	// função para cadastrar um usuario
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		// primeiro valida se o usuário já existe no banco
		if (repository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty();

		// criptografo a senha do usuario caso não exista
		usuario.setSenha(criptografarSenha(usuario.getSenha()));

		// e por ultimo, salvo o usuario com a senha já criptografada no banco de dados
		return Optional.of(repository.save(usuario));
	}
	

	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		// procurar usuario por id
		if (repository.findById(usuario.getId()).isPresent()) {
			// criptografar a senha nova
			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			// retornar a senha cript
			return Optional.of(repository.save(usuario));
		}
		return Optional.empty();
	}
		


	public String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);
	};

	public Optional<UsuarioLogin> autenticaUsuario(Optional<UsuarioLogin> usuarioLogin) {
		Optional<Usuario> usuario = repository.findByUsuario(usuarioLogin.get().getUsuario());

		if (usuario.isPresent()) {
			if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {

				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get()
						.setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());

				return usuarioLogin;

			}
		}

		return Optional.empty();

	}

	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.matches(senhaDigitada, senhaBanco);

	}

	private String gerarBasicToken(String usuario, String senha) {

		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);

	}

}
