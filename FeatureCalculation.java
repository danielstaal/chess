/*
 * File: FeatureCalculation.java
 * -------------------
 * Name: Daniel Staal
 * 
 * Main Components
 *	- calculate feature values of given boardpositions
 *	- return a value between 0 and 1
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
	
	private Chessboard thisBoard = new Chessboard();
	private GPoint whiteKingCoor;
	private GPoint blackKingCoor;
	private GPoint rookCoor;

	
	public FeatureCalculation(Chessboard board)
	{
		thisBoard = board;
		whiteKingCoor = thisBoard.whiteKing.getCoordinates();
		blackKingCoor = thisBoard.blackKing.getCoordinates();
		rookCoor = thisBoard.rook.getCoordinates();
	}
	
	public FeatureCalculation()
	{
	}
	
	public void setBoard(BoardPosition pos)
	{
		thisBoard.setBoardPosition(pos);
		whiteKingCoor = thisBoard.whiteKing.getCoordinates();
		blackKingCoor = thisBoard.blackKing.getCoordinates();
		rookCoor = thisBoard.rook.getCoordinates();	
	}
	

	public double blackKingInStaleMate()
	{
		if(thisBoard.getStaleMate())
		{
			return 1.0;
		}
		return 0.0;
	}
	
	public double blackKingInCheckMate()
	{
		if(thisBoard.getCheckMate())
		{
			return 1.0;
		}
		return 0.0;
	}

	/*
	- kings are two squares horizontal of vertical apart
	*/
	public double kingsInOpposition()
	{
		if(((whiteKingCoor.getX() == blackKingCoor.getX() + 2 || whiteKingCoor.getX() == blackKingCoor.getX() - 2) && whiteKingCoor.getY() == blackKingCoor.getY()) || ((whiteKingCoor.getY() == blackKingCoor.getY() + 2 || whiteKingCoor.getY() == blackKingCoor.getY() - 2) && whiteKingCoor.getX() == blackKingCoor.getX()))
		{
			return 1.0;
		}
		
		return 0.0;
	}
	
	// check if rook is still alive
	public double rookLost()
	{
		if(thisBoard.rook.getRookAlive())
		{
			return 0.0;
		}
		return 1.0;
	}
	
	// what is the size of the 'fence' that the rooks makes around the black king
	public double squaresOfKingvsRook()
	{
		double squares = 0.0;
		
		int rookx = thisBoard.rook.getx();
		int rooky = thisBoard.rook.gety();
		int size = thisBoard.getCBSize() - 1;
		
		if(thisBoard.rook.getx() > thisBoard.blackKing.getx())
		{
			if(thisBoard.rook.gety() > thisBoard.blackKing.gety())
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
			if(thisBoard.rook.gety() > thisBoard.blackKing.gety())
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
	
	// check if in this position the rook is threatened to be taken by the 
	// black king
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
	
	// check if the king can reach a certain square
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

	// check if the white king protects the white rook	
	public double kingProtectsRook()
	{
		if(kingReachesGPoint(whiteKingCoor, rookCoor))
		{
			return 0;
		}
		return 1;
	}
	
	public double distanceBetweenWhiteRookAndBlackKing()
	{
		double xDistance = Math.abs(thisBoard.rook.getx() - thisBoard.blackKing.getx());
		double yDistance = Math.abs(thisBoard.rook.gety() - thisBoard.blackKing.gety());
		
		double totalDis = xDistance + yDistance;
		
		return normaliseFeature(totalDis, (thisBoard.getCBSize()-1)*(thisBoard.getCBSize()-1));
	}
	
	public double rookChecksBlackKing()
	{
		if(!thisBoard.blackKing.notCheckR(blackKingCoor))
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
		
		int size = thisBoard.getCBSize();
		int x = thisBoard.blackKing.getx();
		int y = thisBoard.blackKing.gety();
		
		int left = x;
		int right = size - x; 
		int up = y;
		int down = size - y;
		
		if(left < distance){distance = left;}
		if(right < distance){distance = right;}
		if(up < distance){distance = up;}
		if(down < distance){distance = down;}
		
		//System.out.print(distance);
		
		return normaliseFeature(distance, thisBoard.getCBSize()-1);
	}
	
	// FEATURE: num of pos squares k
	public double noOfPosSquaresk()
	{
		double numOfPosSquares = 0;	
		
		GPoint blackKingCoor2 = new GPoint();
		boolean legalMove;
		
		blackKingCoor2.setLocation(thisBoard.blackKing.getx()-1, thisBoard.blackKing.gety()-1);
		if(thisBoard.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2.setLocation(thisBoard.blackKing.getx(), thisBoard.blackKing.gety()-1);
		if(thisBoard.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2.setLocation(thisBoard.blackKing.getx()-1, thisBoard.blackKing.gety());
		if(thisBoard.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2.setLocation(thisBoard.blackKing.getx()+1, thisBoard.blackKing.gety()+1);
		if(thisBoard.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2.setLocation(thisBoard.blackKing.getx()+1, thisBoard.blackKing.gety()-1);
		if(thisBoard.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2.setLocation(thisBoard.blackKing.getx()-1, thisBoard.blackKing.gety()+1);
		if(thisBoard.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2.setLocation(thisBoard.blackKing.getx(), thisBoard.blackKing.gety()+1);
		if(thisBoard.blackKing.checkIfLegalMovek(blackKingCoor2))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor2.setLocation(thisBoard.blackKing.getx()+1, thisBoard.blackKing.gety());
		if(thisBoard.blackKing.checkIfLegalMovek(blackKingCoor2))
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







