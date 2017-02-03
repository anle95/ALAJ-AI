
public class GameState {
	private Player[][] board;
	private Player currentP;
	private Player currentO;
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
		currentP = c.currentP;
		currentO = c.currentO;
		black = c.black;
		white = c.white;
	}
	
	private boolean onBoard(int a) {
	    return (a >= 0 && a < 8);
	}
	
	private void swapPlayers() {
	    currentO = currentP;
	    currentP = currentP == Player.BLACK ? Player.WHITE : Player.BLACK;
	}
	
	private boolean checkLine(int a, int b, int dirX, int dirY) {
	    int x = a + dirX;
	    int y = b + dirY;
	    if(!(onBoard(x) && onBoard(y))) return false;
	    Player curr = board[x][y];
	    if (curr == currentP) return false;
	    while (curr == currentO) {
	        x += dirX;
	        y += dirY;
	        if(!(onBoard(x) && onBoard(y))) return false;
	        curr = board[x][y];
	    }
	    if (curr == currentP) return true;
	    else return false;
	}
	
	public Player getCurrentPlayer() {
		return currentP;
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
	    board[3][3] = Player.WHITE;
	    board[4][4] = Player.WHITE;
	    board[4][3] = Player.BLACK; 
	    board[3][4] = Player.BLACK;
	    currentP = Player.BLACK;
	    currentO = Player.WHITE;
	    black = 2;
	    white = 2;
	}
	
	public void print() {
	    for (int i = 0; i < 8; i++) {
	        System.out.println();
	        for (int j = 0; j < 8; j++) {
	            switch (board[i][j]) {
	                case BLACK:
	                    System.out.print('X');
	                    break;
	                case WHITE:
	                	System.out.print('O');
	                    break;
	                default:
	                	System.out.print('-');
	                    break;
	            }
	        }
	    }
	    String pString = currentP == Player.BLACK ? "Black" : "White";
	    System.out.println("\n" + black + " - " + white + "\n" + pString + " to move");
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
	    board[a][b] = currentP;
	    if (currentP == Player.WHITE) white++;
	    else						  black++;

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
	    while (board[x][y] != currentP) {
	        board[x][y] = currentP;
	        turned++;
	        x += dirX;
	        y += dirY;
	    }

	    if (currentP == Player.WHITE) {
	        white += turned;
	        black -= turned;
	    } else {
	        black += turned;
	        white -= turned;
	    }
	}
	
	public boolean viableMove(int a, int b) {
	    if (board[a][b] != Player.NONE) return false;;
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
