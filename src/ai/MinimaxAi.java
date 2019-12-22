package de.ttt.ai;

import de.ttt.game.Field;
import de.ttt.game.Game;
import de.ttt.game.Type;

public class MinimaxAi {
	
	public static int doMove(Game game) {
		int[] board=new int[9];
		for(int i=0; i<9; i++) {
			Field f=game.getField(i);
			int id=0;
			if(f.getType()!=null) {
				if(f.getType()==Type.KNOT) id=1;
				else id=2;
			}
			board[i]=id;
		}
		
		int[] moves = getMoves(board);
		int bestMove=0;
		int bestMoveScore=Integer.MIN_VALUE;
		for(int m:moves) {
			board[m]=1;
			
			int score=min(board, bestMoveScore);
			if(score>bestMoveScore) {
				bestMoveScore=score;
				bestMove=m;
			}
			
			board[m]=0;
		}
		return bestMove;
	}
	private static int min(int[] board, int currentMaxScore) {
		int winner=checkForWinner(board);
		if(winner==1) return 100;
		if(winner==2) return -100;
		
		boolean full=true;
		for(int i=0; i<9; i++) {
			if(board[i]==0) {
				full=false;
				break;
			}
		}
		if(full) return 0;
		
		int[] moves = getMoves(board);
		int bestMoveScore=Integer.MAX_VALUE;
		for(int m:moves) {
			board[m]=2;
			
			int score=max(board, bestMoveScore);
			if(score<bestMoveScore) {
				bestMoveScore=score;
			}
			
			board[m]=0;
			if(bestMoveScore<=currentMaxScore) return bestMoveScore;
		}
		return bestMoveScore;
	}
	private static int max(int[] board, int currentMinScore) {
		int winner=checkForWinner(board);
		if(winner==1) return 100;
		if(winner==2) return -100;
		
		boolean full=true;
		for(int i=0; i<9; i++) {
			if(board[i]==0) {
				full=false;
				break;
			}
		}
		if(full) return 0;
		
		int[] moves = getMoves(board);
		int bestMoveScore=Integer.MIN_VALUE;
		for(int m:moves) {
			board[m]=1;
			
			int score=min(board, bestMoveScore);
			if(score>bestMoveScore) {
				bestMoveScore=score;
			}
			
			board[m]=0;
			if(bestMoveScore>=currentMinScore) return bestMoveScore;
		}
		return bestMoveScore;
	}
	private static int[] getMoves(int[] board) {
		int a=0;
		for(int i=0; i<9; i++) {
			if(board[i]==0) a++;
		}
		int[] ms=new int[a];
		int j=0;
		for(int i=0; i<9; i++) {
			if(board[i]==0) {
				ms[j]=i;
				j++;
			}
		}
		return ms;
	}
	private static int checkForWinner(int board[]) {
		int w=checkHor(board);
		if(w!=0) return w;
		w=checkVer(board);
		if(w!=0) return w;
		w=checkDia(board,1);
		if(w!=0) return w;
		w=checkDia(board,2);
		if(w!=0) return w;
		return 0;
	}
	private static int checkVer(int board[]) {
		for(int x=0; x<3; x++) {
			boolean b1=true;
			boolean b2=true;
			for(int y=0; y<3; y++) {
				if(board[y*3+x]!=1) {
					b1=false;
				}
				if(board[y*3+x]!=2) {
					b2=false;
				}
			}
			if(b1) return 1;
			if(b2) return 2;
		}
		return 0;
	}
	private static int checkHor(int board[]) {
		for(int y=0; y<3; y++) {
			boolean b1=true;
			boolean b2=true;
			for(int x=0; x<3; x++) {
				if(board[y*3+x]!=1) {
					b1=false;
				}
				if(board[y*3+x]!=2) {
					b2=false;
				}
			}
			if(b1) return 1;
			if(b2) return 2;
		}
		return 0;
	}
	private static int checkDia(int board[], int color) {
		boolean b1=true;
		boolean b2=true;
		for(int x=0; x<3; x++) {
			if(board[x*3+x]!=color) b1=false;
			if(board[(2-x)*3+x]!=color) b2=false;
		}
		if(b1||b2) return color;
		return 0;
	}
	
}
