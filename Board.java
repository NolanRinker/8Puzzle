import edu.princeton.cs.algs4.*;

import java.util.*;
import java.lang.*;

public class Board{
	
	private int[][] blocks;
	private final int N;
	
	// Cunstructor: a N x N board array of blocks
	public Board(int[][] blocks){
		this.blocks = blocks;
		N = dimension();
		
		/**
		System.out.println("Dimension is: " + N);
		System.out.println(toString());
		System.out.println("Hamming: " + hamming() + " out of place");
		System.out.println("Manhattan: " + manhattan() + " out of place");
		if (isGoal() == true)
			System.out.println("Matches");
		else
			System.out.println("Does not match Goal board");
		System.out.println("Same board = " + equals(goal));
		neighbors(); */
	}
	
	// Board dimension N
	public int dimension(){
		return blocks.length;
	}
	
	// number of blocks out of place
	public int hamming(){
		int correctVal = 1;	// what the correct value should be
		int outOfPlace = 0;		// counts number of out of place blocks
		for(int i = 0; i < blocks.length; i++){
			for(int j = 0; j < blocks.length; j++){
				int val = blocks[i][j];
				if (correctVal != val && val != 0)
					outOfPlace++;
				correctVal++;
			}
		}
		return outOfPlace;
	}
	
	// sum of distance between  blocks and goal
	public int manhattan(){
		int distance = 0;
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				int val = blocks[i][j];
				if (val != 0){
					int targetI = (val - 1) / N;
					int targetJ = (val - 1) % N;
					int di = i - targetI;
					int dj = j - targetJ;
					distance += Math.abs(di) + Math.abs(dj);
				}
			}
		}
		return distance;
	}
	
	// goal board
	public int[][] goal(){
		int[][]goal = new int[N][N];
		int fill = 1;
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				if (fill == N*N)	// when fill reaches last block
					goal[i][j] = 0;
				else
					goal[i][j] = fill;
				fill++;
			}
		}
		return goal;
	}
	
	// is this board the goal board?
	public boolean isGoal(){
		int[][] goal = goal();
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				if (blocks[i][j] != goal[i][j])	
					return false;
			
		return true;	
	}
	
	// a board obtained by exchanging any pair of blocks
	public Board twin(){

		return null;
		
	}
	
	// does this board equal y?
	public boolean equals(Object y){
		if (y == this)
			return true;
		if (y == null)
			return false;
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				if (((Board) y ).blocks[i][j] != blocks[i][j])
					return false;
		return true;
	}
	
	private int[] emptyBlock(){
		int[]loc = new int[2];	// array size of two to store row and col
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				if (blocks[i][j]==0){
					loc[0] = i;
					loc[1] = j;
					return loc;
				}	
			}
		}
		throw new RuntimeException();
	}
	
	// all neighboring boards
	public Iterable<Board> neighbors(){
		ArrayList<Board> al = new ArrayList<Board>();		// add neighbors to array list
		int[]loc = emptyBlock();	// find the empty blocks location
		int r = loc[0];
		int c = loc[1];
		// make copies to make neighboring boards
		int[][]swapLeft = new int[N][N];
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				int target = blocks[i][j];	// copy the blocks board
				swapLeft[i][j] = target;
			}
		}
		int[][]swapRight = new int[N][N];
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				int target = blocks[i][j];
				swapRight[i][j] = target;
			}
		}
		int[][]swapDown = new int[N][N];
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				int target = blocks[i][j];
				swapDown[i][j] = target;
			}
		}
		int[][]swapUp = new int[N][N];
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				int target = blocks[i][j];
				swapUp[i][j] = target;
			}
		}
		
		// now see what directions 0 space can move
		if(c > 0){	// LEFT. still legal block space
			int a = blocks[r][c];	// position of 0
			int b = blocks[r][c-1];	// left swap position
			swapLeft[r][c] = b;
			swapLeft[r][c-1] = a;
			System.out.println("swapped left");
			Board swapL = new Board(swapLeft);
			al.add(swapL);
		}	

		if (c < N - 1){	// Right.
			int a = blocks[r][c];
			int b = blocks[r][c+1];
			swapRight[r][c] = b;
			swapRight[r][c+1] = a;;
			System.out.println("swapped right");
			Board swapR = new Board(swapRight);
			al.add(swapR);
		}
		
		if (r < N - 1){	// Down
			int a = blocks[r][c];
			int b = blocks[r+1][c];
			swapDown[r][c] = b;
			swapDown[r+1][c] = a;;
			System.out.println("swapped down");
			Board swapD = new Board(swapDown);
			al.add(swapD);
		}	
		
		if	(r > 0){	// UP
			int a = blocks[r][c];
			int b = blocks[r-1][c];
			swapUp[r][c] = b;
			swapUp[r-1][c] = a;
			System.out.println("swapped up");
			Board swapU = new Board(swapUp);
			al.add(swapU);
		}
	
		System.out.println("AL size = " + al.size());
		return al;	// returns array list with Boards
	}
	
	// string representation
	public String toString(){
		String tempS = "";
		String numString = "";
		int tempN = 0;
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				tempN = blocks[i][j];
				if (tempN == 0)
					numString = "-";
				else
					numString = String.valueOf(tempN);
				tempS += numString + " ";
			}
			tempS += "\n";
		}
		return tempS;
	}

	/**
	public static void main(String[] args){
		int N = 3;
		int[][] blocks = new int[N][N];
		blocks[0][0] = 8;	// blank space
		blocks[0][1] = 1;
		blocks[0][2] = 3;
		blocks[1][0] = 4;
		blocks[1][1] = 0;
		blocks[1][2] = 2;
		blocks[2][0] = 7;
		blocks[2][1] = 6;
		blocks[2][2] = 5;
		
		Board initial = new Board(blocks);
	}*/
}
