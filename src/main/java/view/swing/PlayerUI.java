package view.swing;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import base.model.GameAction;
import base.model.GameState;
import base.player.ConcurrentAiPlayer;
import base.player.RandomPlayer;
import mvc.GameTable;
import view.swing.ControlPanel;
import view.swing.InfoPanel;
import view.swing.SmartPanel;
import view.swing.BoardUI.BoardListener;
import view.swing.ColorTableUI.ColorModel;
import view.swing.ControlPanel.ControlPanelListener;
import view.swing.InfoPanel.InfoPanelListener;
import view.swing.SmartPanel.SmartPanelListener;

/**
 * Class in charge of the visual components on GUI mode for a given player.
 * 
 * @author claudiaggh & errachete
 * @version 2 (03/05/2017)
 */
public abstract class PlayerUI<S extends GameState<S, A>, A extends GameAction<S, A>> {

	/**
	 * Enumerates the different posible player modes
	 */
	public enum PlayerMode {
		Smart, Random, Manual;
	}

	private GameTable<S, A> game;
	private RandomPlayer rPlayer;
	private ConcurrentAiPlayer sPlayer;
	private Thread smartMove;
	protected int id;
	private PlayerMode playerMode;

	private FrameUI jf;
	private InfoPanel iPanel;
	private ControlPanel cPanel;
	private SmartPanel sPanel;
	private ClockPanel kPanel;
	private BoardUI<S, A> board;

	/**
	 * The constructor with parameters creates a window and adds all the
	 * necessary components to play by means of calling other methods and
	 * creating objects from other classes.
	 * 
	 * @param game
	 *            A given GameTable of the game to play
	 * @param name
	 *            Name of the player
	 * @param id
	 *            Identifier number for the player
	 * @param position
	 */
	public PlayerUI(GameTable<S, A> game, String name, int id, int position) {

		S state = game.getState();
		ColorModel cm = new ColorTableUI().new ColorModel(
				state.getPlayerCount());

		this.id = id;
		this.game = game;
		this.rPlayer = new RandomPlayer(name);
		this.rPlayer.join(id);
		this.sPlayer = new ConcurrentAiPlayer(name);
		sPlayer.setTimeout(5000);
		sPlayer.setMaxThreads(Runtime.getRuntime().availableProcessors());
		this.smartMove = new Thread(new Runnable() {
			@Override
			public void run() {
				double inicio = System.currentTimeMillis();
				A a = sPlayer.requestAction(game.getState());
				final double tiempo = System.currentTimeMillis() - inicio;
				SwingUtilities.invokeLater(() -> {
					sPanel.thinking();
					board.nullSelected();
					if (!game.isStopped() && a != null
							&& !smartMove.isInterrupted()
							&& game.getState().isValid(a)) {
						iPanel.addMessage(sPlayer.getEvaluationCount()
								+ " nodes in " + tiempo + " ms ("
								+ (int) (sPlayer.getEvaluationCount() / tiempo)
								+ " n/ms) value = " + sPlayer.getValue());
						game.execute(a);
						if (!game.getState().isFinished())
							iPanel.addMessage("Turn of player " + (id + 1) % 2);
					}
				});
			}
		});
		;
		this.sPlayer.join(id);
		this.playerMode = PlayerMode.Manual;
		this.jf = createJFrame(game, name, position);
		this.iPanel = new InfoPanel(state.getPlayerCount(), cm,
				new InfoPanelListener() {
					public void changeColor() {
						jf.repaint();
					}
				}, id);
		this.cPanel = new ControlPanel(id, new ControlPanelListener() {
			public void makeRandomMove() {
				if (id == game.getState().getTurn()) {
					iPanel.addMessage("You have requested a random move.");
					randomMove();
				}
			}

			public void makeSmartMove() {
				if (id == game.getState().getTurn()) {
					iPanel.addMessage("You have requested a smart move.");
					smartMove();
				}
			}

			public void restartGame() {
				if (!game.isStopped())
					game.stop();
				game.start();
			}

			public void closeGame() {
				int answer = JOptionPane.showOptionDialog(new JFrame(),
						"Do you really want to exit?", "Confirm Exit",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null,
						new String[] { "Yes, I want to leave",
								"No, I'd rather stay" }, new String(
								"Yes, I want to leave"));
				if (answer == JOptionPane.YES_OPTION)
					System.exit(0);
			}

			public void changePlayerMode(PlayerMode p) {
				playerMode = p;
				autoMove();
			}

			public void sendMessage(String message) {
				iPanel.addMessage(message);
			}
		});
		this.sPanel = new SmartPanel(id, new SmartPanelListener() {

			@Override
			public void changeNumThreads() {
				sPlayer.setMaxThreads(sPanel.getThreads());
			}

			@Override
			public void changeTime() {
				sPlayer.setTimeout(sPanel.getTime());
			}

			@Override
			public void stopSearch() {
				smartMove.interrupt();
				iPanel.addMessage("You have stopped the smart movement.");
				if (playerMode != PlayerMode.Manual) {
					playerMode = PlayerMode.Manual;
					cPanel.changeManual();
				}
			}

		});
		this.kPanel = new ClockPanel(id);
		this.board = createBoard(id, cm, state, new BoardListener<S, A>() {
			public void makeManualMove(A a) {
				if (id == game.getState().getTurn()) {
					game.execute(a);
				}
			}

			@Override
			public void sendMessage(String message) {
				iPanel.addMessage(message);
			}
		});

		jf.add(board, BorderLayout.CENTER);
		JPanel auxEast = new JPanel(new BorderLayout());
		auxEast.add(kPanel, BorderLayout.NORTH);
		auxEast.add(iPanel, BorderLayout.CENTER);
		jf.add(auxEast, BorderLayout.EAST);
		JPanel auxNorth = new JPanel();
		auxNorth.add(cPanel, BorderLayout.WEST);
		auxNorth.add(sPanel, BorderLayout.EAST);
		jf.add(auxNorth, BorderLayout.NORTH);
		jf.setVisible(true);

		game.addObserver((e) -> {
			switch (e.getType()) {
			case Start:
				cPanel.enableButtons(e.getState().getTurn() == id);
				board.setState(e.getState());
				iPanel.clearMessages();
				iPanel.addMessage("The game has started.");
				if (e.getState().getTurn() == id && !e.getState().isFinished()) {
					iPanel.addMessage("It's your turn.");
					autoMove();
				}
				break;
			case Stop:
				smartMove.interrupt();
				cPanel.enableButtons(false);
				board.nullSelected();
				iPanel.addMessage("The game has ended.");
				int winner = e.getState().getWinner();
				if (winner == id)
					iPanel.addMessage("Congratulations. You have won.");
				else if (winner != -1)
					iPanel.addMessage("The winner is player "
							+ e.getState().getWinner() + '.');
				else
					iPanel.addMessage("It's a draw!");
				break;
			case Change:
				smartMove.interrupt();
				cPanel.enableButtons(e.getState().getTurn() == id);
				board.setState(e.getState());
				if (e.getState().getTurn() == id && !e.getState().isFinished()) {
					iPanel.addMessage("It's your turn.");
					autoMove();
				}
			case Error:
				break;
			default:
				if (e.getState().getTurn() == id && !e.getState().isFinished())
					autoMove();
				break;
			}
			jf.repaint();
		});
	}

	/**
	 * Makes a random or a smart move if player mode is the proper one in each
	 * case and it's the turn of the player
	 */
	private void autoMove() {
		if (game != null && game.getState().getTurn() == id
				&& !game.getState().isFinished()) {
			switch (playerMode) {
			case Random:
				randomMove();
				break;
			case Smart:
				smartMove();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Abstract method that given a gameTable with the parameters of the game
	 * which is going to be played and a name, creates a JFrame to held it
	 * 
	 * @param position
	 */
	public abstract FrameUI createJFrame(GameTable<S, A> ctrl, String name,
			int position);

	public abstract BoardUI<S, A> createBoard(int id, ColorModel cm, S s,
			BoardListener<S, A> listener);

	public PlayerMode getPlayerMode() {
		return playerMode;
	}

	public int getPlayerId() {
		return id;
	}

	/**
	 * Method that makes a random move
	 */
	public void randomMove() {
		A a = rPlayer.requestAction(game.getState());
		board.nullSelected();
		SwingUtilities.invokeLater(() -> {
			game.execute(a);
		});
		iPanel.addMessage("Turn of player " + (id + 1) % 2);
	}

	/**
	 * Method that makes a smart move
	 */
	public void smartMove() {
		sPanel.thinking();
		smartMove = new Thread(new Runnable() {
			@Override
			public void run() {
				double inicio = System.currentTimeMillis();
				A a = sPlayer.requestAction(game.getState());
				final double tiempo = System.currentTimeMillis() - inicio;
				SwingUtilities.invokeLater(() -> {
					sPanel.thinking();
					board.nullSelected();
					if (!game.isStopped() && a != null
							&& !smartMove.isInterrupted()
							&& game.getState().isValid(a)) {
						iPanel.addMessage(sPlayer.getEvaluationCount()
								+ " nodes in " + tiempo + " ms ("
								+ (int) (sPlayer.getEvaluationCount() / tiempo)
								+ " n/ms) value = " + sPlayer.getValue());
						game.execute(a);
						if (!game.getState().isFinished())
							iPanel.addMessage("Turn of player " + (id + 1) % 2);
					}
				});
			}
		});
		smartMove.start();
	}
}
