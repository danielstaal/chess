/*
 * File: Chessboard.java
 * -------------------
 * Name: Daniel Staal
 * 
 * This file will eventually implement the game of Chess.
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
	
	/* boardsize */
	static int size;
		
	/* k starting position */
	private int kx = 0;
	private int ky = 0;
	/* K starting position */
	private int Kx = 2;
	private int Ky = 2;
	/* k starting position */
	private int Rx = 3;
	private int Ry = 3;
	
	public Chessboard()
	{
	}
	
	public Chessboard(int boardSize)
	{
		size = boardSize;
		CBarray = new char[size][size];
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
		CBarray[Rx][Ry] = 'R'; 
	}
	
	public boolean movek()
	{
		int random = randInt(1,8);
		int testkx = kx;
		int testky = ky;
		
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
        boolean legalMove = checkIfLegalMovek(kCoor);
             
        if(legalMove)
        {   	
        	kx = testkx;
        	ky = testky;
        	fillEmptyChessboard();
        	addKRKtoChessboard();
        }
        return legalMove;
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
		boolean K = checkK(kCoor);
		boolean R = checkR(kCoor);
		if(K && R)
		{
			return true;
		} 
		return false;
	}
	
	private boolean checkK(GPoint kCoor)
	{
		if((kCoor.getX() >= Kx-1 && kCoor.getX() <= Kx + 1) && (kCoor.getY() >= Ky-1 && kCoor.getY() <= Ky + 1))
		{
			return false;
		}
		return true;
	}
	
	private boolean checkR(GPoint kCoor)
	{
		if(kCoor.getX() != Rx && kCoor.getY() != Ry)
		{
			return true;
		} 
		return false;
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
        GPoint KCoor = new GPoint(testkx, testky);
        boolean legalMove = checkIfLegalMoveK(KCoor);
             
        if(legalMove)
        {   	
        	Kx = testkx;
        	Ky = testky;
        	fillEmptyChessboard();
        	addKRKtoChessboard();
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
		boolean K = checkk(KCoor);
		if(K)
		{
			return true;
		} 
		return false;
	}
	
	private boolean checkk(GPoint kCoor)
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
}

















