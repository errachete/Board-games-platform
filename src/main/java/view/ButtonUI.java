package view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonUI extends JButton {

	private static final long serialVersionUID = -2055819189010323522L;
	
	public ButtonUI(){}

	public ButtonUI(String image, String message, ActionListener listener) throws IOException, IllegalArgumentException{
		setIcon(new ImageIcon(ImageIO.read(getClass().getResource('/' + image + ".png"))));
		addActionListener(listener);
		setToolTipText(message);
		setPreferredSize(new Dimension(45, 45));
	}
}
