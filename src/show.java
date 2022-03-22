import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class show implements ActionListener, ListSelectionListener{
    private final JFrame win;

    private final JButton finished,lastScores,maxPlayerScore,minPlayerScore,minScore,maxScore,TopScorer,LowestScorer;

    private final JList<Vector> outputList;
    private final JList<Vector> allBowlers;

    private String selectedNick;

    private Vector bowlerdb;
    private final Vector party;




    public show(){

        win = new JFrame("Show Scores");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        // Controls Panel
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(9, 2));
        controlsPanel.setBorder(new TitledBorder("Queries"));

        maxScore = new JButton("Top Score");
        JPanel maxScorePanel = new JPanel();
        maxScorePanel.setLayout(new FlowLayout());
        maxScore.addActionListener(this);
        maxScorePanel.add(maxScore);
        controlsPanel.add(maxScorePanel);

        TopScorer = new JButton("Top Scorer");
        JPanel TopScorerPanel = new JPanel();
        TopScorerPanel.setLayout(new FlowLayout());
        TopScorer.addActionListener(this);
        TopScorerPanel.add(TopScorer);
        controlsPanel.add(TopScorerPanel);

        minScore = new JButton("Lowest Score");
        JPanel minScorePanel = new JPanel();
        minScorePanel.setLayout(new FlowLayout());
        minScore.addActionListener(this);
        minScorePanel.add(minScore);
        controlsPanel.add(minScorePanel);


        LowestScorer = new JButton("Lowest Scorer");
        JPanel LowestScorePanel = new JPanel();
        LowestScorePanel.setLayout(new FlowLayout());
        LowestScorer.addActionListener(this);
        LowestScorePanel.add(LowestScorer);
        controlsPanel.add(LowestScorePanel);


        maxPlayerScore = new JButton("Player Highest Score");
        JPanel maxPlayerScorePanel = new JPanel();
        maxPlayerScorePanel.setLayout(new FlowLayout());
        maxPlayerScore.addActionListener(this);
        maxPlayerScorePanel.add(maxPlayerScore);
        controlsPanel.add(maxPlayerScorePanel);

        minPlayerScore = new JButton("Player Lowest Score");
        JPanel minPlayerScorePanel = new JPanel();
        minPlayerScorePanel.setLayout(new FlowLayout());
        minPlayerScore.addActionListener(this);
        minPlayerScorePanel.add(minPlayerScore);
        controlsPanel.add(minPlayerScorePanel);

        lastScores = new JButton("Last Scores by player");
        JPanel lastScoresPanel = new JPanel();
        lastScoresPanel.setLayout(new FlowLayout());
        lastScores.addActionListener(this);
        lastScoresPanel.add(lastScores);
        controlsPanel.add(lastScoresPanel);

        finished = new JButton("Close");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);
        controlsPanel.add(finishedPanel);

        // Scores Database
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout());
        scorePanel.setBorder(new TitledBorder("Results"));


        party = new Vector();

        Vector empty = new Vector();
        empty.add("(Empty)");

        outputList = new JList(empty);
        outputList.setFixedCellWidth(120);
        outputList.setVisibleRowCount(5);
        outputList.addListSelectionListener(this);
        JScrollPane scorePane = new JScrollPane(outputList);
        scorePanel.add(scorePane);


        // Bowler Database
        JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Player List"));

        try {
            bowlerdb = new Vector(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlerdb = new Vector();
        }
        allBowlers = new JList(bowlerdb);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(120);
        JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        colPanel.add(scorePanel);
        colPanel.add(controlsPanel);
        colPanel.add(bowlerPanel);


        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);


    }



        }