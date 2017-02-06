import java.util.LinkedList;

public class AI {
	private int time; //time in milliseconds
	private int maxTime; //maximum time to take on a move
	private int maxDepth; //maximum depth
	
	private static class Move {
	    int x, y, value;
	    public Move (int x, int y, int val) {
	    	this.x = x;
	    	this.y = y;
	    	value = val;
	    }
	    
	    public String toString() {
			return "" + ((char) (x + '1')) + ((char) (y + 'a'));  
		}
	}
	
	private AI(int maxTime) {
		this.maxTime = maxTime;
		time = 0;
	}
	
	public synchronized void incrementTime() {
		time += 10;
	}
	
	public synchronized int getTime() {
		return time;
	}
	
	public static String minimax(GameState game, int maxTime) {
		AI ai = new AI(maxTime);
		String move = "";
		int currentDepth = 0;
		boolean run = true;
		Time timeThread = new Time(ai);
		timeThread.start();
		while(run) {
			if(ai.maxDepth > 64 - game.blackDisks() - game.whiteDisks()) break;
			ai.maxDepth = currentDepth;
			Move res = (game.getCurrentPlayer() == Player.BLACK) ? 
					ai.maxValue(game, -100, 100, 0) : 
					ai.minValue(game, -100, 100, 0);
			
			if(res == null) {
				run = false;
			} else {
				move = res.toString();
				currentDepth += 2;
//				System.out.println("Depth " + currentDepth + ": " + ai.getTime());
			}
		}
		timeThread.interrupt();
		return move;
	}
	
	private boolean timeOut() {
		return getTime() > maxTime-20;
	}
	
	private static int evaluate(GameState game) {
	    return (game.blackDisks() - game.whiteDisks());
	}
	
	public static LinkedList<Move> getAvailableMoves(GameState game) {
	    LinkedList<Move> result = new LinkedList<Move>();
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            if (game.viableMove(i, j)) {
	            	result.add(new Move(i, j, 0));
	            }
	        }
	    }
	    return result;
	}
	
	private Move optimize(GameState game, Move m, int alpha, int beta, int currDepth) {
	    game.play(m.toString());
	    if( currDepth > this.maxDepth || 
	    	this.maxDepth > 64 - game.blackDisks() - game.whiteDisks())  {
	    		m.value = evaluate(game);
	    		return m;
	    }
	    Move best = (game.getCurrentPlayer() == Player.BLACK) ?
	        maxValue(game, alpha, beta, currDepth):
	        minValue(game, alpha, beta, currDepth);
	    return best;
	}

	private Move maxValue(GameState game, int alpha, int beta, int currDepth) {
	    Move maxMove = new Move(-1, -1, -100);
	    LinkedList<Move> actions = getAvailableMoves(game);
	    for(Move m : actions) {
	        Move maxOfMin = optimize(new GameState(game), m, alpha, beta, currDepth+1);
	        if(timeOut()) return null;
//	        for(int i = 0; i < currDepth; i++) System.out.print("  ");
//	        System.out.println(m.toString() + ": " + maxOfMin.value);
	        if(maxOfMin.value > maxMove.value) {
	        	maxMove = m;
	        	maxMove.value = maxOfMin.value;
	        }
	        if(maxMove.value >= beta) return maxMove;
	        alpha = (maxMove.value > alpha) ? maxMove.value : alpha;
	    }
	    return maxMove;
	}

	private Move minValue(GameState game, int alpha, int beta, int currDepth) {
	    Move minMove = new Move(-1, -1, 100);
	    LinkedList<Move> actions = getAvailableMoves(game);
	    for(Move m : actions) {
	        Move minOfMax = optimize(new GameState(game), m, alpha, beta, currDepth+1);
	        if(timeOut()) return null;
//	        for(int i = 0; i < currDepth; i++) System.out.print("  ");
//	        System.out.println(m.toString() + ": " + minOfMax.value);
	        if(minOfMax.value < minMove.value) {
	        	minMove = m;
	        	minMove.value = minOfMax.value;
	        }
	        if(minMove.value <= alpha) return minMove;
	        beta = (minMove.value < beta) ? minMove.value : beta;
	    }
	    return minMove;
	}
	
}
