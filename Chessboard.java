/*
 * File: Chessboard.java
 * -------------------
 * Name: Daniel Staal
 * 
 * In this file the chessBoard is described
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

	/* the chessboard array */
	private char[][] CBarray;
	
	/* checkmate */
	private boolean checkMate = false;
	/* Stalemate */
	private boolean staleMate = false;
	
	/* Size of Chessboard*/
	private int size = 5;
	
	/* Pieces */
	BlackKing blackKing;
	WhiteKing whiteKing;
	Rook rook;		
	
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
	}
	public Chessboard()
	{
	}

/////////////////////////////////////////////////////////////////////////
// Rearranging the Chessboard
/////////////////////////////////////////////////////////////////////////

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
	}
}

















