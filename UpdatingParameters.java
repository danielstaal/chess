/*
 * File: LearnOnData.java
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


/////////////////////////////////////////////////////////////////////////
// Data Learning
/////////////////////////////////////////////////////////////////////////

public class UpdatingParameters extends ConsoleProgram
{		
	/* learning parameters */
	private static final double lambda = 0.7;
	private static final double learningRate = 0.7;
	
	private ArrayList<Double> parVector;
	private ArrayList<BoardPosition> pastStates;
	private RewardFunction rewardFunction;
	
	public UpdatingParameters(ArrayList<Double> pV, ArrayList<BoardPosition> pS, RewardFunction rF)
	{
		parVector = pV;
		pastStates = pS;
		rewardFunction = rF;
	}
	
	public void initiateParVector()
	{
//			FEATURE Weights:
//				no.1: terminal state
		//parVector.add(0.5);
//				no.2: legal squares for black king		
		parVector.add(0.5);	
				
	}
	
	public void learnOnData()
	{
		System.out.println("finished");
			// updating the parameters	

		double[] sumGradJ = sumGradJ();
				System.out.print("C");
		updateParameters(sumGradJ);
	}

	private double[] sumGradJ()
	{
		double[] sumGradients = new double[parVector.size()];
	
		for(int j=0; j<parVector.size();j++)
		{
			for(int i=0; i<pastStates.size();i++)
			{
				sumGradients[j] += calcGradJ(pastStates.get(i), i);
										System.out.print("D");
			}
		}
		return sumGradients;
	}
	
	private double calcGradJ(BoardPosition pos, int t)
	{
		double gradJ = 0;
		
		Chessboard thisBoard = new Chessboard(pos);
		FeatureCalculation thisFC = new FeatureCalculation(thisBoard);

		double correctiondt = calcCorrectiondt(pos, t);
		
		// Feature no.1
		gradJ += thisFC.noOfPosSquaresk() * correctiondt;
		
		return gradJ;
	}
	
	private double calcCorrectiondt(BoardPosition pos, int t)
	{
		double correctiondt = 0;
		
		for(int j=t;j<pastStates.size();j++)
		{
			correctiondt += Math.pow(lambda, j-t) * tempDif(pos); 
		}
		
		return correctiondt;
	}
	
	
	// TODO: can be optemized, double code, double code everywhere...
	private double tempDif(BoardPosition pos)
	{
		double tempDif = 0;
		
		ArrayList<BoardPosition> allPosNextMoves;
		Chessboard tempChessBoard = new Chessboard(pos);
		AllNextStates tempAllNext = new AllNextStates(tempChessBoard);
		
		allPosNextMoves = tempAllNext.findAllPosNextStates();
		
		BoardPosition bestMove = rewardFunction.findHighestReward(allPosNextMoves);

		tempDif = rewardFunction.calcReward(bestMove) - rewardFunction.calcReward(pos);	
			
		//System.out.println(rewardFunction.calcReward(bestMove));
		//System.out.println(rewardFunction.calcReward(pos));
		return tempDif;
	}
	
	// TODO: make this vector multiplication
	private void updateParameters(double[] sumGradJ)
	{
		double feature;
		for(int i=0; i<parVector.size();i++)
		{
			feature = parVector.get(i);
			feature += learningRate*sumGradJ[i];
			parVector.set(i, feature);
			System.out.println(parVector.get(i));
		}
	}
}







