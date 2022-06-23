package br.com.fatecmogidascruzes.pootarde.folhapagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fatecmogidascruzes.pootarde.folhapagamento.persistencia.ConexaoBD;

public class Departamento {

	private Long id;
	private String nome;
	private List<Funcionario> funcionarios = new ArrayList<>();

	public Departamento(String nome) {
		this.nome = nome;
	}

	public Departamento(Long id,String nome) {
		this.nome = nome;
		this.id=id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int countFuncionarios() {
		return funcionarios.size();
	}

	public void addFuncionario(Funcionario funcionario) {
		if(null != funcionario.getDepartamento()) {
			funcionario.getDepartamento().removeFuncionario(funcionario);
		}
		
		funcionarios.add(funcionario);
		funcionario.setFuncionario(this, false);
	}

	public void removeFuncionario(Funcionario funcionario) {
		funcionarios.remove(funcionario);
		funcionario.setFuncionario(null,false);
	}
	
	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome=nome;
	}

	@Override
	public String toString() {
		return nome;
	}
	
	public void salvar() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("INSERT INTO departamento(nome) VALUES(?)");
			sql.setString(1, nome);
			sql.execute();
		} finally {
			conexao.close();
		}
	}

	public void excluir() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("DELETE FROM departamento WHERE id=?");
			sql.setLong(1, id);
			sql.execute();
		} finally {
			conexao.close();
		}
	}
	
	public void atualizar() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("UPDATE departamento SET nome=? WHERE id=?");
			sql.setString(1, nome);
			sql.setLong(2, id);
			sql.execute();
		} finally {
			conexao.close();
		}
	}
	
	public static Departamento consultarPorId(long id) throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("SELECT id, nome FROM departamento WHERE id=?");
			sql.setLong(1, id);
			ResultSet resultado = sql.executeQuery();

			if (resultado.next()) {
				// @formatter:off
				return new Departamento(
					resultado.getLong("id"),
					resultado.getString("nome")
					);
				// @formatter:on
			} else {
				return null;
			}
		} finally {
			conexao.close();
		}
	}
	
	public static int contarDepartamentos() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("SELECT COUNT(id) as quantidade FROM departamento");
			ResultSet resultado = sql.executeQuery();

			if (resultado.next()) {
				return resultado.getInt("quantidade");
			} else {
				return 0;
			}
		} finally {
			conexao.close();
		}
	}


public static List<Departamento> paginar(int inicial, int quantos) throws SQLException {
	Connection conexao = null;

	try {
		conexao = ConexaoBD.getConexao();
		//Aparentemente oracle não suporta LIMIT 
		PreparedStatement sql = conexao.prepareStatement("SELECT id, nome FROM departamento ORDER BY id ");
		//sql.setInt(1, quantos);
		//sql.setInt(2, inicial);
		ResultSet resultado = sql.executeQuery();

		List<Departamento> retorno = new ArrayList<>(quantos);
		while (resultado.next()) {
			// @formatter:off
			retorno.add(new Departamento(
					resultado.getLong("id"),
					resultado.getString("nome")
					));
			// @formatter:on
		}
		return retorno;
	} finally {
		conexao.close();
	}
}
}