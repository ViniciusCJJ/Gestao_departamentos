package br.com.fatecmogidascruzes.pootarde.folhapagamento;

import java.awt.HeadlessException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Principal {

	private static final int TAMANHO_PAGINA = 5;

	public static void main(String[] args) {

		String login = JOptionPane.showInputDialog("Informe o seu usuário");
		String senha = JOptionPane.showInputDialog("Informe a sua senha");

		Usuario usuario = new Usuario(login, senha);
		try {
			if (usuario.validar()) {
				MenuPrincipal.exibir(TAMANHO_PAGINA);
			} else {
				JOptionPane.showMessageDialog(null, "Usuário inválido!");
			}
		} catch (HeadlessException | SQLException e) {
			e.printStackTrace();
		}

	}

}
