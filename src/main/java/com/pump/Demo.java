package com.pump;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.pump.plaf.ChasingArrowsThrobberUI;
import com.pump.swing.JThrobber;

public class Demo {

	public static void main(String[] args) {
		
		getWindow().setVisible(true);

	}
	
	public static JFrame getWindow() {
		JFrame mainwin = new JFrame("JThrobber Demo");
		mainwin.setSize(300, 300);
		
		JThrobber throbber = new JThrobber();
		throbber.setUI(new ChasingArrowsThrobberUI());
		throbber.setPreferredSize(new Dimension(60,60));
		throbber.setForeground(Color.cyan);
		
		mainwin.getContentPane().add(throbber, BorderLayout.CENTER);
		
		return mainwin;
	}

}
