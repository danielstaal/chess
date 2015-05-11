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


public class Rook extends Piece
{
	private boolean rookAlive = true;	
	
	public Rook(GPoint coor, Chessboard board)
	{
		super(coor, board);
	}
	
	public void setRookAlive(boolean flag)
	{
		rookAlive = flag;
	} 
	public boolean getRookAlive()
	{
		return rookAlive;
	} 
	
	/// rook moves
	/////////////////////////////////////////////////////////////////////
//  Moving the white pieces
/////////////////////////////////////////////////////////////////////
	public boolean randomMoveR()
	{
		ArrayList<BoardPosition> posMovesR = posMovesR();
		
		if(posMovesR.size() == 0)
		{
			return false;
		}
		int rand = extra.randInt(0,posMovesR.size()-1);
		BoardPosition boardPos = posMovesR.get(rand);
		
		CB.rook.setx((int)boardPos.getR().getX());
		CB.rook.sety((int)boardPos.getR().getY());
		
		CB.fillEmptyChessboard();
		CB.addKRKtoChessboard();

		return true;
	}
	
	public ArrayList<BoardPosition> posMovesR()
	{
		ArrayList<BoardPosition> posMovesR = new ArrayList<BoardPosition>();
		int testRx = CB.rook.getx();
		int testRy = CB.rook.gety();
	
	   	GPoint RCoor;
		
		if(CB.rook.gety() == CB.whiteKing.gety())
		{
			if(CB.rook.getx() < CB.whiteKing.getx())
			{
				for(testRx=0;testRx<CB.whiteKing.getx();testRx++)
				{
					RCoor = new GPoint(testRx, testRy);
					posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
				}
			}
			else if(CB.rook.getx() > CB.whiteKing.getx())
			{
				for(testRx=CB.whiteKing.getx()+1;testRx<CB.getCBSize();testRx++)
				{
					RCoor = new GPoint(testRx, testRy);
					posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
				}
			}
		}
		else
		{
			for(testRx=0;testRx<CB.getCBSize();testRx++)
			{
				RCoor = new GPoint(testRx, testRy);
				posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
			}
		}
		
		// reset testRx
		testRx = CB.rook.getx();
		 
		if(CB.rook.getx() == CB.whiteKing.getx())
		{
			if(CB.rook.gety() < CB.whiteKing.gety())
			{
				for(testRy=0;testRy<CB.whiteKing.gety();testRy++)
				{
					RCoor = new GPoint(testRx, testRy);
					posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
				}
			}
			else if(CB.rook.gety() > CB.whiteKing.gety())
			{
				for(testRy=CB.whiteKing.gety()+1;testRy<CB.getCBSize();testRy++)
				{
					RCoor = new GPoint(testRx, testRy);
					posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
				}
			}
		}
		else
		{
			for(testRy=0;testRy<CB.getCBSize();testRy++)
			{
				RCoor = new GPoint(testRx, testRy);
				posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
			}
		}
		
		return posMovesR;
	}
	
	private ArrayList<BoardPosition> checkAddRookBoardPosition(GPoint RCoor, ArrayList<BoardPosition> posMovesR)
	{
		BoardPosition boardPos;
			
		GPoint kCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety());
		GPoint KCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety());
		
		boolean legalMove = checkIfLegalMoveR(RCoor);
		if(legalMove)
		{
			boardPos = new BoardPosition(kCoor,KCoor,RCoor);
			posMovesR.add(boardPos);
		}
		return posMovesR;
	}
	
	private boolean checkIfLegalMoveR(GPoint RCoor)
	{
		boolean on = onChessBoard(RCoor);
		boolean noOtherPiece = noOtherPiece(RCoor);
		//boolean notThroughPiece = notThroughPiece(RCoor);
		if(on && noOtherPiece)
		{
			return true;
		}
		return false;
	}
}










