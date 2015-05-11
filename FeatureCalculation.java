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
	
	public double terminalState()
	{
		double reward = 0;
		if(CB.getCheckMate() || CB.getStaleMate())
		{
			reward = 5000;
		}
		if(!CB.rook.getRookAlive())
		{
			reward = -5000;
		}
	
		return reward;
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
