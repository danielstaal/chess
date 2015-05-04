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
	static char[][] CBarray;
	
	/* boolean rook is alive */
	boolean rookAlive = true;
	
	/* checkmate */
	boolean checkMate = false;
	
	/* Stalemate */
	boolean staleMate = false;
	
	/* Size of Chessboard*/
	static final int size = 8;
		
	/* k starting position */
	private int kx;
	private int ky;
	/* K starting position */
	private int Kx;
	private int Ky;
	/* R starting position */
	private int Rx;
	private int Ry;
	
	public Chessboard(BoardPosition boardPos)
	{
		CBarray = new char[size][size];
		kx = (int)boardPos.getk().getX();
		ky = (int)boardPos.getk().getY();
		Kx = (int)boardPos.getK().getX();
		Ky = (int)boardPos.getK().getY();
		Rx = (int)boardPos.getR().getX();
		Ry = (int)boardPos.getR().getY();
	}
	
	public Chessboard()
	{
	}
	
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
		CBarray[kx][ky] = 'k';
		CBarray[Kx][Ky] = 'K';
		if(rookAlive)
		{
			CBarray[Rx][Ry] = 'R'; 
		}
	}
	
	public void movek()
	{
		int random = randInt(1,8);
		int testkx = kx;
		int testky = ky;
		boolean legalMove = false;
		
		GPoint oldkCoor = new GPoint(kx, ky);
		
		int[] tried = new int[8];
		int[] all = {1,2,3,4,5,6,7,8};
		
		while(!legalMove)
		{
			testkx = kx;
			testky = ky;
			random = randInt(1,8);
	
			if(sameArray(tried, all))
			{
				if(notInCheckk(oldkCoor))
				{
					staleMate = true;
				}
				else{checkMate = true;}
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
		    	kx = testkx;
		    	ky = testky;
		    	if(kx == Rx && ky == Ry)
		    	{
		    		rookAlive = false;
		    	}
		    	fillEmptyChessboard();
		    	addKRKtoChessboard();
		    }
		    else
		    {
		    	tried[random-1] = random;
		    }
		    if(!rookAlive)
		    {
		    	break;
		    }
     	}
    }
	
	private boolean checkIfLegalMovek(GPoint kCoor)
	{
		boolean check = notInCheckk(kCoor);
		boolean on = onChessBoard(kCoor);
		if(check && on)
		{	
			return true;
		}
		return false;
	}
	
	private boolean notInCheckk(GPoint kCoor)
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
		if((kCoor.getX() >= Kx-1 && kCoor.getX() <= Kx + 1) && (kCoor.getY() >= Ky-1 && kCoor.getY() <= Ky + 1))
		{
			return false;
		}
		return true;
	}
	
	// k doesnt make a move so R attacks it
	private boolean notCheckR(GPoint kCoor)
	{
		// to check if rook is supported by white king
		GPoint RCoor = new GPoint(Rx, Ry);
		
		if(kCoor.getX() == Rx || kCoor.getY() == Ry)
		{
			// check if rook is supported by K
			if(kCoor.getX() == Rx && kCoor.getY() == Ry && notCheckK(RCoor))
			{
				return true;
			}
			else if(Ky == Ry)
			{
				// if king in between on x row
				if((Kx < Rx && Kx > kCoor.getX()) || (Kx > Rx && Kx < kCoor.getX()))
				{
					return true;
				}
			}
			else if(Kx == Rx)
			{
				// if king in between on x row
				if((Ky < Ry && Ky > kCoor.getY()) || (Ky > Ry && Ky < kCoor.getY()))
				{
					return true;
				}
			}
			return false;
		} 
		return true;
	}
	
	/////////////////////////////////////////////////////////////////////
	//  White pieces
	/////////////////////////////////////////////////////////////////////
	public boolean moveR()
	{
		int testRx = Rx;
		int testRy = Ry;
		while(testRx == Rx && testRy == Ry)
		{
			int randxy = randInt(0,1);
			if(randxy == 0)
			{
				if(Ry == Ky)
				{
					if(Rx < Kx)
					{
						testRx = randInt(0, Kx-1);
					}
					else
					{
						testRx = randInt(Kx+1, 7);
					}
				}
				else{testRx = randInt(0,7);}
			}
			else
			{
				if(Rx == Kx)
				{
					if(Ry < Ky)
					{
						testRy = randInt(0, Ky-1);
					}
					else
					{
						testRy = randInt(Ky+1, 7);
					}
				}
				else{testRy = randInt(0,7);}
			}
		}
		GPoint RCoor = new GPoint(testRx, testRy);
		boolean legalMove = checkIfLegalMoveR(RCoor);
		
		if(legalMove)
        {   	
        	Rx = testRx;
        	Ry = testRy;
        	fillEmptyChessboard();
        	addKRKtoChessboard();
        }
		return legalMove;
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
		if((Coor.getX() == kx && Coor.getY() == ky) || (Coor.getX() == Kx && Coor.getY() == Ky) || (Coor.getX() == Rx && Coor.getY() == Ry))
		{
			return false;
		} 
		return true;
	}

	// to move white King
	public boolean moveK()
	{
		int random = randInt(1,8);
		int testkx = Kx;
		int testky = Ky;
		
		for(int i=1; i<9;i++)
		{
			switch (i) {
		        case 1:  testkx = kx-1;
		        		 testky = ky-1;
		                 break;
		        case 2:  testky = ky-1;
		                 break;
		        case 3:  testkx = kx+1;
		        		 testky = ky-1;
		                 break;
		        case 4:  testkx = kx-1;
		                 break;
		        case 5:  testkx = kx+1;
		                 break;
		        case 6:  testkx = kx-1;
		        		 testky = ky+1;
		                 break;
		        case 7:  testky = ky+1;
		                 break;
		        case 8:  testkx = kx+1;
		        		 testky = ky+1;
		                 break;
		   	}
		   	GPoint KCoor = new GPoint(testkx, testky);
        	boolean legalMove = checkIfLegalMoveK(KCoor); 
	             
		    if(legalMove)
		    {   	
		    	testkx;
		    	testky;
		    	//fillEmptyChessboard();
		    	//addKRKtoChessboard();
		    }
        }

        return legalMove;
	}
	
	private boolean checkIfLegalMoveK(GPoint KCoor)
	{
		boolean check = notInCheckK(KCoor);
		boolean on = onChessBoard(KCoor);
		boolean noOtherPiece = noOtherPiece(KCoor);
		if(check && on && noOtherPiece)
		{	
			return true;
		}
		return false;
	}
	
	private boolean notInCheckK(GPoint KCoor)
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
		if((kCoor.getX() >= kx-1 && kCoor.getX() <= kx + 1) && (kCoor.getY() >= ky-1 && kCoor.getY() <= ky + 1))
		{
			return false;
		}
		return true;
	}	
	
	private boolean onChessBoard(GPoint Coor)
	{
		if((Coor.getX() < size && Coor.getX() >= 0) && (Coor.getY() < size && Coor.getY() >= 0))
		{
			return true;
		}
		return false;
	}

	
	private boolean sameArray(int[] A, int[] B)
	{
		boolean result = true;
		for(int i=0; i<A.length;i++)
		{
			if(A[i] != B[i])
			{
				return false;
			}
		}
		
		return result;
	}
	
	public int getkx()
	{
		return kx;
	}
	public int getky()
	{
		return ky;
	}
	public int getKx()
	{
		return Kx;
	}
	public int getKy()
	{
		return Ky;
	}
	public int getRx()
	{
		return Rx;
	}
	public int getRy()
	{
		return Ry;
	}
	
	public static int randInt(int min, int max) {

    // NOTE: Usually this should be a field rather than a method
    // variable so that it is not re-seeded every call.
    Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
	}	
	
	public ArrayList<BoardPosition> findAllPosNextStates()
	{
		ArrayList<BoardPosition> posPositions = new ArrayList<BoardPosition>();
		
		
		return posPositions;
	}
	
	
/////////////////////////////////////////////////////////////////////////
// Feature calculation
/////////////////////////////////////////////////////////////////////////
	
	// FEATURE: num of pos squares k
	public int noOfPosSquaresk()
	{
		int numOfPosSquares = 0;	
		
		GPoint kCoor;
		boolean legalMove;
		
		kCoor = new GPoint(kx-1, ky-1);
		if(checkIfLegalMovek(kCoor))
		{
			numOfPosSquares++;
		}
		
		kCoor = new GPoint(kx, ky-1);
		if(checkIfLegalMovek(kCoor))
		{
			numOfPosSquares++;
		}
		
		kCoor = new GPoint(kx-1, ky);
		if(checkIfLegalMovek(kCoor))
		{
			numOfPosSquares++;
		}
		
		kCoor = new GPoint(kx+1, ky+1);
		if(checkIfLegalMovek(kCoor))
		{
			numOfPosSquares++;
		}
		
		kCoor = new GPoint(kx+1, ky-1);
		if(checkIfLegalMovek(kCoor))
		{
			numOfPosSquares++;
		}
		
		kCoor = new GPoint(kx-1, ky+1);
		if(checkIfLegalMovek(kCoor))
		{
			numOfPosSquares++;
		}
		
		kCoor = new GPoint(kx, ky+1);
		if(checkIfLegalMovek(kCoor))
		{
			numOfPosSquares++;
		}
		
		kCoor = new GPoint(kx+1, ky);
		if(checkIfLegalMovek(kCoor))
		{
			numOfPosSquares++;
		}
                 
        return numOfPosSquares;
	}
}

















