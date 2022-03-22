/*
Lane
 */

import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Date;

public class Lane extends Thread implements PinsetterObserver {
	private static final int MAX_GAMES = 128;
	private Party party;
	private Pinsetter setter;
	private HashMap scores;
	private Vector subscribers;
	private boolean gameIsHalted;
	private boolean partyAssigned;
	private boolean gameFinished;
	private Iterator bowlerIterator;
	private int ball;
	private int bowlIndex;
	private int frameNumber;
	private boolean isReady;
	private boolean tenthFrameStrike;
	private int[] curScores;
	private ScoreManager currentCumulScores;
	private boolean canThrowAgain;
	private String pauseTime;
	private int[][] finalScores;
	private int gameNumber;
	private boolean initial_halt;
	private boolean game_completed;
	private Bowler currentThrower;
	public Lane() {
		setter = new Pinsetter();
		scores = new HashMap<Bowler, int[]>();
		subscribers = new Vector();
		gameIsHalted = false;
		partyAssigned = false;
		initial_halt=false;
		game_completed=false;
		//canThrowAgain=false;
		isReady = true;
		gameNumber = 0;
		//frameNumber=0;
		setter.subscribe( this );
		currentCumulScores = new ScoreManager(bowlIndex);
		this.start();
	}
	public void run() {
		while (true) {
			if (partyAssigned && !gameFinished) {	// we have a party on this lane,
				// so next bower can take a throw

				while (gameIsHalted||initial_halt) {
					try {
						sleep(500);
					} catch (Exception e) {}
				}

				if (bowlerIterator.hasNext()) {
					currentThrower = (Bowler)bowlerIterator.next();
                    tenthFrameStrike = false;
					ball = 0;
					canThrowAgain=true;
					 while(canThrowAgain) {
						setter.ballThrown();// simulate the thrower's ball hiting
						ball++;
					 }

					if (frameNumber == 9){
						finalScores[bowlIndex][gameNumber] = currentCumulScores.getFinalScore();
						try{
							Date date = new Date();
							String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
							int finalscore=currentCumulScores.getFinalScore();
							String final_score=Integer.toString(finalscore);
							ScoreHistoryFile.addScore(currentThrower.getNickName(), dateString,final_score);
						} catch (Exception e) {System.err.println("Exception in addScore. "+ e );}
					}


					setter.reset();
					bowlIndex++;
					currentCumulScores.setBowlIndex(bowlIndex);
					initial_halt=true;
					publish();

				} else {
					initial_halt=false;
					frameNumber++;
					resetBowlerIterator();
					bowlIndex = 0;
					currentCumulScores.setBowlIndex(bowlIndex);
					if (frameNumber > 9) {
						gameFinished = true;
						gameNumber++;
					}
				}
				
			} else if (partyAssigned && gameFinished) {
				EndGamePrompt egp = new EndGamePrompt( ((Bowler) party.getMembers().get(0)).getNickName() + "'s Party" );
				int result = egp.getResult();
				egp.distroy();
				egp = null;


				System.out.println("result was: " + result);

				// TODO: send record of scores to control desk
				if (result == 1) {	// yes, want to play again
					initial_halt=false;
					game_completed=true;
					resetScores();
					currentCumulScores.reset(party.getMembers().size());
					publish();
					resetBowlerIterator();
					
					game_completed=false;

				} else if (result == 2) {// no, dont want to play another game
					Vector printVector;
					EndGameReport egr = new EndGameReport( ((Bowler)party.getMembers().get(0)).getNickName() + "'s Party", party);
					printVector = egr.getResult();
					partyAssigned = false;
					Iterator scoreIt = party.getMembers().iterator();
					party = null;
					partyAssigned = false;
					publish();
					int myIndex = 0;
					while (scoreIt.hasNext()){
						Bowler thisBowler = (Bowler)scoreIt.next();
						ScoreReport sr = new ScoreReport( thisBowler, finalScores[myIndex++], gameNumber );
						sr.sendEmail(thisBowler.getEmail());
						Iterator printIt = printVector.iterator();
						while (printIt.hasNext()){
							if (thisBowler.getNickName() == (String)printIt.next()){
								System.out.println("Printing " + thisBowler.getNickName());
								sr.sendPrintout();
							}
						}

					}
				}
			}
			


			try {
				sleep(500);
			} catch (Exception e) {}
		}
	}
	public void receivePinsetterEvent(PinsetterEvent pe) {
		
		if (pe.pinsDownOnThisThrow() >=  0) {	
			// this is a real throw
			if(pe.isFoulCommited())
			{
			   System.out.println("yes foul commited");
			   markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), -2);
			}
			else
			{
				markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());
			}

			// next logic handles the ?: what conditions dont allow them another throw?
			// handle the case of 10th frame first
			if (frameNumber == 9) {
				if (pe.totalPinsDown() == 10) {
					setter.resetPins();
					if(pe.getThrowNumber() == 1) {
						tenthFrameStrike = true;
					}
				}

				if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && tenthFrameStrike == false)) {
					canThrowAgain = false;
					//publish( lanePublish() );
				}

				if (pe.getThrowNumber() == 3) {
					canThrowAgain = false;
					//publish( lanePublish() );
				}
			} else { // its not the 10th frame

				if (pe.pinsDownOnThisThrow() == 10) {		// threw a strike
					canThrowAgain = false;
					//publish( lanePublish() );
				} else if (pe.getThrowNumber() == 2) {
					canThrowAgain = false;
					//publish( lanePublish() );
				} else if (pe.getThrowNumber() == 3)
					System.out.println("I'm here...");
			}
		}
        
	}

	/** resetBowlerIterator()
	
	 *
	 * sets the current bower iterator back to the first bowler
	 *
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	private void resetBowlerIterator() {
		initial_halt=false;
		bowlerIterator = (party.getMembers()).iterator();
	}

	/** resetScores()
	 *
	 * resets the scoring mechanism, must be called before scoring starts
	 *
	 * @pre the party has been assigned
	 * @post scoring system is initialized
	 */
	private void resetScores() {
		Iterator bowlIt = (party.getMembers()).iterator();

		while ( bowlIt.hasNext() ) {
			int[] toPut = new int[25];
			for ( int i = 0; i != 25; i++){
				toPut[i] = -1;
			}
			scores.put( bowlIt.next(), toPut );
		}
		gameFinished = false;
		frameNumber = 0;
	}

	/** assignParty()
	 *
	 * assigns a party to this lane
	 * @pre none
	 * @post the party has been assigned to the lane
	 *
	 * @param theParty		Party to be assigned
	 */
	public void assignParty( Party theParty ) {
		party = theParty;
		resetBowlerIterator();
		partyAssigned = true;
		curScores = new int[party.getMembers().size()];
		currentCumulScores.reset(party.getMembers().size());
		finalScores = new int[party.getMembers().size()][MAX_GAMES]; //Hardcoding a max of 128 games, bite me.
		gameNumber = 0;
		resetScores();
	}
	private void markScore( Bowler Cur, int frame, int ball, int score ){
		int[] curScore;
		int index =  ( (frame - 1) * 2 + ball);

		curScore = (int[]) scores.get(Cur);


		curScore[index-1] = score;
		scores.put(Cur, curScore);
		currentCumulScores.getScore(Cur, frame, ball, (int[]) scores.get(Cur));
		publish();
	}
	public boolean isPartyAssigned() {
		return partyAssigned;
	}

	/** isGameFinished
	 *
	 * @return true if the game is done, false otherwise
	 */
	public boolean isGameFinished() {
		return gameFinished;
	}

	/** subscribe
	 *
	 * Method that will add a subscriber
	 */

	public void subscribe( LaneObserver adding ) {
		subscribers.add( adding );
	}

	/** unsubscribe
	 *
	 * Method that unsubscribes an observer from this object
	 *
	 * @param removing	The observer to be removed
	 */

	public void unsubscribe( LaneObserver removing ) {
		subscribers.remove( removing );
	}

	/** publish
	 *
	 * Method that publishes an event to subscribers
	 *
	 */

	public void publish() {
		if( subscribers.size() > 0 ) {
			Iterator eventIterator = subscribers.iterator();

			while ( eventIterator.hasNext() ) {
				( (LaneObserver) eventIterator.next()).receiveLaneEvent( party, bowlIndex, currentThrower, currentCumulScores.getCumulScores(), scores, frameNumber+1, curScores, ball, gameIsHalted);
			}
		}
	}

	/**
	 * Accessor to get this Lane's pinsetter
	 *
	 * @return		A reference to this lane's pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		publish();
	}

	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame(){
		gameIsHalted = false;
		publish();
	}
	
	public void throwBall()
	{
		initial_halt=false;
		//publish();
	}
	

}
