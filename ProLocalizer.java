package model;

import control.EstimatorInterface;

public class ProLocalizer implements EstimatorInterface {
		
	private int rows, cols, head, r, c, dir;
	//dir: 0N, 1E, 2S, 3W

	public ProLocalizer( int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		r = c = 0;
		dir = 1;
		
	}	
	
	public int getNumRows() {
		return rows;
	}
	
	public int getNumCols() {
		return cols;
	}
	
	public int getNumHead() {
		return head;
	}
	
	public double getTProb( int x, int y, int h, int nX, int nY, int nH) {
		if (dir == 0 && nX-x == -1 && nY-y == 0) return 0.7;
		if (dir == 1 && nY-y == 1 && nX-x == 0) return 0.1;
		if (dir == 2 && nX-x == 1 && nY-y == 0) return 0.1;
		if (dir == 3 && nY-y == -1 && nX-x == 0) return 0.1;
		return 0.0;
	}

	public double getOrXY( int rX, int rY, int x, int y) {
		if (rX == -1 || rY == -1) {
			if ((x == 0 || x == rows-1) && (y == 0 || y == cols-1)) {
				return 0.725;
			}
			if ((x == 1 || x == rows-2) && (y == 0 || y == cols-1) ||
					(x == 0 || x == rows-1) && (y == 1 || y == cols-2)) {
				return 0.575;
			}
			if (x == 0 || x == rows-1 || y == 0 || y == cols-1) {
				return 0.525;
			}
			if ((x == 1 || x == rows-2) && (y == 1 || y == cols-2)) {
				return 0.225;
			}
			if (x == 1 || x == rows-2 || y == 1 || y == cols-2) {
				return 0.125;
			}
		}
		int dx = Math.abs(rX-x);
		int dy = Math.abs(rY-y);
		if (dx + dy == 0) return 0.1;
		if (dx == 1 && dy <= 1 || dy == 1 && dx <= 1) return 0.05;
		if (dx == 2 && dy <= 2 || dy == 2 && dx <= 2) return 0.025;
		return 0;
	}


	public int[] getCurrentTruePosition() {
		int[] ret = new int[2];
		ret[0] = r;
		ret[1] = c;
		return new int[]{r, c};
	}

	public int[] getCurrentReading() {
		double rand = Math.random();
		if (rand < 0.1) {
			return new int[]{r, c};
		}
		if (rand < 0.5) {
			rand = 8*Math.random();
			if (rand < 1) return convertToReading(new int[]{r-1, c});
			if (rand < 2) return convertToReading(new int[]{r-1, c+1});
			if (rand < 3) return convertToReading(new int[]{r, c+1});
			if (rand < 4) return convertToReading(new int[]{r+1, c+1});
			if (rand < 5) return convertToReading(new int[]{r+1, c});
			if (rand < 6) return convertToReading(new int[]{r+1, c-1});
			if (rand < 7) return convertToReading(new int[]{r, c-1});
			if (rand < 8) return convertToReading(new int[]{r-1, c-1});
			else System.out.println("getCurrentReading() layer 1 is wrong");
		}
		else {
			rand = 16*Math.random();
			if (rand < 1) return convertToReading(new int[]{r-2, c});
			if (rand < 2) return convertToReading(new int[]{r-2, c+1});
			if (rand < 3) return convertToReading(new int[]{r-2, c+2});
			if (rand < 4) return convertToReading(new int[]{r-1, c+2});
			if (rand < 5) return convertToReading(new int[]{r, c+2});
			if (rand < 6) return convertToReading(new int[]{r+1, c+2});
			if (rand < 7) return convertToReading(new int[]{r+2, c+2});
			if (rand < 8) return convertToReading(new int[]{r+2, c+1});
			if (rand < 9) return convertToReading(new int[]{r+2, c});
			if (rand < 10) return convertToReading(new int[]{r+2, c-1});
			if (rand < 11) return convertToReading(new int[]{r+2, c-2});
			if (rand < 12) return convertToReading(new int[]{r+1, c-2});
			if (rand < 13) return convertToReading(new int[]{r, c-2});
			if (rand < 14) return convertToReading(new int[]{r-1, c-2});
			if (rand < 15) return convertToReading(new int[]{r-2, c-2});
			if (rand < 16) return convertToReading(new int[]{r-2, c-1});
			else System.out.println("getCurrentReading() layer 2 is wrong");
		}
		return null;
	}


	public double getCurrentProb( int x, int y) {
		double ret = 0.0;
		return ret;
	}
	
	public void update() {
		boolean blocked = true;
		while (blocked) {
			double rand = Math.random();			
			if (rand < 0.3) {
				dir = (dir+1+(int)(10*rand))%4;
			}
			if (isClear(dir)) {
				blocked = false;
			}
		}
		if (dir == 0) r--;
		else if (dir == 1) c++;
		else if (dir == 2) r++;
		else if (dir == 3) c--;
		else System.out.println(dir + " dir is wrong");
		System.out.println(r + ", " + c);
	}
	
	private int[] convertToReading(int[] pos) {
		if (pos[0] < 0 || pos[0] >= rows ||
				pos[1] < 0 || pos[1] >= cols) return null;
		return pos;
	}
	
	private boolean isClear(int dir) {
		return (dir == 0 && r > 0) ||
				(dir == 1 && c < cols-1) ||
				(dir == 2 && r < rows-1) ||
				(dir == 3 && c > 0);
	}
	
	
}