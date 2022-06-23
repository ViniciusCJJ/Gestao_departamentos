package br.com.fatecmogidascruzes.pootarde.folhapagamento;

import java.sql.SQLException;

import javax.swing.JOptionPane;

public class MenuPrincipal {

	public static void exibir(int tamanhoPagina) throws NumberFormatException, SQLException {
		// @formatter:off
		String menuPrincipal = "Escolha uma das opções seguintes:\n" + 
								"1. Gestão de cargos\n" +
								"2. Gestão de departamentos\n"+
								"3. Gestão de funcionários\n"+
								"4. Sair\n";
		// @formatter:on

		while (true) {
			String opcao = JOptionPane.showInputDialog(menuPrincipal);
			switch (opcao.trim()) {
			case "1":
				MenuCargos.exibir(tamanhoPagina);
				break;

			case "2":
				MenuDepartamento.exibir(tamanhoPagina);
				break;	
				
			case "3":
				MenuFuncionario.exibir(tamanhoPagina);
				break;	
				
			case "4":
				JOptionPane.showMessageDialog(null, "Obrigado por ter utilizado!");
				System.exit(-1);
			default:
				JOptionPane.showMessageDialog(null, "Opção inválida! Tente novamente.");
			}

		}
	}

}
