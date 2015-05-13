/*
 * File: RewardFunction.java
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

public class RewardFunction extends ConsoleProgram
{

/////////////////////////////////////////////////////////////////////////
// Reward function
/////////////////////////////////////////////////////////////////////////

	private AllNextStates allPosNextStates;
	private Chessboard chessBoard;
	private ArrayList<Double> parVector;
	public ArrayList<Double> featureValues;
	
	public RewardFunction(AllNextStates aPNS, Chessboard board, ArrayList<Double> pV, ArrayList<Double> fV)
	{
		allPosNextStates = aPNS;
		chessBoard = board;
		parVector = pV;
		featureValues = fV;
	}
	
	public void findBestMove()
	{
		ArrayList<BoardPosition> allPosNextMoves;
		allPosNextMoves = allPosNextStates.findAllPosNextStates();
		
		BoardPosition bestMove = findHighestReward(allPosNextMoves);
		//if(RewardIsAboveThreshold());
		
		// NOTE: the random black king move that was used to calculate the 
		// reward is not the final choice of the opponent agent
		chessBoard.whiteKing.setx((int)bestMove.getK().getX());
		chessBoard.whiteKing.sety((int)bestMove.getK().getY());
		chessBoard.rook.setx((int)bestMove.getR().getX());
		chessBoard.rook.sety((int)bestMove.getR().getY());
		chessBoard.fillEmptyChessboard();
		chessBoard.addKRKtoChessboard();
	}
	
	public BoardPosition findHighestReward(ArrayList<BoardPosition> allPosNextMoves)
	{
		double reward = -10000000;
		double thisReward;
		BoardPosition bestBoardPosition = null;
		
		for(int i=0;i<allPosNextMoves.size();i++)
		{
			thisReward = calcReward(allPosNextMoves.get(i));
			if(thisReward > reward)
			{
				reward = thisReward;
				bestBoardPosition = allPosNextMoves.get(i);
			}
		}
		featureValues.add(reward);
		
		return bestBoardPosition;
	}
		
	public double calcReward(BoardPosition pos)
	{
		double reward = 0.0;
		
		Chessboard thisBoard = new Chessboard(pos);

		FeatureCalculation thisFC = new FeatureCalculation(thisBoard);
				
		// make a random move with the black king
		thisBoard.blackKing.randomMovek();
		
		// Feature no.1 terminal state
		reward += thisFC.terminalState();
		
		// Feature no.2
		reward += parVector.get(0) * thisFC.noOfPosSquaresk();
		// Feature no.3
		reward += parVector.get(1) * thisFC.distanceToEdgeBlackKing();	
		
		reward += parVector.get(2) * thisFC.rookChecksBlackKing();	
						
		return reward;
	}
	
	
	
	
	
}
