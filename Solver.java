import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Stack;
import java.util.*;
import java.lang.*;

public class Solver {
	private final int N;
//	private int numMoves = 0;	// number of initial moves
//	private Board previous = null; 	// state of initial previous node
	private Node lastMove;
	
	private class Node implements Comparable<Node>{
		private Node previous;
		private Board board;
		private int numMoves = 0;
		
		public Node(Board board, Node previous){
			this.board = board;
			this.previous = previous;
			this.numMoves = previous.numMoves + 1;
		}
		
		public Node(Board board) {
			this.board = board;
		}

		public int compareTo(Node node) {
		
			return (this.board.manhattan() - node.board.manhattan())+ (this.numMoves - node.numMoves);
		}
		
	}
	
	// Constructor: solution (A* algorith)
	public Solver(Board initial){
		
		MinPQ<Node> pq = new MinPQ<Node>();
		pq.insert(new Node(initial));	// initial added to priority queue
		//MinPQ<Node> twinPQ = new MinPQ<Node>();
		//twinPQ.insert(new Node(initial.twin()));
		N = initial.dimension();
		System.out.println("Dimension size = " + N);
		System.out.println("Initial PQ size = " + pq.size());
		//lastMove = initial;
		System.out.println("initial board\n" + initial);
		// get lowest value and add to que
		while(true){
			lastMove = expand(pq);
			if (lastMove != null)
				return;
		}
	}
	
	private Node expand(MinPQ<Node> pq) {
		if (pq.isEmpty())
			return null;
		Node best = pq.delMin();
		if(best.board.isGoal())
			return best;
		for(Board neighbor : best.board.neighbors()){
			if (best.previous == null || !neighbor.equals(best.previous.board))
				pq.insert(new Node(neighbor, best));
		}
		return null;
	}

	// is it solvable? Count the number of inversions. Even yes, odd no
	public boolean isSolvable(){
		return (lastMove != null);
		/**
		int inversions = 0;;
		 
		// check how many times a number precedes a lesser value
		for (int i = 0; i < N; i++){
			for (int j = i + 1; j < N; j++){
				if (Board.copy(initial)[j] > Board.copy()[i])	// initial board array
					inversions++;
			}
		}
		
		if (inversions%2 == 1)	// if it's odd, it is not solvable
			return false;
		else
			return true; */
	}
	
	// min # of moves to solve initial board, -1 if unsolvable
	public int moves(){
		return isSolvable() ? lastMove.numMoves : -1;
		
	}
	
	// sequence of boards in shortest solution, null if unsolvable
	public Iterable<Board> solution(){
		if (!isSolvable())
			return null;
		Stack<Board> solutionPathList = new Stack<Board>();
		while(lastMove != null){
			solutionPathList.push(lastMove.board);
			lastMove = lastMove.previous;
		}
		return solutionPathList;
	}
	
	// Main: solve slider puzzle
	public static void main(String[] args){
		///**
		// create initial board from file
		int row = 0;	// used for inversion
		
		In in = new In("puzzle3x3-05");
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				blocks[i][j] = in.readInt();
				if (blocks[i][j]== 0)
					row = i;
			}
		}
		//*/
		/**
		int N = 3;
		int[][] blocks = new int[N][N];
		blocks[0][0] = 1;	// blank space
		blocks[0][1] = 0;
		blocks[0][2] = 2;
		blocks[1][0] = 4;
		blocks[1][1] = 6;
		blocks[1][2] = 3;
		blocks[2][0] = 7;
		blocks[2][1] = 5;
		blocks[2][2] = 8;
		*/
		
		int inversions = 0;;
		 
		// check how many times a number precedes a lesser value
		for (int r = 0; r < N; r++)
			for (int c = 0; c < N; c++)
				for (int i = r; i < N; i++)			// start in the same row
					for (int j = c; j < N; j++)		// start in same col
						if ((blocks[r][c] > blocks[i][j]) && (blocks[i][j] != 0))	// target value greater then remaing block values
							inversions++;
		System.out.println("Number of inversions = " + inversions);
		/** https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
		 * if width is odd, inversions can't be odd
		 * if width is even and blank on even or 0 row, then inversions can't be even
		 * if width is even and blank on odd, then inversions can't be odd
		*/
		if (((inversions%2 != 1)&& N%2 == 1)||(N%2!=1&&(row%2!=1 && inversions%2==1)||(row%2==1 && inversions%2!=1))){ 
			Board initial = new Board(blocks);	// blocks is passed to constructor in Board.java
			
			// solve the puzzle
			Solver solver = new Solver(initial);
			
			System.out.println("Construction Finished");
			
			// print solution to standard output
			if (!solver.isSolvable())
				System.out.println("No solution possible");
			else{
				System.out.println("Minimum number of moves = " + solver.moves());
				for (Board board : solver.solution())
					System.out.println(board);
			}
		}else
			System.out.println("Not solvable based on inversion algorithm");
	}
}
