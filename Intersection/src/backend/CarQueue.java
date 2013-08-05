package backend;

import java.awt.Color;

public class CarQueue {
	private Car front;
	private Car rear;
	double startPosition;
	double endPosition;
	double oddsPerSecond = 0.3;
	double carWaitTime;
	CarQueue next;
	public CarQueue(double start, double end) {
		startPosition = start;
		endPosition = end;
	}
	public void enQueue (Car C) {
		C.next = rear;
		try {
			rear.previous = C;
		} catch (NullPointerException npe) {
			front = C;
		}
		rear = C;
		C.position = 0;
	}
	public void deQueue () {
		try {
			carWaitTime -= front.waitTime;
			front = front.previous;
			front.next.previous = null;
			front.next = null;
		} catch (NullPointerException npe) {
			//System.out.println("Failed attempt to remove from empty Queue");
			//npe.printStackTrace();
		}
	}
	public Car getFront() {
		return front;
	}
	public Car getRear() {
		return rear;
	}
	public int getsize() {
		Car I = rear;
		int counter = 0;
		while (I != null) {
			I = I.next;
			counter++;
		}
		return counter;
	}
	public double[] positions() {
		double [] positions = new double[getsize()];
		int counter = 0;
		Car next = rear;
		while (next != null) {
			positions[counter] = next.position;
			counter++;
			next = next.next;
		}
		return positions;
	}
	public Color[] paint() {
		Color [] colors = new Color[getsize()];
		int counter = 0;
		Car next = rear;
		while (next != null) {
			colors[counter] = next.c;
			counter++;
			next = next.next;
		}
		return colors;
	}
	public double length() {
		return endPosition-startPosition;
	}
	void step(double dt) {
		Car I = rear;
		while (I != null) {
			I.step(dt);
			if (I.position>endPosition){
				if (I==null) System.out.println("we're going to have some problems here.");
				deQueue();
				if (next != null) next.enQueue(I);
				break;
			}
			if (!I.moving) carWaitTime += dt;
			I = I.next;
		}
	}
}