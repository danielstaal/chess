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

	/* */
	private AllNextStates allPosNextStates;
	private Chessboard chessBoard;
	private ArrayList<Double> parVector;
	public ArrayList<Double> stateValues;
	
	public ArrayList<String> featureNames = new ArrayList<String>();
	
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
		
		createFeatureNameArrayList();
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
		double reward = -1000000;
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
		
//		if(refReward.checkExploitation(reward))
//		{
//			
//		}
		
		stateValues.add(reward);

		return bestBoardPosition;
	}

	
	public void createFeatureNameArrayList()
	{
		String f1 = "squaresOfKingvsRook";
		String f2 = "noOfPosSquaresk";
		String f3 = "distanceToEdgeBlackKing";
		String f4 = "distanceBetweenWhiteRookAndBlackKing";
		String f5 = "kingProtectsRook";
		
		featureNames.add(f1);
		featureNames.add(f2);
		featureNames.add(f3);		
		featureNames.add(f4);
		featureNames.add(f5);
	}
		
	public double calcReward(BoardPosition pos)
	{
		double reward = 0.0;
		
		Chessboard thisBoard = new Chessboard(pos);

		FeatureCalculation thisFC = new FeatureCalculation(thisBoard);
				
		// make a (virtual) random move with the black king
		thisBoard.blackKing.randomMovek();
		
		
		///////////////// Feature rewards:

		double featureValue0 = thisFC.squaresOfKingvsRook();
		double featureValue1 = thisFC.noOfPosSquaresk();
		double featureValue2 = thisFC.distanceToEdgeBlackKing();
		double featureValue3 = thisFC.distanceBetweenWhiteRookAndBlackKing();
		double featureValue4 = thisFC.kingProtectsRook();
				
		double[] thisFeatureValues = {featureValue0, featureValue1,featureValue2,featureValue3,featureValue4};
			
		double evaluation;
				
		for(int i=0;i<parVector.size();i++)
		{
			evaluation = parVector.get(i) * thisFeatureValues[i];	
			featureValues.get(i).add(thisFeatureValues[i]);
			reward += evaluation;
		}
		return reward;
	}
	
	public void clearFeatureValues()
	{
		for(int i=0;i<featureValues.size();i++)
		{
			featureValues.get(i).clear();
		}
	}	

	
}





