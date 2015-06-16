/*
 * File: BoardPosition.java
 * -------------------
 * Name: Daniel Staal
 * 
 * Main Components:
 *		- GPoint for each piece on the board

 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class BoardPosition extends ConsoleProgram
{
	private GPoint k;
	private GPoint K;
	private GPoint R;

	public BoardPosition(GPoint king, GPoint King, GPoint Rook)
	{
		k = king;
		K = King;
		R = Rook;
	}

	public GPoint getk()
	{
		return k;
	}
	
	public GPoint getK()
	{
		return K;
	}
	
	public GPoint getR()
	{
		return R;
	}
}




