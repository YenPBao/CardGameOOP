package view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import tienlen.TienLenDeck;
import game.BotGame;
//import game.Bot;
import game.Client;
import handmodel.Hand;
import message.Message;

public class Table {
	private Client game; 
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel tienLenPanel; 
	private JButton playButton; 
	private JButton passButton; 
	private JTextArea msgArea;
	private JTextArea msgArea1; 
	private Image[][] cardImages; 
	private Image cardBackImage;
	private Image[] avatars; 
	private boolean[] presence;
	private JTextField message_box;
	private JPanel message ;
	private int clickCount = 0;
	public class ButtonGradient extends JButton {

	    public float getSizeSpeed() {
	        return sizeSpeed;
	    }

	    public void setSizeSpeed(float sizeSpeed) {
	        this.sizeSpeed = sizeSpeed;
	    }

	    public Color getColor1() {
	        return color1;
	    }

	    public void setColor1(Color color1) {
	        this.color1 = color1;
	    }

	    public Color getColor2() {
	        return color2;
	    }

	    public void setColor2(Color color2) {
	        this.color2 = color2;
	    }

	    private Color color1 = Color.decode("#0099F7");
	    private Color color2 = Color.decode("#F11712");
	    private final Timer timer;
	    private final Timer timerPressed;
	    private float alpha = 0.3f;
	    private boolean mouseOver;
	    private boolean pressed;
	    private Point pressedLocation;
	    private float pressedSize;
	    private float sizeSpeed = 9f;
	    private float alphaPressed = 0.5f;

	    public ButtonGradient() {
	        setContentAreaFilled(false);
	        setForeground(Color.WHITE);
	        setCursor(new Cursor(Cursor.HAND_CURSOR));
	        setBorder(new EmptyBorder(10, 20, 10, 20));
	        addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseEntered(MouseEvent me) {
	                mouseOver = true;
	                timer.start();
	            }

	            @Override
	            public void mouseExited(MouseEvent me) {
	                mouseOver = false;
	                timer.start();
	            }

	            @Override
	            public void mousePressed(MouseEvent me) {
	                pressedSize = 0;
	                alphaPressed = 0.5f;
	                pressed = true;
	                pressedLocation = me.getPoint();
	                timerPressed.setDelay(0);
	                timerPressed.start();
	            }
	        });
	        timer = new Timer(5, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent ae) {
	                if (mouseOver) {
	                    if (alpha < 0.6f) {
	                        alpha += 0.05f;
	                        repaint();
	                    } else {
	                        alpha = 0.6f;
	                        timer.stop();
	                        repaint();
	                    }
	                } else {
	                    if (alpha > 0.3f) {
	                        alpha -= 0.05f;
	                        repaint();
	                    } else {
	                        alpha = 0.3f;
	                        timer.stop();
	                        repaint();
	                    }
	                }
	            }
	        });
	        timerPressed = new Timer(0, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent ae) {
	                pressedSize += getSizeSpeed();
	                if (alphaPressed <= 0) {
	                    pressed = false;
	                    timerPressed.stop();
	                } else {
	                    repaint();
	                }
	            }
	        });
	    }

	    @Override
	    protected void paintComponent(Graphics grphcs) {
	        int width = getWidth();
	        int height = getHeight();
	        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2 = img.createGraphics();
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        //  Create Gradients Color
	        GradientPaint gra = new GradientPaint(0, 0, color1, width, 0, color2);
	        g2.setPaint(gra);
	        g2.fillRoundRect(0, 0, width, height, height, height);
	        //  Add Style
	        createStyle(g2);
	        if (pressed) {
	            paintPressed(g2);
	        }
	        g2.dispose();
	        grphcs.drawImage(img, 0, 0, null);
	        super.paintComponent(grphcs);
	    }

	    private void createStyle(Graphics2D g2) {
	        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
	        int width = getWidth();
	        int height = getHeight();
	        GradientPaint gra = new GradientPaint(0, 0, Color.WHITE, 0, height, new Color(255, 255, 255, 60));
	        g2.setPaint(gra);
	        Path2D.Float f = new Path2D.Float();
	        f.moveTo(0, 0);
	        int controll = height + height / 2;
	        f.curveTo(0, 0, width / 2, controll, width, 0);
	        g2.fill(f);
	    }

	    private void paintPressed(Graphics2D g2) {
	        if (pressedLocation.x - (pressedSize / 2) < 0 && pressedLocation.x + (pressedSize / 2) > getWidth()) {
	            timerPressed.setDelay(20);
	            alphaPressed -= 0.05f;
	            if (alphaPressed < 0) {
	                alphaPressed = 0;
	            }
	        }
	        g2.setColor(Color.WHITE);
	        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alphaPressed));
	        float x = pressedLocation.x - (pressedSize / 2);
	        float y = pressedLocation.y - (pressedSize / 2);
	        g2.fillOval((int) x, (int) y, (int) pressedSize, (int) pressedSize);
	    }
	}

	public Table(Client game) 
	{
		this.game = game;	
		this.setup();
		frame.setSize(1240, 740);
		frame.setVisible(false);
	}
	public class CircleButton extends JButton{
		private static final long serialVersionUID = 12;
		private boolean mouseOver = false;
		private boolean mousePressed = false;

		public CircleButton(String text){
			super(text);
			setOpaque(false);
			setFocusPainted(false);
			setBorderPainted(false);

			MouseAdapter mouseListener = new MouseAdapter(){

				@Override
				public void mousePressed(MouseEvent me){
					if(contains(me.getX(), me.getY())){
						mousePressed = true;
						repaint();
					}
				}

				@Override
				public void mouseReleased(MouseEvent me){
					mousePressed = false;
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent me){
					mouseOver = false;
					mousePressed = false;
					repaint();
				}

				@Override
				public void mouseMoved(MouseEvent me){
				mouseOver = contains(me.getX(), me.getY());
					repaint();
				}
			};

			addMouseListener(mouseListener);
			addMouseMotionListener(mouseListener);		
		}

		private int getDiameter(){
			int diameter = Math.min(getWidth(), getHeight());
			return diameter;
		}

		@Override
		public Dimension getPreferredSize(){
			FontMetrics metrics = getGraphics().getFontMetrics(getFont());
			int minDiameter = 10 + Math.max(metrics.stringWidth(getText()), metrics.getHeight());
			return new Dimension(minDiameter, minDiameter);
		}

		@Override
		public boolean contains(int x, int y){
			int radius = getDiameter()/2;
			return Point2D.distance(x, y, getWidth()/2, getHeight()/2) < radius;
		}
		@Override
		public void paintComponent(Graphics g){
			int diameter = getDiameter();
			int radius = diameter/2;

			if(mousePressed){
				g.setColor(Color.LIGHT_GRAY);
			}
			else{
				g.setColor(Color.WHITE);
			}
			g.fillOval(getWidth()/2 - radius, getHeight()/2 - radius, diameter, diameter);

			if(mouseOver){
				g.setColor(Color.BLACK);
			}
			else{
				g.setColor(Color.BLACK);
			}
			g.drawOval(getWidth()/2 - radius, getHeight()/2 - radius, diameter, diameter);
			Icon icon = getIcon();
	        if (icon != null) {
	            int iconWidth = icon.getIconWidth();
	            int iconHeight = icon.getIconHeight();
	            int x = (getWidth() - iconWidth) / 2;
	            int y = (getHeight() - iconHeight) / 2;
	            icon.paintIcon(this, g, x, y);
	        }
			g.setColor(Color.BLACK);
			g.setFont(getFont());
			FontMetrics metrics = g.getFontMetrics(getFont());
			int stringWidth = metrics.stringWidth(getText());
			int stringHeight = metrics.getHeight();
			g.drawString(getText(), getWidth()/2 - stringWidth/2, getHeight()/2 + stringHeight/4);
		}
	}
	
	public void setup()
	{
		setActivePlayer(game.getPlayerID()); 
		selected = new boolean[13];
		resetSelected();
		tienLenPanel = new TienLenPanel();
		
		avatars = new Image[4];
		cardImages = new Image [4][13];

		for (int i = 0 ; i <4;i++)
		{
			int j = i+1;
			avatars[i] = new ImageIcon("src/avatars/icon"+j+".png").getImage();		
		}
		cardBackImage = new ImageIcon("src/cardsImage/BACK.png.png").getImage();
		
		char[] suit = {'S','C','D','H'};
		char[] rank = {'3', '4',  '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K','A', '2'};
		
		String fileLocation = new String();
		for (int i = 0; i < 4; i++)
		    {
				for(int j = 0 ; j < 13;j++)
				{
					fileLocation = "src/cardsImage/" + rank[j] +"-"+ suit[i] + ".png"+".png";
			        cardImages[i][j] = new ImageIcon(fileLocation).getImage();
				}
		        
		    }
		
		
		presence = new boolean[4];                                          
		for (int i = 0; i < 4; i++)
			presence[i] = false;
		
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Tien Len");
		frame.getContentPane().setBackground(new Color(25, 115, 25));
		
		
		
		msgArea = new JTextArea(100, 30);
		DefaultCaret caret = (DefaultCaret)msgArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		msgArea.append("Message Board\n");
		msgArea.setEditable(false);
		
		JScrollPane scroll_1 = new JScrollPane(msgArea);   
		msgArea.setLineWrap(true);           
		scroll_1.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);    
		
		
		message = new JPanel()
		{
	 protected void paintComponent(Graphics g) {
		 super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ nền mờ
            g2.setColor(new Color(255, 255, 255, 100)); // Màu nền mờ với alpha 180
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.dispose();
            super.paintComponent(g);
        }
		};
		message.setOpaque(false);
		message.setLayout(new BoxLayout(message, BoxLayout.Y_AXIS));
		msgArea1 = new JTextArea(100, 30)
		{
			 protected void paintComponent(Graphics g) {
				 super.paintComponent(g);
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	                // Vẽ nền mờ
	                g2.setColor(new Color(255, 255, 255, 100)); // Màu nền mờ với alpha 180
	                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

	                g2.dispose();
	                super.paintComponent(g);
	            }
				}
		;
		DefaultCaret caret1 = (DefaultCaret)msgArea1.getCaret();
		caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		msgArea1.setEditable(false);
		msgArea1.setOpaque(false);	
		msgArea1.setFont(new Font("Arial", Font.BOLD, 15));

		JLabel label = new JLabel("Message: ");
		message_box = new MyTextField(70);
		message_box.setMinimumSize(new Dimension(70, 70));
		message_box.setOpaque(false);
		JPanel message_input = new JPanel()
				{
			 protected void paintComponent(Graphics g) {
				 super.paintComponent(g);
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	                // Vẽ nền mờ
	                g2.setColor(new Color(255, 255, 255, 100)); // Màu nền mờ với alpha 180
	                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

	                g2.dispose();
	                super.paintComponent(g);
	            }
				}
		;
		message_input.setLayout(new FlowLayout(FlowLayout.LEFT));
		message_input.add(message_box);
		message_input.setVisible(true);
		message_input.setOpaque(false);
		
		
		message.add(msgArea1);
		message.add(message_input);
		message.setBounds(20,100,300,390);
		tienLenPanel.add(message);
		
		

		JButton chatButton = new CircleButton("");
		chatButton.setSelectedIcon(null);
		chatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickCount ++;
				if(clickCount%2 == 1)
				{
				message.setVisible (true);
				}
				else 
				{
					message.setVisible(false);
				}

			}		
		});

		chatButton.setIcon(new ImageIcon("src/buttons/CHAT.png"));
		chatButton.setBounds(43, 29, 48, 48);
		tienLenPanel.setLayout(null);
		tienLenPanel.add(chatButton);
		
		
		JPanel button_area = new JPanel();
		button_area.setLayout(new FlowLayout());
		playButton = new ButtonGradient();
		playButton.setText("ĐÁNH ");
		playButton.setFont(new Font("Arial", Font.BOLD, 15));
		((ButtonGradient) playButton).setColor1(new java.awt.Color(0, 70, 150));// RGB: (0, 70, 150)
		((ButtonGradient) playButton).setColor2(new java.awt.Color(100, 200, 255) );

		playButton.addActionListener(new PlayButtonListener());
		playButton.setBounds(950, 500, 100, 48);
		tienLenPanel.add(playButton);
		
		
		passButton = new ButtonGradient();
		passButton.setText("BỎ LƯỢT");
		passButton.setFont(new Font("Arial", Font.BOLD, 15));
		((ButtonGradient) passButton).setColor1(new java.awt.Color(0, 70, 150));// RGB: (0, 70, 150)
		((ButtonGradient) passButton).setColor2(new java.awt.Color(100, 200, 255) );

		passButton.addActionListener(new PassButtonListener());
		passButton.setBounds(1100, 500, 120 ,48);
		tienLenPanel.add(passButton);
		message.setVisible(false);


		frame.add(tienLenPanel);
			}
	
public Client getGame() {
		return game;
	}
	public void setGame(Client game) {
		this.game = game;
	}
	
	class TienLenPanel extends JPanel implements MouseListener {
		
		
		private static final long serialVersionUID = 1L;

		public TienLenPanel() 
		{
			this.addMouseListener(this);
		}
		public void paintComponent(Graphics g) 
		{
			this.setOpaque(true);
			Graphics2D g2 = (Graphics2D) g;
			BufferedImage image = null;
			try {
				image = ImageIO.read(getClass().getResource("/background/background.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			
			int[] playerOrder = new int[4];
		    for (int i = 0; i < game.getNumOfPlayers(); i++) {
		        playerOrder[i] = (activePlayer + i) % game.getNumOfPlayers();
		    }
		    int X = this.getWidth();
		    int Y = this.getHeight();
		    // Vị trí y của mỗi người chơi
		    int[] positionsY = {Y-43, Y/2, Y/2, 25};// dưới, trái,phải,trên
		    int[] positionsX = {X/2-35,20,X-60,X/2-35 };
		    int[] nameposY = {Y-28,Y/2-50,Y/2-50,50};
		    int[] nameposX = {X/2-85,40,X-120,X/2+50};

		    for (int pos = 0; pos < game.getNumOfPlayers(); pos++) {
		        int playerIdx = playerOrder[pos]; // Lấy người chơi theo thứ tự mới
		        int posY = positionsY[pos];      // Vị trí y tương ứng
		        int posX = positionsX[pos];
		        int nameX = nameposX[pos];
		        int nameY = nameposY[pos];

		        if (presence[playerIdx]) {
		            // Đổi màu nếu là người chơi hiện tại
		            if (game.getCurrentIdx() == playerIdx && game.getPlayerList().get(playerIdx).getNumOfCards() != 0) {
		                g.setColor(Color.YELLOW);
		                g.setFont(new Font("Serif", Font.BOLD , 24));
		            }

		            // Hiển thị tên hoặc "YOU"
		            if (activePlayer == playerIdx) {
		            	if(game.getCurrentIdx()	== activePlayer)
		            		{
		            		g.setColor(Color.YELLOW);
		            		}
		            	else
		            	{
		            		g.setColor(Color.WHITE);
		            	}
		            	   g.setFont(new Font("Serif", Font.BOLD , 24));
		                g.drawString("YOU", posX - 50, posY+15);
		            } else {
		                g.drawString(game.getPlayerList().get(playerIdx).getName(), nameX, nameY);
		            }

		            g.setColor(Color.WHITE);
		            g.setFont(new Font("Serif", Font.BOLD, 24));
		            g.drawImage(avatars[playerIdx], posX, posY - 20, this); // Avatar hiển thị ở vị trí cố định

		            // Hiển thị các lá bài
		            if (activePlayer == playerIdx) {
		                // Active Player (YOU): Các lá bài nhô lên phía trên avatar
		                for (int i = 0; i < game.getPlayerList().get(playerIdx).getNumOfCards(); i++) {
		                    int cardX = posX - (game.getPlayerList().get(playerIdx).getNumOfCards() * 20) / 2 + 40 * i-100;
		                    int cardY = posY - 150; // Nhô lên phía trên avatar
		                    if (!selected[i]) {
		                        g.drawImage(
		                            cardImages[game.getPlayerList().get(playerIdx).getCardsInHand().getCard(i).getSuit()]
		                                      [game.getPlayerList().get(playerIdx).getCardsInHand().getCard(i).getRank()],
		                            cardX, cardY, this
		                        );
		                    } else {
		                        g.drawImage(
		                            cardImages[game.getPlayerList().get(playerIdx).getCardsInHand().getCard(i).getSuit()]
		                                      [game.getPlayerList().get(playerIdx).getCardsInHand().getCard(i).getRank()],
		                            cardX, cardY - 20, this
		                        );
		                    }
		                }
		            } else {
		                // Người chơi khác: Hiển thị 1 lá bài úp và số lượng bài còn lại
		                int backCardX = posX;
		                int backCardY = posY;

		                // Điều chỉnh vị trí lá bài dựa trên cạnh
		                if (pos == 1) { // Người chơi bên trái
		                    backCardX += 100; // Lùi vào trong từ cạnh trái
		                } else if (pos == 2) { // Người chơi bên phải
		                    backCardX -= 100; // Lùi vào trong từ cạnh phải
		                } else if (pos == 3) { // Người chơi phía trên
		                    backCardY += 100; // Lùi xuống dưới từ cạnh trên
		                }

		                g.drawImage(cardBackImage, backCardX, backCardY - 30, this); // Lùi lên trên avatar
		                g.setColor(Color.BLACK); // Màu chữ số lượng bài
		                g.setFont(new Font("Serif", Font.BOLD, 24));
		                g.drawString(
		                    String.valueOf(game.getPlayerList().get(playerIdx).getCardsInHand().size()),
		                    backCardX + 25, backCardY + 30 // Hiển thị số lượng bài bên cạnh lá bài
		                );
		                g.setColor(Color.WHITE);
		                g.setFont(new Font("Serif", Font.BOLD, 24));
		            }
		        }
		    }

			    
			    
			    if (game.getHandsOnTable().size() == 0)
			    {
			    }
			   
			    else
			    {
			    		Hand handOnTable = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
			    		//g.drawString("Hand Type:\n ", X/2, Y/2+60);
				    
			    		if (game.getPlayerList().get(game.getCurrentIdx()) != game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer()) 
				    	{
				    		//g.drawString(game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName(), X/2, Y/2+85);
				    		
				    		for (int i = 0; i < handOnTable.size(); i++)
				    		{
				    			g.drawImage(cardImages[handOnTable.getCard(i).getSuit()][handOnTable.getCard(i).getRank()], X/2 + 40*i, Y/2-30, this);
				    		}
				    			
				    	}
			    }
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		    boolean flag = false;
		    int starting_point = game.getPlayerList().get(activePlayer).getNumOfCards() - 1;

		    int X = this.getWidth();
		    int Y = this.getHeight();
		    
		    // Tính vị trí của các lá bài trên màn hình
		    int cardX = X / 2 - (game.getPlayerList().get(activePlayer).getNumOfCards() * 20) / 2 - 100-25;
		    int cardY = Y - 43 - 150;

		    // Kiểm tra click vào lá bài đầu tiên
		    if (e.getX() >= (cardX + starting_point * 40) && e.getX() <= (cardX + starting_point * 40 + 73)) {
		        if (!selected[starting_point] && e.getY() >= cardY && e.getY() <= cardY + 103) {
		            selected[starting_point] = true;
		            flag = true;
		        } else if (selected[starting_point] && e.getY() >= cardY - 20 && e.getY() <= cardY + 103 - 20) {
		            selected[starting_point] = false;
		            flag = true;
		        }
		    }

		    // Kiểm tra click vào các lá bài còn lại
		    for (starting_point = game.getPlayerList().get(activePlayer).getNumOfCards() - 2; starting_point >= 0 && !flag; starting_point--) {
		        int cardXOffset = cardX + starting_point * 40;

		        // Kiểm tra phạm vi click vào lá bài
		        if (e.getX() >= cardXOffset && e.getX() <= (cardXOffset + 73)) {
		            if (!selected[starting_point] && e.getY() >= cardY && e.getY() <= cardY + 103) {
		                selected[starting_point] = true;
		                flag = true;
		            } else if (selected[starting_point] && e.getY() >= cardY - 20 && e.getY() <= cardY + 103 - 20) {
		                selected[starting_point] = false;
		                flag = true;
		            }
		        }

		        // Kiểm tra nếu click vào phần của lá bài bị lùi vào trong
		        else if (e.getX() >= (cardXOffset + 40) && e.getX() <= (cardXOffset + 73) && e.getY() >= cardY && e.getY() <= cardY + 103) {
		            if (selected[starting_point + 1] && !selected[starting_point]) {
		                selected[starting_point] = true;
		                flag = true;
		            }
		        }

		        // Kiểm tra nếu click vào phần trên của lá bài
		        else if (e.getX() >= (cardXOffset + 40) && e.getX() <= (cardXOffset + 73) && e.getY() >= cardY - 20 && e.getY() <= cardY + 103 - 20) {
		            if (!selected[starting_point + 1] && selected[starting_point]) {
		                selected[starting_point] = false;
		                flag = true;
		            }
		        }
		    }

		    // Vẽ lại sau khi xử lý click
		    frame.repaint();
		}
//		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
		class MyTextField extends JTextField implements ActionListener
		{

			private static final long serialVersionUID = 1L;

			public MyTextField(int i)
			{
				super(i);
				addActionListener(this);
				this.setFont(new Font("Arial", Font.PLAIN, 16));
				this.setOpaque(false);
			}
			
			 protected void paintComponent(Graphics g) {
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	                // Vẽ nền mờ
	                g2.setColor(new Color(255, 255, 255, 100)); // Màu nền mờ với alpha 180
	                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

	                g2.dispose();
	                super.paintComponent(g);
	            }

			public void actionPerformed(ActionEvent event)
			{
				String input = getText(); 
				
				if (input != null && input.trim().isEmpty() == false) 
				{  
					Message message = new Message(7, activePlayer, input);
					game.sendMessage(message);
				}
				
				this.setText("");
			}
		}
//	
//	
	
	class PlayButtonListener implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			
			
			if (game.getCurrentIdx() == activePlayer)
			{ 
				if (getSelected().length == 0) 
				{	
					int [] cardIdx = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
					game.makeMove(activePlayer, cardIdx);
				}
				
				else
				{
					game.makeMove(activePlayer, getSelected());
				}
					
				resetSelected();
				repaint();
			}
			
			else 
			{ 
				printMsg("It is not your turn\n");
				resetSelected();
				repaint();
			}
		}
	}
	
	
	class PassButtonListener implements ActionListener
	 	{
		
	
		public void actionPerformed(ActionEvent e) {
			if (game.getCurrentIdx() == activePlayer)
			{ 
				int[] cardIdx = null;
				game.makeMove(activePlayer, cardIdx);
				resetSelected();
				repaint();
			} 
			
			else 
			{
				printMsg("Not your turn!\n");
				resetSelected();
				repaint();
			}
		}
	}
	
	
	class ConnectMenuItemListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e) 
		{
			if (game.getPlayerID() == -1) 
			{
				game.makeConnection();
			} 
			
			else if (game.getPlayerID() >= 0 && game.getPlayerID() <= 3)
			{
				printMsg("Connection already established!\n");
			}
				
		}
	}
	
	
	class QuitMenuItemListener implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
//			printMsg("Game Ended by the User!\n");
//			System.exit(0);
			printMsg("Game Ended by the User!\n");
            // Đóng cửa sổ chính
            frame.dispose();
            // Reset trạng thái trò chơi
            // Hiển thị lại BeginningPanel
            //showBeginningFrame();
		}
	}
	
	class ClearMenuItemListener implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			clearMsgArea();
		}
	}
	
	
	class ClearChatMenuItemListener implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			clearChatMsgArea();
		}
	}
	
	public void setNotExistence(int playerID)
	{
		presence[playerID] = false;
	}
	
	
	public void setActivePlayer(int activePlayer)
	{
		this.activePlayer = activePlayer;
	}

	
	public int[] getSelected()
	{
		int ct = 0;
		
		for (int i = 0; i < 13; i++)
		{
			if (selected[i])
			{
				ct++;
			}
		}
		
		int[] user_input = new int[ct];
		int counter = 0;
		
		for (int i = 0; i < 13; i++)
		{
			if (selected[i])
			{
				user_input[counter] = i;
				counter++;
			}
		}
		
		return user_input; 
	}

	
	public void resetSelected()
	{
		for (int i = 0; i < 13; i++)
			selected[i] = false;
	}

	public void repaint()
	{
		if(game instanceof BotGame)
		{
			frame.setVisible(false);
		}
		else
		{
			if(game.getCurrentIdx() == activePlayer)
			{
				frame.setVisible(true);
			}
		}
		frame.repaint();
	}

	
	public void printMsg(String msg)
	{
		msgArea.append(msg);
	}
	
	public void printEndGameMsg()
	{
		String endOfGame="";
		
		for(int i=0; i<game.getNumOfPlayers(); ++i)
		{
			endOfGame+=game.getPlayerList().get(i).getName()+": ";
			
			if(game.getPlayerList().get(i).getNumOfCards()!=0)
			{
				for(int j=0; j<game.getPlayerList().get(i).getNumOfCards(); ++j)
				{
					endOfGame+=" ["+game.getPlayerList().get(i).getCardsInHand().getCard(j).toString()+"]";
				}
				endOfGame+="\n";
			}
			
			else
			{
				endOfGame+=" THẮNG\n";
				//playSound("Player " + i + " Wins!");
			}
		}
		
		JOptionPane.showMessageDialog(null, "GAME ĐÃ KẾT THÚC!!\n"+endOfGame);
	}
	
	
	public void printChatMsg(String msg) 
	{
		msgArea1.append(msg+"\n");
	}

	
	public void clearMsgArea()
	{
		msgArea.setText("");
	}
	

	public void clearChatMsgArea() 
	{
		this.msgArea1.setText("");
	}
	
	public void setExistence(int playerID)
	{
		presence[playerID] = true;
	}

	public void reset()
	{
		frame.setVisible(false);
		TienLenDeck deck = new TienLenDeck();
		deck.shuffle();
		game.start(deck);
		printMsg("Game Restarted by User!");
	}

	
	public void enable()
	{
		tienLenPanel.setEnabled(true);
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	}

	
	public void disable()
	{
		tienLenPanel.setEnabled(false);
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	}
	//public void  
	public void visible()
	{
		frame.setVisible(false);
		//tienLenPanel.setVisible(false);
	}
	public void quit() 
	{
		System.exit(0);
	}
	public void playSound(String filename)
	 {
	       try{
	    	   	 String fileLocation = new String();
	    	     fileLocation = "src/Sounds/" + filename + ".wav";
	    	     AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileLocation));
	    	     Clip clip = AudioSystem.getClip();
	    	     clip.open(audioInputStream);
	    	     clip.start( );
	    	     
	    	    }
	    	   catch(Exception ex)
	    	   {  
	    		   
	    	   }
	

}
}
