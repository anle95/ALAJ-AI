import java.util.Scanner;

public class ReversiMain {
	public static void main(String[] args) {
	    //set up initial GameState
	    GameState Game = new GameState();

	    Scanner s = new Scanner(System.in);
	    System.out.println("Enter your color (Black or White): ");
	    String player = s.next();
	    while (!player.equals("Black") && !player.equals("White")) {
	    	System.out.println("Invalid choice, please choose Black or White (mind the capital letters)");
	    	player = s.next();
	    }

	    Player p = player.equals("Black") ? Player.BLACK : Player.WHITE;
	    Game.print();
	    while (true) {
	        if (!Game.anyViableMove()) break;

	        if (Game.getCurrentPlayer() == p) {
	            // minimax(&Game, 2, moveBuffer);
	            // printf("I suggest -%s-\n", moveBuffer);
	        	String move = s.next();
	            Game.play(move);
	            // printf("Computer plays -%s-\n", moveBuffer);
	            // play(&Game, moveBuffer);
	        } else {
	            String move = ReversiAI.minimax(Game, 2);
	            System.out.println("Computer plays -" + move + "-");
	            Game.play(move);
	        }
	        Game.print();
	    }
	    String winner = (Game.blackDisks() > Game.whiteDisks()) ? "Black" : "White";
	    System.out.println(winner + " wins!");
	    s.close();
	}

}
