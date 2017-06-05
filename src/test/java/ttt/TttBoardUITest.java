package ttt;

import java.awt.Dimension;

import javax.swing.JFrame;

import view.BoardUI.BoardListener;
import view.ColorTableUI;

public class TttBoardUITest {

	/**
	 * Test checking whether the Tic Tac Toe board is shown properly
	 * @param args
	 */
	public static void main(String ... args) {
		TttBoardUI b = new TttBoardUI(0, new ColorTableUI().new ColorModel(2), new TttState(3), new BoardListener<TttState, TttAction>() {

			@Override
			public void makeManualMove(TttAction a) {
				System.out.println("Clicked on " + a.getRow() + ',' + a.getCol());
			}

			@Override
			public void sendMessage(String s) {
				System.out.println("Sent message: " + s);
			}			
		});
		
		JFrame j = new JFrame();
		j.add(b);
		j.setSize(new Dimension(500, 500));
		j.setVisible(true);
	}
	
}
