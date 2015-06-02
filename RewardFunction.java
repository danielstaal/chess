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

	/* class to get all pos next states */
	private AllNextStates allPosNextStates;
	
	/* the current chessboard */
	private Chessboard chessBoard;
	
	/* the par vector */
	private ArrayList<Double> parVector;
	
	/* Arraylist to keep track of the values of past states */
	public ArrayList<Double> stateValues;
	
	/* ArrayList to keep track of feature names */
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
		// this is just a low value to be lower than any possible reward
		double reward = -1000000.0;
		double[] thisRewardAndFeatureValues = null;
		BoardPosition bestBoardPosition = null;
		
		for(int i=0;i<allPosNextMoves.size();i++)
		{
			thisRewardAndFeatureValues = calcReward(allPosNextMoves.get(i));
			
			if(reward == -1000000.0 || thisRewardAndFeatureValues[0] > reward)
			{
				reward = thisRewardAndFeatureValues[0];
				bestBoardPosition = allPosNextMoves.get(i);
			}
		}
		
//		if(refReward.checkExploitation(reward))
//		{
//			
//		}
		
		// add the reward to the arraylist of rewards of past states
		stateValues.add(reward);
		addFeatureValues(thisRewardAndFeatureValues);

		return bestBoardPosition;
	}

	
	public void createFeatureNameArrayList()
	{
		String f0 = "squaresOfKingvsRook";
		String f1 = "noOfPosSquaresk";
		String f2 = "distanceToEdgeBlackKing";
		String f3 = "distanceBetweenWhiteRookAndBlackKing";
		//String f4 = "kingProtectsRook";
		String f5 = "threatenedRook";
		//String f6 = "rookLost";
		String f7 = "kingsInOpposition";
		
		featureNames.add(f0);
		featureNames.add(f1);
		featureNames.add(f2);		
		featureNames.add(f3);
		//featureNames.add(f4);
		featureNames.add(f5);
		//featureNames.add(f6);
		featureNames.add(f7);
	}
		
	public double[] calcReward(BoardPosition pos)
	{	
		Chessboard thisBoard = new Chessboard(pos);

		FeatureCalculation thisFC = new FeatureCalculation(thisBoard);
				
		// make a (virtual) random move with the black king
		thisBoard.blackKing.randomMovek();
		
		
		///////////////// Feature rewards:

		double featureValue0 = thisFC.squaresOfKingvsRook();
		double featureValue1 = thisFC.noOfPosSquaresk();
		double featureValue2 = thisFC.distanceToEdgeBlackKing();
		double featureValue3 = thisFC.distanceBetweenWhiteRookAndBlackKing();
		//double featureValue4 = thisFC.kingProtectsRook();
		double featureValue5 = thisFC.threatenedRook();
		//double featureValue6 = thisFC.rookLost();
		double featureValue7 = thisFC.kingsInOpposition();
		
		//System.out.println(featureValue7);
				
				
		double[] rewardAndFeatureValues = {0.0, featureValue0, featureValue1,featureValue2,featureValue3,featureValue5, featureValue7};//, featureValue6};//, featureValue5};
			
		double evaluation;
				
		for(int i=0;i<parVector.size();i++)
		{
			evaluation = parVector.get(i) * rewardAndFeatureValues[i+1];	

			rewardAndFeatureValues[0] += evaluation;
		}
		return rewardAndFeatureValues;
	}
	
	public void addFeatureValues(double[] rewardAndFeatureValues)
	{
		for(int i=0;i<parVector.size();i++)
		{
			featureValues.get(i).add(rewardAndFeatureValues[i+1]);
		}
	}
	
	public void clearFeatureValues()
	{	
		for(int i=0;i<featureValues.size();i++)
		{
			featureValues.get(i).clear();
		}
	}	

	
}





