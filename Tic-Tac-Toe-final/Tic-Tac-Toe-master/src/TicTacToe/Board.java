package TicTacToe;

import java.util.HashSet;

public class Board {

	static final int BOARD_WIDTH = 12;
	static final int AIM_LENGTH = 6;
//    
//    static final long[][] SCORE = new long[][] {
//    	{10,	100, 	1000, 	10000,	100000,	 100000000,	 1000000000,	1000000000}, 
//    	{1,		10, 	100, 	1000, 	10000,	 10000000,	 100000000,	 	1000000000}};
//	static final int[][][] SCORE_SIX = new int[][][] {
//			{ { 0, 10, 100, 1000, 10000, 100000, Integer.MAX_VALUE }, { 0, 8, 80, 800, 8000, 80000, Integer.MAX_VALUE } },
//			{ { 0, 6, 60, 600, 6000, 60000, Integer.MAX_VALUE }, { 0, 1, 10, 100, 1000, 10000, Integer.MAX_VALUE} }, };
	static final int[][][] SCORE_SIX = new int[][][] {
		{ { 0, 10, 100, 1000, 10000, 100000, Integer.MAX_VALUE }, { 0, 5, 50, 500, 5000, 50000, Integer.MAX_VALUE } },
		{ { 0, 2, 20, 200, 2000, 20000, Integer.MAX_VALUE }, { 0, 1, 10, 100, 1000, 10000, Integer.MAX_VALUE} }, };
		
	static final int[][][] SCORE_EIGHT = new int[][][] {
		{ { 0, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 },
				{ 0, 5, 50, 500, 5000, 50000, 500000, 5000000, 50000000 } },
		{ { 0, 2, 20, 200, 2000, 20000, 200000, 2000000, 20000000 },
				{ 0, 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000 } }, };
				
	static final int[][][] SCORE_TEN = new int[][][] {
		{ { 0, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000,  1000000000, Integer.MAX_VALUE},
				{ 0, 5, 50, 500, 5000, 50000, 500000, 5000000, 50000000, 500000000, Integer.MAX_VALUE } },
		{ { 0, 2, 20, 200, 2000, 20000, 200000, 2000000, 20000000, 200000000, Integer.MAX_VALUE },
				{ 0, 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, Integer.MAX_VALUE } }, };
	// empty block count
	static final int[] SCORE_SIX_TWO = new int[] { 1000, 500 };
	static final int[] SCORE_EIGHT_TWO = new int[] { 1000000, 500000 };
	static final int[] SCORE_TEN_TWO = new int[] { 100000000, 5000000 };

	public enum State {
		Blank, X, O
	}

	private State[][] board;
	private State playersTurn;
	private State oppnent;
	private State winner;
	private HashSet<Integer> movesAvailable;
	private int[][][] winningWindowsX;
	private int[][][] winningWindowsO;
	private int[] twoBlockX;
	private int[] twoBlockO;
	private long scoreX;
	private long scoreO;
	private int preMoveY;
	private int preMoveX;

	private int moveCount;
	private boolean gameOver;

	Board() {
		board = new State[BOARD_WIDTH][BOARD_WIDTH];
		winningWindowsX = new int[2][2][AIM_LENGTH + 1];
		winningWindowsO = new int[2][2][AIM_LENGTH + 1];
		twoBlockO = new int[] { 0, 0 };
		twoBlockX = new int[] { 0, 0 };
		movesAvailable = new HashSet<>();
		reset();
	}

	private void initialize() {
		for (int row = 0; row < BOARD_WIDTH; row++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				board[row][col] = State.Blank;
			}
		}

		scoreX = 0;
		scoreO = 0;

		for (int i = 0; i < 2; i++) {
			for (int k = 0; i < 2; i++) {
				for (int j = 0; j < AIM_LENGTH + 1; j++) {
					winningWindowsX[i][k][j] = 0;
					winningWindowsO[i][k][j] = 0;
				}
			}
		}

		movesAvailable.clear();

		for (int i = 0; i < BOARD_WIDTH * BOARD_WIDTH; i++) {
			movesAvailable.add(i);
		}
	}

	void reset() {
		moveCount = 0;
		gameOver = false;
		playersTurn = State.X;
		oppnent = State.O;
		winner = State.Blank;
		initialize();
	}

	public int getMoveCount() {
		return this.moveCount;
	}

	public boolean isUseless(int index) {
		int col = index % BOARD_WIDTH;
		int row = index / BOARD_WIDTH;
		int radius = 2;
//        if(this.moveCount < 10) {
//        	radius = 2;
//        }else {
//        	radius = 3;
//        }

		int start_row = Math.max(0, row - radius);
		int start_col = Math.max(0, col - radius);
		int end_row = Math.min(row + radius, BOARD_WIDTH - 1);
		int end_col = Math.min(col + radius, BOARD_WIDTH - 1);

		int count = 0;
		int playerCount = 0;
		int oppnentCount = 0;
		outLoop: for (int i = start_row; i <= end_row; i++) {
			for (int j = start_col; j <= end_col; j++) {
				if (board[i][j] != State.Blank)
					count++;
				if (this.moveCount > 1) {
					if (count > 1)
						return false;
				} else {
					if (count >= 1)
						return false;
				}
			}
		}

		return true;
	}

	public boolean isMoreTwo(int y, int x) {
		int startX = Math.max(0, x - 2);
		int startY = Math.max(0, y - 2);
		int endX = Math.min(BOARD_WIDTH - 1, x + 2);
		int endY = Math.min(BOARD_WIDTH - 1, y + 2);

		for (int i = startX; i < endX; i++) {
			if (i == x) {
				continue;
			}
			if (i == x - 1 && i + 2 <= endX) {
				if (this.board[y][i] == this.board[y][i + 2] && this.board[y][i] != State.Blank) {
					return true;
				}
			}
			if (this.board[y][i] == this.board[y][i + 1] && this.board[y][i] != State.Blank) {
				return true;
			}
		}

		for (int i = startY; i < endY; i++) {
			if (i == y) {
				continue;
			}
			if (i == y - 1 && i + 2 <= endY) {
				if (this.board[i][x] == this.board[i + 2][x] && this.board[i][x] != State.Blank) {
					return true;
				}
			}
			if (this.board[i][x] == this.board[i + 1][x] && this.board[i][x] != State.Blank) {
				return true;
			}
		}

		for (int i = startX, j = startY; i < endX && j < endY; i++, j++) {
			if (i == x) {
				continue;
			}
			if (i == x - 1 && i + 2 <= endX && j + 2 <= endY) {
				if (this.board[j][i] == this.board[j + 2][i + 2] && this.board[j][i] != State.Blank) {
					return true;
				}
			}
			if (this.board[j][i] == this.board[j + 1][i + 1] && this.board[j][i] != State.Blank) {
				return true;
			}
		}

		for (int i = startX, j = endY; i < endX && j > startY; i++, j--) {
			if (i == x) {
				continue;
			}
			if (i == x - 1 && i + 2 <= endX && j - 2 >= startY) {
				if (this.board[j][i] == this.board[j - 2][i + 2] && this.board[j][i] != State.Blank) {
					return true;
				}
			}
			if (this.board[j][i] == this.board[j - 1][i + 1] && this.board[j][i] != State.Blank) {
				return true;
			}
		}

		return false;
	}

	public boolean move(int index) {
		this.setPreMove(index);
		return move(index % BOARD_WIDTH, index / BOARD_WIDTH);
	}

	private boolean move(int x, int y) {

		if (gameOver) {
			throw new IllegalStateException("TicTacToe is over. No moves can be played.");
		}

		if (board[y][x] == State.Blank) {
			board[y][x] = playersTurn;
		} else {
			return false;
		}

		moveCount++;
		movesAvailable.remove(y * BOARD_WIDTH + x);

		// The game is a draw.
		if (moveCount == BOARD_WIDTH * BOARD_WIDTH) {
			winner = State.Blank;
			gameOver = true;
		}

		// Check for a winner.
		if(checkWin(x, y, playersTurn)) {
			
		}
		updateScoreWindow(x, y, playersTurn);
		oppnent = playersTurn;
		playersTurn = (playersTurn == State.X) ? State.O : State.X;
		return true;
	}

	public void setPreMove(int index) {
		preMoveX = index % BOARD_WIDTH;
		preMoveY = index / BOARD_WIDTH;
	}

	public int[] getPreMove() {
		return new int[] { preMoveX, preMoveY };
	}

	public int getPreMoveY() {
		return this.preMoveY;
	}

	public int getPreMoveX() {
		return this.preMoveX;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	State[][] toArray() {
		return board.clone();
	}

	public State getTurn() {
		return playersTurn;
	}
	
	public State getOppnent() {
		return oppnent;
	}

	public State getWinner() {
		if (!gameOver) {
			throw new IllegalStateException("TicTacToe is not over yet.");
		}
		return winner;
	}

	public HashSet<Integer> getAvailableMoves() {
		return movesAvailable;
	}

	public int getBoardWidth() {
		return BOARD_WIDTH;
	}

	public void printScoreWindow() {
		System.out.println("Score window OOOOOO");
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < AIM_LENGTH + 1; k++) {
					if(this.winningWindowsO[i][j][k] != 0) {
						System.out.printf("block:%d empty:%d lenth:%d number:%d\n", i, j, k, this.winningWindowsO[i][j][k]);
					}
				}
			}
		}
		System.out.println("Score window XXXXXX");
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < AIM_LENGTH + 1; k++) {
					if(this.winningWindowsX[i][j][k] != 0) {
						System.out.printf("block:%d empty:%d lenth:%d number:%d\n", i, j, k, this.winningWindowsX[i][j][k]);
					}
				}
			}
		}

	}

	public void updateScoreWindow(int x, int y, State player) {
//		System.out.println("update Player");
		updateScoreWindowPlayer(x, y, player);
//		System.out.println("update oppnent");
		updateScoreWindowOppnent(x, y, player);

	}

	public void updateScoreWindowOppnent(int x, int y, State player) {

		int count = 0;
		int secondCount = 0;
		int block = 0;
		int secondBlock = 0;
		int empty = -1;
		int secondEmpty = -1;
		
		// count col
		for (int i = y + 1; true; i++) {
			if (i >= BOARD_WIDTH) {
				block++;
				break;
			}
			if (this.board[i][x] == State.Blank) {
				if (empty == -1 && i < BOARD_WIDTH - 1 && board[i + 1][x] == oppnent) {
					empty = count;
					continue;
				} else {
					break;
				}
			}

			if (this.board[i][x] == oppnent) {
				count++;
				continue;
			} else {
				block++;
				break;
			}
		}

		for (int i = y - 1; true; i--) {
			if (i < 0) {
				secondBlock++;
				break;
			}
			if (this.board[i][x] == State.Blank) {
				if (secondEmpty == -1 && i > 0 && board[i - 1][x] == oppnent) {
					secondEmpty = secondCount;
					continue;
				} else {
					break;
				}
			}
			if (this.board[i][x] == oppnent) {
				secondCount++;
//				if(secondEmpty != -1) {
//					secondEmpty++;
//				}
				continue;
			} else {
				secondBlock++;
				break;
			}
		}
//      count += secondCount;
		updateScoreArrayOppnent(count, secondCount, block, secondBlock, empty, secondEmpty, player);

		
//		System.out.printf("\ncount: %d, secondCount: %d, empty: %d, secondEmpty: %d, block: %d, secondBlock: %d\n", count, secondCount, empty, secondEmpty, block, secondBlock);
		// count row
		count = 0;
		secondCount = 0;
		block = 0;
		secondBlock = 0;
		empty = -1;
		secondEmpty = -1;

		for (int i = x + 1; true; i++) {
			if (i >= BOARD_WIDTH) {
				block++;
				break;
			}
			if (this.board[y][i] == State.Blank) {
				if (empty == -1 && i < BOARD_WIDTH - 1 && board[y][i + 1] == oppnent) {
					empty = count;
					continue;
				} else {
					break;
				}
			}
			if (this.board[y][i] == oppnent) {
				count++;
				continue;
			} else {
				block++;
				break;
			}
		}

		for (int i = x - 1; true; i--) {
			if (i < 0) {
				secondBlock++;
				break;
			}

			if (this.board[y][i] == State.Blank) {
				if (secondEmpty == -1 && i > 0 && this.board[y][i - 1] == oppnent) {
					secondEmpty = secondCount;
					continue;
				} else {
					break;
				}
			}
			if (this.board[y][i] == oppnent) {
				secondCount++;
//                if(secondEmpty != -1) { 
//                	secondEmpty++;
//                }
				continue;
			} else {
				secondBlock++;
				break;
			}
		}
//      count += secondCount;
		updateScoreArrayOppnent(count, secondCount, block, secondBlock, empty, secondEmpty, player);

//		System.out.printf("count: %d, secondCount: %d, empty: %d, secondEmpty: %d, block: %d, secondBlock: %d\n", count, secondCount, empty, secondEmpty, block, secondBlock);
		// count diagonal \
		count = 0;
		secondCount = 0;
		block = 0;
		secondBlock = 0;
		empty = -1;
		secondEmpty = -1;

		for (int i = 1; true; i++) {
			int tempX = x + i;
			int tempY = y + i;
			if (tempX >= BOARD_WIDTH || tempY >= BOARD_WIDTH) {
				block++;
				break;
			}
			State t = this.board[tempY][tempX];
			if (t == State.Blank) {
				if (empty == -1 && (tempX < BOARD_WIDTH - 1 && tempY < BOARD_WIDTH - 1)
						&& this.board[tempY + 1][tempX + 1] == oppnent) {
					empty = count;
					continue;
				} else {
					break;
				}
			}
			if (t == oppnent) {
				count++;
				continue;
			} else {
				block++;
				break;
			}
		}
		for (int i = 1; true; i++) {
			int tempX = x - i;
			int tempY = y - i;
			if (tempX < 0 || tempY < 0) {
				secondBlock++;
				break;
			}
			State t = this.board[tempY][tempX];
			if (t == State.Blank) {
				if (secondEmpty == -1 && (tempX > 0 && tempY > 0) && this.board[tempY - 1][tempX - 1] == oppnent) {
					secondEmpty = secondCount;
					continue;
				} else {
					break;
				}
			}
			if (t == oppnent) {
				secondCount++;
//                if(secondEmpty != -1) { 
//                	secondEmpty++;
//                }
				continue;
			} else {
				secondBlock++;
				break;
			}
		}
//      count += secondCount;
		updateScoreArrayOppnent(count, secondCount, block, secondBlock, empty, secondEmpty, player);

//		System.out.printf("count: %d, secondCount: %d, empty: %d, secondEmpty: %d, block: %d, secondBlock: %d\n", count, secondCount, empty, secondEmpty, block, secondBlock);
		// count diagonal /
		count = 0;
		secondCount = 0;
		block = 0;
		secondBlock = 0;
		empty = -1;
		secondEmpty = -1;

		for (int i = 1; true; i++) {
			int tempX = x + i;
			int tempY = y - i;
			if (tempX < 0 || tempY < 0 || tempX >= BOARD_WIDTH || tempY >= BOARD_WIDTH) {
				block++;
				break;
			}
			State t = this.board[tempY][tempX];
			if (t == State.Blank) {
				try {
					if (empty == -1 && (tempX < BOARD_WIDTH - 1 && tempY > 0)
							&& this.board[tempY - 1][tempX + 1] == oppnent) {
						empty = count;
						continue;
					} else {
						break;
					}
				} catch (Exception e) {
					System.out.println("\n" + this.toString() + "\n");
					System.out.print("y:");
					System.out.println(tempY - 1);
					System.out.print("x:");
					System.out.println(tempX - 1);
				}
			}
			if (t == oppnent) {
				count++;
				continue;
			} else {
				block++;
				break;
			}
		}

		for (int i = 1; true; i++) {
			int tempX = x - i;
			int tempY = y + i;
			if (tempX < 0 || tempY < 0 || tempX >= BOARD_WIDTH || tempY >= BOARD_WIDTH) {
				secondBlock++;
				break;
			}
			State t = this.board[tempY][tempX];
			if (t == State.Blank) {
				if (secondEmpty == -1 && (tempX > 0 && tempY > BOARD_WIDTH - 1)
						&& this.board[tempY + 1][tempX - 1] == oppnent) {
					secondEmpty = secondCount;
					continue;
				} else {
					break;
				}
			}
			if (t == oppnent) {
				secondCount++;
//                if(secondEmpty != -1) { 
//                	secondEmpty++;
//                }
				continue;
			} else {
				secondBlock++;
				break;
			}
		}
//      count += secondCount;
		updateScoreArrayOppnent(count, secondCount, block, secondBlock, empty, secondEmpty, player);
//		System.out.printf("count: %d, secondCount: %d, empty: %d, secondEmpty: %d, block: %d, secondBlock: %d\n", count, secondCount, empty, secondEmpty, block, secondBlock);
	}

	public void updateScoreArrayOppnent(int count, int secondCount, int block, int secondBlock, int empty,
			int secondEmpty, State player) {
		if (count == 0 && secondCount == 0) {
			return;
		}

		int[] twoBlock = new int[2];
		int[][][] scoreWindows = new int[2][2][AIM_LENGTH + 1];

		if (player == State.O) {
			scoreWindows = this.winningWindowsX;
			twoBlock = this.twoBlockX;
		} else {
			scoreWindows = this.winningWindowsO;
			twoBlock = this.twoBlockO;
		}

		if (count == 0) {
			if (secondBlock == 0) {
				if (secondEmpty == -1) {
					scoreWindows[0][0][secondCount]--;
					scoreWindows[1][0][secondCount]++;
					return;
				} else if (secondEmpty == 0) {
					return;
				} else {
					if(secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
						return;
					}else {
						scoreWindows[0][1][secondCount]--;
						scoreWindows[1][1][secondCount]++;
						return;
					}
				}
			} else {
				if (secondEmpty == -1) {
					scoreWindows[1][0][secondCount]--;
					return;
				} else {
					if(secondEmpty == 0) {
						scoreWindows[1][0][secondCount]--;
					}else {
						if(secondCount > AIM_LENGTH) {
							scoreWindows[1][1][AIM_LENGTH]--;
						}else {
							scoreWindows[1][1][secondCount]--;
						}
					}
					if (secondCount + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
					return;
				}
			}
		}

		if (secondCount == 0) {
			if (block == 0) {
				if (empty == -1) {
					scoreWindows[0][0][count]--;
					scoreWindows[1][0][count]++;
					return;
				} else if (empty == 0) {
					return;
				} else {
					if(count > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
						return;
					}else {
						scoreWindows[0][1][count]--;
						scoreWindows[1][1][count]++;
						return;
					}
				}
			} else {
				if (empty == -1) {
					scoreWindows[1][0][count]--;
					return;
				} else {
					if(empty == 0) {
						scoreWindows[1][0][count]--;
					}else {
						if(count > AIM_LENGTH) {
							scoreWindows[1][1][AIM_LENGTH]--;
						}else {
							scoreWindows[1][1][count]--;
						}
					}
					if (count + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
					return;
				}
			}
		}

		// count && secondCount != 0
		if (block == 0 && secondBlock == 0) {
			if (empty == -1) {
				if (secondEmpty == -1) {
					if(count + secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
					}else {
						scoreWindows[0][1][count + secondCount]--;
					}
					scoreWindows[1][0][count]++;
					scoreWindows[1][0][secondCount]++;
					return;
				} else if (secondEmpty == 0) {
					scoreWindows[0][0][count]--;
					scoreWindows[1][0][count]++;
					return;
				} else {
					
					if(count + secondEmpty > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
					}else {
						scoreWindows[0][1][count + secondEmpty]--;
					}
					if(secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][secondCount]--;
						scoreWindows[1][1][secondCount]++;
					}
					scoreWindows[1][0][count]++;
					return;
				}
			} else if (empty == 0) {
				if (secondEmpty == -1) {
					scoreWindows[0][0][secondCount]--;
					scoreWindows[1][0][secondCount]++;
					return;
				} else if (secondEmpty == 0) {
					return;
				} else {
					if(secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][secondCount]--;
						scoreWindows[1][1][secondCount]++;
					}
					return;
				}
			} else {
				if (secondEmpty == -1) {
					if(empty + secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
					}else {
						scoreWindows[0][1][secondCount + empty]--;
					}
					if(count > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][count]--;
						scoreWindows[1][1][count]++;
					}
					scoreWindows[1][0][secondCount]++;
					return;
				} else if (secondEmpty == 0) {
					if(count > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][count]--;
						scoreWindows[1][1][count]++;
					}
					return;
				} else {
					if(empty + secondEmpty > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
					}else {
						scoreWindows[0][1][secondEmpty + empty]--;
					}
					if(secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][secondCount]--;
						scoreWindows[1][1][secondCount]++;
					}
					if(count > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][count]--;
						scoreWindows[1][1][count]++;
					}					
					return;
				}
			}
		}

		if (block != 0 && secondBlock == 0) {
			if (empty == -1) {
				if (secondEmpty == -1) {
					if (count + secondCount > AIM_LENGTH) {
						scoreWindows[1][1][AIM_LENGTH]--;
					} else {
						scoreWindows[1][1][count + secondCount]--;
					}
					scoreWindows[1][0][secondCount]++;
					return;
				} else if (secondEmpty == 0) {
					scoreWindows[1][0][count]--;
					return;
				} else {
					if(count + secondEmpty > AIM_LENGTH) {
						scoreWindows[1][1][AIM_LENGTH]--;
					}else {
						scoreWindows[1][1][count + secondEmpty]--;
					}
					if(secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][secondCount]--;
						scoreWindows[1][1][secondCount]++;
					}
					return;
				}
			} else if (empty == 0) {
				if (secondEmpty == -1) {
					scoreWindows[0][0][secondCount]--;
					scoreWindows[1][0][secondCount]++;

				} else if (secondEmpty == 0) {
//					System.out.println();
				} else {
					if(secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][secondCount]--;
						scoreWindows[1][1][secondCount]++;
					}
				}
				scoreWindows[1][0][count]--;
				if (count + 1 >= AIM_LENGTH) {
					twoBlock[1 - 1]++;
				}
				return;
			} else {
				if (secondEmpty == -1) {
					if(empty + secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
					}else {
						scoreWindows[0][1][secondCount + empty]--;
					}
					scoreWindows[1][0][secondCount]++;
				} else if (secondEmpty == 0) {
//					System.out.println();
				} else {
					if(empty + secondEmpty > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
					}else {
						scoreWindows[0][1][secondEmpty + empty]--;
					}
					if(secondCount > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][secondCount]--;
						scoreWindows[1][1][secondCount]++;
					}
					if (secondCount + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
				}
				
				if(count > AIM_LENGTH) {
					scoreWindows[1][1][AIM_LENGTH]++;
				}else {
					scoreWindows[1][1][count]--;
				}
				if (count + 1 >= AIM_LENGTH) {
					twoBlock[1 - 1]++;
				}
				return;
			}

		}

		if (block == 0 && secondBlock != 0) {
			if (secondEmpty == -1) {
				if (empty == -1) {
					if(count + secondCount > AIM_LENGTH) {
						scoreWindows[1][1][AIM_LENGTH]--;
					}else {
						scoreWindows[1][1][secondCount + count]--;
					}
					scoreWindows[1][0][count]++;
					return;
				} else if (empty == 0) {
					scoreWindows[1][0][secondCount]--;
					return;
				} else {
					if(empty + secondCount > AIM_LENGTH) {
						scoreWindows[1][1][AIM_LENGTH]--;
					}else {
						scoreWindows[1][1][secondCount + empty]--;
					}
					if(count > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][count]--;
						scoreWindows[1][1][count]--;
					}
					return;
				}
			} else if (secondEmpty == 0) {
				if (empty == -1) {
					scoreWindows[0][0][count]--;
					scoreWindows[1][0][count]++;

				} else if (empty == 0) {

				} else {
					if(count > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
					}else {
						scoreWindows[0][1][count]--;
					}
				}
				scoreWindows[1][0][secondCount]--;
				if (secondCount + 1 >= AIM_LENGTH) {
					twoBlock[1 - 1]++;
					return;
				}
			} else {
				if (empty == -1) {
					if(count + secondEmpty <= AIM_LENGTH) {
						scoreWindows[0][1][secondEmpty + count]--;
					}else {
						scoreWindows[0][1][AIM_LENGTH]--;
					}
					scoreWindows[1][0][count]++;
				} else if (empty == 0) {
//					System.out.println();
				} else {
					if(empty + secondEmpty > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
					}else {
						scoreWindows[0][1][secondEmpty + empty]--;
					}
					if(count > AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
						scoreWindows[1][1][AIM_LENGTH]++;
					}else {
						scoreWindows[0][1][count]--;
						scoreWindows[1][1][count]++;
					}
					if (secondCount + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
				}
				
				if(secondCount > AIM_LENGTH) {
					scoreWindows[1][1][AIM_LENGTH]--;
				}else {
					scoreWindows[1][1][secondCount]--;
				}
				if (count + 1 >= AIM_LENGTH) {
					twoBlock[1 - 1]++;
					return;
				}
			}
		}

		if (block != 0 && secondBlock != 0) {
			if (empty == -1) {
				if (secondEmpty == -1) {
					if(secondCount + count + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]--;
					}
					return;
				}else if(secondEmpty == 0) {
					scoreWindows[1][0][count]--;
					scoreWindows[1][0][secondCount]--;
					return;
				}  else {
					if(count + secondCount + 2 >= AIM_LENGTH) {
						twoBlock[2 - 1]--;
					}
					return;
				}
			} else if (empty == 0) {
				if (secondEmpty == -1) {
					scoreWindows[1][0][secondCount]--;
				} else if (secondEmpty == 0) {
					scoreWindows[1][0][secondCount]--;
					if (secondCount + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
				} else {
					scoreWindows[1][1][secondCount]--;
					if (secondCount + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
				}
				scoreWindows[1][0][count]--;
				if (count + 1 >= AIM_LENGTH) {
					twoBlock[1 - 1]++;
				}
				return;
			} else {
				if (secondEmpty == -1) {
					if(secondCount + count + 2 >= AIM_LENGTH) {
						twoBlock[2 - 1]--;
					}
					return;
				} else if (secondEmpty == 0) {
					scoreWindows[1][0][secondCount]--;
					if (secondCount + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
					scoreWindows[1][1][count]--;
					if (count + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
					return;
				} else {
					scoreWindows[1][1][secondCount]--;
					if(empty + secondEmpty <= AIM_LENGTH) {
						scoreWindows[0][1][AIM_LENGTH]--;
					}else {
						scoreWindows[0][1][secondEmpty + empty]--;
					}
					if (secondCount + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
					scoreWindows[1][1][count]--;
					if (count + 1 >= AIM_LENGTH) {
						twoBlock[1 - 1]++;
					}
					return;
				}
			}
		}
	}

	public void updateScoreWindowPlayer(int x, int y, State player) {

		int count = 1;
		int secondCount = 0;
		int block = 0;
		int secondBlock = 0;
		int empty = -1;
		int secondEmpty = -1;

		// count col
		for (int i = y + 1; true; i++) {
			if (i >= BOARD_WIDTH) {
				block++;
				break;
			}
			if (this.board[i][x] == State.Blank) {
				if (empty == -1 && i < BOARD_WIDTH - 1 && board[i + 1][x] == player) {
					empty = count;
					continue;
				} else {
					break;
				}
			}

			if (this.board[i][x] == player) {
				count++;
				continue;
			} else {
				block++;
				break;
			}
		}

		for (int i = y - 1; true; i--) {
			if (i < 0) {
				secondBlock++;
				break;
			}
			if (this.board[i][x] == State.Blank) {
				if (secondEmpty == -1 && i > 0 && board[i - 1][x] == player) {
					secondEmpty = secondCount;
					continue;
				} else {
					break;
				}
			}
			if (this.board[i][x] == player) {
				secondCount++;
//				if (secondEmpty != -1) {
//					secondEmpty++;
//				}
				continue;
			} else {
				secondBlock++;
				break;
			}

		}
//		count += secondCount;
		this.updateScoreArrayPlayer(count, secondCount, block, secondBlock, empty, secondEmpty, player);
		
//		System.out.printf("\ncount: %d, secondCount: %d, empty: %d, secondEmpty: %d, block: %d, secondBlock: %d\n", count, secondCount, empty, secondEmpty, block, secondBlock);
		// count row
		count = 1;
		secondCount = 0;
		block = 0;
		secondBlock = 0;
		empty = -1;
		secondEmpty = -1;

		for (int i = x + 1; true; i++) {
			if (i >= BOARD_WIDTH) {
				block++;
				break;
			}
			if (this.board[y][i] == State.Blank) {
				if (empty == -1 && i < BOARD_WIDTH - 1 && board[y][i + 1] == player) {
					empty = count;
					continue;
				} else {
					break;
				}
			}
			if (this.board[y][i] == player) {
				count++;
				continue;
			} else {
				block++;
				break;
			}
		}

		for (int i = x - 1; true; i--) {
			if (i < 0) {
				secondBlock++;
				break;
			}

			if (this.board[y][i] == State.Blank) {
				if (secondEmpty == -1 && i > 0 && this.board[y][i - 1] == player) {
					secondEmpty = secondCount;
					continue;
				} else {
					break;
				}
			}
			if (this.board[y][i] == player) {
				secondCount++;
//                if(secondEmpty != -1) { 
//                	secondEmpty++;
//                }
				continue;
			} else {
				secondBlock++;
				break;
			}
		}
//		count += secondCount;
		this.updateScoreArrayPlayer(count, secondCount, block, secondBlock, empty, secondEmpty, player);

//		System.out.printf("count: %d, secondCount: %d, empty: %d, secondEmpty: %d, block: %d, secondBlock: %d\n", count, secondCount, empty, secondEmpty, block, secondBlock);
		// count diagonal \
		count = 1;
		secondCount = 0;
		block = 0;
		secondBlock = 0;
		empty = -1;
		secondEmpty = -1;

		for (int i = 1; true; i++) {
			int tempX = x + i;
			int tempY = y + i;
			if (tempX >= BOARD_WIDTH || tempY >= BOARD_WIDTH) {
				block++;
				break;
			}
			State t = this.board[tempY][tempX];
			if (t == State.Blank) {
				if (empty == -1 && (tempX < BOARD_WIDTH - 1 && tempY < BOARD_WIDTH - 1)
						&& this.board[tempY + 1][tempX + 1] == player) {
					empty = count;
					continue;
				} else {
					break;
				}
			}
			if (t == player) {
				count++;
				continue;
			} else {
				block++;
				break;
			}
		}
		for (int i = 1; true; i++) {
			int tempX = x - i;
			int tempY = y - i;
			if (tempX < 0 || tempY < 0) {
				secondBlock++;
				break;
			}
			State t = this.board[tempY][tempX];
			if (t == State.Blank) {
				if (secondEmpty == -1 && (tempX > 0 && tempY > 0) && this.board[tempY - 1][tempX - 1] == player) {
					secondEmpty = secondCount;
					continue;
				} else {
					break;
				}
			}
			if (t == player) {
				secondCount++;
//                if(secondEmpty != -1) { 
//                	secondEmpty++;
//                }
				continue;
			} else {
				secondBlock++;
				break;
			}
		}
//		count += secondCount;
		this.updateScoreArrayPlayer(count, secondCount, block, secondBlock, empty, secondEmpty, player);

//		System.out.printf("count: %d, secondCount: %d, empty: %d, secondEmpty: %d, block: %d, secondBlock: %d\n", count, secondCount, empty, secondEmpty, block, secondBlock);
		// count diagonal /
		count = 1;
		secondCount = 0;
		block = 0;
		secondBlock = 0;
		empty = -1;
		secondEmpty = -1;

		for (int i = 1; true; i++) {
			int tempX = x + i;
			int tempY = y - i;
			if (tempX < 0 || tempY < 0 || tempX >= BOARD_WIDTH || tempY >= BOARD_WIDTH) {
				block++;
				break;
			}
			State t = this.board[tempY][tempX];
			if (t == State.Blank) {
				try {
					if (empty == -1 && (tempX < BOARD_WIDTH - 1 && tempY > 0)
							&& this.board[tempY - 1][tempX + 1] == player) {
						empty = count;
						continue;
					} else {
						break;
					}
				} catch (Exception e) {
					System.out.println("\n" + this.toString() + "\n");
					System.out.print("y:");
					System.out.println(tempY - 1);
					System.out.print("x:");
					System.out.println(tempX - 1);
				}
			}
			if (t == player) {
				count++;
				continue;
			} else {
				block++;
				break;
			}
		}

		for (int i = 1; true; i++) {
			int tempX = x - i;
			int tempY = y + i;
			if (tempX < 0 || tempY >= BOARD_WIDTH) {
				secondBlock++;
				break;
			}
			State t = this.board[tempY][tempX];
			if (t == State.Blank) {
				if (secondEmpty == -1 && (tempX > 0 && tempY > BOARD_WIDTH - 1) && this.board[tempY + 1][tempX - 1] == player) {
					secondEmpty = secondCount;
					continue;
				} else {
					break;
				}
			}
			if (t == player) {
				secondCount++;
//                if(secondEmpty != -1) { 
//                	secondEmpty++;
//                }
				continue;
			} else {
				secondBlock++;
				break;
			}
		}
//        count += secondCount;
		this.updateScoreArrayPlayer(count, secondCount, block, secondBlock, empty, secondEmpty, player);
//		System.out.printf("count: %d, secondCount: %d, empty: %d, secondEmpty: %d, block: %d, secondBlock: %d\n", count, secondCount, empty, secondEmpty, block, secondBlock);
	}

	public void updateScoreArrayPlayer(int count, int secondCount, int block, int secondBlock, int empty,
			int secondEmpty, State player) {

		int[][][] scoreWindows = new int[2][2][AIM_LENGTH + 1];
		
		if(player == State.O) {
			scoreWindows = this.winningWindowsO;
		}else {
			scoreWindows = this.winningWindowsX;
		}
//		if (player == State.O) {
//			for (int i = 0; i < 2; i++) {
//				for (int j = 0; i < 2; i++) {
//					for(int k = 0; k <= AIM_LENGTH; k++) {
//						scoreWindows[i][j][k] = this.winningWindowsO[i][j][k];
//					}
//				}
//			}
//		} else {
//			for (int i = 0; i < 2; i++) {
//				for (int j = 0; i < 2; i++) {
//					for(int k = 0; k <= AIM_LENGTH; k++) {
//						scoreWindows[i][j][k] = this.winningWindowsX[i][j][k];
//					}
//				}
//			}
//		}
		try {
			if (block == 0 && secondBlock == 0) {
				if (empty == -1 && secondEmpty == -1) {
					if (count == 1 && secondCount == 0) {
						scoreWindows[0][0][count]++;
//						return;
					}
					if (count != 1 && secondCount == 0) {
						scoreWindows[0][0][count]++;
						scoreWindows[0][0][count - 1]--;
//						return;
					}
					if (count == 1 && secondCount != 0) {
						if(secondCount + 1 > AIM_LENGTH) {
							scoreWindows[0][0][AIM_LENGTH]++;
						}else {
							scoreWindows[0][0][secondCount + 1]++;
						}
						scoreWindows[0][0][secondCount]--;
//						return;
					}
					if (count != 1 && secondCount != 0) {
						if (count + secondCount > AIM_LENGTH) {
							scoreWindows[0][0][AIM_LENGTH]++;
						} else {
							scoreWindows[0][0][count + secondCount]++;
						}
						if (count + secondCount - 1 > AIM_LENGTH) {
							scoreWindows[0][1][AIM_LENGTH]--;
						} else {
							scoreWindows[0][1][count + secondCount - 1]--;
						}
//						return;
					}
				}
				if (empty != -1 && secondEmpty == -1) {
					if (empty == 1) {
						if (secondCount == 0) {
							scoreWindows[0][1][count]++;
							scoreWindows[0][0][count - 1]--;
//							return;
						}
	
						if (secondCount != 0) {
							if (count + secondCount > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							} else {
								scoreWindows[0][1][count + secondCount]++;
							}
							scoreWindows[0][0][secondCount]--;
							scoreWindows[0][0][count - 1]--;
//							return;
						}
					} else {
						if (secondCount == 0) {
							if(count > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][count]++;
							}
							scoreWindows[0][1][count - 1]--;
							
//							return;
						}
	
						if (secondCount != 0) {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][count + secondCount]++;
							}
							if(empty - 1 + secondCount > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]--;
							}else {
								scoreWindows[0][1][secondCount + empty - 1]--;
							}
							scoreWindows[0][1][count - 1]--;
//							return;
						}
					}
				}
				if (empty == -1 && secondEmpty != -1) {
					if (secondEmpty == 0) {
						if (count == 1) {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + 1]++;
							}
							scoreWindows[0][0][secondCount]--;
//							return;
						}
						if (count != 1) {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][count + secondCount]++;
							}
							scoreWindows[0][0][secondCount]--;
							scoreWindows[0][0][count - 1]--;
//							return;
						}
					} else {
						if (count == 1) {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + 1]++;
							}
							scoreWindows[0][1][secondCount]--;
//							return;
						}
						if (count != 1) {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][count + secondCount]++;
							}
							if(count - 1 + secondEmpty > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]--;
							}else {
								scoreWindows[0][1][count - 1 + secondEmpty]--;
							}
							scoreWindows[0][1][secondCount]--;
//							return;
						}
					}
				}
	
				if (empty != -1 && secondEmpty != -1) {
					if (empty == 1) {
						if (secondEmpty == 0) {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + 1]++;
							}
							scoreWindows[0][1][count]++;
							scoreWindows[0][0][count - 1]--;
							scoreWindows[0][0][secondCount]--;
//							return;
						} else {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + 1]++;
							}
							if(count + secondEmpty > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondEmpty + count]++;
							}
							scoreWindows[0][1][secondCount]--;
							scoreWindows[0][0][count - 1]--;
//							return;
						}
					} else {
						if (secondEmpty == 0) {
							if(count > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][count]++;
							}
							if(empty + secondCount > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + empty]++;
							}
							scoreWindows[0][1][count - 1]--;
							scoreWindows[0][0][secondCount]--;
//							return;
						} else {
							if(count + secondEmpty > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][count + secondEmpty]++;
							}
							if(secondCount + empty > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + empty]++;
							}
							if(empty + secondEmpty > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]--;
							}else {
								scoreWindows[0][1][empty + secondEmpty]--;
							}
							scoreWindows[0][1][count - 1]--;
							scoreWindows[0][1][secondCount]--;
//							return;
						}
					}
				}
			}
	//
			if (block != 0 && secondBlock == 0) {
				if (empty == -1 && secondEmpty == -1) {
					if (count == 1 && secondCount == 0) {
						scoreWindows[1][0][count]++;
//						return;
					}
					if (count != 1 && secondCount == 0) {
						scoreWindows[1][0][count]++;
						scoreWindows[1][0][count - 1]--;
//						return;
					}
					if (count == 1 && secondCount != 0) {
						if(secondCount + 1 > AIM_LENGTH) {
							scoreWindows[1][0][AIM_LENGTH]++;
						}else {
							scoreWindows[1][0][secondCount + 1]++;
						}
						scoreWindows[0][0][secondCount]--;
//						return;
					}
					if (count != 1 && secondCount != 0) {
						if(count + secondCount > AIM_LENGTH) {
							scoreWindows[1][0][AIM_LENGTH]++;
						}else {
							scoreWindows[1][0][count + secondCount]++;
						}
						if(count + secondCount - 1> AIM_LENGTH) {
							scoreWindows[1][1][AIM_LENGTH]--;
						}else {
							scoreWindows[1][1][count + secondCount - 1]--;
						}
//						return;
					}
				}
	
				if (empty != -1 && secondEmpty == -1) {
					if (empty == 1) {
						if (secondCount == 0) {
							scoreWindows[1][1][count]++;
							scoreWindows[1][0][count - 1]--;
//							return;
						}
						if (secondCount != 0) {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count + secondCount]++;
							}
							scoreWindows[1][0][count - 1]--;
							scoreWindows[0][0][secondCount]--;
//							return;
						}
					} else {
						if (secondCount == 0) {
							if(count > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count]++;
							}
							scoreWindows[1][1][count - 1]--;
//							return;
						}
						if (secondCount != 0) {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count + secondCount]++;
							}
							if(empty - 1 + secondCount > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]--;
							}else {
								scoreWindows[0][1][secondCount + empty - 1]--;
							}
							scoreWindows[1][1][count - 1]--;
//							return;
						}
					}
				}
				
				if (empty == -1 && secondEmpty != -1) {
					if(secondEmpty == 0) {
						if(count == 1) {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + 1]++;
							}
							scoreWindows[0][0][secondCount]--;
//							return;
						}else {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count + secondCount]++;
							}
							scoreWindows[0][0][secondCount]--;
							scoreWindows[1][0][count - 1]--;
//							return;
						}
							
					}else {
						if(count == 1) {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + 1]++;
							}
							scoreWindows[0][1][secondCount]--;
//							return;
						}else {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count + secondCount]++;
							}
							if(count + secondEmpty - 1> AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]--;
							}else {
								scoreWindows[1][1][secondEmpty + count - 1]--;
							}
							scoreWindows[0][1][secondCount]--;
//							return;
						}
							
					}
	
				}
				if (empty != -1 && secondEmpty != -1) {
					if (empty == 1) {
						if (secondEmpty == 0) {
							scoreWindows[1][1][count]++;
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + 1]++;
							}
							scoreWindows[0][0][secondCount]--;
							scoreWindows[1][0][count - 1]--;
//							return;
						} else {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + 1]++;
							}
							if(count + secondEmpty > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count + secondEmpty]++;
							}
							scoreWindows[0][1][secondCount]--;
							scoreWindows[1][0][count - 1]--;
//							return;
						}
					} else {
						if (secondEmpty == 0) {
							if(empty + secondCount > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + empty]++;
							}
							if(count > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count]++;
							}
							scoreWindows[0][0][secondCount]--;
							scoreWindows[1][1][count - 1]--;
//							return;
						} else {
							if(empty + secondCount > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][secondCount + empty]++;
							}
							if(count + secondEmpty > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count + secondEmpty]++;
							}
							scoreWindows[0][1][secondCount]--;
							scoreWindows[1][1][count - 1]--;
//							return;
						}
					}
	
				}
				
			}
	//
			if (block == 0 && secondBlock != 0) {
				if (empty == -1 && secondEmpty == -1) {
					if (count == 1 && secondCount == 0) {
						scoreWindows[1][0][count]++;
//						return;
					}
					if (count != 1 && secondCount == 0) {
						scoreWindows[1][0][count]++;
						scoreWindows[0][0][count - 1]--;
//						return;
					}
					if (count == 1 && secondCount != 0) {
						if(secondCount + 1 > AIM_LENGTH) {
							scoreWindows[1][0][AIM_LENGTH]++;
						}else {
							scoreWindows[1][0][secondCount + 1]++;
						}
						scoreWindows[1][0][secondCount]--;
//						return;
					}
					if (count != 1 && secondCount != 0) {
						if (count + secondCount > AIM_LENGTH) {
							scoreWindows[1][0][AIM_LENGTH]++;
						} else {
							scoreWindows[1][0][count + secondCount]++;
						}
						if (count - 1 + secondCount > AIM_LENGTH) {
							scoreWindows[1][1][AIM_LENGTH]--;
						} else {
							scoreWindows[1][1][secondCount + count - 1]--;
						}
//						return;
					}
	
				}
				if (empty != -1 && secondEmpty == -1) {
					if (empty == 1) {
						if (secondCount == 0) {
							scoreWindows[1][1][count]++;
							scoreWindows[0][0][count - 1]--;
//							return;
						} else {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count + secondCount]++;
							}
							scoreWindows[1][0][secondCount]--;
							scoreWindows[0][0][count - 1]--;
//							return;
						}
					} else {
						if (secondCount == 0) {
							if(count > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count]++;
							}
							scoreWindows[0][1][count - 1]--;
//							return;
						} else {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][count + secondCount]++;
							}
							if(empty - 1 + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]--;
							}else {
								scoreWindows[1][1][secondCount + empty - 1]--;
							}
							scoreWindows[0][1][count - 1]--;
//							return;
						}
					}
				}
				if (empty == -1 && secondEmpty != -1) {
					if (secondEmpty == 0) {
						if (count == 1) {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + 1]++;
							}
							scoreWindows[1][0][secondCount]--;
//							return;
						} else {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + count]++;
							}
							scoreWindows[0][0][count - 1]--;
							scoreWindows[1][0][secondCount]--;
//							return;
						}
					} else {
						if (count == 1) {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + 1]++;
							}
							scoreWindows[1][1][secondCount]--;
//							return;
						} else {
							if(count + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + count]++;
							}
							scoreWindows[1][1][secondCount]--;
							if(count - 1 + secondEmpty > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]--;
							}else {
								scoreWindows[0][1][count - 1 + secondEmpty]--;
							}
//							return;
						}
					}
	
				}
				if (empty != -1 && secondEmpty != -1) {
					if (secondEmpty == 0) {
						if (empty == 1) {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + 1]++;
							}
							scoreWindows[0][1][count]++;
							scoreWindows[1][0][secondCount]--;
							scoreWindows[0][0][count - 1]--;
//							return;
						} else {
							if(empty + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + empty]++;
							}
							if(count > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][count]++;
							}
							scoreWindows[1][0][secondCount]--;
							scoreWindows[0][1][count - 1]--;
//							return;
						}
					} else {
						if (empty == 1) {
							if(secondCount + 1 > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + 1]++;
							}
							if(count + secondEmpty > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][count + secondEmpty]++;
							}
							scoreWindows[1][1][secondCount]--;
							scoreWindows[0][0][count - 1]--;
//							return;
						} else {
							if(empty + secondCount > AIM_LENGTH) {
								scoreWindows[1][1][AIM_LENGTH]++;
							}else {
								scoreWindows[1][1][secondCount + empty]++;
							}
							if(count + secondEmpty > AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]++;
							}else {
								scoreWindows[0][1][count + secondEmpty]++;
							}
							scoreWindows[1][1][secondCount]--;
							if(empty + secondEmpty - 1> AIM_LENGTH) {
								scoreWindows[0][1][AIM_LENGTH]--;
							}else {
								scoreWindows[0][1][secondEmpty + empty - 1]--;
							}
							scoreWindows[0][1][count - 1]--;
//							return;
						}
					}
	
				}
			}
	//
			if (block != 0 && secondBlock != 0) {
				if (empty == -1 && secondEmpty == -1) {
//					return;
	
				}
				if (empty != -1 && secondEmpty == -1 || empty == -1 && secondEmpty != -1) {
					if (count + secondCount - 1 > AIM_LENGTH) {
						if (player == State.O) {
							this.twoBlockO[1 - 1]++;
						} else {
							this.twoBlockX[1 - 1]++;
						}
					}
//					return;
	
				}
	
				if (empty != -1 && secondEmpty != -1) {
					if (count + secondCount - 2 > AIM_LENGTH) {
						if (player == State.O) {
							this.twoBlockO[2 - 1]++;
						} else {
							this.twoBlockX[2 - 1]++;
						}
					}
//					return;
				}
			}
//		if (player == State.O) {
//			for (int i = 0; i < 2; i++) {
//				for (int j = 0; i < 2; i++) {
//					for(int k = 0; k < AIM_LENGTH + 1; k++) {
//						this.winningWindowsO[i][j][k] = scoreWindows[i][j][k];
//					}
//				}
//			}
//		} else {
//			for (int i = 0; i < 2; i++) {
//				for (int j = 0; i < 2; i++) {
//					for(int k = 0; k < AIM_LENGTH + 1; k++) {
//						this.winningWindowsX[i][j][k] = scoreWindows[i][j][k];
//					}
//				}
//			}
//		}
		}catch(Exception e) {
			System.out.println("error!!!!!!!!");
			System.out.println("error!!!!!!!!");
			System.out.println("error!!!!!!!!");
			System.out.println("error!!!!!!!!");
			System.out.println("error!!!!!!!!");
			System.out.println("error!!!!!!!!");
			
			
			System.out.println(this.toString());
			
			System.out.printf("count: %d, secondCount: %d, empty: %d, secondEmpty: %d, block: %d, secondBlock: %d\n", count, secondCount, empty, secondEmpty, block, secondBlock);
			System.out.println("error!!!!!!!!");
			System.out.println("error!!!!!!!!");
			System.out.println("error!!!!!!!!");
			System.out.println("error!!!!!!!!");
			System.out.println("error!!!!!!!!");
		}
		
	}
	
	public int getBlockFour(State player) {
		int[][][] scoreWindow = new int[2][2][AIM_LENGTH + 1]; 
		if(player == State.O) {
			scoreWindow = this.winningWindowsO;
		}else {
			scoreWindow = this.winningWindowsX;
		}
			
		int num = 0;
		for(int j = 0; j < 2; j++) {
			if(scoreWindow[1][j][4] != 0) {
				num ++;
			}
		}
		return num;
	}
	
	public int getFour(State player) {
		int[][][] scoreWindow = new int[2][2][AIM_LENGTH + 1]; 
		if(player == State.O) {
			scoreWindow = this.winningWindowsO;
		}else {
			scoreWindow = this.winningWindowsX;
		}
			
		int num = 0;
		for(int j = 0; j < 2; j++) {
			if(scoreWindow[0][j][4] != 0) {
				num +=scoreWindow[0][j][4];
			}
		}
		return num;
	}
	
	
	public int getThree(State player) {
		int[][][] scoreWindow = new int[2][2][AIM_LENGTH + 1]; 
		if(player == State.O) {
			scoreWindow = this.winningWindowsO;
		}else {
			scoreWindow = this.winningWindowsX;
		}
			
		int num = 0;
		for(int j = 0; j < 2; j++) {
			if(scoreWindow[0][j][3] != 0) {
				num += scoreWindow[0][j][3];
			}
		}
		return num;
	}
	
	public int getFive(State player) {
		int[][][] scoreWindow = new int[2][2][AIM_LENGTH + 1]; 
		if(player == State.O) {
			scoreWindow = this.winningWindowsO;
		}else {
			scoreWindow = this.winningWindowsX;
		}
			
		int num = 0;
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				if(scoreWindow[i][j][5] != 0) {
					num += scoreWindow[i][j][5];
				}
			}
		}
		return num;
	}
	
	public int getEight(State player) {
		int[][][] scoreWindow = new int[2][2][AIM_LENGTH + 1]; 
		if(player == State.O) {
			scoreWindow = this.winningWindowsO;
		}else {
			scoreWindow = this.winningWindowsX;
		}
			
		int num = 0;
		for(int j = 0; j < 2; j++) {
			if(scoreWindow[0][j][8] != 0) {
				num +=scoreWindow[0][j][8];
			}
		}
		return num;
	}
	
	public int getBlockEight(State player) {
		int[][][] scoreWindow = new int[2][2][AIM_LENGTH + 1]; 
		if(player == State.O) {
			scoreWindow = this.winningWindowsO;
		}else {
			scoreWindow = this.winningWindowsX;
		}
			
		int num = 0;
		for(int j = 0; j < 2; j++) {
			if(scoreWindow[1][j][8] != 0) {
				num ++;
			}
		}
		return num;
	}
	
	public int getNine(State player) {
		int[][][] scoreWindow = new int[2][2][AIM_LENGTH + 1]; 
		if(player == State.O) {
			scoreWindow = this.winningWindowsO;
		}else {
			scoreWindow = this.winningWindowsX;
		}
			
		int num = 0;
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				if(scoreWindow[i][j][9] != 0) {
					num += scoreWindow[i][j][9];
				}
			}
		}
		return num;
	}

	public int getScoreX() {
		int result = 0;
		int aim = AIM_LENGTH;
		int[][][] aimScore;
		if (aim == 6) {
			aimScore = SCORE_SIX;
			for (int i = 0; i < 2; i++) {
				result += this.twoBlockX[i] * SCORE_SIX_TWO[i];
			}
		} else if(aim == 8){
			aimScore = SCORE_EIGHT;
			for (int i = 0; i < 2; i++) {
				result += this.twoBlockX[i] * SCORE_EIGHT_TWO[i];
			}
		}else {
			aimScore = SCORE_TEN;
			for (int i = 0; i < 2; i++) {
				result += this.twoBlockX[i] * SCORE_EIGHT_TWO[i];
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 1; k < AIM_LENGTH + 1; k++) {
					result += this.winningWindowsX[i][j][k] * aimScore[i][j][k];
				}
			}
		}
//		if(this.getFour(State.X) > 2) {
//			result += Integer.MAX_VALUE;
//		}
//		if(this.getFour(State.X) > 1 && this.getFive(State.X) > 1) {
//			result += Integer.MAX_VALUE;
//		}
		return result;
	}

	public int getScoreO() {
		int result = 0;
		int aim = AIM_LENGTH;
		int[][][] aimScore;
		if (aim == 6) {
			aimScore = SCORE_SIX;
			for (int i = 0; i < 2; i++) {
				result += this.twoBlockX[i] * SCORE_SIX_TWO[i];
			}
		} else if(aim == 8){
			aimScore = SCORE_EIGHT;
			for (int i = 0; i < 2; i++) {
				result += this.twoBlockX[i] * SCORE_EIGHT_TWO[i];
			}
		}else {
			aimScore = SCORE_TEN;
			for (int i = 0; i < 2; i++) {
				result += this.twoBlockX[i] * SCORE_EIGHT_TWO[i];
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 1; k < AIM_LENGTH + 1; k++) {
					result += this.winningWindowsO[i][j][k] * aimScore[i][j][k];
				}
			}
		}
//		if(this.getFour(State.O) > 2) {
//			result += Integer.MAX_VALUE;
//		}
//		if(this.getFour(State.O) > 1 && this.getFive(State.O) > 1) {
//			result += Integer.MAX_VALUE;
//		}
		return result;
	}

	private boolean checkWin(int col, int row, State player) {
		int N = BOARD_WIDTH;
		// Check vertical
		int count_vertical = 1;
		int row_up = row - 1;
		while (row_up >= 0 && board[row_up][col] == player) {
			count_vertical++;
			row_up--;
		}
		int row_down = row + 1;
		while (row_down < N && board[row_down][col] == player) {
			count_vertical++;
			row_down++;
		}

		if (count_vertical >= AIM_LENGTH) {
			winner = playersTurn;
			gameOver = true;
			return true;
		}

		// Check horizontal
		int count_horizontal = 1;
		int col_left = col - 1;
		while (col_left >= 0 && board[row][col_left] == player) {
			count_horizontal++;
			col_left--;
		}
		int col_right = col + 1;
		while (col_right < N && board[row][col_right] == player) {
			count_horizontal++;
			col_right++;
		}

		if (count_horizontal >= AIM_LENGTH) {
			winner = playersTurn;
			gameOver = true;
			return true;
		}

		// Check diagonal
		int count_diagonal_left = 1;
		int row_left_up = row - 1, col_left_up = col - 1;
		while (row_left_up >= 0 && col_left_up >= 0 && board[row_left_up][col_left_up] == player) {
			count_diagonal_left++;
			row_left_up--;
			col_left_up--;
		}
		int row_right_down = row + 1, col_right_down = col + 1;
		while (row_right_down < N && col_right_down < N && board[row_right_down][col_right_down] == player) {
			count_diagonal_left++;
			row_right_down++;
			col_right_down++;
		}

		int count_diagonal_right = 1;
		int row_left_down = row + 1, col_left_down = col - 1;
		while (row_left_down < N && col_left_down >= 0 && board[row_left_down][col_left_down] == player) {
			count_diagonal_right++;
			row_left_down++;
			col_left_down--;
		}
		int row_right_up = row - 1, col_right_up = col + 1;
		while (row_right_up >= 0 && col_right_up < N && board[row_right_up][col_right_up] == player) {
			count_diagonal_right++;
			row_right_up--;
			col_right_up++;
		}

		if (count_diagonal_left >= AIM_LENGTH || count_diagonal_right >= AIM_LENGTH) {
			winner = playersTurn;
			gameOver = true;
			return true;
		}
		return false;
	}

	public Board getDeepCopy() {
		Board board = new Board();

		for (int i = 0; i < board.board.length; i++) {
			board.board[i] = this.board[i].clone();
		}

		for (int i = 0; i < 2; i++) {
			for (int j = 0; i < 2; i++) {
				for(int k = 0; k < AIM_LENGTH + 1; k++) {
					board.winningWindowsX[i][j][k] = this.winningWindowsX[i][j][k];
					board.winningWindowsO[i][j][k] = this.winningWindowsO[i][j][k];
				}
			}
		}

		board.twoBlockO = this.twoBlockO.clone();
		board.twoBlockX = this.twoBlockX.clone();
		board.playersTurn = this.playersTurn;
		board.oppnent = this.oppnent;
		board.winner = this.winner;
		board.movesAvailable = new HashSet<>();
		board.movesAvailable.addAll(this.movesAvailable);
		board.moveCount = this.moveCount;
		board.gameOver = this.gameOver;
		board.preMoveY = this.preMoveY;
		board.preMoveX = this.preMoveX;

		return board;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < BOARD_WIDTH; y++) {
			if (y == 0) {
				sb.append("  ");
				for (int i = 0; i < BOARD_WIDTH; i++) {
					sb.append(i < 10 ? " " + i : i);
				}
				sb.append("\n");
			}
			for (int x = 0; x < BOARD_WIDTH; x++) {
				if (x == 0) {
					sb.append(y < 10 ? " " + y : y);
					sb.append(" ");
				}
				if (board[y][x] == State.Blank) {
					sb.append("-");
				} else {
					sb.append(board[y][x].name());
				}
				sb.append(" ");

			}
			if (y != BOARD_WIDTH - 1) {
				sb.append("\n");
			}
		}

		return new String(sb);
	}

}
