package backend;

import java.awt.Color;

public class Car {
	/* All parameters are in SI units: meters, seconds, m/s, etc. */
	static final double topSpeed = 75;
	static final double accelRate = 3; 
	static final double carLength = 10;
	boolean moving;
	TrafficLight Light;
	double accel = 0;
	double speed = topSpeed/2;
	double position = 0;
	double maxPosition = 0;
	double waitTime = 0;
	Color c;
	Car next;
	Car previous;
	public Car(double pos, double max, TrafficLight L) {
		c = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
		position = pos;
		maxPosition = max;
		Light = L;
	}
	void step(double dt) {
		/*
		if (next==null) {//No car ahead:
			if (Light.isRed()) 
				accel = -speed*speed/(2*Light.position-position);
			if (Light.isGreen()) //green||yellow: full speed ahead
				accel = accelRate*(1-speed/topSpeed);
			else //Light is Yellow:
				if (-speed*speed/(2*Light.position-position)>accelRate)
					//^IF car is going too fast:
					accel = accelRate*(1-speed/topSpeed);
				else //If car is slow enough:
					accel = -speed*speed/(2*Light.position-position);
		}
		else { //There's a car ahead:
			if (next.speed>1.1*speed) //If next car is too fast, ignore it:
				accel = accelRate*(1-speed/topSpeed);
			else { //If next car isn't too fast, slow down accordingly:
				if (Light.isRed()) //Accelerate as if top speed were 0:
					accel = -speed*speed/(2*(next.position-position-carLength));
				else //balance top speed with not crashing into next car:
					accel = -Math.pow(speed-1.1*next.speed,2)/(2*(next.position-position))
					+ accelRate*(1-speed/topSpeed);
				}
		}
		*/
		int carsAhead = 0; Car I = this;
		while (I.next!=null) {I = I.next; carsAhead++;}
		if (!moving&&carsAhead>0) if (!next.moving) return;
		/*
		 * Temporarily switching from Light.getLightCode() to 0:
		 */
		switch (0) {
		case 0: { //Green Light:
			if (next==null) {//No car ahead:
				accel = accelRate*(1-speed/topSpeed);
			} else { //Car Ahead:
				if (next.position==position) {accel=0; break;}
				if (next.speed>speed) //If next car is faster, ignore it:
					accel = accelRate*(1-speed/topSpeed);
				else { //If next car is moving slower, slow down:
					accel = -Math.pow(speed-next.speed,2)/(2*(next.position-position))
						+ accelRate*(1-speed/topSpeed);
				}
			}
			break;
		}
		case 1: { //Yellow Light:
			if (speed*speed/(2*(Light.position-position)+carsAhead*carLength)>accelRate)
				//^IF car is going too fast:
				accel = accelRate*(1-speed/topSpeed);
			else //If car is slow enough:
				accel = -speed*speed/(2*(Light.position-position)-carsAhead*carLength);
			break;
		}
		case 2: { //Red Light:
			accel = -speed*speed/(2*(Light.position-position)-carsAhead*carLength);
			break;
		}
		default: accel = 0; speed = 0; 
		System.out.println("Error: TrafficLight code out of bounds!");
		}
		speed += accel*dt;
		position += speed*dt;
		if (speed==0) moving = false;
	}
}