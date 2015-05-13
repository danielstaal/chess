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
	
	public double rookChecksBlackKing()
	{
		GPoint kCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety());
		if(!CB.blackKing.notCheckR(kCoor))
		{
			return 10000000;
		}
		
		return 0;
	}
	
	public double terminalState()
	{
		double reward = 0;
		if(CB.getCheckMate() || CB.getStaleMate())
		{
			reward = 50000000;
		}
		if(!CB.rook.getRookAlive())
		{
			reward = -50000000;
		}
	
		return reward;
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
		
		return distance;
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
                 
        return numOfPosSquares;
	}
}
