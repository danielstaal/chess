/*
 * File: Agent.java
 * -------------------
 * Name: Daniel Staal
 * 
 * In this file the agent is described
 * 
 * 		- everything that has to to with the agent is described here
 *
 * Main Components:
 *		-
 *		-
 *		-
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class Agent
{
	/* the parameter vector with all the weights */
	private ArrayList<Double> parVector;
	
	/* reward function in pastStates */
	public ArrayList<Double> stateValues = new ArrayList<Double>(); 
	
	/* to access extra methods */
	private Extra extra = new Extra();
	
	/* arraylist for all boardpositions in a game */
	public ArrayList<BoardPosition> pastStates = new ArrayList<BoardPosition>();
	
	/* to access calc all next moves */
	private AllNextStates allPosNextStates;
	
	/* to access the reward function */
	public RewardFunction rewardFunction;
	
	/* to access UpdatingParameters */
	public UpdatingParameters update;
	
	/* the CB */
	private Chessboard chessBoard;
	
	private boolean randomMoves;
	
	
	public Agent(Chessboard board, ArrayList<Double> parVec, boolean random)
	{
		chessBoard = board;
		parVector = parVec;
		
		allPosNextStates = new AllNextStates(chessBoard);
		rewardFunction = new RewardFunction(allPosNextStates, chessBoard, parVector, stateValues);
		update = new UpdatingParameters(parVector, pastStates, rewardFunction);
		
		randomMoves = random;
	}

	public void makeMove()
	{
		if(randomMoves){randomWhiteMove();}
		else{whiteMove();}
	}

	// NOTE: is te veranderen in random uit allPosNextStates.findAllPosNextStates
	private void randomWhiteMove()
	{
		// choose random piece
		int rand = extra.randInt(0,1);
		boolean legalMove = false;
		
		// maybe not most efficient with while loop
		while(!legalMove)
		{
			if(rand == 0)
			{
				legalMove = chessBoard.rook.randomMoveR();
			}else
			{
				legalMove = chessBoard.whiteKing.randomMoveK();
			}
			rand = extra.randInt(0,1);
		}
	}
		
	private void whiteMove()
	{
		rewardFunction.findBestMove();
	}	
}
