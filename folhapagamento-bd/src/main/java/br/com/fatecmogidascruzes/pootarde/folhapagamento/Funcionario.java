package br.com.fatecmogidascruzes.pootarde.folhapagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fatecmogidascruzes.pootarde.folhapagamento.persistencia.ConexaoBD;

public class Funcionario {

	private Long id;
	private Long id_dep;
	private String nome;
	private Departamento departamento;

	
	
	public Funcionario(String nome, Departamento departamento) {
		setNome(nome);
		setDepartamento(departamento);
	}
	
	public Funcionario(String nome,Long id_dep ) throws SQLException {
		setNome(nome);
		setId_dep(id_dep);
		Departamento dep=Departamento.consultarPorId(id_dep);
		setDepartamento(dep);
	}
	public Funcionario(Long id,String nome,Long id_dep) throws SQLException {
		setNome(nome);
		setId(id);
		Departamento dep=Departamento.consultarPorId(id_dep);
		setDepartamento(dep);
	}
	
	public Long getId_dep() {
		return id_dep;
	}

	public void setId_dep(Long id_dep) {
		this.id_dep = id_dep;
	}

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeDep() {
		return departamento.toString();
	}
	
	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		setFuncionario(departamento, true);
	}

	public void setFuncionario(Departamento departamento, boolean propagarAlteracao) {
		this.departamento = departamento;
		if (null == departamento && null != this.departamento) {
			this.departamento.removeFuncionario(this);
		} else if (propagarAlteracao && null != departamento)
			this.departamento.addFuncionario(this);
	}
	
	public void salvar() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("INSERT INTO funcionario(nome, id_dep) VALUES(?, ?)");
			sql.setString(1, nome);
			sql.setLong(2, id_dep);
			sql.execute();
		} finally {
			conexao.close();
		}
	}

	public void excluir() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();
			

			PreparedStatement sql = conexao.prepareStatement("DELETE FROM funcionario WHERE id=?");
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

			PreparedStatement sql = conexao.prepareStatement("UPDATE funcionario SET nome=? WHERE id=?");
			sql.setString(1, nome);
			sql.setLong(2, id);
			sql.execute();
		} finally {
			conexao.close();
		}
	}
	
	public static int contarFuncionarios() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("SELECT COUNT(id) as quantidade FROM funcionario");
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

	
	public static List<Funcionario> paginar(int inicial, int quantos) throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();
			//Aparentemente oracle não suporta LIMIT 
			PreparedStatement sql = conexao.prepareStatement("SELECT id, nome, id_dep FROM funcionario ORDER BY id ");
			//sql.setInt(1, quantos);
			//sql.setInt(2, inicial);
			ResultSet resultado = sql.executeQuery();

			List<Funcionario> retorno = new ArrayList<>(quantos);
			while (resultado.next()) {
				// @formatter:off
				retorno.add(new Funcionario(
						resultado.getLong("id"),
						resultado.getString("nome"),
						resultado.getLong("id_dep")
						));
				// @formatter:on
			}
			return retorno;
		} finally {
			conexao.close();
		}
	}
	
	public static Funcionario consultarPorId(long id) throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("SELECT id, nome, id_dep FROM funcionario WHERE id=?");
			sql.setLong(1, id);
			ResultSet resultado = sql.executeQuery();

			if (resultado.next()) {
				// @formatter:off
				return new Funcionario(
					resultado.getLong("id"),
					resultado.getString("nome"),
					resultado.getLong("id_dep")
					);
				// @formatter:on
			} else {
				return null;
			}
		} finally {
			conexao.close();
		}
	}
}
