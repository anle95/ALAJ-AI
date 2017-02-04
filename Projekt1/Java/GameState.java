
public class GameState {
	private Player[][] board;
	private Player currentPlayer;
	private int black;
	private int white;
	
	public GameState() {
		board = new Player[8][8];
		reset();
	}
	
	public GameState(GameState c) {
		this.board = new Player[8][];
		for(int i = 0; i < 8; i++) {
			Player[] arr = c.board[i];
			this.board[i] = new Player[8];
			System.arraycopy(arr, 0, this.board[i], 0, 8);
		}
		currentPlayer = c.currentPlayer;
		black = c.black;
		white = c.white;
	}
	
	private boolean onBoard(int a) {
	    return (a >= 0 && a < 8);
	}
	
	private void swapPlayers() {
	    currentPlayer = (currentPlayer == Player.BLACK) ? Player.WHITE : Player.BLACK;
	}
	
	private boolean checkLine(int a, int b, int dirX, int dirY) {
	    int x = a + dirX;
	    int y = b + dirY;
	    if(!(onBoard(x) && onBoard(y))) return false;
	    Player curr = board[x][y];
	    if (curr == currentPlayer) return false;
	    Player currentOpponent = (currentPlayer == Player.BLACK) ? Player.WHITE : Player.BLACK;
	    while (curr == currentOpponent) {
	        x += dirX;
	        y += dirY;
	        if(!(onBoard(x) && onBoard(y))) return false;
	        curr = board[x][y];
	    }
	    if (curr == currentPlayer) 	return true;
	    else 						return false;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int blackDisks() {
		return black;
	}
	
	public int whiteDisks() {
		return white;
	}
	
	public void reset() {
		for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            board[i][j] = Player.NONE;
	        }
	    }
	    board[3][3] = board[4][4] = Player.WHITE;
	    board[4][3] = board[3][4] = Player.BLACK;
	    currentPlayer = Player.BLACK;
	    black = 2;
	    white = 2;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < 8; i++) {
	    	sb.append('\n');
	        for (int j = 0; j < 8; j++) {
	            switch (board[i][j]) {
	                case BLACK:
	                	sb.append('X');
	                    break;
	                case WHITE:
	                	sb.append('O');
	                    break;
	                default:
	                	sb.append('-');
	                    break;
	            }
	        }
	    }
	    sb.append('\n');
	    sb.append(black + " - " + white);
	    sb.append('\n');
	    sb.append(currentPlayer + " to move");
	    return sb.toString();
	}
	
	public boolean play(String move) {
	    int a = move.charAt(0) - '1';
	    if (!onBoard(a)) {
	        System.out.println("Invalid move");
	        return false;
	    }
	    int b = move.charAt(1) - 'a';
	    if (!onBoard(b)) {
	    	System.out.println("Invalid move");
	        return false;
	    }
	    if(viableMove(a, b)) {
	        update(a, b);
	        return true;
	    } else {
	    	System.out.println("Invalid move");
	        return false;
	    }
	}
	
	private void update(int a, int b) {
	    board[a][b] = currentPlayer;
	    if (currentPlayer == Player.BLACK)	black++;
	    else								white++;

	    for (int i = -1; i <= 1; i++) {
	        for (int j = -1; j <= 1; j++) {
	            if(checkLine(a, b, i, j)) {
	                turn(a, b, i, j);
	            }
	        }
	    }
	    swapPlayers();
	    if(!anyViableMove()) swapPlayers();
	}
	
	private void turn(int a, int b, int dirX, int dirY) {
	    int x = a + dirX;
	    int y = b + dirY;
	    int turned = 0;
	    while (board[x][y] != currentPlayer) {
	        board[x][y] = currentPlayer;
	        turned++;
	        x += dirX;
	        y += dirY;
	    }

	    if (currentPlayer == Player.WHITE) {
	        white += turned;
	        black -= turned;
	    } else {
	        black += turned;
	        white -= turned;
	    }
	}
	
	public boolean viableMove(int a, int b) {
	    if (board[a][b] != Player.NONE) return false;
	    for (int i = -1; i <= 1; i++) {
	        for (int j = -1; j <= 1; j++) {
	            if (!(i == 0 && j == 0) && checkLine(a, b, i, j)) return true;
	        }
	    }
	    return false;
	}
	
	public boolean anyViableMove() {
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            if (viableMove(i, j)) return true;
	        }
	    }
	    return false;
	}
}
