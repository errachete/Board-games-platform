package was;

import java.awt.Dimension;

import javax.swing.JFrame;

import view.BoardUI.BoardListener;
import view.ColorTableUI;

public class WasBoardUITest {

	/**
	 * Test checking whether the Wolf and Sheep board is shown properly
	 * @param args
	 */
	public static void main(String ... args) {
		WasBoardUI b = new WasBoardUI(0, new ColorTableUI().new ColorModel(2), new WasState(), new BoardListener<WasState, WasAction>() {

			@Override
			public void makeManualMove(WasAction a) {
				System.out.println("Clicked on " + a.getEndPos());
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
