package co.edu.uniquindio.software3.proyecto.UI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import co.edu.uniquindio.software3.proyecto.OrcidScraper.Orcid;

public class VentanaPrincipalOrcid extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtURL;
	Orcid os = new Orcid();
	String url;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipalOrcid frame = new VentanaPrincipalOrcid();
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
	public VentanaPrincipalOrcid() {
		setTitle("ResearchScraper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 250);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPrincipal = new JLabel("Ingrese un ORCID URL:");
		lblPrincipal.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPrincipal.setBounds(102, 39, 190, 39);
		contentPane.add(lblPrincipal);

		txtURL = new JTextField();
		txtURL.setBounds(30, 89, 331, 28);
		contentPane.add(txtURL);
		txtURL.setColumns(10);

		JButton btnExtraer = new JButton("Extraer Información");
		btnExtraer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setUrl(txtURL.getText());
				if (!txtURL.getText().contains("orcid")) {
					JOptionPane.showMessageDialog(contentPane, "Debe ingresar una ORCID URL válida.");
				} else {
					dispose();
					VentanaReporteOrcid vr = new VentanaReporteOrcid(txtURL.getText());
					vr.setLocationRelativeTo(null);
					vr.setVisible(true);
				}
			}
		});
		btnExtraer.setBounds(102, 149, 190, 23);
		contentPane.add(btnExtraer);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
