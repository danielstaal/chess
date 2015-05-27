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


public class FeatureCalculation
{
	
/////////////////////////////////////////////////////////////////////////
// Feature calculation
/////////////////////////////////////////////////////////////////////////
	
	private Chessboard CB;
	
	public FeatureCalculation(Chessboard board)
	{
		CB = board;
	}
	
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
	
	public double kingProtectsRook()
	{
		GPoint RCoor = new GPoint(CB.rook.getx(), CB.rook.gety());
		if(CB.blackKing.notCheckK(RCoor))
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
		GPoint kCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety());
		if(!CB.blackKing.notCheckR(kCoor))
		{
			System.out.print("check");
			return 1;
		}
		return 0;
	}
	
//	public double terminalState()
//	{
//		double reward = 0;
//		if(CB.getCheckMate())
//		{
//			reward = 2000;
//		}
//		if(CB.getStaleMate())
//		{
//			reward = -2000;
//		}
//		if(!CB.rook.getRookAlive())
//		{
//			reward = -2000;
//		}
//	
//		return reward;
//	}
	
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
		
		GPoint blackKingCoor;
		boolean legalMove;
		
		blackKingCoor = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety()-1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety()-1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety());
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety()+1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety()-1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety()+1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety()+1);
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety());
		if(CB.blackKing.checkIfLegalMovek(blackKingCoor))
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







