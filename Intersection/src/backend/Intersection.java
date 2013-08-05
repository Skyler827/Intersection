package backend;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

public class Intersection extends Canvas implements Runnable{
	private static final long serialVersionUID = 116103708734406002L;
	double realRoadWidth = 20;
	double YPerMeter;
	double XPerMeter;
	int pixelWidth;
	int pixelHeight;
	double realWidth() {return pixelWidth/XPerMeter;}
	double realHeight() {return pixelHeight/YPerMeter;}
	int roadWidthX() {return (int)(realRoadWidth*XPerMeter);}
	int roadWidthY() {return (int)(realRoadWidth*YPerMeter);}
	/*
	 * Explanation of numbering scheme:
	 * CarQueues are numbered 0-7, counted like radians on a unit circle.
	 * Roads from away heading into the intersection are 0-3
	 * Roads from the intersection going away are 4-7.
	 * For example, the road coming from east would be 0, FromNorth := 1, FromW := 2, FS := 3
	 * Roads going east would be 4, GoingN :=5, GoingW :=6, and GoingS := 7
	 */
	CarQueue[] Road;
	TrafficLight[] Light;
	private double dt = 0.1;
	private double rate = 0.4;
	int numOfCars;
	int size;
	long time;
	int[][] coord;
	public Intersection(double NorthSouthLength, double EastWestLength, Dimension D) {
		
		Road = new CarQueue[8];
		Road[0] = new CarQueue(0, (EastWestLength-realRoadWidth)/2); //From East
		Road[1] = new CarQueue(0, (NorthSouthLength-realRoadWidth)/2); //From North
		Road[2] = new CarQueue(0, (EastWestLength-realRoadWidth)/2); //From West
		Road[3] = new CarQueue(0, (NorthSouthLength-realRoadWidth)/2); //From South
		Road[4] = new CarQueue((EastWestLength-realRoadWidth)/2, EastWestLength); //To East
		Road[5] = new CarQueue((NorthSouthLength-realRoadWidth)/2, NorthSouthLength); //To North
		Road[6] = new CarQueue((EastWestLength-realRoadWidth)/2, EastWestLength); //To West
		Road[7] = new CarQueue((NorthSouthLength-realRoadWidth)/2, NorthSouthLength); //To South
		Road[0].next = Road[6];
		Road[1].next = Road[7];
		Road[2].next = Road[4];
		Road[3].next = Road[5];
		Light = new TrafficLight[8];
		for (int i=0; i<8; i++) {
			Light[i] = new TrafficLight((i%2==0)||(i>3));
		}
		YPerMeter = D.height/NorthSouthLength;
		XPerMeter = D.width/EastWestLength;
		pixelWidth = D.width;
		pixelHeight = D.height;
		System.out.println("Width: " + D.width + "   Height: " + D.height);
	}
	public void run() {
		time = System.currentTimeMillis();
		step(dt);
		/*
		if (timeToSwitch()) {
			new SwitchIntersection(this);
		}*/
		paint(getGraphics());
		while (999*dt+time > System.currentTimeMillis()) {}
	}
	public static void main(String[] args) {
		JFrame F = new JFrame("Intersection Simulation");
		F.setSize(700, 700);
		Intersection I = new Intersection(400,400, F.getSize());
		F.add(I);
		I.roadCoordinates();
		F.setVisible(true);
		while(true) I.run();	
	}
	public void paint (Graphics g) {
		g.setColor(new Color(10,190,20));
		g.fillRect(0,0, pixelWidth, pixelHeight);
		g.setColor(Color.BLACK);
		g.fillRect(0, ((pixelHeight-2*roadWidthY())/2), pixelWidth, 2*roadWidthY());
		g.fillRect((pixelWidth-2*roadWidthX())/2, 0, 2*roadWidthX(), pixelHeight);
		
		for (int i=0; i<8; i++) {
			//Draw Lines along path of motion:
			//g.drawLine(coords[i][0], coords[i][1], coords[i][2], coords[i][3]);
			double[] carLocations = Road[i].positions();
			Color[] colors = Road[i].paint();
			for (int j=0; j<carLocations.length; j++) {
				g.setColor(colors[j]);
				g.fillRect(
					(int)(coord[i][0]+carLocations[j]/Road[i].length()
					*(coord[i][2]-coord[i][0]))-size/2, 
					(int)(coord[i][1]+ carLocations[j]/Road[i].length()
					*(coord[i][3]-coord[i][1]))-size/2, size, size);
			}
		}
	}
	void moveCars(Graphics g) {
		
	}
	void step (double dt) {
		//Move cars along:
		for (int i=0; i<8; i++) {
			Road[i].step(dt);
		}
		//move cars into next Queue: (incorporated into Road.step)
		//Create new cars:
		for (int i=0; i<4; i++) {
			if (Math.random()>Math.pow(1-rate, dt)) {
				Road[i].enQueue(new Car(Road[i].startPosition, Road[i].endPosition, Light[i]));
				System.out.print("Car coming from the ");
				switch (i) {
				case 0: System.out.print("East"); break;
				case 1: System.out.print("North");break;
				case 2: System.out.print("West "); break;
				case 3: System.out.print("South"); break;
				}
				numOfCars++;
				System.out.println("   Total Cars: " + numOfCars);
			}
				
		}
	}
	void toggle1() {
		for (int i=0; i<4; i++) {
			Light[i].toggle1();
		}
	}
	void toggle2() {
		for (int i=0; i<4; i++) {
			Light[i].toggle2();
		}
	}
	void toggle3() {
		for (int i=0; i<4; i++) {
			Light[i].toggle3();
		}
	}
	private boolean timeToSwitch() {
		if (Light[0].isYellow()||Light[1].isYellow()) return false;
		double GreenWaiting;
		double RedWaiting;
		if (Light[0].isGreen()) {
			GreenWaiting = Road[0].carWaitTime + Road[2].carWaitTime;
			RedWaiting = Road[1].carWaitTime + Road[3].carWaitTime;
		} else {
			GreenWaiting = Road[1].carWaitTime + Road[3].carWaitTime;
			RedWaiting = Road[0].carWaitTime + Road[2].carWaitTime;
		}
		if (RedWaiting>2*GreenWaiting) return true;
		else return false;
	}
	/**
	 * This function returns the coordinates of each road's starting point (x0, y0) and 
	 * ending point (x1, y1) in pixels.  Used in the paint function above.
	 * ans[i][j] = Coordinates for Road[i]
	 * ans[i][0] = x0[i], ans[1] = y0[i], ans[2] = x1[i], ans[3] = y1[i]
	 * @return the coordinates of the Road's endpoints in pixels
	 */
	private void roadCoordinates () {
		coord = new int[8][4];
		//East->Center
		coord[0][0] = pixelWidth;
		coord[0][1] = (pixelHeight-roadWidthY())/2;
		coord[0][2] = (int)((pixelWidth+2.3*roadWidthX())/2);
		coord[0][3] = (pixelHeight-roadWidthY())/2;
		//North->Center
		coord[1][0] = (pixelWidth-roadWidthX())/2;
		coord[1][1] = 0;
		coord[1][2] = (pixelWidth-roadWidthX())/2;
		coord[1][3] = (int)(pixelHeight-2.3*roadWidthY())/2;
		//W->Center
		coord[2][0] = 0;
		coord[2][1] = (pixelHeight+roadWidthY())/2;
		coord[2][2] = (int)(pixelWidth-2.3*roadWidthX())/2;
		coord[2][3] = (pixelHeight+roadWidthY())/2;
		//South->Center
		coord[3][0] = (pixelWidth+roadWidthX())/2;
		coord[3][1] = pixelHeight;
		coord[3][2] = (pixelWidth+roadWidthX())/2;
		coord[3][3] = (int)(pixelHeight+2.3*roadWidthY())/2;
		//Center->East
		coord[4][0] = (int)(pixelWidth-2.3*roadWidthX())/2;
		coord[4][1] = (pixelHeight+roadWidthY())/2;
		coord[4][2] = pixelWidth;
		coord[4][3] = (pixelHeight+roadWidthY())/2;
		//Center->North
		coord[5][0] = (pixelWidth+roadWidthX())/2;
		coord[5][1] = (int)(pixelHeight+2.3*roadWidthY())/2;
		coord[5][2] = (pixelWidth+roadWidthX())/2;
		coord[5][3] = 0;
		//Center->West
		coord[6][0] = (int)(pixelWidth+2.3*roadWidthX())/2;
		coord[6][1] = (pixelHeight-roadWidthY())/2;
		coord[6][2] = 0;
		coord[6][3] = (pixelHeight-roadWidthY())/2;
		//Center->South
		coord[7][0] = (pixelWidth-roadWidthX())/2;
		coord[7][1] = (int)(pixelHeight-2.3*roadWidthY())/2;
		coord[7][2] = (pixelWidth-roadWidthX())/2;
		coord[7][3] = pixelHeight;
		size = (int) (4*XPerMeter);
	}
}