package view;

import java.awt.Dimension;

import javax.swing.JFrame;

import view.InfoPanel;
import view.InfoPanel.InfoPanelListener;

public class InfoPanelTest {

	/**
	 * Test checking whether the Info Panel is shown properly
	 * @param args
	 */
	public static void main(String ... args) {
		
		InfoPanel rp = new InfoPanel(3, new ColorTableUI().new ColorModel(3), new InfoPanelListener(){

			public void changeColor() {
				System.out.println("Clicked ColorTable");
			}
			
		}
		, 0);
		
		JFrame j = new JFrame();
		j.add(rp);
		j.setSize(new Dimension(400, 600));
		j.setVisible(true);
		rp.addMessage("Hola");
	}
}

