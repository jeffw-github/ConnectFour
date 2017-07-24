import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class AI extends AIModule
{
	int maxPly = 2;
	@Override
	public void getNextMove(final GameStateModule state)
	{
		// current player
		final int p = state.getActivePlayer();

		// minimax
		while(!terminate)
		{
			int util = maxValue(state);
			int tempMove = -1;
			int moves[] = generateMoves(state);
			for(GameStateModule s : successors(state))
			{
				for(int move : moves) {
					if(minValue(s) == util)
					{
						tempMove = move;
					}
				}
			}
			chosenMove = tempMove;
		}

	}

	private int maxValue(final GameStateModule state)
	{
		int util = Integer.MIN_VALUE;
		int moves[] = generateMoves(state);
		if(state.isGameOver())
		{
			//return util(state)
			int winner = state.getWinner();
			if(winner == 0)
				return 0; //if game ended in draw, no reinforcement
			else if(winner == state.getActivePlayer())
				return Integer.MAX_VALUE; //if max wins negative reinforcement
			else
				return Integer.MIN_VALUE; //if min wins positive reinforcement
		}

		for(int move : moves) //state s in successors(state)
		{
			if(terminate == true){
				break;
			}
			if(move != -1)
			{
				state.makeMove(move);
				util = Math.max(util, minValue(state));
				state.unMakeMove();
			}
		}
		return util;
	}

	private int minValue(final GameStateModule state)
	{
		int util;
		int moves[] = generateMoves(state);
		if(state.isGameOver())
		{
			//return util(state)
			int winner = state.getWinner();
			if(winner == 0)
				return 0; //if game ended in draw, no reinforcement
			else if(winner == state.getActivePlayer())
				return Integer.MIN_VALUE; //if min wins negative reinforcement
			else
				return Integer.MAX_VALUE; //if max wins positive reinforcement
		}
		util = Integer.MAX_VALUE;


			for(int move : moves)
			{
				if(terminate == true){
					break;
				}

				if(move != -1)
				{
					state.makeMove(move);
					util = Math.min(util, maxValue(state));
					state.unMakeMove();
				}
			}

		return util;
	}

	// To get all possible moves from a certain state
	private ArrayList<GameStateModule> successors(GameStateModule state)
	{
		ArrayList<GameStateModule> children = new ArrayList<GameStateModule>();

		for(int i = 0; i < state.getWidth(); i++)
		{
			if(state.canMakeMove(i))
			{
				state.makeMove(i);
				children.add(state);
				state.unMakeMove();
			}
		}
		return children;
	}

	private int[] generateMoves(GameStateModule state)
	{
		int moves[] = new int[state.getWidth()];
		for(int i = 0; i < state.getWidth(); ++i)
		{
				if(state.canMakeMove(i))
				{
						moves[i] = i; //can play column i
				}
				else
				{
						moves[i] = -1; //mark move as invalid
				}
		}
		return moves;

	}

	private int[] legalMoves(final GameStateModule state)
	{
		int moves[] = new int[state.getWidth()];

		for (int i = 0; i < state.getWidth() ; i++)
		{
			if (state.canMakeMove(i))
				moves[i] = 1;
			else
				moves[i] = -1;
		}

		return moves;
	}

	private int eval(final GameStateModule state)
	{
		int height[] = new int[state.getWidth()];

		for(int i = 0; i < state.getWidth(); i++)
		{
			if (state.getHeightAt(i) > 0)
				height[i] = state.getHeightAt(i);
		}
		return 0;
	}
}
