import java.util.Iterator;
import java.util.Vector;

public class ControlDeskSubscriber {
	
	private Vector subscribers;
	
	public ControlDeskSubscriber() {
		subscribers = new Vector();
	}

    /**
     * Allows objects to subscribe as observers
     * 
     * @param adding	the ControlDeskObserver that will be subscribed
     *
     */

	public void subscribe(ControlDeskObserver adding) {
		subscribers.add(adding);
	}

    /**
     * Broadcast an event to subscribing objects.
     * 
     * @param event	the ControlDeskEvent to broadcast
     *
     */

	public void publish(ControlDeskEvent event) {
		for (Object subscriber : subscribers) {
			((ControlDeskObserver) subscriber).receiveControlDeskEvent(event);
		}
	}
	
}
