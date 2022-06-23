package br.com.fatecmogidascruzes.pootarde.folhapagamento;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

public class MenuDepartamento {

	public static void exibir(int tamanhoPagina) {
		int paginaAtual = 0;
		while (true) {
			int maximoPaginas = 1;
			try {
				maximoPaginas = (int) Math.ceil(Departamento.contarDepartamentos() / (double) tamanhoPagina);
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

			String menuDepartamento = criarTextoMenu(tamanhoPagina, paginaAtual, maximoPaginas);

			boolean retornarMenuPrincipal = false;
			String opcaoMenudepartamentos = JOptionPane.showInputDialog(menuDepartamento);
			switch (opcaoMenudepartamentos.trim()) {
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
	
	private static String criarTextoMenu(int tamanhoPagina, int paginaAtual, int maximoPaginas) {
		// @formatter:off
		String menuDepartamento = "Departamentos atuais (p�gina " + (paginaAtual + 1) + " de " + maximoPaginas + "):\n";
		
		List<Departamento> departamentosAtuais;
		try {
			departamentosAtuais = Departamento.paginar(paginaAtual * tamanhoPagina, tamanhoPagina);
			for(Departamento departamento: departamentosAtuais) {
				menuDepartamento += String.format("%d:  %s ", departamento.getId(), departamento.getNome()+"\n");
			}
		} catch (SQLException e1) {
			menuDepartamento += "N�o foi poss�vel obter os departamentos atuais no momento\n";
			e1.printStackTrace();
		}
		
		menuDepartamento += "\n\nEscolha uma das op��es seguintes:\n" + 
						"1. Inserir novo\n" +
						"2. Excluir\n" +
						"3. Alterar\n" +
						(paginaAtual == 0 ? "-. N�o existe p�gina anterior\n" : "4. P�gina anterior\n") +
						(paginaAtual+1 == maximoPaginas ? "-. N�o existe pr�xima p�gina\n" : "5. Pr�xima p�gina\n") +
						"6. Voltar ao menu principal";
		// @formatter:on
		return menuDepartamento;
	}
	
	private static void exibirOpcaoNovo() {
		String nome = JOptionPane.showInputDialog("Informe o nome do departamento");
	
		Departamento novoDepartamento = new Departamento(nome);
		try {
			novoDepartamento.salvar();
			JOptionPane.showMessageDialog(null, "O departamento foi gravado com sucesso!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel salvar o departamento no momento.");
			e.printStackTrace();
		}
	}
	
	private static void exibirOpcaoExcluir() {
		String idStr = JOptionPane.showInputDialog("Informe o id do departamento que deseja excluir");
		Departamento departamento = null;
		try {
			departamento = Departamento.consultarPorId(Long.valueOf(idStr));
		} catch (NumberFormatException | SQLException e) {
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel encontrar este departamento no momento");
			e.printStackTrace();
		}
		if (null == departamento) {
			JOptionPane.showMessageDialog(null, "N�o encontramos este departamento");
		} else {
			int opcaoEscolhida = JOptionPane.showConfirmDialog(null,
					"Voc� tem certeza que deseja apagar o departamento " + departamento.getNome() + " (" + departamento.getId() + ")?");
			if (opcaoEscolhida == JOptionPane.YES_OPTION) {
				try {
					departamento.excluir();
					JOptionPane.showMessageDialog(null, "O departamento foi excluído com sucesso!");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "N�o foi poss�vel excluir este departamento no momento");
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void exibirOpcaoAlterar() {
		String idStr = JOptionPane.showInputDialog("Informe o id do departamento que deseja alterar");
		Departamento departamento = null;
		try {
			departamento = Departamento.consultarPorId(Long.valueOf(idStr));
		} catch (NumberFormatException | SQLException e) {
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel encontrar este departamento no momento");
			e.printStackTrace();
		}
		if (null == departamento) {
			JOptionPane.showMessageDialog(null, "N�o encontramos este departamento");
		} else {

			int opcaoEscolhida = JOptionPane.showConfirmDialog(null,
					"Voc� tem certeza que deseja alterar o departamento " + departamento.getNome() + " (" + departamento.getId() + ")?");
			if (opcaoEscolhida == JOptionPane.YES_OPTION) {
				try {
					String nome = JOptionPane.showInputDialog("Informe o t�tulo do departamento", departamento.getNome());

					departamento.setNome(nome);
					departamento.atualizar();
					JOptionPane.showMessageDialog(null, "O departamento foi atualizado com sucesso!");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "N�o foi poss�vel atualizar este departamento no momento");
					e.printStackTrace();
				}
			}
		}
	}

}

