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


public class AllNextStates
{
/////////////////////////////////////////////////////////////////////////
// To find the move with the highest reward
/////////////////////////////////////////////////////////////////////////	
	
	private Chessboard CB;
	
	public AllNextStates(Chessboard board)
	{
		CB = board;
	}
	
	public ArrayList<BoardPosition> findAllPosNextStates()
	{
		ArrayList<BoardPosition> posPositions = new ArrayList<BoardPosition>();
		
		ArrayList<BoardPosition> posKPositions = CB.whiteKing.posMovesK();
		ArrayList<BoardPosition> posRPositions = CB.rook.posMovesR();
		
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
}
