import java.util.LinkedList;

public class AI {
	private static final int MAX_DEPTH = 20;
	private int time; //time in milliseconds
	private int maxTime; //maximum time to take on a move
	private int depth;
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
	
	private AI(int maxTime) {
		this.maxTime = maxTime;
		time = 0;
	}
	
	synchronized void incrementTime() {
		time += 10;
	}
	
	synchronized int getTime() {
		return time;
	}
	
	private boolean timeOut() {
		return getTime() > maxTime-20;
	}
	
	public static String getMove(GameState game, int maxTime) {
		AI ai = new AI(maxTime);
		String move = "";
		int currentDepth = 0;
		boolean run = true;
		Time timeThread = new Time(ai);
		timeThread.start();
		while(run) {
			String res = ai.minimax(game, currentDepth);
			if(res == "END") {
				run = false;
			} else {
				move = res;
				currentDepth += 2;
//				System.out.println("Depth " + currentDepth + ": " + ai.getTime());
			}
		}
		timeThread.interrupt();
		return move;
	}
	
	private String minimax(GameState game, int depth) {
		this.depth = depth;
		int best = (game.getCurrentPlayer() == Player.BLACK) ? -100 : 100;
	    Move bestMove = new Move(-1,-1);
	    LinkedList<Move> actions = getAvailableMoves(game);
	    for(Move m : actions) {
	    	int val = optimize(new GameState(game), m, -100, 100, 1);
	    	if(timeOut()) return "END";
//	    	System.out.println(">>" + m.toString() + ": " + val);
	    	if ((val > best && game.getCurrentPlayer() == Player.BLACK) ||
	    		(val < best && game.getCurrentPlayer() == Player.WHITE)) {
	 	            best = val;
	 	            bestMove = m;
	 	    }
	    }
	    return bestMove.toString();
	}
	
	private int optimize(GameState game, Move m, int alpha, int beta, int currDepth) {
		if(timeOut()) return 0;
		        
	    game.play(m.toString());
	    if(m.x == -1 || currDepth > this.depth || currDepth > MAX_DEPTH) return evaluate(game);

	    return (game.getCurrentPlayer() == Player.BLACK) ?
	        maxValue(game, alpha, beta, currDepth):
	        minValue(game, alpha, beta, currDepth);
	}

	private int maxValue(GameState game, int alpha, int beta, int currDepth) {
	    int value = -100;
	    LinkedList<Move> actions = getAvailableMoves(game);
	    for(Move m : actions) {
	        int best = optimize(new GameState(game), m, alpha, beta, currDepth+1);
//	        for(int i = 0; i < depth; i++) System.out.print("  ");
//	        System.out.println("  " + m.toString() + ": " + best);
	        value = (best > value) ? best : value;
	        if(value >= beta) return value;
	        alpha = (alpha > value) ? alpha : value;
	    }
	    return value;
	}

	private int minValue(GameState game, int alpha, int beta, int currDepth) {
	    int value = 100;
	    LinkedList<Move> actions = getAvailableMoves(game);
	    for(Move m : actions) {
	        int best = optimize(new GameState(game), m, alpha, beta, currDepth+1);
//	        for(int i = 0; i < depth; i++) System.out.print("  ");
//	        System.out.println("  " + m.toString() + ": " + best);
	        value = (value < best) ? value : best;
	        if(value <= alpha) return value;
	        beta = (beta < value) ? beta : value;
	    }
	    return value;
	}
	
	private int evaluate(GameState game) {
	    return (game.blackDisks() - game.whiteDisks());
	}
	
	private LinkedList<Move> getAvailableMoves(GameState game) {
	    LinkedList<Move> result = new LinkedList<Move>();
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            if (game.viableMove(i, j)) {
	            	result.add(new Move(i, j));
	            }
	        }
	    }
	    return result;
	}
	
}
