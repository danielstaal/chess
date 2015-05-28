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


public class BlackKing extends Piece
{
	
	public BlackKing(GPoint coor, Chessboard board)
	{
		super(coor, board);
	}
	
/////////////////////////////////////////////////////////////////////////
// Moving the black king
/////////////////////////////////////////////////////////////////////////	
	
	public void randomMovek()
	{
		int random = extra.randInt(1,8);
		int testkx = CB.blackKing.getx();
		int testky = CB.blackKing.gety();
		boolean legalMove = false;
		
		GPoint oldkCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety());
		
		// to check if the black king has no moves
		int[] tried = new int[8];
		int[] all = {1,2,3,4,5,6,7,8};
		
		while(!legalMove)
		{
			testkx = CB.blackKing.getx();
			testky = CB.blackKing.gety();
			random = extra.randInt(1,8);
	
			if(extra.sameArray(tried, all))
			{
				if(kingNotInCheck(oldkCoor))
				{
					CB.setStaleMate(true);
				}
				else{CB.setCheckMate(true);}
				break;
			}
		
			switch (random) {
		        case 1:  testkx--;
		        		 testky--;
		                 break;
		        case 2:  testky--;
		                 break;
		        case 3:  testkx++;
		        		 testky--;
		                 break;
		        case 4:  testkx--;
		                 break;
		        case 5:  testkx++;
		                 break;
		        case 6:  testkx--;
		        		 testky++;
		                 break;
		        case 7:  testky++;
		                 break;
		        case 8:  testkx++;
		        		 testky++;
		                 break;
		    }
		    GPoint kCoor = new GPoint(testkx, testky);
		    legalMove = checkIfLegalMovek(kCoor);
		         
		    if(legalMove)
		    {   	
		    	CB.blackKing.setx(testkx);
		    	CB.blackKing.sety(testky);
		    	if(CB.blackKing.getx() == CB.rook.getx() && CB.blackKing.gety() == CB.rook.gety())
		    	{
		    		CB.rook.setRookAlive(false);
		    	}
		    	CB.fillEmptyChessboard();
		    	CB.addKRKtoChessboard();
		    }
		    else
		    {
		    	tried[random-1] = random;
		    }
		    if(!CB.rook.getRookAlive())
		    {
		    	break;
		    }
     	}
    }
	
	public boolean checkIfLegalMovek(GPoint kCoor)
	{
		boolean check = kingNotInCheck(kCoor);
		boolean on = onChessBoard(kCoor);
		if(check && on)
		{	
			return true;
		}
		return false;
	}
	
	public boolean kingNotInCheck(GPoint kCoor)
	{
		boolean K = notCheckK(kCoor);
		boolean R = notCheckR(kCoor);
		
		if(K && R)
		{
			return true;
		} 
		return false;
	}
	
	// k doesnt make a move so K attacks it
	public boolean notCheckK(GPoint kCoor)
	{
		if((kCoor.getX() >= CB.whiteKing.getx()-1 && kCoor.getX() <= CB.whiteKing.getx() + 1) && (kCoor.getY() >= CB.whiteKing.gety()-1 && kCoor.getY() <= CB.whiteKing.gety() + 1))
		{
			return false;
		}
		return true;
	}
	
	// k doesnt make a move so R attacks it
	
	//TODO: check if this works
	public boolean notCheckR(GPoint kCoor)
	{
		// to check if rook is supported by white king
		GPoint RCoor = new GPoint(CB.rook.getx(), CB.rook.gety());
		
		if(kCoor.getX() == CB.rook.getx() || kCoor.getY() == CB.rook.gety())
		{
			// king takes rook if not protected
			if(kCoor.getX() == CB.rook.getx() && kCoor.getY() == CB.rook.gety() && notCheckK(RCoor))
			{
				return true;
			}
			else if(CB.whiteKing.gety() == CB.rook.gety() && CB.rook.gety() == kCoor.getY())
			{
				// if king in between on x row
				if((CB.whiteKing.getx() < CB.rook.getx() && CB.whiteKing.getx() > kCoor.getX()) || (CB.whiteKing.getx() > CB.rook.getx() && CB.whiteKing.getx() < kCoor.getX()))
				{
					return true;
				}
			}
			else if(CB.whiteKing.getx() == CB.rook.getx() && CB.rook.getx() == kCoor.getX())
			{
				// if king in between on x row
				if((CB.whiteKing.gety() < CB.rook.gety() && CB.whiteKing.gety() > kCoor.getY()) || (CB.whiteKing.gety() > CB.rook.gety() && CB.whiteKing.gety() < kCoor.getY()))
				{
					return true;
				}
			}
			return false;
		} 
		return true;
	}
	
	
}












