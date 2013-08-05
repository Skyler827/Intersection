package backend;

public class TrafficLight{
	private byte lightCode;
	CarQueue Road;
	public double position;
	public TrafficLight(boolean start) {
		if (start) lightCode = 0;
		else lightCode = 2;
	}
	public byte getLightCode() {
		return lightCode;
	}
	public void setLightCode(byte b) {
		lightCode = b;
	}
	public void toYellow() {
		lightCode = 1;
	}
	public void toRed() {
		lightCode = 2;
	}
	public void toGreen() {
		lightCode = 0;
	}
	public boolean isGreen() {
		if (lightCode==0) return true;
		else /*if yellow or red*/ return false;
	}
	public boolean isRed() {
		if (lightCode==2) return true;
		else return false;
	}
	public boolean isYellow() {
		if (lightCode==1) return true;
		else return false;
	}
	public void toggle1() {
		if (lightCode==0) lightCode = 1;
		//if (lightCode==2) {lightCode = 0; return;}
	}
	public void toggle2() {
		if (lightCode==1) lightCode = 2;
	}
	public void toggle3() {
		if (lightCode==2) lightCode = 0;
	}
}