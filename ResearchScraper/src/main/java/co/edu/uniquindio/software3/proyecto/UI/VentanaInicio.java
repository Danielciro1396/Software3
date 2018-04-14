package co.edu.uniquindio.software3.proyecto.UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaInicio extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInicio frame = new VentanaInicio();
					 frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaInicio() {
		setTitle("Research Scraper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 479, 373);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblBienvenido = new JLabel("Bienvenido");
		lblBienvenido.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblBienvenido.setBounds(177, 30, 123, 24);
		contentPane.add(lblBienvenido);
		
		JLabel lblElijaSuFuente = new JLabel("Elija su fuente de información:");
		lblElijaSuFuente.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblElijaSuFuente.setBounds(139, 66, 199, 14);
		contentPane.add(lblElijaSuFuente);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				VentanaPrincipalOrcid vpo = new VentanaPrincipalOrcid();
				vpo.setLocationRelativeTo(null);
				vpo.setVisible(true);
				
			}
		});
		btnNewButton.setIcon(new ImageIcon(VentanaInicio.class.getResource("/imagenes/logo_orcid.jpg")));
		btnNewButton.setBounds(23, 128, 199, 181);
		contentPane.add(btnNewButton);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				VentanaPrincipal vp = new VentanaPrincipal();
				vp.setLocationRelativeTo(null);
				vp.setVisible(true);
			}
		});
		button.setIcon(new ImageIcon(VentanaInicio.class.getResource("/imagenes/logo_cvlac.jpg")));
		button.setBounds(243, 128, 199, 181);
		contentPane.add(button);
	}
}
