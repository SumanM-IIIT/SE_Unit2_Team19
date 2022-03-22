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
            for(int index = 0; index < scores.size(); index++)
            {   
                scoresitem = (Score)scores.get(index);
                int s = Integer.parseInt(scoresitem.getScore());
                if (s<min)
                    min = s;
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
            for(int index = 0; index < scores.size(); index++)
            {   
                scoresitem = (Score)scores.get(index);
                //newly added
                System.out.println(scoresitem.getNickName());
                int s = Integer.parseInt(scoresitem.getScore());
                if (s>max)
                    max = s;
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
        for(int index = 0; index < scores.size(); index++)
        {
            scoresitem = (Score)scores.get(index);
            tempname = scoresitem.getNickName();
            int s = Integer.parseInt(scoresitem.getScore());
            if (s>max)	{
            	max = s;
            	topScorername = tempname;
            }
        }
        Vector<String> result =  new Vector<>();
        result.add(topScorername);
        System.out.println(result.get(0));
        return result;
    }

    public ShowScores() {

     show s1=new show();

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
            for (int index=1;index < bowlerdb.size();index++)
            {
                pm = Integer.parseInt(getScores("minperson",(bowlerdb.get(index)).toString()).get(0));
                if (pm < min)
                    min = pm;
            }
            party.add(Integer.toString(min));
            outputList.setListData(party);
        }
        if (e.getSource().equals(maxScore)) {
            System.out.println("in maxScore");
            party.clear();
            int pm,max = Integer.parseInt(getScores("maxperson",(bowlerdb.get(0)).toString()).get(0));
            for (int index=1;index < bowlerdb.size();index++)
            {
                pm = Integer.parseInt(getScores("maxperson",(bowlerdb.get(index)).toString()).get(0));
                if (pm > max)
                    max = pm;
            }
            party.add(Integer.toString(max));
            outputList.setListData(party);
        }
        if (e.getSource().equals(TopScorer)) {
            System.out.println("in TopScorer");
            party.clear();
            int pm,max = Integer.parseInt(getScores("maxperson",(bowlerdb.get(0)).toString()).get(0));
            String tname,TPName = getScorer((bowlerdb.get(0)).toString()).get(0);
            for (int index=1;index < bowlerdb.size();index++)
            {
                pm = Integer.parseInt(getScores("maxperson",(bowlerdb.get(index)).toString()).get(0));
                tname = getScorer((bowlerdb.get(index)).toString()).get(0);
                if (pm > max) {
                	max = pm;
                	TPName = tname;
                }
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
            for (int index=1;index < bowlerdb.size();index++)
            {
                pm = Integer.parseInt(getScores("minperson",(bowlerdb.get(index)).toString()).get(0));
                tname = getScorer((bowlerdb.get(index)).toString()).get(0);
                if (pm < min) {
                	min = pm;
                	LPName = tname;
                }
            }
            party.add(LPName);
            outputList.setListData(party);
        }
        

        if (e.getSource().equals(lastScores)) {
            System.out.println("in lastScores");
            if (selectedNick != null) {
                party.clear();
                Vector<String> ls = getScores("lastscores",selectedNick);
                for (int index=0;index < ls.size();index++) {
                    System.out.println(ls.get(index));
                    party.add(ls.get(index));
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
