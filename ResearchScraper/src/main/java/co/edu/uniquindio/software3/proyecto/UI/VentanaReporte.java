package co.edu.uniquindio.software3.proyecto.UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import co.edu.uniquindio.software3.proyecto.CvLacScraper.Articulo;
import co.edu.uniquindio.software3.proyecto.CvLacScraper.CvLac;
import co.edu.uniquindio.software3.proyecto.CvLacScraper.Investigador;

public class VentanaReporte extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	CvLac cv = new CvLac();

	/**
	 * Create the frame.
	 */
	public VentanaReporte(String url) {
		setTitle("Reporte CvLAC");
		cv.extraer(url);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 731, 539);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

//		DefaultCategoryDataset dcd = new DefaultCategoryDataset();
//		List<Investigador> lista = cv.getInvestigadores();
//		ArrayList<Articulo> listaProducciones = lista.get(0).getArticulos();
//
//		int anio = 2005;
//		while (anio < 2019) {
//			int cont = 0;
//			for (int i = 0; i < listaProducciones.size(); i++) {
//				if (Integer.parseInt(listaProducciones.get(i).getAnio()) == anio) {
//					cont++;
//				}
//			}
//			dcd.setValue(cont, "Publicaciones", anio + "");
//			anio++;
//		}

//		JPanel chart = new JPanel();
//		chart.setBackground(Color.WHITE);
//		chart.setBounds(10, 0, 697, 442);
//		contentPane.add(chart);
//
//		JFreeChart jchart = ChartFactory.createBarChart("Artículos Científicos por año (Según CvLAC)", "Año",
//				"Cantidad de publicaciones", dcd, PlotOrientation.VERTICAL, true, true, false);
//		CategoryPlot plot = jchart.getCategoryPlot();
//		plot.setRangeGridlinePaint(Color.BLACK);
//		ChartPanel chartPanel = new ChartPanel(jchart);
//		// contentPane.removeAll();
//
//		chartPanel.setLayout(null);
//		contentPane.updateUI();
//		setContentPane(contentPane);
//		contentPane.setLayout(null);
//		chart.add(chartPanel);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 453, 697, 42);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btnInicio = new JButton("Inicio");
		btnInicio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				VentanaInicio vi = new VentanaInicio();
				vi.setLocationRelativeTo(null);
				vi.setVisible(true);
			}
		});
		btnInicio.setBounds(599, 11, 77, 26);
		panel.add(btnInicio);

	}
}
