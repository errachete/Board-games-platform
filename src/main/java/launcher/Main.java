package launcher;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import javax.swing.SwingUtilities;

import base.console.ConsolePlayer;
import base.model.GameAction;
import base.model.GamePlayer;
import base.model.GameState;
import base.player.RandomPlayer;
import base.player.SmartPlayer;
import mvc.GameTable;
import reversi.ReversiAction;
import reversi.ReversiPlayerUI;
import reversi.ReversiState;
import ttt.TttAction;
import ttt.TttPlayerUI;
import ttt.TttState;
import view.ConsoleController;
import view.ConsoleView;
import was.WasAction;
import was.WasPlayerUI;
import was.WasState;

/**
 * Main class of the application. It's the launcher of the game.
 * @author claudiaggh & errachete
 * @version 2 (03/05/2017)
 */
public class Main {

	/**
	 * Constant strings which contains the error messages
	 */
	final static String NotArguments = "Error: number of arguments not suitable";
	final static String WrongGame = "Error: game not available";
	final static String WrongPlayersNumber = "Error: number of players not suitable";

	/**
	 * Array with names to be chosen for the players.
	 */
	private static String[] people = new String[] { "Ana", "Berto", "Carlos",
			"Daniela", "Evaristo", "Fátima", "Gloria", "Hilario", "Isabel",
			"Juan", "Katia", "Luis", "María", "Norberto", "Olga", "Pedro",
			"Quirico", "Raúl", "Sara", "Teresa", "Úrsula", "Víctor", "William",
			"Xeno", "Yago", "Zacarías" };

	/**
	 * Method that returns an array of size n full with different names from the
	 * array "people".
	 * 
	 * @param n
	 *            Size of the array returned
	 * @return List of String which contains n different names from the array
	 *         "people"
	 */
	private static List<String> notRepNames(int n) {
		ArrayList<String> elegidos = new ArrayList<>(Arrays.asList(people));
		Collections.shuffle(elegidos);
		while (elegidos.size() > n)
			elegidos.remove(elegidos.size() - 1);
		return elegidos;
	}

	/**
	 * Method which starts and returns a new GameTable of the type given
	 * @param gType type of the game
	 * @return GameTable of the type given
	 */
	private static GameTable<?, ?> createGame(String gType) {
		switch (gType) {
		case "ttt":
			return new GameTable<TttState, TttAction>(new TttState(3)); // dimension
		case "was":
			return new GameTable<WasState, WasAction>(new WasState());
		case "reversi":
			return new GameTable<ReversiState, ReversiAction>(new ReversiState());
		}
		return null;
	}

	/**
	 * Initiates a game in console mode with players in the given modes
	 * @param game given to run
	 * @param playerModes of the players who are going to play
	 */
	private static <S extends GameState<S, A>, A extends GameAction<S, A>> void startConsoleMode(
			GameTable<S, A> game, String playerModes[]) {
		List<GamePlayer> players = new ArrayList<GamePlayer>();
		List<String> names = notRepNames(playerModes.length);
		for (int i = 0; i < playerModes.length; ++i) {
			if (playerModes[i].equals("manual")) {
				players.add(new ConsolePlayer(names.get(i), new Scanner(
						System.in)));
			} else if (playerModes[i].equals("rand")) {
				players.add(new RandomPlayer("Random" + names.get(i)));
			} else if (playerModes[i].equals("smart")) {
				players.add(new SmartPlayer("AI" + names.get(i), 20));
			} else {
				throw new IllegalArgumentException("player \"" + playerModes[i]
						+ "\" not defined");
			}
		}
		new ConsoleView<S, A>(game);
		ConsoleController<S, A> ctrl = new ConsoleController<S, A>(players, game);
		ctrl.run();
	}

	/**
	 * Initiates a game in gui mode with the players in the given modes
	 * @param gType type of the game which is going to be run
	 * @param game given to run
	 * @param playerModes of the players who are going to play
	 */
	private static <S extends GameState<S, A>, A extends GameAction<S, A>> void startGUIMode(
			String gType, GameTable<S, A> game, String playerModes[]) {

		List<GamePlayer> players = new ArrayList<GamePlayer>();
		List<String> names = notRepNames(playerModes.length);
		
		int screenSize = Toolkit.getDefaultToolkit().getScreenSize().width;
		int separator = screenSize / playerModes.length;

		SwingUtilities.invokeLater(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				if (gType.equals("ttt")) {
					for (int i = 0; i < playerModes.length; ++i) {
						players.add(new ConsolePlayer(names.get(i),
								new Scanner(System.in)));
						new TttPlayerUI((GameTable<TttState, TttAction>) game,
								players.get(i).getName(), i, i * separator);
					}
					game.start();
				} else if (gType.equals("was")){
					for (int i = 0; i < playerModes.length; ++i) {
						players.add(new ConsolePlayer(names.get(i),
								new Scanner(System.in)));
						new WasPlayerUI((GameTable<WasState, WasAction>) game,
								players.get(i).getName(), i, i * separator);
					}
					game.start();
				} else if (gType.equals("reversi")) {
					for (int i = 0; i < playerModes.length; ++i) {
						players.add(new ConsolePlayer(names.get(i),
								new Scanner(System.in)));
						new ReversiPlayerUI((GameTable<ReversiState, ReversiAction>) game,
								players.get(i).getName(), i, i * separator);
					}
					game.start();
				}
			}
		});

	}

	/**
	 * Shows an error message with the arguments through the console
	 */
	public static void usage() {
		System.out.println(NotArguments);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		
		Log.setupLogging(Level.INFO);
		
		if (args.length < 2) {
			usage();
			System.exit(1);
		}
		GameTable<?, ?> game = createGame(args[0]);
		if (game == null) {
			System.err.println(WrongGame);
			System.exit(1);
		}
		String[] playerModes = Arrays.copyOfRange(args, 2, args.length);
		if (game.getState().getPlayerCount() != playerModes.length) {
			System.err.println(WrongPlayersNumber);
			System.exit(1);
		}
		switch (args[1]) {
		case "console":
			startConsoleMode(game, playerModes);
			break;
		case "gui":
			startGUIMode(args[0], game, playerModes);
			break;
		default:
			System.err.println("Invalid view mode: " + args[1]);
			usage();
			System.exit(1);
		}
	}
}
