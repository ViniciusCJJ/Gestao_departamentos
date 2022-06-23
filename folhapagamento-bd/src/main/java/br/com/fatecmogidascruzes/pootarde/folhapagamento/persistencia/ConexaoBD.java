package br.com.fatecmogidascruzes.pootarde.folhapagamento.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

	public static Connection getConexao() throws SQLException {
		return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xE", "c##vinicius", "12345");
	}

}
