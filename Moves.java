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


public class Moves
{

/////////////////////////////////////////////////////////////////////////
// Moving the black king
/////////////////////////////////////////////////////////////////////////	
	private Extra extra = new Extra();
	
	private Chessboard CB;
	
	public Moves(Chessboard board)
	{
		CB = board;
	}
	
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
	
	private boolean kingNotInCheck(GPoint kCoor)
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
	private boolean notCheckK(GPoint kCoor)
	{
		if((kCoor.getX() >= CB.whiteKing.getx()-1 && kCoor.getX() <= CB.whiteKing.getx() + 1) && (kCoor.getY() >= CB.whiteKing.gety()-1 && kCoor.getY() <= CB.whiteKing.gety() + 1))
		{
			return false;
		}
		return true;
	}
	
	// k doesnt make a move so R attacks it
	private boolean notCheckR(GPoint kCoor)
	{
		// to check if rook is supported by white king
		GPoint RCoor = new GPoint(CB.rook.getx(), CB.rook.gety());
		
		if(kCoor.getX() == CB.rook.getx() || kCoor.getY() == CB.rook.gety())
		{
			// check if rook is supported by K
			if(kCoor.getX() == CB.rook.getx() && kCoor.getY() == CB.rook.gety() && notCheckK(RCoor))
			{
				return true;
			}
			else if(CB.whiteKing.gety() == CB.rook.gety())
			{
				// if king in between on x row
				if((CB.whiteKing.getx() < CB.rook.getx() && CB.whiteKing.getx() > kCoor.getX()) || (CB.whiteKing.getx() > CB.rook.getx() && CB.whiteKing.getx() < kCoor.getX()))
				{
					return true;
				}
			}
			else if(CB.whiteKing.getx() == CB.rook.getx())
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

	private boolean noOtherPiece(GPoint Coor)
	{
		if((Coor.getX() == CB.blackKing.getx() && Coor.getY() == CB.blackKing.gety()) || (Coor.getX() == CB.whiteKing.getx() && Coor.getY() == CB.whiteKing.gety()) || (Coor.getX() == CB.rook.getx() && Coor.getY() == CB.rook.gety()))
		{
			return false;
		} 
		return true;
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
		int testKx = CB.whiteKing.getx();
		int testKy = CB.whiteKing.getx();
		
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
	
	private boolean notCheckk(GPoint kCoor)
	{
		if((kCoor.getX() >= CB.blackKing.getx()-1 && kCoor.getX() <= CB.blackKing.getx() + 1) && (kCoor.getY() >= CB.blackKing.gety()-1 && kCoor.getY() <= CB.blackKing.gety() + 1))
		{
			return false;
		}
		return true;
	}	
	
	private boolean onChessBoard(GPoint Coor)
	{
		if((Coor.getX() < CB.getCBSize() && Coor.getX() >= 0) && (Coor.getY() < CB.getCBSize() && Coor.getY() >= 0))
		{
			return true;
		}
		return false;
	}	
	
	
	

}
