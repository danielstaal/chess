/*
 * File: Chessboard.java
 * -------------------
 * Name: Daniel Staal
 * 
 * Main Components
 *	-
 *	-
 *	-
 *	-
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class FeatureCalculation
{
	
/////////////////////////////////////////////////////////////////////////
// Feature calculation
//	- Below is a list of methods to calculate all different features available
/////////////////////////////////////////////////////////////////////////
	
	private Chessboard CB;
	private GPoint whiteKingCoor;
	private GPoint blackKingCoor;
	private GPoint rookCoor;
	
	public FeatureCalculation(Chessboard board)
	{
		CB = board;
		whiteKingCoor = CB.thisPosition.getK();
		blackKingCoor = CB.thisPosition.getk();
		rookCoor = CB.thisPosition.getR();
	}
	
	public double kingsInOpposition()
	{
		if(((whiteKingCoor.getX() == blackKingCoor.getX() + 2 || whiteKingCoor.getX() == blackKingCoor.getX() - 2) && whiteKingCoor.getY() == blackKingCoor.getY()) || ((whiteKingCoor.getY() == blackKingCoor.getY() + 2 || whiteKingCoor.getY() == blackKingCoor.getY() - 2) && whiteKingCoor.getX() == blackKingCoor.getX()))
		{
			return 1.0;
		}
		
		return 0.0;
	}
	
	public double rookLost()
	{
		if(CB.rook.getx() == CB.blackKing.getx() && CB.rook.gety() == CB.blackKing.gety())
		{
			return 1.0;
		}
		return 0.0;
	}
	
	// what is the size of the 'fence' that the rooks makes around the black king
	public double squaresOfKingvsRook()
	{
		double squares = 0.0;
		
		int rookx = CB.rook.getx();
		int rooky = CB.rook.gety();
		int size = CB.getCBSize() - 1;
		
		if(CB.rook.getx() > CB.blackKing.getx())
		{
			if(CB.rook.gety() > CB.blackKing.gety())
			{
				// down right
				squares = rookx * rooky;
			}
			else
			{
				// up right
				squares = (rookx) * (size - rooky);
			}
		}
		else
		{
			if(CB.rook.gety() > CB.blackKing.gety())
			{
				// down left
				squares = (size - rookx) * (rooky);
			}
			else
			{
				// up left
				squares = (size - rookx) * (size - rooky);
			}
		}
		
		return normaliseFeature(squares, size * size);
	}
	
	public double threatenedRook()
	{
		boolean blackKingAttacksRook = false;
		if(kingReachesGPoint(blackKingCoor, rookCoor))
		{
			blackKingAttacksRook = true;
		}
		
		boolean whiteKingDefendsRook = false;	
		if(kingReachesGPoint(whiteKingCoor, rookCoor))
		{
			whiteKingDefendsRook = true;
		}
		
		if(blackKingAttacksRook && !whiteKingDefendsRook)
		{
			return 1.0;
		}
		return 0.0;
	}
	
	private boolean kingReachesGPoint(GPoint king, GPoint piece)
	{
		int kingX = (int)king.getX();
		int kingY = (int)king.getY();
		int pieceX = (int)piece.getX();
		int pieceY = (int)piece.getY();
		
		if((kingX >= pieceX - 1 && kingX <= pieceX + 1) && (kingX >= pieceX - 1 && kingX <= pieceX + 1))
		{
			return true;
		}			
		return false;
	}
	
	public double kingProtectsRook()
	{
//		GPoint RCoor = new GPoint(CB.rook.getx(), CB.rook.gety());
//		GPoint KCoor = new GPoint(CB.whiteKing.getx(), CB.whiteKing.gety());
		if(kingReachesGPoint(whiteKingCoor, rookCoor))
		{
			return 0;
		}
		return 1;
	}
	
	public double distanceBetweenWhiteRookAndBlackKing()
	{
		double xDistance = Math.abs(CB.rook.getx() - CB.blackKing.getx());
		double yDistance = Math.abs(CB.rook.gety() - CB.blackKing.gety());
		
		double totalDis = xDistance + yDistance;
		
		return normaliseFeature(totalDis, (CB.getCBSize()-1)*(CB.getCBSize()-1));
	}
	
	public double rookChecksBlackKing()
	{
//		GPoint kCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety());
		if(!CB.blackKing.notCheckR(blackKingCoor))
		{
			System.out.print("check");
			return 1;
		}
		return 0;
	}
	
	// FEATURE: distance to edge black king
	public double distanceToEdgeBlackKing()
	{
		double distance = 8;
		
		int size = CB.getCBSize();
		int x = CB.blackKing.getx();
		int y = CB.blackKing.gety();
		
		int left = x;
		int right = size - x; 
		int up = y;
		int down = size - y;
		
		if(left < distance){distance = left;}
		if(right < distance){distance = right;}
		if(up < distance){distance = up;}
		if(down < distance){distance = down;}
		
		//System.out.print(distance);
		
		return normaliseFeature(distance, CB.getCBSize()-1);
	}
	
	// FEATURE: num of pos squares k
	public double noOfPosSquaresk()
	{
		double numOfPosSquares = 0;	
		
		GPoint blackKingCoor2;
		boolean legalMove;
		
		blackKingCoor2 = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety()-1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2 = new GPoint(CB.blackKing.getx(), CB.blackKing.gety()-1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2 = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety());
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2 = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety()+1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2 = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety()-1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2 = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety()+1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2 = new GPoint(CB.blackKing.getx(), CB.blackKing.gety()+1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2 = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety());
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
                 
        return normaliseFeature(numOfPosSquares, 8);
	}
	
	private double normaliseFeature(double featureValue, double maxValue)
	{
		return featureValue/maxValue;
	}	
	
}







