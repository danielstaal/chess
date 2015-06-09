/*
 * File: BlackKing.java
 * -------------------
 * Name: Daniel Staal
 * 
 * Main Components
 * - make a random move with the black king
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
	
	private boolean rookIsTaken = false;
	
	private boolean alwaysTakingRook = false;
	
	public BlackKing(GPoint coor, Chessboard board)
	{
		super(coor, board);
	}
	
/////////////////////////////////////////////////////////////////////////
// Moving the black king
/////////////////////////////////////////////////////////////////////////	
	
	public void randomMovek()
	{
		rookIsTaken = false;
	
		ArrayList<BoardPosition> posMovesk = posMovesk();
		
		GPoint kCoor = CB.blackKing.getCoordinates();
		
		if(posMovesk.size() == 0)
		{
			if(kingNotInCheck(kCoor))
			{
				CB.setStaleMate(true);
			}
			else{CB.setCheckMate(true);}
		}
		else if(!rookIsTaken)
		{
			int rand = extra.randInt(0,posMovesk.size()-1);
			BoardPosition boardPos = posMovesk.get(rand);
		
			CB.blackKing.setx((int)boardPos.getk().getX());
			CB.blackKing.sety((int)boardPos.getk().getY());
				    	
			if(checkIfBlackKingTakesRook(CB.rook.getCoordinates(), CB.blackKing.getCoordinates()))
			{
				CB.rook.setRookAlive(false);
			}
				    	
			CB.fillEmptyChessboard();
			CB.addKRKtoChessboard();
		}
		
    }
    
    public ArrayList<BoardPosition> posMovesk()
	{
		ArrayList<BoardPosition> posMovesk = new ArrayList<BoardPosition>();
		
		GPoint kCoor = null;
		
		for(int i=1; i<9;i++)
		{
			switch (i) {
		        case 1:  kCoor = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety()-1);
		                 break;
		        case 2:  kCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety()-1);
		                 break;
		        case 3:  kCoor = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety()-1);
		                 break;
		        case 4:  kCoor = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety());
		                 break;
		        case 5:  kCoor = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety());
		                 break;
		        case 6:  kCoor = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety()+1);
		                 break;
		        case 7:  kCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety()+1);
		                 break;
		        case 8:  kCoor = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety()+1);
		                 break;
		   	}
        	boolean legalMove = checkIfLegalMovek(kCoor); 
	             
		    if(legalMove)
		    {   	
		    	GPoint KCoor = new GPoint(CB.whiteKing.getx(), CB.whiteKing.gety());
		    	GPoint RCoor = new GPoint(CB.rook.getx(), CB.rook.gety());
		    	
		    	BoardPosition pos = new BoardPosition(kCoor, KCoor, RCoor);
		    	posMovesk.add(pos);
		    }
        }

        return posMovesk;
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
	public boolean notCheckR(GPoint kCoor)
	{
		GPoint RCoor = new GPoint(CB.rook.getx(), CB.rook.gety());
		
		// if a row or column is the same, it is check unless black king takes
		// rook or the white king is between the rook and the black king
		if(kCoor.getX() == RCoor.getX() || kCoor.getY() == RCoor.getY())
		{
			if(checkIfBlackKingTakesRook(RCoor, kCoor))
			{
				// if it is set that the black king takes the white rook if possible
				if(alwaysTakingRook)
				{
					takeTheRook(kCoor);
				}
				return true;
			}
			else if(whiteKingBetweenOnYaxis(kCoor))
			{
				return true;
			}
			else if(whiteKingBetweenOnXaxis(kCoor))
			{
				return true;
			}
			return false;
		} 
		return true;
	}
	
	private boolean checkIfBlackKingTakesRook(GPoint RCoor, GPoint kCoor)
	{
			if(kCoor.getX() == CB.rook.getx() && kCoor.getY() == CB.rook.gety() && notCheckK(RCoor))
			{
				return true;
			}
			return false;
	}
	
	private boolean whiteKingBetweenOnYaxis(GPoint kCoor)
	{
		if(CB.whiteKing.gety() == CB.rook.gety() && CB.rook.gety() == kCoor.getY())
		{
			if((CB.whiteKing.getx() < CB.rook.getx() && CB.whiteKing.getx() > kCoor.getX()) || (CB.whiteKing.getx() > CB.rook.getx() && CB.whiteKing.getx() < kCoor.getX()))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean whiteKingBetweenOnXaxis(GPoint kCoor)
	{
		if(CB.whiteKing.getx() == CB.rook.getx() && CB.rook.getx() == kCoor.getX())
		{
			// if king in between on x row
			if((CB.whiteKing.gety() < CB.rook.gety() && CB.whiteKing.gety() > kCoor.getY()) || (CB.whiteKing.gety() > CB.rook.gety() && CB.whiteKing.gety() < kCoor.getY()))
			{
				return true;
			}
		}
		return false;
	}
	
	private void takeTheRook(GPoint kCoor)
	{
		CB.rook.setRookAlive(false);
		CB.blackKing.setx((int)kCoor.getX());
		CB.blackKing.sety((int)kCoor.getY());

		CB.fillEmptyChessboard();
		CB.addKRKtoChessboard();
		rookIsTaken = true;
	}
	
	public void setAlwaysTakingRook(boolean flag)
	{
		alwaysTakingRook = flag;
	}
	
//	private void randomMovek()
//	{	
//		int random = extra.randInt(1,8);
//		int testkx = CB.blackKing.getx();
//		int testky = CB.blackKing.gety();
//		boolean legalMove = false;
//		
//		GPoint oldkCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety());
//		
//		// to check if the black king has no moves
//		int[] tried = new int[8];
//		int[] all = {1,2,3,4,5,6,7,8};
//		
//		while(!legalMove)
//		{
//			testkx = CB.blackKing.getx();
//			testky = CB.blackKing.gety();
//			random = extra.randInt(1,8);
//	
//			if(extra.sameArray(tried, all))
//			{
//				if(kingNotInCheck(oldkCoor))
//				{
//					CB.setStaleMate(true);
//				}
//				else{CB.setCheckMate(true);}
//				break;
//			}
//		
//			switch (random) {
//		        case 1:  testkx--;
//		        		 testky--;
//		                 break;
//		        case 2:  testky--;
//		                 break;
//		        case 3:  testkx++;
//		        		 testky--;
//		                 break;
//		        case 4:  testkx--;
//		                 break;
//		        case 5:  testkx++;
//		                 break;
//		        case 6:  testkx--;
//		        		 testky++;
//		                 break;
//		        case 7:  testky++;
//		                 break;
//		        case 8:  testkx++;
//		        		 testky++;
//		                 break;
//		    }
//		    GPoint kCoor = new GPoint(testkx, testky);
//		    legalMove = checkIfLegalMovek(kCoor);
//		         
//		    if(legalMove)
//		    {   	
//		    	CB.blackKing.setx(testkx);
//		    	CB.blackKing.sety(testky);
//		    	if(CB.blackKing.getx() == CB.rook.getx() && CB.blackKing.gety() == CB.rook.gety())
//		    	{
//		    		CB.rook.setRookAlive(false);
//		    	}
//		    	CB.fillEmptyChessboard();
//		    	CB.addKRKtoChessboard();
//		    }
//		    else
//		    {
//		    	tried[random-1] = random;
//		    }
//		    if(!CB.rook.getRookAlive())
//		    {
//		    	break;
//		    }
//     	}
//    }
}












