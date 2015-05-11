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


public class Extra
{
		
/////////////////////////////////////////////////////////////////////////
// Extra methods
/////////////////////////////////////////////////////////////////////////

	public boolean sameArray(int[] A, int[] B)
	{
		boolean result = true;
		for(int i=0; i<A.length;i++)
		{
			if(A[i] != B[i])
			{
				return false;
			}
		}
		
		return result;
	}
	
	public static int randInt(int min, int max) {

    // NOTE: Usually this should be a field rather than a method
    // variable so that it is not re-seeded every call.
    Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
	}

}
