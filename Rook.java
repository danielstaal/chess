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


public class Rook extends Piece
{
	private boolean rookAlive = true;	
	
	public Rook(GPoint coor)
	{
		super(coor);
	}
	
	public void setRookAlive(boolean flag)
	{
		rookAlive = flag;
	} 
	public boolean getRookAlive()
	{
		return rookAlive;
	} 
}
