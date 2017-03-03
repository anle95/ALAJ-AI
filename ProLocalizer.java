package model;

import java.util.ArrayList;

import control.EstimatorInterface;

public class ProLocalizer implements EstimatorInterface {
		
	private int rows, cols, head, r, c, dir, rX, rY;
	//dir: 0N, 1E, 2S, 3W
	private ArrayList<double[][][]> probs;
	boolean first = true;

	public ProLocalizer( int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		r = c = 0;
		dir = 0;
		probs = new ArrayList<double[][][]>();
		double[][][] prob0 = new double[rows][cols][4];
		double p0 = 1.0/(rows*cols*4);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				for (int k = 0; k < 4; k++) {
					prob0[i][j][k] = p0;
				}
			}
		}
		probs.add(prob0);
		System.out.println(p0);
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
		int dx = nX-x;
		int dy = nY-y;
		int newH = getHeading(x, y, nX, nY);
		if (newH == -1 || nH != newH) return 0.0;
		int count = 0;
		for (int d = 0; d < 4; d++) {
			if (isClear(x, y, d)) {
				count++;
			}
		}

		if (!isClear(x, y, h)) {
			if (isClear(x, y, newH)) {
				return 1.0/count;
			}
		} else {
			if (newH == h) {
				return 0.7;
			} else if (isClear(x, y, newH)){
				return 0.3/(count-1);
			}
		}
		return 0.0;
	}

	private int nOfLs(int r, int c) {
		int res = 0;
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(r+i >= 0 && r+i < rows && c+j >= 0 && c+j < cols && (i != 0 || j != 0)) {
					res++;
				}
			}
		}
		return res;
	}
	
	private int nOfLs2(int r, int c) {
		int res = 0;
		for(int i = -2; i < 3; i++) {
			for(int j = -2; j < 3; j++) {
				
				if(r+i >= 0 && r+i < rows && c+j >= 0 && c+j < cols && (i != 0 || j != 0)) {
					res++;
				}
			}
		}
		res -= nOfLs(r, c);
		return res;
	}
	
	public double getOrXY( int rX, int rY, int x, int y) {
		int n = nOfLs(x, y);
		int n2 = nOfLs2(x, y);
		if (rX == -1 || rY == -1) {
			return 1.0-0.1-0.05*n-0.025*n2;
		}
		int dx = Math.abs(rX-x);
		int dy = Math.abs(rY-y);
		if (dx + dy == 0) return 0.1;
		if (dx == 1 && dy <= 1 || dy == 1 && dx <= 1) return 0.05;
		if (dx == 2 && dy <= 2 || dy == 2 && dx <= 2) return 0.025;
		return 0.0;
//		if (rX == -1 || rY == -1) {
//			if ((x == 0 || x == rows-1) && (y == 0 || y == cols-1)) {
//				return 0.725;
//			}
//			if ((x == 1 || x == rows-2) && (y == 0 || y == cols-1) ||
//					(x == 0 || x == rows-1) && (y == 1 || y == cols-2)) {
//				return 0.575;
//			}
//			if (x == 0 || x == rows-1 || y == 0 || y == cols-1) {
//				return 0.525;
//			}
//			if ((x == 1 || x == rows-2) && (y == 1 || y == cols-2)) {
//				return 0.225;
//			}
//			if (x == 1 || x == rows-2 || y == 1 || y == cols-2) {
//				return 0.125;
//			}
//		}
//		int dx = Math.abs(rX-x);
//		int dy = Math.abs(rY-y);
//		if (dx + dy == 0) return 0.1;
//		if (dx == 1 && dy <= 1 || dy == 1 && dx <= 1) return 0.05;
//		if (dx == 2 && dy <= 2 || dy == 2 && dx <= 2) return 0.025;
//		return 0;
	}


	public int[] getCurrentTruePosition() {
		int[] ret = new int[2];
		ret[0] = r;
		ret[1] = c;
		return new int[]{r, c};
	}

	public int[] getCurrentReading() {
		first = true;
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
		//here
		return null;
	}
	
	public void setReading(int[] rPos) {
		if (rPos == null) {
			rX = -1;
			rY = -1;
			return;
		}
		rX = rPos[0];
		rY = rPos[1];
	}


	public double getCurrentProb( int x, int y) {
		if (!first) {
			double[] currProb = probs.get(probs.size()-1)[x][y];
			double ret = currProb[0]+currProb[1]+currProb[2]+currProb[3];
			return ret;
		}
		first = false;
//		int ci = 0, cj = 0, ck = 0;
		double[][][] sum = new double[rows][cols][4];
		double norm = 0;
		for (int ci = 0; ci < rows; ci++) {
			for (int cj = 0; cj < cols; cj++) {
				for (int ck = 0; ck < 4; ck++) {
					double element = 0;
					for (int i = 0; i < rows; i++) {
						for (int j = 0; j < cols; j++) {
							for (int k = 0; k < 4; k++) {
								double P1 = getTProb(i, j, k, ci, cj, ck);
								double P2 = probs.get(probs.size()-1)[i][j][k];
//								System.out.println(P2);
								element += P1*P2;
							}
						}
					}
//					double[][][] sum = new double[rows][cols][4];
//					double norm = 0;
					double p = getOrXY(rX, rY, ci, cj);
					sum[ci][cj][ck] = p*element;
					norm += sum[ci][cj][ck];
//					for (int i = 0; i < rows; i++) {
//						for (int j = 0; j < cols; j++) {
//							for (int k = 0; k < 4; k++) {
//								double p = getOrXY(rX, rY, i, j);
//								sum[ci][cj][ck] = p*element;
//							}
//							norm += 4*sum[i][j][0];
//						}
//					}
//					for (int i = 0; i < rows; i++) {
//						for (int j = 0; j < cols; j++) {
//							for (int k = 0; k < 4; k++) {
//								sum[i][j][k] /= norm;
//							}
//						}
//					}
				}
			}
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				for (int k = 0; k < 4; k++) {
					sum[i][j][k] /= norm;
				}
			}
		}

		
//		for (int i = 0; i < rows; i++) {
//			for (int j = 0; j < cols; j++) {
//				for (int k = 0; k < 4; k++) {
//					double P1 = getTProb(i, j, k, ci, cj, ck);
//					double P2 = probs.get(probs.size()-1)[i][j][k];
//					System.out.println(P2);
//					element += P1*P2;
//				}
//			}
//		}
//		double[][][] sum = new double[rows][cols][4];
//		double norm = 0;
//		for (int i = 0; i < rows; i++) {
//			for (int j = 0; j < cols; j++) {
//				for (int k = 0; k < 4; k++) {
//					double p = getOrXY(rX, rY, i, j);
//					sum[i][j][k] = p*element;
//				}
//				norm += 4*sum[i][j][0];
//			}
//		}
//		for (int i = 0; i < rows; i++) {
//			for (int j = 0; j < cols; j++) {
//				for (int k = 0; k < 4; k++) {
//					sum[i][j][k] /= norm;
//				}
//			}
//		}
		probs.add(sum);
		double[] currProb = probs.get(probs.size()-1)[x][y];
		double ret = currProb[0]+currProb[1]+currProb[2]+currProb[3];
		return ret;
	}
	
	public void update() {
		double p0 = getTProb(r, c, dir, r-1, c, 0);
		double p1 = getTProb(r, c, dir, r, c+1, 1);
		double p2 = getTProb(r, c, dir, r+1, c, 2);
		double p3 = getTProb(r, c, dir, r, c-1, 3);
		double rand = Math.random();
		System.out.println(p0 + " " + p1 + " " + p2 + " " + p3 + " " + rand);
		if (rand < p0) r--;
		else if (rand < p0+p1) c++;
		else if (rand < p0+p1+p2) r++;
		else c--;
//		boolean blocked = true;
//		while (blocked) {
//			double rand = Math.random();			
//			if (rand < 0.3) {
//				dir = (dir+1+(int)(10*rand))%4;
//			}
//			if (isClear(r, c, dir)) {
//				blocked = false;
//			}
//		}
//		if (dir == 0) r--;
//		else if (dir == 1) c++;
//		else if (dir == 2) r++;
//		else if (dir == 3) c--;
//		else System.out.println(dir + " dir is wrong");
//		System.out.println(r + ", " + c);
	}
	
	private int[] convertToReading(int[] pos) {
		if (pos[0] < 0 || pos[0] >= rows ||
				pos[1] < 0 || pos[1] >= cols) return null;
		return pos;
	}
	
	private boolean isClear(int x, int y, int dir) {
		return (dir == 0 && x > 0) ||
				(dir == 1 && y < cols-1) ||
				(dir == 2 && x < rows-1) ||
				(dir == 3 && y > 0);
	}
	
	private int getHeading(int x, int y, int nX, int nY) {
		int dx = nX-x;
		int dy = nY-y;
		if(dx == -1 && dy == 0) return 0;
		if(dx == 0 && dy == 1) return 1;
		if(dx == 1 && dy == 0) return 2;
		if(dx == 0 && dy == -1) return 3;
		return -1;
	}
}