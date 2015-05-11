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
	private Moves moves;
	
	public FeatureCalculation(Chessboard board, Moves Moves)
	{
		CB = board;
		moves = Moves;
	}
	
	// FEATURE: num of pos squares k
	public double noOfPosSquaresk()
	{
		double numOfPosSquares = 0;	
		
		GPoint blackKingCoor;
		boolean legalMove;
		
		blackKingCoor = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety()-1);
		if(moves.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety()-1);
		if(moves.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety());
		if(moves.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety()+1);
		if(moves.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety()-1);
		if(moves.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()-1, CB.blackKing.gety()+1);
		if(moves.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx(), CB.blackKing.gety()+1);
		if(moves.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
		
		blackKingCoor = new GPoint(CB.blackKing.getx()+1, CB.blackKing.gety());
		if(moves.checkIfLegalMovek(blackKingCoor))
		{
			numOfPosSquares++;
		}
                 
        return numOfPosSquares;
	}
}
