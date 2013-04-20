package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.Element;
import model.ElementsManager;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextField;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int ELEMENT_SIZE_X = 63;
	private static final int ELEMENT_SIZE_Y = 83;
	
	private JPanel contentPane;
	private ElementsManager elemMgr;
	private JScrollPane scrollPane;
	private JPanel panelElements;
	private JPanel panelBoard;
	
	public int dragX;
	public int dragY;
	private JMenuItem mntmClose;
	private JMenuItem mntmClearBoard;
	private JMenu mnAyuda;
	private JMenuItem mntmAbout;
	private JLabel lblStatus;
	private JLabel lblLastCombination;
	private JMenuItem mntmReset;
	private JTextField textSearch;
	private JMenuItem mntmClue;
	private JLabel lblClues;
	
	private void refreshCluesCount() {
		lblClues.setText("Pistas: " + elemMgr.getCluesCount());
	}
	
	private void addFoundedElement() {
		int visibleElements = 0;
		JLabel firstElement = null;
		
		for(int i=0; i<panelElements.getComponentCount(); i++) {
			JLabel label = (JLabel)panelElements.getComponent(i);
			if(label.isVisible()) {
				if(firstElement == null)
					firstElement = label;
				visibleElements++;
			}
		}
		
		if(0 < visibleElements) {
			Element element = elemMgr.getElementByName(firstElement.getText());
			Random rand = new Random();
			addElementToBoard(element, rand.nextInt(300), rand.nextInt(300));
		}
	}
		
	private void search(String text) {
		for(int i=0; i<panelElements.getComponentCount(); i++) {
			JLabel label = (JLabel)panelElements.getComponent(i);
			
			Element element = elemMgr.getElementByName(label.getText());			
			
			if(element.isVisible()) {
				String name = element.getName().toLowerCase();
				
				name = name.replace("á", "a");
				name = name.replace("é", "e");
				name = name.replace("í", "i");
				name = name.replace("ó", "o");
				name = name.replace("ú", "u");
				
				if(name.contains(text.toLowerCase())) {
					label.setVisible(true);
				} else {
					label.setVisible(false);
				}
			}
		}
	}
	
	public void playSound(String sound){
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/" + sound + ".wav").getAbsoluteFile());
	        
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex){
	    	ex.printStackTrace();
	    }
	}
	
	private void showNewElement(String elementName) {
		playSound("new_element");
		elemMgr.addPoint();
		refreshCluesCount();
	}
	
	private void combine(JLabel a, JLabel b) {
		Element ea = elemMgr.getElementByName(a.getText());
		Element eb = elemMgr.getElementByName(b.getText());
		
		ArrayList<String> results = ea.combine(eb);
		String combResult = "";
			
		if(null != results) {
			for (String result : results) {				
				Element res = elemMgr.getElementByName(result);
				
				if(!res.isVisible()) {
					showNewElement(res.getName());
				}
				
				res.setVisible(true);				
				showElementInList(res);
				
				int x = 0;
				int y = 0;
				
				if(results.size() > 1) {
					Random rand = new Random();
					x = rand.nextInt(100)-50;
					y = rand.nextInt(100)-50;
				}
				
				addElementToBoard(res, a.getX() + x, a.getY() + y);
				panelBoard.remove(a);
				panelBoard.remove(b);
				panelBoard.repaint();		
				elemMgr.saveState();		
				
				if(combResult.length() > 0)
					combResult += ", " + result;
				else
					combResult = result;
			}
			lblStatus.setText("Elementos: " + elemMgr.getDiscovered() + "/" + elemMgr.getCount());
			lblLastCombination.setText(combResult + " = " +ea.getName() + " + " + eb.getName());	
		}
	}	
	
	private void checkCollision(JLabel c) {
		JLabel b;
		JLabel pair = null;
		
		for(int i=0; i<panelBoard.getComponentCount(); i++) {
			b = (JLabel)panelBoard.getComponent(i);
			
			if(b != c) {

				/*if((c.getX() < b.getX() + b.getWidth()-10) && (c.getX() > b.getX() - b.getWidth()-10)) {
					if((c.getY() < b.getY() + b.getHeight()-10) && (c.getY() > b.getY() - b.getHeight()-10)) {*/
				if(c.getBounds().intersects(b.getBounds())) {
						
						if(null == pair) {
							pair = b;	
						} else
						if((Math.abs(panelBoard.getComponentZOrder(b) - panelBoard.getComponentZOrder(c))) < 
							(Math.abs(panelBoard.getComponentZOrder(pair) - panelBoard.getComponentZOrder(c)))) {
							pair = b;
						}
					//}
				}
			}
		}
		
		if(null != pair)
			combine(c, pair);
	}
	
	private void showElementInList(Element element) {
		for(int i=0; i<panelElements.getComponentCount(); i++) {
			JLabel label = (JLabel)panelElements.getComponent(i);
			
			if(label.getText().equals(element.getName())) {
				label.setVisible(true);
			}
		}
	}
	
	private void updateVisibleElementsInList() {
		for(int i=0; i<panelElements.getComponentCount(); i++) {
			JLabel label = (JLabel)panelElements.getComponent(i);			
			label.setVisible(elemMgr.getElementByName(label.getText()).isVisible());
		}
	}
	
	private void addElementToList(Element element) {

		JLabel label = new JLabel(); 
		Icon image = new ImageIcon (Alchemy.class.getResource("data/" + element.getFilename())); 
		label.setIcon (image); 
		label.setText(element.getName());
		
		panelElements.add(label);
		label.setVisible(element.isVisible());

		label.addMouseListener(new MouseAdapter() {				
			@Override
			public void mouseReleased(MouseEvent arg0) {
				JLabel label = (JLabel)arg0.getSource();
				label.setEnabled(true);

				Random rand = new Random();
				Element element = elemMgr.getElementByName(label.getText());
				addElementToBoard(element, rand.nextInt(300), rand.nextInt(300));
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				JLabel label = (JLabel)arg0.getSource();					
				label.setEnabled(false);
			}							
		});

	}
	
	private void addElementToBoard(Element element, int posx, int posy) {
		JLabel elementLabel = new JLabel();
		Icon imageIcon = new ImageIcon (Alchemy.class.getResource("data/" + element.getFilename())); 
		elementLabel.setIcon(imageIcon);
		elementLabel.setBounds(posx, posy, ELEMENT_SIZE_X, ELEMENT_SIZE_Y);	
		//elementLabel.setToolTipText(element.getName());
		elementLabel.setText(element.getName());		
		elementLabel.setForeground(Color.WHITE);
		elementLabel.setHorizontalTextPosition(JLabel.CENTER);
		elementLabel.setVerticalTextPosition(JLabel.BOTTOM);
		panelBoard.add(elementLabel);	
		panelBoard.setComponentZOrder(elementLabel, 0);

		elementLabel.repaint();
		panelBoard.repaint();

		elementLabel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				JLabel image = (JLabel)e.getSource();

				int x = e.getX();
				int y = e.getY();
				image.setLocation(image.getX() + (x - dragX), image.getY() + (y - dragY));
				panelBoard.setComponentZOrder(image, 0);
				
			}
		});

		elementLabel.addMouseListener(new MouseAdapter() {						
			@Override
			public void mousePressed(MouseEvent e) {
				JLabel image = (JLabel)e.getSource();
				dragX = e.getX();
				dragY = e.getY();
				
				if(e.getButton() == MouseEvent.BUTTON3) {
					panelBoard.remove(image);
					panelBoard.repaint();
				} else
				if(e.getButton() == MouseEvent.BUTTON2) {
					addElementToBoard(elemMgr.getElementByName(image.getText()), image.getX()+ELEMENT_SIZE_X/2, image.getY()+ELEMENT_SIZE_Y/2);
					panelBoard.repaint();
				}					
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					JLabel image = (JLabel)e.getSource();
					checkCollision(image);				
				}
			}
		});
	}
	
	private void loadElements() {
		ArrayList<Element> elements = elemMgr.getElements();
		
		for (Element element : elements) {
			System.out.println("data/" + element.getFilename());
			addElementToList(element);
		}
	}

	private void resetGame() {
		elemMgr.reset();
		updateVisibleElementsInList();
		panelElements.repaint();
		panelBoard.removeAll();
		panelBoard.repaint();
		lblStatus.setText("Elementos: " + elemMgr.getDiscovered() + "/" + elemMgr.getCount());
		lblLastCombination.setText("");
		insertBasicElements();
		refreshCluesCount();
	}
	
	private void insertBasicElements() {
		Element fire = elemMgr.getElementByName("Fuego");
		Element water = elemMgr.getElementByName("Agua");
		Element air = elemMgr.getElementByName("Aire");
		Element earth = elemMgr.getElementByName("Tierra");
		addElementToBoard(fire, 305, 152);
		addElementToBoard(water, 211, 76);
		addElementToBoard(air, 201, 244);
		addElementToBoard(earth, 108, 152);
	}
	
	private void requestClue() {
		String message = "";
		String clue[] = elemMgr.getClue();
		elemMgr.saveState();
		
		if((clue[0].length() > 0) && (clue[1].length() > 0)) {
			message = clue[0] + " + " + clue[1];			
			JOptionPane.showMessageDialog(null, message);					
		} else
		if((clue[0].equals("NONE") )) {
			message = "No hay más pistas disponible.";
			JOptionPane.showMessageDialog(null, message);	
		} else {
			message = "No quedan elementos por descubrir.";
			JOptionPane.showMessageDialog(null, message);					
		}
		refreshCluesCount();
	}
	
	public MainFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/view/data/icon.png")));
		setForeground(Color.WHITE);
		setType(Type.POPUP);
		setResizable(false);
		elemMgr = ElementsManager.getSingleton();
		elemMgr.loadElements();		
		
		setTitle("Alquimia v1.0 Beta");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 721, 509);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnAlquimia = new JMenu("Alquimia");
		menuBar.add(mnAlquimia);
		
		mntmClose = new JMenuItem("Salir");
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.this.dispose();
			}
		});
		
		mntmClearBoard = new JMenuItem("Limpiar tablero");
		mntmClearBoard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mntmClearBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelBoard.removeAll();
				panelBoard.repaint();
			}
		});
		
		mntmClue = new JMenuItem("Pista");
		mntmClue.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mntmClue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				requestClue();
			}
		});
		mnAlquimia.add(mntmClue);
		mnAlquimia.add(mntmClearBoard);
		
		mntmReset = new JMenuItem("Reiniciar");
		mntmReset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] botones = {"Aceptar", "Cancelar" };
				int i = JOptionPane.showOptionDialog(null, "Todos los elementos descubiertos serán eliminados. ¿Está seguro que desea reiniciar su progreso en el juego? ", "Reiniciar",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, botones, botones[1]);
				
				if(i == 0) {
					resetGame();
				}
			}
		});
		mnAlquimia.add(mntmReset);
		mnAlquimia.add(mntmClose);
		
		mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);
		
		mntmAbout = new JMenuItem("Acerca de");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutFrame af = new AboutFrame();
				af.setVisible(true);
			}
		});
		
		JMenuItem mntmHelp = new JMenuItem("Ayuda");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] botones = {"Aceptar" };
				String message = "Agregue los elementos disponibles a su izquierda presionando sobre ellos, \nluego arrastre un elemento sobre otro para intentar combinarlos. "+
									"\nPresione con click-derecho sobre un elemento para eliminarlo de la mesa de trabajo. \nIntente descubrir todos los elementos.\n¡Suerte!";
				JOptionPane.showOptionDialog(null, message, "Ayuda",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, botones, botones[0]);
			}
		});
		mnAyuda.add(mntmHelp);
		mnAyuda.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 202, 375);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);
		
		panelElements = new JPanel();
		scrollPane.setViewportView(panelElements);
		panelElements.setLayout(new BoxLayout(panelElements, BoxLayout.Y_AXIS));
		
		panelBoard = new JPanel();
		panelBoard.setBackground(Color.BLACK);
		panelBoard.setBounds(222, 11, 483, 406);
		contentPane.add(panelBoard);
		panelBoard.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 428, 695, 21);
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblStatus = new JLabel("Elementos:4/10");
		lblStatus.setBounds(10, 3, 113, 14);
		lblStatus.setText("Elementos: " + elemMgr.getDiscovered() + "/" + elemMgr.getCount());
		panel.add(lblStatus);
		
		lblLastCombination = new JLabel("");
		lblLastCombination.setBounds(133, 3, 443, 14);
		panel.add(lblLastCombination);
		
		lblClues = new JLabel("Pistas:");
		lblClues.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				requestClue();
			}
		});
		lblClues.setForeground(Color.BLACK);
		lblClues.setBounds(622, 3, 63, 14);
		panel.add(lblClues);
		
		textSearch = new JTextField();
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if(c == '\n') {
					addFoundedElement();
					textSearch.setText("");					
				}
			}
		});
		
		textSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent arg0) {
				search(textSearch.getText());
			}
			
			public void insertUpdate(DocumentEvent arg0) {
				search(textSearch.getText());
			}
			
			public void changedUpdate(DocumentEvent arg0) {
				search(textSearch.getText());
			}
		});
				
		textSearch.setBounds(10, 11, 202, 20);
		contentPane.add(textSearch);
		textSearch.setColumns(10);
					
		loadElements();		
		insertBasicElements();
		refreshCluesCount();
		//elemMgr.cypherData();
	}
}
