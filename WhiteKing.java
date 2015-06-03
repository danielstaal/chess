/*
 * File: Chessboard.java
 * -------------------
 * Name: Daniel Staal
 * 
 * Main Components
 * - 
 * -
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

	// to move white King
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
	
	private boolean checkIfLegalMoveK(GPoint KCoor)
	{
		boolean check = KingNotInCheck(KCoor);
		boolean on = onChessBoard(KCoor);
		boolean noOtherPiece = noOtherPiece(KCoor);
		if(check && on && noOtherPiece)
		{	
			return true;
		}
		return false;
	}
	
	private boolean KingNotInCheck(GPoint KCoor)
	{
		boolean K = notCheckk(KCoor);
		if(K)
		{
			return true;
		} 
		return false;
	}
	
	public boolean notCheckk(GPoint KCoor)
	{
		if((KCoor.getX() >= CB.blackKing.getx()-1 && KCoor.getX() <= CB.blackKing.getx() + 1) && (KCoor.getY() >= CB.blackKing.gety()-1 && KCoor.getY() <= CB.blackKing.gety() + 1))
		{
			return false;
		}
		return true;
	}	
}
