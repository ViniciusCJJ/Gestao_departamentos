package br.com.fatecmogidascruzes.pootarde.folhapagamento.teste;

import java.sql.SQLException;
import java.util.List;

import br.com.fatecmogidascruzes.pootarde.folhapagamento.Cargo;

public class TesteCargoBD {

	public static void main(String[] args) {

		try {
//			salvar("Analista programador", 9000);
//			salvar("Gerente", 12000);
//			salvar("QA", 7000);

			List<Cargo> cargos = paginar();
			for (Cargo cargo : cargos) {
				System.out.printf("%d     %30s     %.2f\n", cargo.getId(), cargo.getTitulo(), cargo.getSalario());
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro ao tentar manipular cargos no banco de dados");
			e.printStackTrace();
		}

	}

	public static List<Cargo> paginar() throws SQLException {
		return Cargo.paginar(0, 10);
	}

	public static void salvar(String titulo, double salario) throws SQLException {
		Cargo cargo = new Cargo(titulo, salario);
		cargo.salvar();
	}

}
