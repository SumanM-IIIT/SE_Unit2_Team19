/* ShowScore.java 
 * 
 * class for adhoc queries like get score of player, overall game max score,  
 * min score etc 
 * 
 */

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


public class ShowScores implements ActionListener, ListSelectionListener {

    private final JFrame win;

    private final JButton finished,lastScores,maxPlayerScore,minPlayerScore,minScore,maxScore,TopScorer,LowestScorer;

    private final JList<Vector> outputList;
    private final JList<Vector> allBowlers;

    private String selectedNick;

    private Vector bowlerdb;
    private final Vector party;

    
    public Vector<String> getScores(String type,String nick){

        if (type.equals("minperson"))
        {   
            Vector scores = null;
            try {
                scores = ScoreHistoryFile.getScores(nick);
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }

            assert scores != null;
            // Vector scores = ScoreHistoryFile.getScores(nick);
            Score scoresitem = (Score)scores.get(0);
            int min = Integer.parseInt(scoresitem.getScore());
            int index=0;
            while(index < scores.size())
            {   
                scoresitem = (Score)scores.get(index);
                int s = Integer.parseInt(scoresitem.getScore());
                if (s<min)
                    min = s;
                index++;
            }
            Vector<String> result =  new Vector<>();
            result.add(Integer.toString(min));
            System.out.println(result.get(0));
            return result;
        }
        if (type.equals("maxperson"))
        {
            Vector scores = null;
            try {
                scores = ScoreHistoryFile.getScores(nick);
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }

            assert scores != null;
            // Vector scores = ScoreHistoryFile.getScores(nick);
            int max = 0;
            Score scoresitem;
            int index=0;
            while(index < scores.size())
            {    
                scoresitem = (Score)scores.get(index);
                //newly added
                System.out.println(scoresitem.getNickName());
                int s = Integer.parseInt(scoresitem.getScore());
                if (s>max)
                    max = s;
                index++;
            }
            Vector<String> result =  new Vector<>();
            result.add(Integer.toString(max));
            System.out.println(result.get(0));
            return result;
        }
        
        Vector scores = null;
            try {
                scores = ScoreHistoryFile.getScores(nick);
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }
            assert scores != null;

            Score scoresitem;
            Vector<String> result =  new Vector<>();

            int numPastScores = 6;
            int i = 0;
            for (int index = (scores.size()-1); (index >= 0) && (i < numPastScores); index--,i++)
            {   
                System.out.println("printing index");
                System.out.println(index);
                scoresitem = (Score)scores.get(index);
                result.add(scoresitem.getScore());
            }
            return result;
    }
    
    
    public Vector<String> getScorer(String nick){
    	Vector scores = null;
        try {
            scores = ScoreHistoryFile.getScores(nick);
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }

        assert scores != null;
        // Vector scores = ScoreHistoryFile.getScores(nick);
        int max = 0;
        String tempname,topScorername = null;
        Score scoresitem;
        int index=0;
        while(index < scores.size())
        {   
            scoresitem = (Score)scores.get(index);
            tempname = scoresitem.getNickName();
            int s = Integer.parseInt(scoresitem.getScore());
            if (s>max)	{
            	max = s;
            	topScorername = tempname;
            }
            index++;
        }
        Vector<String> result =  new Vector<>();
        result.add(topScorername);
        System.out.println(result.get(0));
        return result;
    }

    public ShowScores() {

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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(minPlayerScore)) {
            System.out.println("in minPlayerScore");
            if (selectedNick != null) {
                party.clear();
                party.add(getScores("minperson",selectedNick).get(0));
                outputList.setListData(party);
            }
        }

        if (e.getSource().equals(maxPlayerScore)) {
            System.out.println("in maxPlayerScore");
            if (selectedNick != null) {
                party.clear();
                party.add(getScores("maxperson",selectedNick).get(0));
                outputList.setListData(party);
            }
        }

        if (e.getSource().equals(minScore)) {
            System.out.println("in minScore");
            party.clear();
            int pm,min = Integer.parseInt(getScores("minperson",(bowlerdb.get(0)).toString()).get(0));
            int index=1;
            while (index < bowlerdb.size())
            {
                pm = Integer.parseInt(getScores("minperson",(bowlerdb.get(index)).toString()).get(0));
                if (pm < min)
                    min = pm;
                index++;
            }
            party.add(Integer.toString(min));
            outputList.setListData(party);
        }
        if (e.getSource().equals(maxScore)) {
            System.out.println("in maxScore");
            party.clear();
            int pm,max = Integer.parseInt(getScores("maxperson",(bowlerdb.get(0)).toString()).get(0));
            int index=1;
            while (index < bowlerdb.size())
            {
                pm = Integer.parseInt(getScores("maxperson",(bowlerdb.get(index)).toString()).get(0));
                if (pm > max)
                    max = pm;
                index++;
            }
            party.add(Integer.toString(max));
            outputList.setListData(party);
        }
        if (e.getSource().equals(TopScorer)) {
            System.out.println("in TopScorer");
            party.clear();
            int pm,max = Integer.parseInt(getScores("maxperson",(bowlerdb.get(0)).toString()).get(0));
            String tname,TPName = getScorer((bowlerdb.get(0)).toString()).get(0);
            int index=1;
            while (index < bowlerdb.size())
            {
                pm = Integer.parseInt(getScores("maxperson",(bowlerdb.get(index)).toString()).get(0));
                tname = getScorer((bowlerdb.get(index)).toString()).get(0);
                if (pm > max) {
                	max = pm;
                	TPName = tname;
                }
                index++;
            }
            //party.add(Integer.toString(max));
            party.add(TPName);
            outputList.setListData(party);
        }
        
        if (e.getSource().equals(LowestScorer)) {
            System.out.println("in LowestScorer");
            party.clear();
            int pm,min = Integer.parseInt(getScores("minperson",(bowlerdb.get(0)).toString()).get(0));
            String tname,LPName = getScorer((bowlerdb.get(0)).toString()).get(0);
            int index=1;
            while (index < bowlerdb.size())
            {
                pm = Integer.parseInt(getScores("minperson",(bowlerdb.get(index)).toString()).get(0));
                tname = getScorer((bowlerdb.get(index)).toString()).get(0);
                if (pm < min) {
                	min = pm;
                	LPName = tname;
                }
                index++;
            }
            party.add(LPName);
            outputList.setListData(party);
        }
        

        if (e.getSource().equals(lastScores)) {
            System.out.println("in lastScores");
            if (selectedNick != null) {
                party.clear();
                Vector<String> ls = getScores("lastscores",selectedNick);
                int index=0;
                while (index < ls.size()) {
                    System.out.println(ls.get(index));
                    party.add(ls.get(index));
                    index++;
                }
                outputList.setListData(party);
            }
           
        }
        if (e.getSource().equals(finished)) {
            win.hide();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(allBowlers)) {
            selectedNick =
                    ((String) ((JList) e.getSource()).getSelectedValue());
        }
        
    }
    

}

