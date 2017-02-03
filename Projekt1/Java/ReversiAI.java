import java.util.LinkedList;

public class ReversiAI {
	private static int MAX_DEPTH = 16;
	
	private static class Move {
	    int x, y;
	    public Move (int x, int y) {
	    	this.x = x;
	    	this.y = y;
	    }
	    
	    public String toString() {
			return "" + ((char) (x + '1')) + ((char) (y + 'a'));  
		}
	}
	
	public static String minimax(GameState Game, int depth) {
	    MAX_DEPTH = depth;
	    int best = (Game.getCurrentPlayer() == Player.BLACK) ? -100 : 100;
	    Move bestMove = new Move(-1,-1);
	    LinkedList<Move> actions = getAvailableMoves(Game);
	    for(Move m : actions) {
	    	int val = optimize(new GameState(Game), m, -100, 100, 1);
	    	System.out.println(">>" + m.toString() + ": " + val);
	    	if ((val > best && Game.getCurrentPlayer() == Player.BLACK) ||
	    		(val < best && Game.getCurrentPlayer() == Player.WHITE)) {
	 	            best = val;
	 	            bestMove = m;
	 	    }
	    }
	    return bestMove.toString();
	}

	private static int evaluate(GameState Game) {
	    return (Game.blackDisks() - Game.whiteDisks());
	}

	private static int optimize(GameState Game, Move m, int alpha, int beta, int depth) {
	    Game.play(m.toString());
	    if(m.x == -1 || depth > MAX_DEPTH) return evaluate(Game);

	    return (Game.getCurrentPlayer() == Player.BLACK) ?
	        maxValue(Game, alpha, beta, depth):
	        minValue(Game, alpha, beta, depth);
	}

	private static int maxValue(GameState Game, int alpha, int beta, int depth) {
	    int value = -100;
	    LinkedList<Move> actions = getAvailableMoves(Game);
	    for(Move m : actions) {
	    	
	        int best = optimize(new GameState(Game), m, alpha, beta, depth+1);
	        for(int i = 0; i < depth; i++) System.out.print("  ");
	        System.out.println("  " + m.toString() + ": " + best);
	        value = (best > value) ? best : value;
	        if(value >= beta) return value;
	        alpha = (alpha > value) ? alpha : value;
	    }
	    return value;
	}

	private static int minValue(GameState Game, int alpha, int beta, int depth) {
	    int value = 100;
	    LinkedList<Move> actions = getAvailableMoves(Game);
	    for(Move m : actions) {
	        int best = optimize(new GameState(Game), m, alpha, beta, depth+1);
	        for(int i = 0; i < depth; i++) System.out.print("  ");
	        System.out.println("  " + m.toString() + ": " + best);
	        value = (value < best) ? value : best;
	        if(value <= alpha) return value;
	        beta = (beta < value) ? beta : value;
	    }
	    return value;
	}
	
	private static LinkedList<Move> getAvailableMoves(GameState Game) {
	    LinkedList<Move> result = new LinkedList<Move>();
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            if (Game.viableMove(i, j)) {
	            	result.add(new Move(i, j));
	            }
	        }
	    }
	    return result;
	}

}
