/*
 * File: Chessboard.java
 * -------------------
 * Name: Daniel Staal
 * 
 *
 * Main Components:
 *		- chessboard size
 *		- booleans whether this checkboard is in a terminal state
 *		- methods to set a position to a given or random boardposition

 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class Chessboard extends ConsoleProgram
{
	/* Size of Chessboard*/
	private int size = 6;

	/* the chessboard array */
	private char[][] CBarray;
	
	/* checkmate */
	private boolean checkMate = false;
	/* Stalemate */
	private boolean staleMate = false;
	
	
	/* Pieces */
	public BlackKing blackKing;
	public WhiteKing whiteKing;
	public Rook rook;	
	
	/* BoardPosition */
	public BoardPosition thisPosition;	
	
	/* to access extra methods */
	Extra extra = new Extra();
	
	// Constructors
	public Chessboard(BoardPosition boardPos)
	{
		CBarray = new char[size][size];

		blackKing = new BlackKing(boardPos.getk(), this);
		whiteKing = new WhiteKing(boardPos.getK(), this);					
		rook = new Rook(boardPos.getR(), this);

		fillEmptyChessboard();
		addKRKtoChessboard();
		
		thisPosition = boardPos;
	}
	
	public Chessboard()
	{
	}
	
	public void setBoardPosition(BoardPosition boardPos)
	{
		CBarray = new char[size][size];

		blackKing = new BlackKing(boardPos.getk(), this);
		whiteKing = new WhiteKing(boardPos.getK(), this);					
		rook = new Rook(boardPos.getR(), this);

		fillEmptyChessboard();
		addKRKtoChessboard();
		
		thisPosition = boardPos;
		
		staleMate = false;
		checkMate = false;
		rook.setRookAlive(true);
	}

/////////////////////////////////////////////////////////////////////////
// Rearranging the Chessboard
/////////////////////////////////////////////////////////////////////////

	// fill chessboard with "."
	public void fillEmptyChessboard()
	{
		for(int j=0; j<CBarray.length; j++)
		{
			for(int i=0; i<CBarray.length; i++)
			{
				CBarray[i][j] = '.';
			}
		}
	}
	
	// add the pieces to the board
	public void addKRKtoChessboard()
	{
		CBarray[blackKing.getx()][blackKing.gety()] = 'k';
		CBarray[whiteKing.getx()][whiteKing.gety()] = 'K';
		if(rook.getRookAlive())
		{
			CBarray[rook.getx()][rook.gety()] = 'R'; 
		}
	}	
	
	
/////////////////////////////////////////////////////////////////////////
// getters and setters
/////////////////////////////////////////////////////////////////////////
		
	public BoardPosition getBoardPosition()
	{
		GPoint k = new GPoint(blackKing.getx(), blackKing.gety());
		GPoint K = new GPoint(whiteKing.getx(), whiteKing.gety());
		GPoint R = new GPoint(rook.getx(), rook.gety());
		BoardPosition currentPos = new BoardPosition(k,K,R);
		return currentPos;
	}
	
	public char[][] getCBarray()
	{
		return CBarray;
	}
	
	public int getCBSize()
	{
		return size;
	}

	public boolean getStaleMate()
	{
		return staleMate;
	} 
	public boolean getCheckMate()
	{
		return checkMate;
	} 	
	public void setStaleMate(boolean flag)
	{
		staleMate = flag;
	} 
	public void setCheckMate(boolean flag)
	{
		checkMate = flag;
	} 
	
	// reset to the same initial position
	public void resetPosition(BoardPosition pos)
	{
		blackKing.setx((int)pos.getk().getX());
		blackKing.sety((int)pos.getk().getY());
		whiteKing.setx((int)pos.getK().getX());
		whiteKing.sety((int)pos.getK().getY());
		rook.setx((int)pos.getR().getX());
		rook.sety((int)pos.getR().getY());
		
		staleMate = false;
		checkMate = false;
		rook.setRookAlive(true);
		
		fillEmptyChessboard();
		addKRKtoChessboard();
	}
	
	// reset to a random position
	public void resetRandom()
	{
	
	// black king 		
		// size min 1 because of array bounds
		int blackKingX = extra.randInt(0,size-1);
		int blackKingY = extra.randInt(0,size-1);
		
		blackKing.setx(blackKingX);
		blackKing.sety(blackKingY);
		
	// white king
		int whiteKingX = extra.randInt(0,size-1);
		int whiteKingY = extra.randInt(0,size-1);
		
		while(whiteKingX == blackKingX && whiteKingY == blackKingY)
		{
			whiteKingX = extra.randInt(0,size-1);
			whiteKingY = extra.randInt(0,size-1);
		}
		
		whiteKing.setx(whiteKingX);
		whiteKing.sety(whiteKingY);
		
	// white rook	
		int whiteRookX = extra.randInt(0,size-1);
		int whiteRookY = extra.randInt(0,size-1);
		
		while((whiteRookX == blackKingX && whiteRookY == blackKingY) || (whiteRookX == whiteKingX && whiteRookY == whiteKingY))
		{
			whiteRookX = extra.randInt(0,size-1);
			whiteRookY = extra.randInt(0,size-1);
		}
			
		rook.setx(whiteRookX);
		rook.sety(whiteRookY);
		
		GPoint kCoor = new GPoint(blackKingX, blackKingY);
		
		if(!blackKing.kingNotInCheck(kCoor))
		{
			//Recursion!
			resetRandom();
		}
		staleMate = false;
		checkMate = false;
		rook.setRookAlive(true);

		fillEmptyChessboard();
		addKRKtoChessboard();

	}
	
	public void printBoard()
	{
	// printing
		System.out.print("After move");
		System.out.println();
				
		for(int j=0; j<getCBSize(); j++)
		{
			for(int k=0; k<getCBSize(); k++)
			{
				System.out.print(getCBarray()[k][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}

















