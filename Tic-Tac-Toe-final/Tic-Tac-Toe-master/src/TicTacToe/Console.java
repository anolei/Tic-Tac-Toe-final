package TicTacToe;
import TicTacToe.Algorithms;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Console {

    private Board board;
    private Scanner sc = new Scanner(System.in);
    private APISender sender;
    
    private Console(int GameId) {
        board = new Board();
        this.sender = new APISender(GameId);
    }

    private void play () {

        System.out.println("Starting a new game.");

        while (true) {
//        	printGameStatus();
//            playMove();
            playManual();

            if (board.isGameOver()) {
                printWinner();

                if (!tryAgain()) {
                    break;
                }
            }
        }
    }
    
    private void firstPlay() {
    	System.out.println("Starting a new game.");
    	int[] move = new int[2];
    	int[] oppnentMove = new int[2];
    	while (true) {
    		// run AI
    		long bt = System.currentTimeMillis();  
        	Algorithms.alphaBetaAdvanced(Board.State.X, board);
        	move = board.getPreMove();
        	System.out.printf("x:%d, y:%d", move[0], move[1]);
        	System.out.printf("\nmoveNum:%d", board.getMoveCount());
        	long et2 = System.currentTimeMillis();  
        	System.out.println("\n[1]Runtime:"+(et2 - bt)+ "ms");  
        	
        	// send to API
        	try {
        		while(true) {
        			if(sender.sendMove(board.getPreMoveX(), board.getPreMoveY())) break;
        		}
			} catch (IOException e) {
				System.out.println("console sender get error");
				e.printStackTrace();
			}
        	printGameStatus();
        	
        	
        	// get oppnent's move
        	while (true) {
        		try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					System.out.println("console sleep error");
					e.printStackTrace();
				}
        		try {
        			
					oppnentMove = sender.getMove();
				} catch (IOException e) {
					System.out.println("console sender get error");
					e.printStackTrace();
				}
        		if(!Arrays.equals(move, oppnentMove)) {
        			if(board.move(oppnentMove[0] + oppnentMove[1] * Board.BOARD_WIDTH )) {
        				break;
        			}
        		}
        	}
        	
        	printGameStatus();

            if (board.isGameOver()) {
                printWinner();
                break;
            }
        }
    }
    
    private void secondPlay() {
    	System.out.println("Starting a new game.");
    	int[] move = new int[2];
    	int[] oppnentMove = new int[2];
    	while (true) {
    		// get oppnent's move
        	while (true) {
        		try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					System.out.println("console sleep error");
					e.printStackTrace();
				}
        		try {
        			
					oppnentMove = sender.getMove();
				} catch (IOException e) {
					System.out.println("console sender get error");
					e.printStackTrace();
				}
        		if(!Arrays.equals(move, oppnentMove) && oppnentMove[0] != -1) {
        			if(board.move(oppnentMove[0] + oppnentMove[1] * Board.BOARD_WIDTH )) {
        				break;
        			}
        		}
        	}
        	
        	printGameStatus();
    		
    		
    		// run AI
    		long bt = System.currentTimeMillis();  
        	Algorithms.alphaBetaAdvanced(Board.State.X, board);
        	move = board.getPreMove();
        	System.out.printf("x:%d, y:%d", move[0], move[1]);
        	System.out.printf("\nmoveNum:%d", board.getMoveCount());
        	long et2 = System.currentTimeMillis();  
        	System.out.println("\n[1]Runtime:"+(et2 - bt)+ "ms");  
        	
        	// send to API
        	
        	try {
        		while(true) {
        			if(sender.sendMove(board.getPreMoveX(), board.getPreMoveY())) break;
        		}
			} catch (IOException e) {
				System.out.println("console sender get error");
				e.printStackTrace();
			}
        	printGameStatus();
        	
        	
        	

            if (board.isGameOver()) {
                printWinner();
                break;
            }
        }
    }
    

    private void playMove () {
    	
        if (board.getTurn() == Board.State.X) {
        	long bt = System.currentTimeMillis();  
//        	getPlayerMove();
        	Algorithms.alphaBetaAdvanced(Board.State.X, board);
        	int[] move = board.getPreMove();
        	System.out.printf("x:%d, y:%d", move[0], move[1]);
        	System.out.printf("\nmoveNum:%d", board.getMoveCount());
        	long et2 = System.currentTimeMillis();  
        	System.out.println("\n[1]Runtime:"+(et2 - bt)+ "ms");  
        	
        } else {
        	long bt = System.currentTimeMillis();  
//        	Algorithms.alphaBetaAdvanced(Board.State.O, board);
        	getPlayerMove();
        	int[] move = board.getPreMove();
        	System.out.printf("x:%d, y:%d", move[0], move[1]);
        	System.out.printf("\nmoveNum:%d", board.getMoveCount());
        	long et2 = System.currentTimeMillis();  
        	System.out.println("\n[1]Runtime:"+(et2 - bt)+ "ms");
        }
        
    	//Algorithms.alphaBetaAdvanced(board.getTurn(), board);
    }
    
    private void playManual() {
    	getPlayerMove();
    	printGameStatus();
    	System.out.println("five:" + board.getFive(board.getOppnent()));
    	System.out.println("four:" + board.getFour(board.getOppnent()));
    	System.out.println("blockFour:" + board.getBlockFour(board.getOppnent()));
    	System.out.println("three:" + board.getThree(board.getOppnent()));
    	
    }

    private void printGameStatus () {
        System.out.println("\n" + board + "\n");
        System.out.println(board.getTurn().name() + "'s turn.");
        board.printScoreWindow();
    }

    private void getPlayerMove () {
        System.out.print("Index of move: ");

        int x = sc.nextInt();
        int y = sc.nextInt();
        int move = y * Board.BOARD_WIDTH + x;
        

        if (move < 0 || move >= Board.BOARD_WIDTH* Board.BOARD_WIDTH) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe index of the move must be between 0 and "
                    + (Board.BOARD_WIDTH * Board.BOARD_WIDTH - 1) + ", inclusive.");
        } else if (!board.move(move)) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe selected index must be blank.");
        }
    }

    private void printWinner () {
        Board.State winner = board.getWinner();

        System.out.println("\n" + board + "\n");

        if (winner == Board.State.Blank) {
            System.out.println("The TicTacToe is a Draw.");
        } else {
            System.out.println("Player " + winner.toString() + " wins!");
        }
    }

    private boolean tryAgain () {
        if (promptTryAgain()) {
            board.reset();
            System.out.println("Started new game.");
            System.out.println("X's turn.");
            return true;
        }

        return false;
    }

    private boolean promptTryAgain () {
        while (true) {
            System.out.print("Would you like to start a new game? (Y/N): ");
            String response = sc.next();
            if (response.equalsIgnoreCase("y")) {
                return true;
            } else if (response.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Invalid input.");
        }
    }

    public static void main(String[] args) {
    	System.out.println("Input the game Id: ");
    	Scanner sc = new Scanner(System.in);
        Console ticTacToe = new Console(sc.nextInt());
        
        System.out.println("input your order:(first: 1, second 2): ");
        int i = sc.nextInt();
        while(true) {
        	if(i == 1 || i == 2) {
        		break;
        	}
        	else {
        		i = sc.nextInt();
        	}
        }
        
        if(i == 1) {
        	ticTacToe.firstPlay();
        }else {
        	ticTacToe.secondPlay();
        }
//    	Console ticTacToe = new Console(1);
//        ticTacToe.play();
    }

}
