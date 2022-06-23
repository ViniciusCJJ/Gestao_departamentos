package br.com.fatecmogidascruzes.pootarde.folhapagamento;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

public class MenuCargos {

	public static void exibir(int tamanhoPagina) {
		int paginaAtual = 0;
		while (true) {
			int maximoPaginas = 1;
			try {
				maximoPaginas = (int) Math.ceil(Cargo.contarCargos() / (double) tamanhoPagina);
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

			String menuCargos = criarTextoMenu(tamanhoPagina, paginaAtual, maximoPaginas);

			boolean retornarMenuPrincipal = false;
			String opcaoMenuCargos = JOptionPane.showInputDialog(menuCargos);
			switch (opcaoMenuCargos.trim()) {
			case "1":
				exibirOpcaoNovo();
				break;
			case "2":
				exibirOpcaoExcluir();
				break;
			case "3":
				exibirOpcaoAlterar();
				break;
			case "4":
				if (paginaAtual > 0)
					paginaAtual--;
				break;
			case "5":
				paginaAtual++;
				break;
			case "6":
				retornarMenuPrincipal = true;
				break;
			default:
				JOptionPane.showMessageDialog(null, "Op��o inv�lida! Tente novamente.");
			}

			if (retornarMenuPrincipal) {
				break;
			}
		}
	}

	private static void exibirOpcaoAlterar() {
		String idStr = JOptionPane.showInputDialog("Informe o id do cargo que deseja alterar");
		Cargo cargo = null;
		try {
			cargo = Cargo.consultarPorId(Long.valueOf(idStr));
		} catch (NumberFormatException | SQLException e) {
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel encontrar este cargo no momento");
			e.printStackTrace();
		}
		if (null == cargo) {
			JOptionPane.showMessageDialog(null, "N�o encontramos este cargo");
		} else {

			int opcaoEscolhida = JOptionPane.showConfirmDialog(null,
					"Voc� tem certeza que deseja alterar o cargo " + cargo.getTitulo() + " (" + cargo.getId() + ")?");
			if (opcaoEscolhida == JOptionPane.YES_OPTION) {
				try {
					String titulo = JOptionPane.showInputDialog("Informe o t�tulo do cargo", cargo.getTitulo());
					String salarioStr = JOptionPane.showInputDialog("Informe o sal�rio do cargo", cargo.getSalario());

					cargo.setTitulo(titulo);
					cargo.setSalario(Double.valueOf(salarioStr));
					cargo.atualizar();
					JOptionPane.showMessageDialog(null, "O cargo foi atualizado com sucesso!");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "N�o foi poss�vel atualizar este cargo no momento");
					e.printStackTrace();
				}
			}
		}
	}

	private static void exibirOpcaoNovo() {
		String titulo = JOptionPane.showInputDialog("Informe o t�tulo do cargo");
		String salarioStr = JOptionPane.showInputDialog("Informe o sal�rio do cargo");

		Cargo novoCargo = new Cargo(titulo, Double.valueOf(salarioStr));
		try {
			novoCargo.salvar();
			JOptionPane.showMessageDialog(null, "O cargo foi gravado com sucesso!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel salvar o cargo no momento.");
			e.printStackTrace();
		}
	}

	private static String criarTextoMenu(int tamanhoPagina, int paginaAtual, int maximoPaginas) {
		// @formatter:off
		String menuCargos = "Cargos atuais (p�gina " + (paginaAtual + 1) + " de " + maximoPaginas + "):\n";
		
		List<Cargo> cargosAtuais;
		try {
			cargosAtuais = Cargo.paginar(paginaAtual * tamanhoPagina, tamanhoPagina);
			for(Cargo cargo: cargosAtuais) {
				menuCargos += String.format("%d:  %s  %.2f\n", cargo.getId(), cargo.getTitulo(), cargo.getSalario());
			}
		} catch (SQLException e1) {
			menuCargos += "N�o foi poss�vel obter os cargos atuais no momento\n";
			e1.printStackTrace();
		}
		
		menuCargos += "\n\nEscolha uma das op��es seguintes:\n" + 
						"1. Inserir novo\n" +
						"2. Excluir\n" +
						"3. Alterar\n" +
						(paginaAtual == 0 ? "-. N�o existe p�gina anterior\n" : "4. P�gina anterior\n") +
						(paginaAtual+1 == maximoPaginas ? "-. N�o existe pr�xima p�gina\n" : "5. Pr�xima p�gina\n") +
						"6. Voltar ao menu principal";
		// @formatter:on
		return menuCargos;
	}

	private static void exibirOpcaoExcluir() {
		String idStr = JOptionPane.showInputDialog("Informe o id do cargo que deseja excluir");
		Cargo cargo = null;
		try {
			cargo = Cargo.consultarPorId(Long.valueOf(idStr));
		} catch (NumberFormatException | SQLException e) {
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel encontrar este cargo no momento");
			e.printStackTrace();
		}
		if (null == cargo) {
			JOptionPane.showMessageDialog(null, "N�o encontramos este cargo");
		} else {
			int opcaoEscolhida = JOptionPane.showConfirmDialog(null,
					"Você tem certeza que deseja apagar o cargo " + cargo.getTitulo() + " (" + cargo.getId() + ")?");
			if (opcaoEscolhida == JOptionPane.YES_OPTION) {
				try {
					cargo.excluir();
					JOptionPane.showMessageDialog(null, "O cargo foi excluído com sucesso!");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "N�o foi poss�vel excluir este cargo no momento");
					e.printStackTrace();
				}
			}
		}
	}

}
