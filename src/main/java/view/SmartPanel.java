package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

/**
 * Smart panel of the window. It contains the configurations to the smart movements and
 * allows you change this configurations
 * @author claudiaggh & errachete
 * @version 3 (29/05/2017)
 */
public class SmartPanel extends JPanel{

	private static final long serialVersionUID = 2396384271986958886L;
	
	public interface SmartPanelListener{
		public void changeNumThreads();
		public void changeTime();
		public void stopSearch();
	}

	private int id;
	private JLabel brain;
	private SpinnerNumberModel numThreads;
	private JSpinner numThreadsSpin;
	private SpinnerNumberModel time;
	private JSpinner timeSpin;
	private ButtonUI stop;
	private SmartPanelListener listener;

	public SmartPanel(int id, SmartPanelListener listener){
		this.listener = listener;
		this.brain = new JLabel();
		this.numThreads = new SpinnerNumberModel(Runtime.getRuntime().availableProcessors(), 1, 
												Runtime.getRuntime().availableProcessors(), 1);
		this.time = new SpinnerNumberModel(5000, 500, 5000, 500);
		this.stop = new ButtonUI();
		
		
		initGUI();
	}
	
	/**
	 * It initializes the user's interface of the smartPanel
	 */
	private void initGUI() {
		// ThreadsPanel
		brain.setIcon(new ImageIcon("src/main/resources/brain.png"));
		brain.setOpaque(true);
		brain.setBackground(Color.BLACK);
		
		numThreadsSpin = new JSpinner(numThreads);
		numThreadsSpin.setToolTipText("Number of threads available to the SmartPlayer algorithm.");
		numThreadsSpin.addChangeListener((e) -> {
			Logger.getLogger("log").info("Player " + id + " changed threads to " + getThreads() + "ms");
			listener.changeNumThreads();
			});
		
		JLabel threadsText = new JLabel("threads");
		
		JPanel threadsPanel = new JPanel();
		threadsPanel.add(brain);
		threadsPanel.add(numThreadsSpin);
		threadsPanel.add(threadsText);
		
		// TimePanel
		JLabel clock = new JLabel();
		clock.setIcon(new ImageIcon("src/main/resources/timer.png"));
		
		timeSpin = new JSpinner(time);
		timeSpin.setToolTipText("Maximum time to make the smart movement.");
		timeSpin.addChangeListener((e) -> {
			Logger.getLogger("log").info("Player " + id + " changed time to " + getTime() + "ms");
			listener.changeTime();
		});
		
		JLabel timeText = new JLabel("ms.");
		
		JPanel timePanel = new JPanel();
		timePanel.add(clock);
		timePanel.add(timeSpin);
		timePanel.add(timeText);
		
		// Stop
		try {
			stop = new ButtonUI("stop", "Stop the current calculations to obtain a smart movement", (e) ->{
				Logger.getLogger("log").info("Player " + id + " clicked stop smart movement");
				listener.stopSearch();
			});
		} catch (IOException  | IllegalArgumentException e) {
			Logger.getLogger("log").severe("El boton no se cargo correctamente");
		}
		stop.setEnabled(false);
		
		// SmartPanel
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(threadsPanel);
		this.add(timePanel);
		this.add(stop);
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2, false),
				"Smart Moves", TitledBorder.CENTER, TitledBorder.TOP));
	}
	
	public int getThreads(){
		return numThreads.getNumber().intValue();
	}
	
	public int getTime(){
		return time.getNumber().intValue();
	}
	
	/**
	 * It changes the brain's color and enables or disables the stop button when necessary
	 */
	public void thinking(){
		if(brain.getBackground().equals(Color.BLACK)) {
			brain.setBackground(Color.YELLOW);
			numThreadsSpin.setEnabled(false);
			timeSpin.setEnabled(false);
			stop.setEnabled(true);
		} else {
			brain.setBackground(Color.BLACK);
			numThreadsSpin.setEnabled(true);
			timeSpin.setEnabled(true);
			stop.setEnabled(false);
		}
		repaint();
	}	
}
