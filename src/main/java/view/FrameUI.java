package view;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Class that creates a JFrame and initializes it in order to use it
 * @author claudiaggh & errachete
 * @version 2 (03/05/2017)
 */
public class FrameUI extends JFrame {

	private static final long serialVersionUID = 347350631824174741L;

	/**
	 * Constructor that given a title creates a JFrame with that name
	 * @param title Name
	 * @param position 
	 */
	public FrameUI(String title, int position) {
		super(title);
		initGUI(position);
	}
	
	/**
	 * Method that initializes the JFrame (give it a size, makes it resizable and visible...)
	 */
	private void initGUI(int position) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(775, 675);
		setLocation(position, 10);
		setMinimumSize(new Dimension(775, 675));
		setResizable(true);
		setVisible(true);
	}
	
	
	
}
