package view;

import java.awt.Dimension;

import javax.swing.JFrame;

import view.SmartPanel;
import view.SmartPanel.SmartPanelListener;

public class SmartPanelTest {

	/**
	 * Test checking whether the Smart Panel is shown properly
	 * @param args
	 */
	public static void main(String ... args) {
		
		SmartPanel sp = new SmartPanel(0, new SmartPanelListener(){

			@Override
			public void changeNumThreads() {
				System.out.println("Number of threads changed");	
			}

			@Override
			public void changeTime() {
				System.out.println("Maximum time changed");			
			}

			@Override
			public void stopSearch() {
				System.out.println("Clicked stop");	
			}

			
		});
		
		JFrame j = new JFrame();
		j.add(sp);
		j.setSize(new Dimension(400, 100));
		j.setVisible(true);
	}
}

