/*
 *  constructs a prototype Lane View
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class LaneView implements LaneObserver, ActionListener {

	//private int roll;
	private boolean initDone;

	JFrame frame;
	Container cpanel;
	Vector bowlers;
	//int cur;
	//Iterator bowlIt;

	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;
	JLabel currBallThrowerName, currentThrow;
	JButton maintenance;
	JButton ballThrow;

	Lane lane;
	String imgPath = "images/";
	int pinCount = 10;

	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;

		initDone = true;
		frame = new JFrame("Lane - " + laneNum);
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
			}
		});

		cpanel.add(new JPanel());

	}

	public void show() {
		frame.setVisible(true);
	}

	public void hide() {
		frame.setVisible(false);
	}

	private JPanel makeFrame(Party party) {
		int i, j;
		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][23];
		ballLabel = new JLabel[numBowlers][23];
		scores = new JPanel[numBowlers][10];
		scoreLabel = new JLabel[numBowlers][10];
		ballGrid = new JPanel[numBowlers][10];
		pins = new JPanel[numBowlers];

		for (i = 0; i != numBowlers; i++) {
			for (j = 0; j != 23; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (i = 0; i != numBowlers; i++) {
			for (j = 0; j < pinCount - 1; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			j = pinCount - 1;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}

		for (i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(
				BorderFactory.createTitledBorder(
					((Bowler) bowlers.get(i)).getNickName()));
			pins[i].setLayout(new GridLayout(0, pinCount));
			for (int k = 0; k < pinCount; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(2, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}
	int gifArray[][] = new int[10][10];
	
	public int[][] initGifArray(int[][] gifArr) {
		int i, j;
		for(i = 0; i < 10; i++) {
			for(j = 0; j < 10; j++) {
				gifArr[i][j] = 0;
			}
		}
		return gifArr;
	}

	public void receiveLaneEvent(Party pty, int theIndex, Bowler theBowler, int[][] theCumulScore, HashMap theScore, int theFrameNum, int[] theCurScores, int theBall, boolean mechProblem) {
		if (lane.isPartyAssigned()) {
			gifArray = initGifArray(gifArray);
			
			int numBowlers = pty.getMembers().size();
			while (!initDone) {
				//System.out.println("chillin' here.");
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}

			if (theFrameNum == 1
					&& theBall == 0
					&& theIndex == 0) {
				System.out.println("Making the frame.");
				cpanel.removeAll();
				cpanel.add(makeFrame(pty), "Center");

				// Button Panel
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());

				Insets buttonMargin = new Insets(4, 4, 4, 4);

				maintenance = new JButton("Maintenance Call");
				JPanel maintenancePanel = new JPanel();
				maintenancePanel.setLayout(new FlowLayout());
				maintenance.addActionListener(this);
				maintenancePanel.add(maintenance);

				buttonPanel.add(maintenancePanel);

				cpanel.add(buttonPanel, "South");
				
				JPanel demoButtonPanel = new JPanel();
				demoButtonPanel.setLayout(new FlowLayout());
				
				Insets new_buttonMargin = new Insets(8, 8, 8, 8);
				
				JPanel demoThrowPanel = new JPanel();
				demoThrowPanel.setLayout(new FlowLayout());
				
				ballThrow = new JButton("Throw");
				ballThrow.addActionListener(this);
				demoThrowPanel.add(ballThrow);
                demoButtonPanel.add(demoThrowPanel);
                cpanel.add(demoButtonPanel, "North");
                
                JPanel demoPanel = new JPanel();
                demoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                currBallThrowerName = new JLabel("Thrower - ");
          
                      
                currentThrow = new JLabel();
                
                demoPanel.add(currBallThrowerName);
                demoPanel.add(currentThrow);
                
                cpanel.add(demoPanel,"East");

				frame.pack();

			}
			this.currentThrow.setText(theBowler.getNickName());
			int[][] lescores = theCumulScore;
			for (int k = 0; k < numBowlers; k++) {
				for (int i = 0; i <= theFrameNum - 1; i++) {
					if (lescores[k][i] != 0)
						scoreLabel[k][i].setText(
								(Integer.valueOf(lescores[k][i])).toString());
				}
				for (int i = 0; i < 21; i++) {
					if (((int[]) ((HashMap) theScore)
							.get(bowlers.get(k)))[i]
							!= -1)
						if (((int[]) ((HashMap) theScore)
								.get(bowlers.get(k)))[i]
								== 10
								&& (i % 2 == 0 || i == 19))
							ballLabel[k][i].setText("X");
						else if (
								i > 0
										&& ((int[]) ((HashMap) theScore)
										.get(bowlers.get(k)))[i]
										+ ((int[]) ((HashMap) theScore)
										.get(bowlers.get(k)))[i
										- 1]
										== 10
										&& i % 2 == 1)
							ballLabel[k][i].setText("/");
						else if ( ((int[])((HashMap) theScore).get(bowlers.get(k)))[i] == -2 ){

							ballLabel[k][i].setText("F");
						} else
							ballLabel[k][i].setText(
									(Integer.valueOf(((int[]) ((HashMap) theScore)
											.get(bowlers.get(k)))[i]))
											.toString());
				}
				int flag;
				for (int i = 0; i <= theFrameNum - 1; i++) {
					flag = 0;
					if (lescores[k][i] != 0) {
						if(i == 0 || (i > 0 && lescores[k][i] != lescores[k][i - 1] - 1)) {
							if(i == 9) {
								scoreLabel[k][i].setText(
										(Integer.valueOf(lescores[k][i] + 1)).toString());
							}
							else {
						      scoreLabel[k][i].setText((Integer.valueOf(lescores[k][i])).toString());
							}
					
							int previous;
							int diffLimits[] = {6, 8, 10, 11};
							Icon img;

							if (i > 0) {
								previous = lescores[k][i - 1];
							}
							else previous = 0;

							int absDifference = Math.abs(lescores[k][i] - previous);

							if(absDifference < diffLimits[0])
								img = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(imgPath + "lowest_little.jpg")));
							else if(absDifference < diffLimits[1])
								img = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(imgPath + "mid_low_little.jpg")));

							else if(absDifference < diffLimits[3]) {
								if (absDifference == diffLimits[2]) {
									flag = 1;
								}
								img = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(imgPath + "mid_high_little.jpg")));
							}
							else
								img = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(imgPath + "high_little.jpg")));

							JLabel imgJLabel = new JLabel(img);
							JPanel imgJPanel = new JPanel();
							imgJPanel.setLayout(new FlowLayout());
							imgJPanel.add(imgJLabel);

							if(gifArray[k][i] == 1 || flag == 1 && gifArray[k][i] != -1) {
								scores[k][i].add(imgJPanel);
								gifArray[k][i] = -1;
							}
							if(gifArray[k][i] != -1)
								gifArray[k][i] += 1;
						}
						
					}
				}
				
			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame();
		}
		if(e.getSource().equals(ballThrow)) {
		   lane.throwBall();
		}
	}

}
