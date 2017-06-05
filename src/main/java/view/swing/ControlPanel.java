package view.swing;

import java.awt.FlowLayout;
import java.io.IOException;
import java.util.logging.*;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.swing.PlayerUI.PlayerMode;

/**
 * North panel of the window. Contains four buttons and one comboBox to change
 * the mode
 * 
 * @author claudiaggh & errachete
 * @version 1 (03/05/2017)
 * @param <S>
 *            GameState of the game played
 * @param <A>
 *            GameAction of the game played
 */
public class ControlPanel extends JPanel {

	private static final long serialVersionUID = -8845489814772928993L;

	/**
	 * Listener to the actions made in this panel
	 * 
	 * @author Claudia Guerrero García-Heras and Rafael Herrera Troca
	 * @version 1 (03/05/2017)
	 */
	public interface ControlPanelListener {
		public void makeRandomMove();

		public void makeSmartMove();

		public void restartGame();

		public void closeGame();

		public void changePlayerMode(PlayerMode p);

		public void sendMessage(String s);
	}

	private ButtonUI bRandom;
	private ButtonUI bSmart;
	private ButtonUI bRestart;
	private ButtonUI bExit;
	private JComboBox<String> mode;
	private ControlPanelListener listener;
	private int id;

	public ControlPanel(int id, ControlPanelListener listener) {

		String modes[] = { "Manual", "Random", "Smart" };

		this.listener = listener;
		this.id = id;
		this.bRandom = new ButtonUI();
		this.bSmart = new ButtonUI();
		this.bRestart = new ButtonUI();
		this.bExit = new ButtonUI();

		this.mode = new JComboBox<String>(modes);

		initGUI();

	}

	/**
	 * Initializes the north panel with the default values
	 */
	private void initGUI() {
		// Buttons
		try {
			bRandom = new ButtonUI("dice", "Make random movement", (e) -> {
				Logger.getLogger("log").info("Player " + id + " clicked random move");
				listener.makeRandomMove();
			});
			bSmart = new ButtonUI("nerd", "Make smart movement", (e) -> {
				Logger.getLogger("log").info("Player " + id + " clicked smart move");
				listener.makeSmartMove();
			});
			bRestart = new ButtonUI("restart", "Restart game", (e) -> {
				Logger.getLogger("log").info("Player " + id + " clicked restart");
				listener.restartGame();
			});
			bExit = new ButtonUI("exit", "Close game", (e) -> {
				Logger.getLogger("log").info("Player " + id + " clicked exit");
				listener.closeGame();
			});
		} catch (IOException | IllegalArgumentException e) {
			Logger.getLogger("log").severe("Los botones no se cargaron correctamente");
		}

		JPanel buttons = new JPanel();
		buttons.add(bRandom);
		buttons.add(bSmart);
		buttons.add(bRestart);
		buttons.add(bExit);

		// Player Mode
		mode.setSelectedIndex(0);
		mode.addActionListener((e) -> {
			PlayerUI.PlayerMode pMode = null;
			switch (mode.getSelectedIndex()) {
			case 0:
				pMode = PlayerUI.PlayerMode.Manual;
				break;
			case 1:
				pMode = PlayerUI.PlayerMode.Random;
				break;
			case 2:
				pMode = PlayerUI.PlayerMode.Smart;
				break;
			}
			Logger.getLogger("log").info("Player " + id + " changed mode to "
					+ mode.getSelectedItem() + '.');
			listener.changePlayerMode(pMode);
			listener.sendMessage("You have changed to mode "
					+ mode.getSelectedItem() + '.');
		});

		JLabel modeText = new JLabel("Player Mode: ");

		JPanel plMode = new JPanel();
		plMode.add(modeText);
		plMode.add(mode);

		// North Panel
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(buttons);
		this.add(plMode);
	}

	public void changeManual() {
		mode.setSelectedIndex(0);
	}
	
	public void enableButtons(boolean enab){
		bRandom.setEnabled(enab);
		bSmart.setEnabled(enab);
	}
}