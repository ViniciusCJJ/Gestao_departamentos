package br.com.fatecmogidascruzes.pootarde.folhapagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fatecmogidascruzes.pootarde.folhapagamento.persistencia.ConexaoBD;

public class Cargo {

	private Long id;
	private String titulo;
	private double salario;

	public Cargo(String titulo, double salario) {
		this.titulo = titulo;
		this.salario = salario;
	}

	public Cargo(Long id, String titulo, double salario) {
		this.id = id;
		this.titulo = titulo;
		this.salario = salario;
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}

	public void salvar() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("INSERT INTO cargo(titulo, salario) VALUES(?, ?)");
			sql.setString(1, titulo);
			sql.setDouble(2, salario);
			sql.execute();
		} finally {
			conexao.close();
		}
	}

	public void excluir() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("DELETE FROM cargo WHERE id=?");
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

			PreparedStatement sql = conexao.prepareStatement("UPDATE cargo SET titulo=?, salario=? WHERE id=?");
			sql.setString(1, titulo);
			sql.setDouble(2, salario);
			sql.setLong(3, id);
			sql.execute();
		} finally {
			conexao.close();
		}
	}

	public static Cargo consultarPorId(long id) throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("SELECT id, titulo, salario FROM cargo WHERE id=?");
			sql.setLong(1, id);
			ResultSet resultado = sql.executeQuery();

			if (resultado.next()) {
				// @formatter:off
				return new Cargo(
					resultado.getLong("id"),
					resultado.getString("titulo"),
					resultado.getDouble("salario")
					);
				// @formatter:on
			} else {
				return null;
			}
		} finally {
			conexao.close();
		}
	}

	public static List<Cargo> paginar(int inicial, int quantos) throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();
			//Aparentemente oracle não suporta LIMIT 
			PreparedStatement sql = conexao.prepareStatement("SELECT id, titulo, salario FROM cargo ORDER BY id ");
			//sql.setInt(1, quantos);
			//sql.setInt(2, inicial);
			ResultSet resultado = sql.executeQuery();

			List<Cargo> retorno = new ArrayList<>(quantos);
			while (resultado.next()) {
				// @formatter:off
				retorno.add(new Cargo(
						resultado.getLong("id"),
						resultado.getString("titulo"),
						resultado.getDouble("salario")
						));
				// @formatter:on
			}
			return retorno;
		} finally {
			conexao.close();
		}
	}

	public static int contarCargos() throws SQLException {
		Connection conexao = null;

		try {
			conexao = ConexaoBD.getConexao();

			PreparedStatement sql = conexao.prepareStatement("SELECT COUNT(id) as quantidade FROM cargo");
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

}
