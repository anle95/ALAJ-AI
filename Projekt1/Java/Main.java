import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
	    //set up initial GameState
	    GameState game = new GameState();

	    Scanner s = new Scanner(System.in);
	    System.out.println("Enter your color (Black or White): ");
	    String player = s.next();
	    while (!player.equals("Black") && !player.equals("White")) {
	    	System.out.println("Invalid choice, please choose Black or White (mind the capital letters)");
	    	player = s.next();
	    }
	    System.out.println("Enter computer time limit (in seconds, a minimum of 0,02): ");
	    int time = 0;
	    try {
	    	time = (int) (s.nextFloat()*1000);
	    	if(time < 20) { 
	    		s.close();
	    		throw new InputMismatchException();
	    	}
	    } catch (InputMismatchException e) {
	    	System.out.println("That is not a valid number.");
	    	System.exit(1);
	    }

	    Player p = player.equals("Black") ? Player.BLACK : Player.WHITE;
	    game.print();
	    while (true) {
	        if (!game.anyViableMove()) break;

	        if (game.getCurrentPlayer() == p) {
	        	String move = s.next();
	            game.play(move);
	        } else {
	            String move = AI.getMove(game, time);
	            System.out.println("Computer plays -" + move + "-");
	            game.play(move);
	        }
	        game.print();
	    }
	    String winner = (game.blackDisks() > game.whiteDisks()) ? "Black" : "White";
	    System.out.println(winner + " wins!");
	    s.close();
	}
}
