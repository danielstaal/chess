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
	public ArrayList<Double> stateValues;
	
	/* multidimensional arraylist to keep track of feature values */
	public ArrayList<ArrayList<Double>> featureValues = new ArrayList<ArrayList<Double>>();
	
	/* reference reward - exploration/exploitation */
	private ReferenceReward refReward = new ReferenceReward();
	
	public RewardFunction(AllNextStates aPNS, Chessboard board, ArrayList<Double> pV, ArrayList<Double> sV)
	{
		allPosNextStates = aPNS;
		chessBoard = board;
		parVector = pV;
		stateValues = sV;
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
			thisReward = calcReward (allPosNextMoves.get(i));
			if(thisReward > reward)
			{
				reward = thisReward;
				bestBoardPosition = allPosNextMoves.get(i);
			}
		}
		
//		if(refReward.checkExploitation(reward))
//		{
//			
//		}
		
		stateValues.add(reward);

		return bestBoardPosition;
	}
		
	public double calcReward(BoardPosition pos)
	{
		double reward = 0.0;
		
		Chessboard thisBoard = new Chessboard(pos);

		FeatureCalculation thisFC = new FeatureCalculation(thisBoard);
				
		// make a (virtual) random move with the black king
		thisBoard.blackKing.randomMovek();
		
		
		///////////////// Feature rewards:
		
		double feature0 = parVector.get(0) * thisFC.squaresOfKingvsRook();
		featureValues.get(0).add(feature0);
		
		double feature1 = parVector.get(1) * thisFC.distanceToEdgeBlackKing();	
		featureValues.get(1).add(feature1);
		
		double feature2 = parVector.get(2) * thisFC.kingProtectsRook();	
		featureValues.get(2).add(feature2);	
		
//		double feature3 = parVector.get(3) * thisFC.noOfPosSquaresk();	
//		featureValues.get(3).add(feature3);	
							
		return feature0 + feature1 + feature2;// + feature3; 
	}
}
