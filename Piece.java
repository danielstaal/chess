/*
 * File: Piece.java
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


public class Piece extends ConsoleProgram
{
	private int x;
	private int y;
	
	private GPoint coordinates;

	public Extra extra = new Extra();

	Chessboard CB;

	public Piece(GPoint coor, Chessboard board)
	{
		x = (int)coor.getX();
		y = (int)coor.getY();	
		
		CB = board;
		
		coordinates = new GPoint(x, y);
	}
	
	public Piece()
	{
	}
	
	public boolean onChessBoard(GPoint Coor)
	{
		if((Coor.getX() < CB.getCBSize() && Coor.getX() >= 0) && (Coor.getY() < CB.getCBSize() && Coor.getY() >= 0))
		{
			return true;
		}
		return false;
	}	
	
	public boolean noOtherPiece(GPoint Coor)
	{
		if((Coor.getX() == CB.blackKing.getx() && Coor.getY() == CB.blackKing.gety()) || (Coor.getX() == CB.whiteKing.getx() && Coor.getY() == CB.whiteKing.gety()) || (Coor.getX() == CB.rook.getx() && Coor.getY() == CB.rook.gety()))
		{
			return false;
		} 
		return true;
	}
	
	public int getx()
	{
		return x;
	}
	public int gety()
	{
		return y;
	}
	public void setx(int newx)
	{
		x = newx;
		coordinates.setLocation(x, y);
	}
	public void sety(int newy)
	{
		y = newy;
		coordinates.setLocation(x, y);
	}
	public GPoint getCoordinates()
	{
		return coordinates;
	}
}







