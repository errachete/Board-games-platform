package view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ClockPanel extends JPanel {

	private static final long serialVersionUID = 1075837968263299139L;

	int id;
	boolean started;
	Thread clock;
	JButton start;
	JButton stop;
	JButton restart;
	Integer cont;
	JTextArea time;

	public ClockPanel(int id) {
		this.clock = new Thread(new Runnable() {

			@Override
			public void run() {
				while (started) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Logger.getLogger("log").severe("El reloj esta parado");
					}
					if (started) {
						++cont;
						time.setText(cont.toString());
					}
				}
			}

		});
		this.started = false;
		this.start = new JButton();
		this.stop = new JButton();
		this.restart = new JButton();
		this.cont = 0;
		this.time = new JTextArea(cont.toString());

		initGUI();

	}

	private void initGUI() {
		Border b = BorderFactory.createLineBorder(Color.BLACK, 2, false);
		try {
			this.start = new ButtonUI("start", "Starts the clock", (e) -> {
				Logger.getLogger("log").info(
						"Player " + id + " clicked start clock");
				start();
			});
			this.start.setPreferredSize(new Dimension(25,25));
			this.stop = new ButtonUI("pause", "Stops the clock", (e) -> {
				Logger.getLogger("log").info(
						"Player " + id + " clicked stop clock");
				stop();
			});
			this.stop.setPreferredSize(new Dimension(25,25));
			this.restart = new ButtonUI("rstclk", "Restarts the clock",
					(e) -> {
						Logger.getLogger("log").info(
								"Player " + id + " clicked restart clock");
						restart();
					});
			this.restart.setPreferredSize(new Dimension(25,25));
		} catch (IllegalArgumentException | IOException e) {
			Logger.getLogger("log").severe(
					"Los botones no se cargaron correctamente");
		}
		this.time.setEditable(false);

		JPanel buttons = new JPanel(new BorderLayout());
		buttons.setLayout(new FlowLayout());
		buttons.add(start);
		buttons.add(stop);
		buttons.add(restart);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(buttons);
		this.add(time);
		this.setBorder(BorderFactory.createTitledBorder(b, "Clock",
				TitledBorder.CENTER, TitledBorder.TOP));
	}

	public void start() {
		started = true;
		if (!clock.isAlive()) {
			clock = new Thread(new Runnable() {

				@Override
				public void run() {
					while (started) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							Logger.getLogger("log").severe(
									"El reloj esta parado");
						}
						if (started) {
							++cont;
							time.setText(cont.toString());
						}
					}
				}

			});
			clock.start();
		}
	}

	public void stop() {
		started = false;
	}

	public void restart() {
		cont = 0;
		time.setText(cont.toString());
	}

}
