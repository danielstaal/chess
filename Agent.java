/*
 * File: Agent.java
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


public class Agent
{
	private ArrayList<Double> parVector;
	
	/* reward function in pastStates */
	private ArrayList<Double> featureValues = new ArrayList<Double>(); 
	
	/* to access extra methods */
	private Extra extra = new Extra();
	
	/* arraylist for all boardpositions in a game */
	public ArrayList<BoardPosition> pastStates = new ArrayList<BoardPosition>();
	private FeatureCalculation featCalc;
	
	/* to access calc all next moves */
	private AllNextStates allPosNextStates;
	/* to access the reward function */
	private RewardFunction rewardFunction;
	/* to access UpdatingParameters */
	public UpdatingParameters update;
	
	/* the CB */
	private Chessboard chessBoard;
	
	
	public Agent(Chessboard board, ArrayList<Double> parVec)
	{
		chessBoard = board;
		parVector = parVec;
		
		allPosNextStates = new AllNextStates(chessBoard);
		rewardFunction = new RewardFunction(allPosNextStates, chessBoard, parVector, featureValues);
		featCalc = new FeatureCalculation(chessBoard);
		update = new UpdatingParameters(parVector, pastStates, rewardFunction);
	}

	public void makeMove()
	{
		randomWhiteMove();
		//whiteMove();
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
