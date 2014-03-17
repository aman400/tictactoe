package game.tictactoe;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;

public class Game 
{
	public static void main(String args[])
	{
		TicTacToe ttt = new TicTacToe();
		ttt.newGame();
		
	}
}

class TicTacToe implements ActionListener
{
	private Board b;
	private Player p1, p2;
	private boolean gameRunning;
	private JFrame jf;
	private JButton startButton;
	private final int FRAME_SIZE = 500;
	private JLabel l1, l2, score1, score2;
	private boolean need_to_update_scores;
	private String firstPlayerName, secondPlayerName;
	Thread t;
	
	TicTacToe()
	{
		firstPlayerName = JOptionPane.showInputDialog("Enter First Player name!!!");
		secondPlayerName = JOptionPane.showInputDialog("Enter Second Player Name!!!");
		
		this.p1 = new Player(firstPlayerName);
		this.p2 = new Player(secondPlayerName);
		
		this.score1 = new JLabel();
		this.score2 = new JLabel();
		this.score1.setText(Integer.toString(p1.getPlayerScores()));
		this.score2.setText(Integer.toString(p2.getPlayerScores()));
		
		this.b = new Board(FRAME_SIZE);
		this.gameRunning = false;
		
		this.jf = new JFrame();
		this.startButton =new JButton(" Start ");
		
		this.l1 = new JLabel(p1.getPlayerName() +" : ");
		this.l2 = new JLabel(p2.getPlayerName() + " : ");
		
		this.startButton.setBounds( 20, FRAME_SIZE + 10, 100, 50);
		
		// set label bounds
		l1.setBounds(150, FRAME_SIZE + 10, 100, 50);
		l2.setBounds(300, FRAME_SIZE + 10, 100, 50);
		this.score1.setBounds(250, FRAME_SIZE+ 10, 100, 50);
		this.score2.setBounds(400, FRAME_SIZE + 10, 100, 50);
		
		b.getBoardPanel().setBounds(0, 0, FRAME_SIZE, FRAME_SIZE);

		// Add components to frame
		jf.add(l1);
		jf.add(l2);
		jf.add(score1);
		jf.add(score2);
		
		this.jf.add(b.getBoardPanel());
		this.jf.add(startButton);
		
		this.startButton.addActionListener(this);
		
		this.jf.setLayout(null);
		this.jf.setTitle("TicTacToe");
		this.jf.setVisible(true);
		this.jf.setSize(FRAME_SIZE, FRAME_SIZE + 100);
		this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void newGame()
	{		
		p1.updatePlayerScores(0);
		p2.updatePlayerScores(0);
		this.toggleFreezeBoard(true);
		this.toggleTimeToUpdateScores(false);
		while(true)
		{			
			t = new Thread(new GamePlay(this));
			t.start();
		}
		
	}
	
	public boolean isButtonEnabled(JButton button)
	{
		return button.isEnabled();
	}
	
	public boolean isGameRunning()
	{
		return this.gameRunning;
	}
	
	public void toggleGameRunning(boolean value)
	{
		this.gameRunning = value;
	}
	
	public Board getBoard()
	{
		return this.b;
	}
	
	public Player getFirstPlayer()
	{
		return this.p1;
	}
	
	public boolean playerTurn()
	{
		return b.getBooleanValue();
	}
	
	public Player getSecondPlayer()
	{
		return this.p2;
	}
	
	public JButton getStartButton()
	{
		return this.startButton;
	}
	
	public boolean isTimeToUpdateScores()
	{
		return this.need_to_update_scores;
	}
	
	public void toggleTimeToUpdateScores(boolean value)
	{
		this.need_to_update_scores = value;
	}
	
	public void setPlayerScores()
	{	
		if(this.isTimeToUpdateScores())
		{
			this.toggleTimeToUpdateScores(false);
			if(this.playerTurn())
			{
				this.p2.updatePlayerScores(1);
				JOptionPane.showMessageDialog(jf, p2.getPlayerName() + " won!!!");
			}
			if(!this.playerTurn())
			{
				this.p1.updatePlayerScores(1);
				JOptionPane.showMessageDialog(jf, p1.getPlayerName() + " won!!");
			}
			this.toggleStartButton(true);
		}
		
		this.score1.setText(Integer.toString(p1.getPlayerScores()));
		this.score2.setText(Integer.toString(p2.getPlayerScores()));
	}
	
	public void toggleStartButton(boolean value)
	{
		this.startButton.setEnabled(value);
	}
	
	public void toggleFreezeBoard(boolean value)
	{
		for(JButton[] j : b.getBoardButtons())
			for(JButton button : j)
				b.toggleButton(button, !value);
	}
	
	public void resetBoard()
	{
		for(JButton[] j : b.getBoardButtons())
			for( JButton button : j)
				b.setButtonText(button, "");
	}

	@Override
	public void actionPerformed(ActionEvent ae) 
	{
		if(ae.getSource() == startButton)
		{
			
			this.resetBoard();
			gameRunning = true;
			this.toggleFreezeBoard(false);
			this.toggleStartButton(false);
			b.setBooleanValue(true);
		}
	}

}



class Board implements ActionListener
{
	
	// Declare variables 
	private JPanel boardPanel;
	private JButton[][] boardButtons;
	
	private Font f;
	private boolean turn;

	Board(int FRAME_SIZE)
	{
		//Initialize variable
		this.boardPanel = new JPanel();
		
		this.boardButtons = new JButton[3][3];
		
		this.f = new Font("arial", Font.BOLD, FRAME_SIZE/10);
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
			{
				this.boardPanel.setLayout(null);
				
				this.boardButtons[i][j] = new JButton("");				
				this.boardButtons[i][j].setBounds(j*FRAME_SIZE/3, i*FRAME_SIZE/3, FRAME_SIZE/3, FRAME_SIZE/3);
				
				this.boardPanel.add(boardButtons[i][j]);
				this.turn = true;
				
				//register listener
				this.boardButtons[i][j].addActionListener(this);
				this.boardButtons[i][j].setFont(f);
			}
		
	}
	
	public JPanel getBoardPanel()
	{
		return boardPanel;
	}
	
	public void setButtonText(JButton button, String text)
	{
		button.setText(text);
	}
	
	public void setBooleanValue(boolean bool)
	{
		this.turn = bool;
	}

	public boolean isButtonEnabled(JButton button)
	{
		return button.isEnabled();
	}
	
	public void toggleButton(JButton button, boolean value)
	{
		button.setEnabled(value);
	}
	
	public String getButtonText(JButton button)
	{
		return button.getText();
	}	
	
	public JButton getBoardButton(int i, int j)
	{
		return boardButtons[i][j];
	}
	
	public boolean getBooleanValue()
	{
		return this.turn;
	}

	public JButton[][] getBoardButtons()
	{
		return this.boardButtons;
	}
	
	public boolean areEqual(String b1, String b2, String b3)
	{
		return (b1.equals(b2) && b2.equals(b3) && b2 != "");
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				if(ae.getSource() == this.getBoardButton(i, j) && this.isButtonEnabled(this.getBoardButton(i, j)))
				{					
					if(this.getBooleanValue())
					{
				//		this.getBoardButton(i, j).setBackground(Color.blue);
						this.setButtonText(this.getBoardButton(i, j), "X");
						this.setBooleanValue(false);
					}
					
					else
					{
				//		this.getBoardButton(i, j).setBackground(Color.RED);
						this.setButtonText(this.getBoardButton(i, j), "0");
						this.setBooleanValue(true);
					}
					
					this.toggleButton(this.getBoardButton(i, j), false);
				}
	}
}




class Player
{
	private String player_name;
	private int player_scores;
	
	
	Player(String name)
	{
		this.player_name = name;
		this.player_scores = 0;
	}

	public void setPlayerName(String name)
	{
		this.player_name = name;
	}
	
	public final void updatePlayerScores(int value)
	{
		this.player_scores += value;
	}
	
	public String getPlayerName()
	{
		return this.player_name;
	}
	
	public int getPlayerScores()
	{
		return this.player_scores;
	}
}	


class GamePlay implements Runnable
{
	
	private final TicTacToe game;
	private Board board;
	
	GamePlay(TicTacToe g)
	{
		this.game = g;
		this.board = game.getBoard();
	}

	public void run()
	{	
		boolean boardFull = true;
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				if(board.isButtonEnabled(board.getBoardButton(i, j)))
					boardFull = false;
		
		if(boardFull)
			game.toggleGameRunning(false);
		
		if(game.isGameRunning())
		{
			game.toggleStartButton(false);
			for(int i = 0; i < 3; i++)
			{
				if((board.areEqual(board.getButtonText(board.getBoardButton(i, 0)), board.getButtonText(board.getBoardButton(i, 1)), board.getButtonText(board.getBoardButton(i, 2))) || 
						board.areEqual(board.getButtonText(board.getBoardButton(0, i)), board.getButtonText(board.getBoardButton(1, i)), board.getButtonText(board.getBoardButton(2, i))) ||
						board.areEqual(board.getButtonText(board.getBoardButton(0, 0)), board.getButtonText(board.getBoardButton(1, 1)), board.getButtonText(board.getBoardButton(2, 2))) ||
						game.getBoard().areEqual(board.getButtonText(board.getBoardButton(2, 0)), board.getButtonText(board.getBoardButton(1, 1)), board.getButtonText(board.getBoardButton(0, 2)))) && game.isGameRunning())
				{
					game.toggleGameRunning(false);
					game.toggleFreezeBoard(true);
					game.toggleTimeToUpdateScores(true);
				} 
			}
		}
		else
		{
			game.setPlayerScores();
		}
		
	}
}