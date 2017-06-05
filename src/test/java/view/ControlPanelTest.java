package view;

import java.awt.Dimension;

import javax.swing.JFrame;

import view.ControlPanel;
import view.ControlPanel.ControlPanelListener;
import view.PlayerUI.PlayerMode;

public class ControlPanelTest {
	
	/**
	 * Test checking whether the Control Panel is shown properly
	 * @param args
	 */
	public static void main(String ... args) {
		ControlPanel np = new ControlPanel(0, new ControlPanelListener() {

			@Override
			public void makeRandomMove() {
				System.out.println("Clicked random");
			}

			@Override
			public void makeSmartMove() {
				System.out.println("Clicked smart");
				
			}

			@Override
			public void restartGame() {
				System.out.println("Clicked restart");
				
			}

			@Override
			public void closeGame() {
				System.out.println("Clicked exit");
				
			}

			@Override
			public void changePlayerMode(PlayerMode p) {
				System.out.println("Mode changed to " + p);
			}

			@Override
			public void sendMessage(String s) {
				System.out.println("Sent message: " + s);
			}
			
		});
		
		JFrame j = new JFrame();
		j.add(np);
		j.setSize(new Dimension(400, 100));
		j.setVisible(true);
	}

}
