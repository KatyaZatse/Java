package com.game;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.game.rmi.GameService;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.SystemColor;


public class MainFrame extends JFrame {
 

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws FontFormatException 
	 */
	public MainFrame() throws FontFormatException, IOException {
		setBackground(Color.BLACK);
		setForeground(SystemColor.desktop);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 272);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Font font = new Font("Arial Black", Font.BOLD, 22);

		JLabel lblNewLabel = new JLabel("TIC TAC TOE");
		lblNewLabel.setBounds(132, 11, 180, 51);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Showcard Gothic", Font.BOLD, 25));
		contentPane.add(lblNewLabel);

		JComboBox<Size> chSize = new JComboBox();
		chSize.setPreferredSize(new Dimension(100, 25));
		chSize.setBackground(new Color(246,174,50));
		chSize.setBounds(144, 75, 155, 35);
		chSize.setFont(new Font("Showcard Gothic", Font.PLAIN, 22));
		chSize.setRenderer(new DefaultListCellRenderer() {
		    {
		        setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		        setEnabled(false);
		    }
		});
		contentPane.add(chSize);
		chSize.setModel(new DefaultComboBoxModel(new Size[] { new Size(3,"3x3"),new Size(4,"4x4"),new Size(5,"5x5"),
				new Size(6,"6x6"),new Size(7, "7x7"),new Size(8, "8x8") ,new Size(9, "9x9"),new Size(10, "10x10") }));

		JButton btStart = new JButton("NEW GAME");
		btStart.setBackground(new Color(246,174,50));
		btStart.setPreferredSize(new Dimension(100, 25));
		btStart.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		btStart.setBounds(147, 123, 150, 35);
		contentPane.add(btStart);
		contentPane.setBackground(new Color(250,217,14));
		JButton btInfo = new JButton("i");
		btInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					InfoDialog dialog = new InfoDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btInfo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		btInfo.setBounds(335, 130, 57, 25);
		// раскоментируй, если нужна справка
		//contentPane.add(btInfo);
		
		JButton btExit = new JButton("EXIT");
		btExit.setBackground(new Color(246,174,50));
		btExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		btExit.setPreferredSize(new Dimension(100, 25));
		btExit.setFont(new Font("Showcard Gothic", Font.PLAIN, 18));
		btExit.setBounds(147, 180, 150, 35);
		contentPane.add(btExit);
		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Size choose = (Size) chSize.getSelectedItem();
				int size = choose.getSize();
				try {
					GameService server = (GameService) Naming.lookup("//localhost/"+GameService.SERVICE_NAME);
					PlayingField fields = server.newGame(size);
 				    Game dialog = new Game(fields);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (MalformedURLException |RemoteException | NotBoundException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,"Ошибка подключения к серверу!");
				} 
			}
		});
		setLocationRelativeTo(null);

	}

	
	
	private class Size {
		int size;
		String info;
		public Size(int size, String info) {
			this.size = size;
			this.info = info;
		}
		public String getInfo() {
			return info;
		}
		public int getSize() {
			return size;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return info;
		}
		
	}
}
