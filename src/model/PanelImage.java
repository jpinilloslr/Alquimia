package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelImage extends JPanel{

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private String text;
	
	public PanelImage(InputStream is, String text) {
		try {
			image = ImageIO.read(is);
			this.text = text;
		} catch (IOException ie) {
			System.out.println("Error:"+ie.getMessage());
		}
		
	}

	public void paint(Graphics g) {
		g.drawImage( image, 0, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Tahoma", Font.BOLD, 11));
		g.drawString(text, 1, image.getHeight() - 1);
		super.paint(g);
	}

	/*public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}*/
}
