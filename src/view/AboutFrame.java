package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class AboutFrame extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public AboutFrame() {
		setResizable(false);
		setTitle("Acerca de Alquimia");
		setType(Type.UTILITY);
		
		setBounds(100, 100, 338, 332);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnClose = new JButton("Cerrar");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutFrame.this.dispose();
			}
		});
		btnClose.setBounds(233, 274, 89, 23);
		contentPane.add(btnClose);
		
		JTextPane txtpnEstaVersinDe = new JTextPane();
		txtpnEstaVersinDe.setEditable(false);
		txtpnEstaVersinDe.setBackground(Color.PINK);
		txtpnEstaVersinDe.setText("Esta versi\u00F3n de Alquimia est\u00E1 basada en una idea original de Andrey Zaikin. La versi\u00F3n original est\u00E1 disponible s\u00F3lo para iPhone y Android, si desea obtenerla puede hacerlo en http://zed.0xff.me/alchemy.");
		txtpnEstaVersinDe.setBounds(10, 191, 312, 72);
		contentPane.add(txtpnEstaVersinDe);
		
		JLabel lblAlquimiaParaWindows = new JLabel("Alquimia para Windows");
		lblAlquimiaParaWindows.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAlquimiaParaWindows.setBounds(10, 11, 312, 14);
		contentPane.add(lblAlquimiaParaWindows);
		
		JLabel lblNewLabel = new JLabel("v1.0 Beta");
		lblNewLabel.setBounds(10, 25, 312, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Alex Fuentes Palmero");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 70, 312, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblAlexFuentesPalmero = new JLabel("Carlos Casas Sancesario");
		lblAlexFuentesPalmero.setHorizontalAlignment(SwingConstants.CENTER);
		lblAlexFuentesPalmero.setBounds(10, 86, 312, 14);
		contentPane.add(lblAlexFuentesPalmero);
		
		JLabel lblJoaqunPinillosLa = new JLabel("Day\u00E1n San Miguel Labrada");
		lblJoaqunPinillosLa.setHorizontalAlignment(SwingConstants.CENTER);
		lblJoaqunPinillosLa.setBounds(10, 104, 312, 14);
		contentPane.add(lblJoaqunPinillosLa);
		
		JLabel lblDaynSanMiguel = new JLabel("F\u00E9lix Ulloa Pe\u00F1a");
		lblDaynSanMiguel.setHorizontalAlignment(SwingConstants.CENTER);
		lblDaynSanMiguel.setBounds(10, 139, 312, 14);
		contentPane.add(lblDaynSanMiguel);
		
		JLabel lblFlixUlloaPea = new JLabel("Joaqu\u00EDn Pinillos La Rosa");
		lblFlixUlloaPea.setHorizontalAlignment(SwingConstants.CENTER);
		lblFlixUlloaPea.setBounds(10, 156, 312, 14);
		contentPane.add(lblFlixUlloaPea);
		
		JLabel lblNewLabel_2 = new JLabel("Diana Rodriguez Colli");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(10, 121, 312, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblEquipoDeDesarrollo = new JLabel("Equipo de desarrollo del grupo 16:");
		lblEquipoDeDesarrollo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEquipoDeDesarrollo.setHorizontalAlignment(SwingConstants.CENTER);
		lblEquipoDeDesarrollo.setBounds(10, 50, 312, 14);
		contentPane.add(lblEquipoDeDesarrollo);
		
		JLabel lblNewLabel_3 = new JLabel("21 de marzo del 2013");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 8));
		lblNewLabel_3.setBounds(10, 263, 213, 14);
		contentPane.add(lblNewLabel_3);
		setLocationRelativeTo(null);
	}
}
