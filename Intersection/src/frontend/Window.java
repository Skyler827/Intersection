package frontend;

import java.awt.Canvas;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import backend.*;

public class Window implements ActionListener {
	private Intersection I;
	private Container contentPane;
	private Canvas canvas;
	JFrame frame;
	Window() {
		frame = new JFrame("Intersection Simulator");
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		contentPane = frame.getContentPane();
		canvas = new Canvas();
		canvas.paint(canvas.getGraphics());
		contentPane.add(canvas);
		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new Window();
	}
}
