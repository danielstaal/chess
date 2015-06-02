/*
 * File: Tester.java
 * -------------------
 * Name: Daniel Staal
 * 
 * This file will eventually test the game of Chess.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Tester extends ConsoleProgram
{
	private Chessboard CB;
	private Agent agent;
	private BoardPosition initPos;
	
	private int numberOfTestRounds = 1;
	
	private ArrayList<Double> rewardValues = new ArrayList<Double>();
	
	public Tester(Chessboard board, Agent a, BoardPosition ip)
	{
		CB = board;
		agent = a;
		initPos = ip;
	}

	public void test()
	{
		for(int i=0;i<numberOfTestRounds;i++)
		{
			testGames();
		}
	}
	
	private void testGames()
	{
		printBoard();
		makeMoves();
		agent.rewardFunction.stateValues.add(2.0);
		
		for(int i=0;i<agent.rewardFunction.stateValues.size();i++)
		{
			System.out.print(i); System.out.print(" reward state:");
			System.out.println(agent.rewardFunction.stateValues.get(i));
		}
		
		learnOnData();
		CB.resetPosition(initPos);
		resetValues();
	}

	private void makeMoves()
	{
		ArrayList<BoardPosition> positions = getBoardPositions();
		
		makeForcedMove(positions.get(0));
		makeForcedMove(positions.get(1));	
			
		int numOfMoves = 5;
		for(int i=1;i<numOfMoves;i++)
		{
			makeForcedMove(positions.get(i));
			
			printBoard();
			
			// To create feature values array in agent
			if(i%2==1)
			{
				calcReward(positions.get(i));
			}

		}
		
	}

	private void makeForcedMove(BoardPosition pos)
	{	
		CB = new Chessboard(pos);
	}
	
	private void calcReward(BoardPosition pos)
	{
		double rewardAndFeatureValues[] = agent.rewardFunction.calcReward(pos);
		agent.rewardFunction.stateValues.add(rewardAndFeatureValues[0]);
		
		agent.rewardFunction.addFeatureValues(rewardAndFeatureValues);
		
		//System.out.println(reward);
	}
	
	private void learnOnData()
	{
		agent.update.learnOnData();
	}
	
	private ArrayList<BoardPosition> getBoardPositions()
	{
		GPoint kCoor = new GPoint(0,0);
		GPoint KCoor = new GPoint(0,2);
		GPoint RCoor = new GPoint(3,3);
		BoardPosition pos1 = new BoardPosition(kCoor, KCoor, RCoor);
		
		// black move	
		kCoor = new GPoint(1,0);
		KCoor = new GPoint(0,2);
		RCoor = new GPoint(3,3);		
		BoardPosition pos2 = new BoardPosition(kCoor, KCoor, RCoor);
		
		// white move
		kCoor = new GPoint(1,0);
		KCoor = new GPoint(0,2);
		RCoor = new GPoint(3,1);		
		BoardPosition pos3 = new BoardPosition(kCoor, KCoor, RCoor);
		
		// black move
		kCoor = new GPoint(0,0);
		KCoor = new GPoint(0,2);
		RCoor = new GPoint(3,1);
		BoardPosition pos4 = new BoardPosition(kCoor, KCoor, RCoor);
		// white move
		kCoor = new GPoint(0,0);
		KCoor = new GPoint(0,2);
		RCoor = new GPoint(1,1);
		BoardPosition pos5 = new BoardPosition(kCoor, KCoor, RCoor);
		
		agent.pastStates.add(pos3);
		agent.pastStates.add(pos5);	
		
		ArrayList<BoardPosition> positions = new ArrayList<BoardPosition>();
		positions.add(pos1);
		positions.add(pos2);
		positions.add(pos3);
		positions.add(pos4);
		positions.add(pos5);
		
		return positions;
	}
	
	private void printBoard()
	{
	// printing
		System.out.print("After move");
		System.out.println();
				
		for(int j=0; j<CB.getCBSize(); j++)
		{
			for(int k=0; k<CB.getCBSize(); k++)
			{
				System.out.print(CB.getCBarray()[k][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	private void resetValues()
	{
		// clear all past positions
		agent.pastStates.clear();
		
		agent.rewardFunction.stateValues.clear();
			
		agent.rewardFunction.clearFeatureValues();
	}

}











