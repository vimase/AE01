package es.florida.gestorFicheros;

//import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JCheckBox;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtDirectorio;
	private JButton btnSeleccionarDirectorio;
	private JTextArea textArea;
	private JButton btnMostrarFicheros;
	private JScrollPane scrollPane;
	private JTextField txtBuscarCoincidencias;
	private JButton btnBuscarCoincidencias;
	private JCheckBox chckbxCoincidirAcentos;
	private JCheckBox chckbxCoincidirMayusMinus;
	private JTextField txtTextoReemplazar;
	private JButton btnReemplazar;
	
	public Vista() {
		setTitle("Gestor de ficheros");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 842, 567);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSeleccionarDirectorio = new JLabel("Selecciona directorio de trabajo:");
		lblSeleccionarDirectorio.setBounds(12, 17, 205, 16);
		contentPane.add(lblSeleccionarDirectorio);
		
		btnSeleccionarDirectorio = new JButton("Elegir directorio");
		btnSeleccionarDirectorio.setBounds(206, 13, 136, 25);
		contentPane.add(btnSeleccionarDirectorio);
		
		txtDirectorio = new JTextField();
		txtDirectorio.setBounds(354, 14, 310, 22);
		contentPane.add(txtDirectorio);
		txtDirectorio.setColumns(10);
		txtDirectorio.setText(System.getProperty("user.home"));
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 188, 802, 327);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Lucida Console", Font.PLAIN, 13));
		scrollPane.setViewportView(textArea);
		
		btnMostrarFicheros = new JButton("Mostrar ficheros");
		btnMostrarFicheros.setBounds(676, 13, 138, 25);
		contentPane.add(btnMostrarFicheros);
		
		JLabel lblBuscarEnArchivos = new JLabel("Buscar coincidencias en archivos:");
		lblBuscarEnArchivos.setBounds(126, 75, 205, 16);
		contentPane.add(lblBuscarEnArchivos);
		
		txtBuscarCoincidencias = new JTextField();
		txtBuscarCoincidencias.setBounds(334, 72, 235, 22);
		contentPane.add(txtBuscarCoincidencias);
		txtBuscarCoincidencias.setColumns(10);
		
		btnBuscarCoincidencias = new JButton("Buscar");
		btnBuscarCoincidencias.setBounds(579, 71, 109, 25);
		contentPane.add(btnBuscarCoincidencias);
		
		chckbxCoincidirMayusMinus = new JCheckBox("Coincidir mayúsculas y minúsculas");
		chckbxCoincidirMayusMinus.setBounds(126, 98, 227, 19);
		contentPane.add(chckbxCoincidirMayusMinus);
		
		chckbxCoincidirAcentos = new JCheckBox("Ignorar acentos");
		chckbxCoincidirAcentos.setSelected(true);
		chckbxCoincidirAcentos.setBounds(354, 99, 162, 16);
		contentPane.add(chckbxCoincidirAcentos);
		
		JLabel lblTextoReemplazar = new JLabel("Reemplazar por:");
		lblTextoReemplazar.setBounds(225, 143, 121, 25);
		contentPane.add(lblTextoReemplazar);
		
		txtTextoReemplazar = new JTextField();
		txtTextoReemplazar.setBounds(334, 144, 236, 22);
		contentPane.add(txtTextoReemplazar);
		txtTextoReemplazar.setColumns(10);
		
		btnReemplazar = new JButton("Reemplazar");
		btnReemplazar.setBounds(579, 143, 109, 25);
		contentPane.add(btnReemplazar);
		
		setVisible(true);
	}
	
	public JCheckBox getChckbxCoincidirAcentos() {
		return chckbxCoincidirAcentos;
	}

	public JButton getBtnReemplazar() {
		return btnReemplazar;
	}

	public JCheckBox getChckbxCoincidirMayusMinus() {
		return chckbxCoincidirMayusMinus;
	}
	
	public JTextField getTxtTextoReemplazar() {
		return txtTextoReemplazar;
	}

	public JButton getBtnBuscarCoincidencias() {
		return btnBuscarCoincidencias;
	}

	public JTextField getTxtBuscarCoincidencias() {
		return txtBuscarCoincidencias;
	}

	public JButton getBtnMostrarFicheros() {
		return btnMostrarFicheros;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public JButton getBtnSeleccionarDirectorio() {
		return btnSeleccionarDirectorio;
	}
	
	public JPanel getContentPane() {
		return contentPane;
	}

	public JTextField getTxtDirectorio() {
		return txtDirectorio;
	}
}
