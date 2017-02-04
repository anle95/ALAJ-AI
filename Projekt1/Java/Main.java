import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
	    GameState game = new GameState();
	    Scanner s = new Scanner(System.in);
	    
	    System.out.println("Enter your color (X (Black) or O (White)): ");
	    String player = s.next();
	    while (!player.toUpperCase().equals("X") && !player.toUpperCase().equals("O")) {
	    	System.out.println("Invalid choice, please choose X or O (the letter, not zero).");
	    	player = s.next();
	    }
	    Player p = player.toUpperCase().equals("X") ? Player.BLACK : Player.WHITE;
	    
	    System.out.println("Enter computer time limit (in ms, a minimum of 20): ");
	    int time = 0;
	    try {
	    	time = s.nextInt();
	    	if(time < 20) { 
	    		s.close();
	    		throw new InputMismatchException();
	    	}
	    } catch (InputMismatchException e) {
	    	System.out.println("That is not a valid number.");
	    	System.exit(1);
	    }

	    System.out.println(game);
	    while (true) {
	        if (!game.anyViableMove()) break;

	        if (game.getCurrentPlayer() == p) {
	        	String move = s.next();
//	        	String move = AI.getMove(game, time);
//	            System.out.println("Computer " + game.getCurrentPlayer() + " plays -" + move + "-");
	            game.play(move);
	        } else {
	            String move = AI.getMove(game, time);
	            System.out.println("Computer " + game.getCurrentPlayer() + " plays -" + move + "-");
	            game.play(move);
	        }
	        System.out.println(game);
	    }
	    String winner = (game.blackDisks() > game.whiteDisks()) ? "Black" : "White";
	    System.out.println(winner + " wins!");
	    s.close();
	}
}
