package backend;

public class SwitchIntersection implements Runnable{
	Intersection I;
	SwitchIntersection (Intersection I) {
		this.I = I;
		run();
	}
	@Override
	public void run() {
		long time = System.currentTimeMillis();
		I.toggle1();
		do {
			if (System.currentTimeMillis()-time>999) {
				time = System.currentTimeMillis();
				I.toggle2();
				break;
			}
		} while (true);
		do {
			if (System.currentTimeMillis()-time>999) {
				I.toggle3();
				break;
			}
		} while (true);
	}
}
