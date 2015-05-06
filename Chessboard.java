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
	
	/* boolean rook is alive */
	boolean rookAlive = true;
	
	/* checkmate */
	boolean checkMate = false;
	
	/* Stalemate */
	boolean staleMate = false;
	
	/* Size of Chessboard*/
	private int size = 3;
		
	/* k starting position */
	private int kx;
	private int ky;
	/* K starting position */
	private int Kx;
	private int Ky;
	/* R starting position */
	private int Rx;
	private int Ry;
	
	
	// Constructors
	public Chessboard(BoardPosition boardPos)
	{
		CBarray = new char[size][size];
		kx = (int)boardPos.getk().getX();
		ky = (int)boardPos.getk().getY();
		Kx = (int)boardPos.getK().getX();
		Ky = (int)boardPos.getK().getY();
		Rx = (int)boardPos.getR().getX();
		Ry = (int)boardPos.getR().getY();
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
		CBarray[kx][ky] = 'k';
		CBarray[Kx][Ky] = 'K';
		if(rookAlive)
		{
			CBarray[Rx][Ry] = 'R'; 
		}
	}
	
	
	
/////////////////////////////////////////////////////////////////////////
// Moving the black king
/////////////////////////////////////////////////////////////////////////	
	
	public void randomMovek()
	{
		int random = randInt(1,8);
		int testkx = kx;
		int testky = ky;
		boolean legalMove = false;
		
		GPoint oldkCoor = new GPoint(kx, ky);
		
		// to check if the black king has no moves
		int[] tried = new int[8];
		int[] all = {1,2,3,4,5,6,7,8};
		
		while(!legalMove)
		{
			testkx = kx;
			testky = ky;
			random = randInt(1,8);
	
			if(sameArray(tried, all))
			{
				if(kingNotInCheck(oldkCoor))
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
//  Moving the white pieces
/////////////////////////////////////////////////////////////////////
	
	public boolean randomMoveR()
	{
		ArrayList<BoardPosition> posMovesR = posMovesR();

		System.out.println(posMovesR.size()); 
		
		if(posMovesR.size() == 0)
		{
			return false;
		}
		int rand = randInt(0,posMovesR.size()-1);
		BoardPosition boardPos = posMovesR.get(rand);
		
		Rx = (int)boardPos.getR().getX();
		Ry = (int)boardPos.getR().getY();
		
		fillEmptyChessboard();
		addKRKtoChessboard();

		return true;
	}
	
	public ArrayList<BoardPosition> posMovesR()
	{
		ArrayList<BoardPosition> posMovesR = new ArrayList<BoardPosition>();
		int testRx = Rx;
		int testRy = Ry;
	
	   	GPoint RCoor;
		
		if(Ry == Ky)
		{
			if(Rx < Kx)
			{
				for(testRx=0;testRx<Kx;testRx++)
				{
					RCoor = new GPoint(testRx, testRy);
					posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
				}
			}
			else if(Rx > Kx)
			{
				for(testRx=Kx+1;testRx<size;testRx++)
				{
					RCoor = new GPoint(testRx, testRy);
					posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
				}
			}
		}
		else
		{
			for(testRx=0;testRx<size;testRx++)
			{
				RCoor = new GPoint(testRx, testRy);
				posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
			}
		}
		
		// reset testRx
		testRx = Rx;
		 
		if(Rx == Kx)
		{
			if(Ry < Ky)
			{
				for(testRy=0;testRy<Ky;testRy++)
				{
					RCoor = new GPoint(testRx, testRy);
					posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
				}
			}
			else if(Ry > Ky)
			{
				for(testRy=Ky+1;testRy<size;testRy++)
				{
					RCoor = new GPoint(testRx, testRy);
					posMovesR = checkAddRookBoardPosition(RCoor, posMovesR);
				}
			}
		}
		else
		{
			for(testRy=0;testRy<size;testRy++)
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
			
		GPoint kCoor = new GPoint(kx, ky);
		GPoint KCoor = new GPoint(Kx, Ky);
		
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
		if((Coor.getX() == kx && Coor.getY() == ky) || (Coor.getX() == Kx && Coor.getY() == Ky) || (Coor.getX() == Rx && Coor.getY() == Ry))
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
		int rand = randInt(0,posMovesK.size()-1);
		BoardPosition boardPos = posMovesK.get(rand);
		
		Kx = (int)boardPos.getK().getX();
		Ky = (int)boardPos.getK().getY();
		
		fillEmptyChessboard();
		addKRKtoChessboard();
		

		return true;		
	}

	// to move white King
	private ArrayList<BoardPosition> posMovesK()
	{
		ArrayList<BoardPosition> posMovesK = new ArrayList<BoardPosition>();
		int testKx = Kx;
		int testKy = Ky;
		
		for(int i=1; i<9;i++)
		{
			switch (i) {
		        case 1:  testKx = Kx-1;
		        		 testKy = Ky-1;
		                 break;
		        case 2:  testKy = Ky-1;
		                 break;
		        case 3:  testKx = Kx+1;
		        		 testKy = Ky-1;
		                 break;
		        case 4:  testKx = Kx-1;
		                 break;
		        case 5:  testKx = Kx+1;
		                 break;
		        case 6:  testKx = Kx-1;
		        		 testKy = Ky+1;
		                 break;
		        case 7:  testKy = Ky+1;
		                 break;
		        case 8:  testKx = Kx+1;
		        		 testKy = Ky+1;
		                 break;
		   	}
		   	GPoint KCoor = new GPoint(testKx, testKy);
        	boolean legalMove = checkIfLegalMoveK(KCoor); 
	             
		    if(legalMove)
		    {   	
		    	GPoint kCoor = new GPoint(kx, ky);
		    	GPoint RCoor = new GPoint(Rx, Ry);
		    	
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
	
	
	
/////////////////////////////////////////////////////////////////////////
// To find the move with the highest reward
/////////////////////////////////////////////////////////////////////////	
	
	public ArrayList<BoardPosition> findAllPosNextStates()
	{
		ArrayList<BoardPosition> posPositions = new ArrayList<BoardPosition>();
		
		ArrayList<BoardPosition> posKPositions = posMovesK();
		ArrayList<BoardPosition> posRPositions = posMovesR();
		
		for(int i=0;i<posKPositions.size();i++)
		{
			posPositions.add(posKPositions.get(i));
		}
		
		for(int i=0;i<posRPositions.size();i++)
		{
			posPositions.add(posRPositions.get(i));
		}
		return posPositions;
	}
	
	
/////////////////////////////////////////////////////////////////////////
// Feature calculation
/////////////////////////////////////////////////////////////////////////
	
	// FEATURE: num of pos squares k
	public double noOfPosSquaresk()
	{
		double numOfPosSquares = 0;	
		
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
	

	
/////////////////////////////////////////////////////////////////////////
// Extra methods
/////////////////////////////////////////////////////////////////////////

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
	
	public BoardPosition getBoardPosition()
	{
		GPoint k = new GPoint(kx, ky);
		GPoint K = new GPoint(Kx, Ky);
		GPoint R = new GPoint(Rx, Ry);
		BoardPosition currentPos = new BoardPosition(k, K, R);
		return currentPos;
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
	
	public char[][] getCBarray()
	{
		return CBarray;
	}
	
	public int getCBSize()
	{
		return size;
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
}

















