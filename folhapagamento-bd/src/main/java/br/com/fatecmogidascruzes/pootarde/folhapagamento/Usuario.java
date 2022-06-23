package br.com.fatecmogidascruzes.pootarde.folhapagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.fatecmogidascruzes.pootarde.folhapagamento.persistencia.ConexaoBD;

public class Usuario {

	private Long id;
	private String login;
	private String senha;

	public Usuario(Long id, String login, String senha) {
		this.id = id;
		this.login = login;
		this.senha = senha;
	}

	public Usuario(String login, String senha) {
		this.login = login;
		this.senha = senha;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getSenha() {
		return senha;
	}

	public boolean validar() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			// JAMAIS!
			// Injeção de SQL - SQL Injection
//			PreparedStatement sql = conexao.prepareStatement(String
//			.format("SELECT id, login, senha FROM _usuario WHERE login='%s' and senha='%s'", login, senha));

			PreparedStatement sql = conexao
					.prepareStatement("SELECT id, login, senha FROM usuario WHERE login=? and senha=?");
			sql.setString(1, login);
			sql.setString(2, senha);
			ResultSet resultado = sql.executeQuery();

			return resultado.next();
		} finally {
			conexao.close();
		}
	}

}
