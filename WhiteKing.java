/*
 * File: WhiteKing.java
 * -------------------
 * Name: Daniel Staal
 * 
 * Main Components
 * - making random move with the white king
 * - Retrieving all possible next positions after a move with the white king
 * -
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class WhiteKing extends Piece
{
	public WhiteKing(GPoint coor, Chessboard board)
	{
		super(coor, board);
	}

	public boolean randomMoveK()
	{
		ArrayList<BoardPosition> posMovesK = posMovesK();
		
		if(posMovesK.size() == 0)
		{
			return false;
		}
		int rand = extra.randInt(0,posMovesK.size()-1);
		BoardPosition boardPos = posMovesK.get(rand);
		
		CB.whiteKing.setx((int)boardPos.getK().getX());
		CB.whiteKing.sety((int)boardPos.getK().getY());
		
		CB.fillEmptyChessboard();
		CB.addKRKtoChessboard();
		

		return true;		
	}

	/*
	- create an arraylist of all possible next states after a move with the white king 
	- if a move is not legal don't put it in the array
	- return the possible next boardpositions
	*/
	public ArrayList<BoardPosition> posMovesK()
	{
		ArrayList<BoardPosition> posMovesK = new ArrayList<BoardPosition>();
		
		GPoint KCoor = null;
		
		for(int i=1; i<9;i++)
		{
			switch (i) {
		        case 1:  KCoor = new GPoint(CB.whiteKing.getx()-1, CB.whiteKing.gety()-1);
		                 break;
		        case 2:  KCoor = new GPoint(CB.whiteKing.getx(), CB.whiteKing.gety()-1);
		                 break;
		        case 3:  KCoor = new GPoint(CB.whiteKing.getx()+1, CB.whiteKing.gety()-1);
		                 break;
		        case 4:  KCoor = new GPoint(CB.whiteKing.getx()-1, CB.whiteKing.gety());
		                 break;
		        case 5:  KCoor = new GPoint(CB.whiteKing.getx()+1, CB.whiteKing.gety());
		                 break;
		        case 6:  KCoor = new GPoint(CB.whiteKing.getx()-1, CB.whiteKing.gety()+1);
		                 break;
		        case 7:  KCoor = new GPoint(CB.whiteKing.getx(), CB.whiteKing.gety()+1);
		                 break;
		        case 8:  KCoor = new GPoint(CB.whiteKing.getx()+1, CB.whiteKing.gety()+1);
		                 break;
		   	}
        	boolean legalMove = checkIfLegalMoveK(KCoor); 
	             
		    if(legalMove)
		    {   	
		    	GPoint kCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety());
		    	GPoint RCoor = new GPoint(CB.rook.getx(), CB.rook.gety());
		    	
		    	BoardPosition pos = new BoardPosition(kCoor, KCoor, RCoor);
		    	posMovesK.add(pos);
		    }
        }

        return posMovesK;
	}
	
	// a move is legal if:
	//	- the king is not in check
	//	- the king is on the chessboard
	//	- there is not other piece on this square
	private boolean checkIfLegalMoveK(GPoint KCoor)
	{
		boolean notCheck = KingNotInCheck(KCoor);
		// these two methods are in the parent class: Piece.java
		boolean onChessBoard = onChessBoard(KCoor);
		boolean noOtherPiece = noOtherPiece(KCoor);
		
		if(notCheck && onChessBoard && noOtherPiece)
		{	
			return true;
		}
		return false;
	}
	
	private boolean KingNotInCheck(GPoint KCoor)
	{
		boolean K = notCheckBlackKing(KCoor);
		if(K)
		{
			return true;
		} 
		return false;
	}
	
	public boolean notCheckBlackKing(GPoint KCoor)
	{
		if((KCoor.getX() >= CB.blackKing.getx()-1 && KCoor.getX() <= CB.blackKing.getx() + 1) && (KCoor.getY() >= CB.blackKing.gety()-1 && KCoor.getY() <= CB.blackKing.gety() + 1))
		{
			return false;
		}
		return true;
	}	
}
