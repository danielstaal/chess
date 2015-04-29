/*
 * File: Main.java
 * -------------------
 * Name: Daniel Staal
 * 
 * This file will eventually implement the game of Chess.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class Main extends ConsoleProgram
{
	/* Size of Chessboard*/
	static final int chessBoardSize = 4;
		
	/* Chessboard */
	Chessboard chessBoard = new Chessboard(chessBoardSize);

	public void run()
	{
		chessBoard.fillEmptyChessboard();
		chessBoard.addKRKtoChessboard();
		printChessboard();
		playRandomMoves();
	}	
		
	private void playRandomMoves()
	{
		while(true)
		{
			readLine("?");
			blackMove();
			println();
			printChessboard();
			if(!chessBoard.rookAlive)
			{
				println("Remis!");
			}
			readLine("?");
			randomWhiteMove();
			println();
			printChessboard();
		}
	}	
	
	private void blackMove()
	{
		boolean legalMove = chessBoard.movek();
		while(!legalMove)
		{
			{
				legalMove = chessBoard.movek();
			}
		}
	}
	
	private void randomWhiteMove()
	{
		// choose random piece
		int rand = chessBoard.randInt(0,1);
		boolean legalMove = false;
		print(rand);
		
		while(!legalMove)
		{
			if(rand == 0)
			{
				legalMove = chessBoard.moveR();
			}else
			{
				legalMove = chessBoard.moveK();
			}
		}
	}
		
	private void printChessboard()
	{
		for(int j=0; j<chessBoardSize; j++)
		{
			for(int i=0; i<chessBoardSize; i++)
			{
				print(chessBoard.CBarray[i][j]);
				print(" ");
			}
			println();
		}
	}	
	
	private void fileWriting()
	{
		try {
 
			String content = "This is the content to write into file";
 
			File file = new File("/home/jharvard/java/chess/test.txt");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}























