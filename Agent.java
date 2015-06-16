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
 *		- all classes needed for learning and making moves are initiated here
 *		- control chamber of the agent. Make a random or a move based on the evaluation function
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
	
	/* reference reward - exploration/exploitation */
	private ReferenceReward refReward = new ReferenceReward();
	
	public int numberOfRandomMoves = 0;
	
	
	public Agent(Chessboard board, ArrayList<Double> parVec, boolean random)
	{
		chessBoard = board;
		parVector = parVec;
		
		allPosNextStates = new AllNextStates(chessBoard);
		rewardFunction = new RewardFunction(allPosNextStates, chessBoard, parVector, stateValues);
		update = new UpdatingParameters(parVector, pastStates, rewardFunction);
		
		randomMoves = random;
	}

	public void makeMove(int checkMates, int remis)
	{
		boolean exploration = true;
		if(refReward.checkExploitation(checkMates, remis))
		{
			exploration = false;
		}
		
		// checkExploitation by checking checkmates > remis
		if(randomMoves || exploration){randomWhiteMove();}
		else{whiteMove();}
	}

	// NOTE: is te veranderen in random uit allPosNextStates.findAllPosNextStates
	private void randomWhiteMove()
	{
		// picking random 0 or 1
		int rand = extra.randInt(0,1);
		// presetting legalmove to false
		boolean legalMove = false;
		
		// random number whether moving with the rook or with the white king
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
		
		// add this to feature value arrays
		double[] thisRewardAndFeatureValues = rewardFunction.calcReward(chessBoard.thisPosition, chessBoard);
		
		rewardFunction.stateValues.add(thisRewardAndFeatureValues[0]);
		rewardFunction.addFeatureValues(thisRewardAndFeatureValues);
	}
		
	private void whiteMove()
	{
		rewardFunction.findBestMove();
	}	
}
