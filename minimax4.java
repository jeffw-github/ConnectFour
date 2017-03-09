import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class minimax4 extends AIModule
{
	private int maxLayer = 5;
	private int p, width, height;
	private int[][] evalFunc = new int[height][width];

	@Override
	public void getNextMove(final GameStateModule state)
	{
		width = state.getWidth();
		height = state.getHeight();

		p = state.getActivePlayer();

		evalFunc = evalFunction(state);

		int moves[] = legalMoves(state);

		int choose = -1;

		int max = Integer.MIN_VALUE;

		while(!terminate)
		{
			max = Integer.MIN_VALUE;

			for(int move: moves)
			{
				if(move > -1)
				{
					if(terminate == true)
					{
						// maxLayer--;
						return;
					}

					int hold = move;

					state.makeMove(move);

					/*if(maxLayer == 1)
					{
						System.out.println("At move:" + move + ", original state is = " + evaluate(state));
					}*/

					int newMax = Math.max( max, minValue(state, 1, evaluate(state)));

					if (newMax > max)
					{
						max = newMax;
						choose = hold;
					}

					state.unMakeMove();

					/*if(maxLayer == 1)
					{
						System.out.println("At move:" + move + " = " + newMax + " so choose --> " + choose);
						System.out.println(" ");
					}*/
				}

			}
			// maxLayer++;
			chosenMove = choose; //set our move
		}
	}

	// minVal
	private int minValue(final GameStateModule state, int curLayer, int initial)
	{
		// if terminal state
		if(state.isGameOver())
		{
			int winner = state.getWinner();
			if(winner == 0)
				return 0; //if game ended in draw, no reinforcement
			else
			{
				return Integer.MAX_VALUE;
			}
		}
		// if max layer
		if( curLayer == maxLayer )
		{
			return (int)((int)initial + (int)eval(state));
		}

		int moves[] = legalMoves(state);

		int min =  Integer.MAX_VALUE;

		for(int move : moves)
		{
			if(terminate == true)
	      		break;

			if( move > -1)
			{
				//return (int)((int)initial + (int)eval(state));


				state.makeMove(move);
				min =  Math.min(min, maxValue(state, curLayer + 1, (int)(initial + eval(state))));
				state.unMakeMove();

			}
		}

		return min;
	}

	// maxVal
	private int maxValue(final GameStateModule state, int curLayer, int initial)
	{
		// if terminal state
		if(state.isGameOver())
		{
			int winner = state.getWinner();
			if(winner == 0)
				return 0; //if game ended in draw, no reinforcement
			else
			{
				return Integer.MIN_VALUE + 1;
			}
		}
		// if max layer
		if( curLayer == maxLayer )
		{
			return (int)(initial + eval(state));
		}

		// min
		// generate all legal moves for the given state
		int moves[] = legalMoves(state);

		int max = Integer.MIN_VALUE;

		for(int move:moves)
		{
			if(terminate == true)
	        {
	        	break;
				//		return Math.max(max, evaluation(state));
	        }//if we have run out of time

			if( move > -1)
			{
				state.makeMove(move);
				max =  Math.max(max, minValue(state, curLayer + 1, (int)(initial + eval(state))));
				state.unMakeMove();
			}
		}
		return max;
	}

	// generate all possible legal moves within a given state
	private int[] legalMoves(final GameStateModule state)
	{
		int moves[] = new int[state.getWidth()];

		for (int i = 0; i < state.getWidth() ; i++)
		{
			if (state.canMakeMove(i))
				moves[i] = i;
			else
				moves[i] = -1;
		}

		return moves;
	}

	// generates the evaluation function
	private int[][] evalFunction(final GameStateModule state)
	{
		int ans[][] = new int[height][width];

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				// calculate cardinal lines and +/- diagonal lines
				ans[i][j] = growCoin(state, j, i, 1, 0) + growCoin(state, j, i, 0, 1) + growCoin(state, j, i, 1, 1) + growCoin(state, j, i, -1, 1);
			}
		}

		return ans;
	}

	private int evaluate(final GameStateModule state)
	{
		int ans = 0, block = 0, win = 0;

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(state.getAt(j,i) == p)
				{
					ans += evalFunc[i][j];
				}
				else
				{
					if(state.getAt(j,i) != 0 && state.getAt(j,i) != p)
					{
						ans -= evalFunc[i][j];
					}
				}

			}
		}



		return ans;
	}

	private int eval(final GameStateModule state)
	{
		int ans = 0, block = 0, win = 0, opponent = 0;

		if((p+1)%2 == 0)
			opponent = 2;
		else
			opponent = 1;

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(state.getAt(j,i) == p)
				{
					ans += evalFunc[i][j];
					//System.out.println("  +evalFunc[" + j + "," + i + "] = " + evalFunc[i][j]);
					ans += blocks(state, p)* maxCount(state,opponent);//*evalWin(state, p, j, i);
					//System.out.println("  maxCoins = " + maxCount(state, opponent));
					//ans += evalWin(state, p, j, i);
					//System.out.println("eval blocked = " + (blocks(state, p, j, i)+evalFunc[i][j]));
					//ans += evalWin(state, p, j, i);

				}
				else
				{
					if(state.getAt(j,i) != 0 && state.getAt(j,i) != p)
					{
						ans -= evalFunc[i][j];
						ans -= blocks(state, opponent)* maxCount(state,p);
						//ans -= blocks(state, p);
						//System.out.println("  -evalFunc[" + j + "," + i + "] = " + evalFunc[i][j]);
						//ans -= blocks(state, opponent)* maxCount(state,p);
						//ans -= evalWin(state, opponent, j, i);
						//ans -= blocks(state, state.getAt(j,i), j, i);
					}
				}

			}
		}



		return ans;
	}


	private int evalWin(GameStateModule state, int player, int x, int y)
	{
		int win = 0;

		if(state.getAt(x,y+1) == player || state.getAt(x,y-1) == player)
		{
			win += growWin(state, player, x, y, 0, 1);
		}

		if(state.getAt(x+1,y) == player || state.getAt(x-1,y) == player)
		{
			win += growWin(state, player, x, y, 1, 0);
		}

		if(state.getAt(x+1,y+1) == player || state.getAt(x-1,y-1) == player)
		{
			win += growWin(state, player, x, y, 1, 1);
		}

		if(state.getAt(x-1,y+1) == player || state.getAt(x+1,y-1) == player)
		{
			win += growWin(state, player, x, y, -1, 1);
		}

		return win;
	}

	private int maxCount(GameStateModule state, int player)
	{
		int ans = 0;

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(state.getAt(j,i) == player)
				{
					int ver = growCount(state, j, i, 1, 0, player);
					int hor = growCount(state, j, i, 0, 1, player);
					int pos = growCount(state, j, i, 1, 1, player);
					int neg = growCount(state, j, i, -1, 1, player);


					ans = Math.min(Math.max(Math.max(ver,hor),Math.max(pos,neg)),3);
				}
			}
		}

		int[] points = { 1, 10, 100, 500};

		return points[ans];
	}

	private int growWin(final GameStateModule state, int player, int x, int y, int dX, int dY)
	{
		// from each position of the coin *only grow until 4 in a row
		int sides[] = {1,-1};
		int win = 0, xTemp = x, yTemp = y;


		for(int side: sides)
		{
			// explore the other direction from origin
			dX = dX * side;
			dY = dY * side;

			// reset the origin value
			x = xTemp + dX;
			y = yTemp + dY;

			while( x >= 0 && y >= 0 && x <  width &&  y < height && state.getAt(x,y) == player )
			{
				win += evalFunc[y][x];

				// increment to the next position
				x += dX;
				y += dY;
			}
		}

		return win;
	}


	// given posiiton
	// count number of coins it blocks
	// for each given block recount new lines
	private int blocks(final GameStateModule state, int player)
	{
		int ans = 0, block = 0, win = 0, opponent = 0;
		//System.out.println("  blocking");

		if((p+1)%2 == 0)
			opponent = 2;
		else
			opponent = 1;

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(state.getAt(j,i) == opponent)
				{
					int temp = 0;
					temp += growLine(state, j, i, 1, 0, opponent);
					//System.out.println("        + " + growLine(state, j, i, 1, 0, opponent) + " blocks ");
					temp += growLine(state, j, i, 0, 1, opponent);
					//System.out.println("        + " + growLine(state, j, i, 0, 1, opponent) + " blocks ");
					temp += growLine(state, j, i, 1, 1, opponent);
					//System.out.println("        + " + growLine(state, j, i, 1, 1, opponent) + " blocks ");
					temp += growLine(state, j, i, -1, 1, opponent);
					//System.out.println("        + " + growLine(state, j, i, -1, 1, opponent) + " blocks ");

					//System.out.println("        opponent at position " + j + "," + i + "  with blocks of = " + temp);

					ans += (evalFunc[i][j] - temp);
				}
			}
		}

		return ans;
	}

		// count the number of ways to win given a (x,y) position
 	private int growCount(final GameStateModule state, int x, int y, int dX, int dY, int player)
	{
		// from each position of the coin *only grow until 4 in a row
		int sides[] = {1,-1};
		int coins = 0, xTemp = x, yTemp = y, opponent = 0;


		if((p+1)%2 == 0)
			opponent = 2;
		else
			opponent = 1;


		for(int side: sides)
		{
			// reset the origin value
			x = xTemp;
			y = yTemp;

			// explore the other direction from origin
			dX = dX * side;
			dY = dY * side;

			if (side == -1 && x == xTemp && y == yTemp)
			{
				x += dX;
				y += dY;
			}

			int max = 0;

			while( x >= 0 && y >= 0 && x <  width &&  y < height && Math.abs(x-xTemp) < 4 && Math.abs(y-yTemp) < 4 && state.getAt(x,y) == player)
			{
				coins++;
				max++;

				// increment to the next position
				x += dX;
				y += dY;
			}

			if(state.getAt(x,y) == opponent)
				coins = 0;
			else
			{
				if(state.getAt(x + dX,y + dY) == player)
					coins++;
			}
		}

		return coins;
	}

	// count the number of ways to win given a (x,y) position
 	private int growLine(final GameStateModule state, int x, int y, int dX, int dY, int player)
	{
		// from each position of the coin *only grow until 4 in a row
		int sides[] = {1,-1};
		int coins = 0, xTemp = x, yTemp = y;


		for(int side: sides)
		{
			// reset the origin value
			x = xTemp;
			y = yTemp;

			// explore the other direction from origin
			dX = dX * side;
			dY = dY * side;

			if (side == -1 && x == xTemp && y == yTemp)
			{
				x += dX;
				y += dY;
			}

			int max = 0;

			while( x >= 0 && y >= 0 && x <  width &&  y < height && Math.abs(x-xTemp) < 4 && Math.abs(y-yTemp) < 4 && (state.getAt(x,y) == player || state.getAt(x,y) == 0))
			{
				coins++;
				max++;

				// increment to the next position
				x += dX;
				y += dY;
			}
		}

		// if there is four coins
		if (coins > 3)
		{
			// if there is more than four coins account for more than possible of winning
			if (coins > 4)
				return Math.min(4, coins-3);
			else
				return 1;
		}
		else
			return 0;
	}


	// count the number of ways to win given a (x,y) position
 	private int growCoin(final GameStateModule state, int x, int y, int dX, int dY)
	{
		// from each position of the coin *only grow until 4 in a row
		int sides[] = {1,-1};
		int coins = 0, xTemp = x, yTemp = y;


		for(int side: sides)
		{
			// reset the origin value
			x = xTemp;
			y = yTemp;

			// explore the other direction from origin
			dX = dX * side;
			dY = dY * side;

			if (side == -1 && x == xTemp && y == yTemp)
			{
				x += dX;
				y += dY;
			}

			int max = 0;

			while( x >= 0 && y >= 0 && x <  width &&  y < height && Math.abs(x-xTemp) < 4 && Math.abs(y-yTemp) < 4 )
			{

				coins++;
				max++;

				// increment to the next position
				x += dX;
				y += dY;
			}
		}

		// if there is four coins
		if (coins > 3)
		{
			// if there is more than four coins account for more than possible of winning
			if (coins > 4)
				return Math.min(4, coins-3);
			else
				return 1;
		}
		else
			return 0;
	}
}
