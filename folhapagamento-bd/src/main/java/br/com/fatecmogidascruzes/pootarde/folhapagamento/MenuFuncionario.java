package br.com.fatecmogidascruzes.pootarde.folhapagamento;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

public class MenuFuncionario {
	
	public static void exibir(int tamanhoPagina) throws NumberFormatException, SQLException {
		int paginaAtual = 0;
		while (true) {
			int maximoPaginas = 1;
			try {
				maximoPaginas = (int) Math.ceil(Funcionario.contarFuncionarios() / (double) tamanhoPagina);
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

			String menuFuncionario = criarTextoMenu(tamanhoPagina, paginaAtual, maximoPaginas);

			boolean retornarMenuPrincipal = false;
			String opcaoMenufuncionarios = JOptionPane.showInputDialog(menuFuncionario);
			switch (opcaoMenufuncionarios.trim()) {
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
		String menuFuncionario = "Funcionarios atuais (p�gina " + (paginaAtual + 1) + " de " + maximoPaginas + "):\n";
		
		List<Funcionario> funcionariosAtuais;
		try {
			funcionariosAtuais = Funcionario.paginar(paginaAtual * tamanhoPagina, tamanhoPagina);
			for(Funcionario funcionario: funcionariosAtuais) {
				menuFuncionario += String.format("%d:  %s  Departemento:%s", funcionario.getId(), funcionario.getNome(), funcionario.getNomeDep()+"\n");
			}
		} catch (SQLException e1) {
			menuFuncionario += "N�o foi poss�vel obter os funcionarios atuais no momento\n";
			e1.printStackTrace();
		}
		
		menuFuncionario += "\n\nEscolha uma das op��es seguintes:\n" + 
						"1. Inserir novo\n" +
						"2. Excluir\n" +
						"3. Alterar\n" +
						(paginaAtual == 0 ? "-. N�o existe p�gina anterior\n" : "4. P�gina anterior\n") +
						(paginaAtual+1 == maximoPaginas ? "-. N�o existe pr�xima p�gina\n" : "5. Pr�xima p�gina\n") +
						"6. Voltar ao menu principal";
		// @formatter:on
		return menuFuncionario;
	}

	
	private static void exibirOpcaoNovo() throws NumberFormatException, SQLException {
		String nome = JOptionPane.showInputDialog("Informe o nome do funcionario");
		String id = JOptionPane.showInputDialog("Informe o id do departamento");
		Funcionario novoFuncionario = new Funcionario(nome,(Long.valueOf(id)));
		try {
			novoFuncionario.salvar();
			JOptionPane.showMessageDialog(null, "O funcionario foi gravado com sucesso!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel salvar o funcionario no momento.");
			e.printStackTrace();
		}
	}
	
	private static void exibirOpcaoExcluir() {
		String idStr = JOptionPane.showInputDialog("Informe o id do funcionario que deseja excluir");
		Funcionario funcionario = null;
		try {
			funcionario = Funcionario.consultarPorId(Long.valueOf(idStr));
		} catch (NumberFormatException | SQLException e) {
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel encontrar este funcionario no momento");
			e.printStackTrace();
		}
		if (null == funcionario) {
			JOptionPane.showMessageDialog(null, "N�o encontramos este funcionario");
		} else {
			int opcaoEscolhida = JOptionPane.showConfirmDialog(null,
					"Voc� tem certeza que deseja apagar o funcionario " + funcionario.getNome() + " (" + funcionario.getId() + ")?");
			if (opcaoEscolhida == JOptionPane.YES_OPTION) {
				try {
					funcionario.excluir();
					JOptionPane.showMessageDialog(null, "O funcionario foi excluído com sucesso!");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "N�o foi poss�vel excluir este funcionario no momento");
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void exibirOpcaoAlterar() {
		String idStr = JOptionPane.showInputDialog("Informe o id do funcionario que deseja alterar");
		Funcionario funcionario = null;
		try {
			funcionario = Funcionario.consultarPorId(Long.valueOf(idStr));
		} catch (NumberFormatException | SQLException e) {
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel encontrar este funcionario no momento");
			e.printStackTrace();
		}
		if (null == funcionario) {
			JOptionPane.showMessageDialog(null, "N�o encontramos este funcionario");
		} else {

			int opcaoEscolhida = JOptionPane.showConfirmDialog(null,
					"Voc� tem certeza que deseja alterar o funcionario " + funcionario.getNome() + " (" + funcionario.getId() + ")?");
			if (opcaoEscolhida == JOptionPane.YES_OPTION) {
				try {
					String nome = JOptionPane.showInputDialog("Informe o nome do funcionario", funcionario.getNome());
					//String id_dep = JOptionPane.showInputDialog("Informe o departamento do funcionario", funcionario.getId_dep());
					funcionario.setNome(nome);
					//1funcionario.setId_dep(Long.valueOf(id_dep));
					funcionario.atualizar();
					JOptionPane.showMessageDialog(null, "O funcionario foi atualizado com sucesso!");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "N�o foi poss�vel atualizar este funcionario no momento");
					e.printStackTrace();
				}
			}
		}
	}

}
