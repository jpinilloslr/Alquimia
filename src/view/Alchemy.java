package view;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class Alchemy {
	public static void main(String[] args) {
		try {
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		  System.out.println("Unable to load native look and feel");
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
