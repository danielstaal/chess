/*
 * File: Piece.java
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


public class Piece extends ConsoleProgram
{
	private int x;
	private int y;

	public Piece(GPoint coor)
	{
		x = (int)coor.getX();
		y = (int)coor.getY();	
	}
	
	public Piece()
	{
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
	}
	public void sety(int newy)
	{
		y = newy;
	}
}







