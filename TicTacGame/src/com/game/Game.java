package com.game;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.game.rmi.GameService;

import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game extends JDialog implements ActionListener {

 
	private final JPanel gridPanel = new JPanel();
	private final JPanel topPanel = new JPanel();
	JLabel info  = new JLabel();
	JButton[][] buttons;
	int size = 0;

	// игровое поле
	PlayingField fields;
    // изображение кнопок
	Image cross;
	Image zerro;
	Image crossWin;
	Image zerroWin;
	Image white;

	// размер кнопки
	static int sizeCell = 80;
	 
	boolean gameOver = false;
	boolean isComputer = false;
	// игровой сервис
	GameService service;

	/**
	 * Create the dialog.
	 */
	public Game(PlayingField fields) {
        // создадим подключение
		try {
			service = (GameService) Naming.lookup("//localhost/" + GameService.SERVICE_NAME);
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			JOptionPane.showMessageDialog(null, "Не могу подключиться к серверу");
			dispose();
		}
		// зададим размер картинок
		if (fields.getSize()>6) sizeCell=75;

		// загрузим картинки
		try {
			cross = ImageIO.read(getClass().getResource("/resources/img/cross.png")).getScaledInstance(sizeCell,
					sizeCell, Image.SCALE_SMOOTH);
			zerro = ImageIO.read(getClass().getResource("/resources/img/zero.png")).getScaledInstance(sizeCell,
					sizeCell, Image.SCALE_SMOOTH);
			crossWin = ImageIO.read(getClass().getResource("/resources/img/crosswin.png")).getScaledInstance(sizeCell,
					sizeCell, Image.SCALE_SMOOTH);
			zerroWin = ImageIO.read(getClass().getResource("/resources/img/zerowin.png")).getScaledInstance(sizeCell,
					sizeCell, Image.SCALE_SMOOTH);
			white = ImageIO.read(getClass().getResource("/resources/img/white.png")).getScaledInstance(sizeCell,
					sizeCell, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setModal(true);
		setResizable(false);
		this.fields = fields;
		size = fields.getSize();
		setBounds(100, 100, 212, 261);
		gridPanel.setLayout(new GridLayout(size, 10, size, 10));
		gridPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		gridPanel.setBackground(new Color(250,217,14));
		getContentPane().add(gridPanel, BorderLayout.CENTER);
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		info.setFont(new Font("Showcard Gothic", Font.PLAIN, 22));
		info.setPreferredSize(new Dimension(200,40));
		info.setText("YOUR TURN");
		topPanel.add(info);
		topPanel.setBackground(new Color(250,217,14));
		getContentPane().add(topPanel, BorderLayout.NORTH);

		//btStart.setBackground(new Color(246,174,50));

		Dimension btSize = new Dimension(sizeCell, sizeCell);
		// массив кнопок
		buttons = new JButton[size][size];
		// заполним игровое поле
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				JButton button = new JButton();
				// в actionComman будем хранить индекс массива
				button.setActionCommand(String.format("%d,%d", i, j));
				button.setPreferredSize(btSize);
				button.setBackground(new Color(250,217,14));
				// добавим слушатель
				button.addActionListener(this);
				button.setIcon(new ImageIcon(white));
				buttons[i][j] = button;
				gridPanel.add(button);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(250,217,14));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton btExit = new JButton("FINISH");
				btExit.setFont(new Font("Showcard Gothic", Font.PLAIN, 22));
				btExit.setBackground(new Color(246,174,50));
				btExit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(btExit);
				getRootPane().setDefaultButton(btExit);
			}
		}
		// setLocationRelativeTo(null);
		pack();
		setLocationRelativeTo(null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameOver)
			return;
		if (isComputer)
			return;

		JButton button = (JButton) e.getSource();
		String[] temp = button.getActionCommand().split(",");
		// получим индекс
		int i = Integer.parseInt(temp[0]);
		int j = Integer.parseInt(temp[1]);
		if (fields.isFree(i, j)) {
			
			fields.setRow(i, j, State.X);
			// отрисуем
			draw();
			if (fields.isFull()) {
				checkWin();
			}
			// ход компьютера
			isComputer = true;
			if (gameOver) return;
			computeMove();
			if (fields.isFull()) {
				checkWin();
			}
			
			
		}

	}
    // рисует ряд который выиграл
	public void drawWin(List<Cell> list, State state) {
		Image image = crossWin;
		if (state == State.O)
			image = zerroWin;

		for (Cell cell : list) {

			buttons[cell.getX()][cell.getY()].setIcon(new ImageIcon(image));
		}

	}
    // проверка кто победил
	public void checkWin() {
		gameOver = true;
		Cell[][] cells = toCellFieild(fields);
		
		List<List<Cell>> listAll = new ArrayList<>();
		
		// получим все строки, столбцы  и диагонали
		
		List<List<Cell>> tempList = getRows(cells);
        listAll.addAll(tempList);
        

        tempList = getColls(cells);
        listAll.addAll(tempList);
                
        tempList = getDiagMain(cells);
        listAll.addAll(tempList);
        
        
        tempList = getDiagMainTop(cells);
        listAll.addAll(tempList);
        
        tempList = getDiagMainBottom(cells);
        listAll.addAll(tempList);
        
        tempList = getDiagSide(cells);
        listAll.addAll(tempList);
        
        tempList = getDiagSideTop(cells);
        listAll.addAll(tempList);
        
        tempList = getDiagSideBottom(cells);
        listAll.addAll(tempList);
        
        // найдем самую длинную непрерывную строку X
		List<Cell> listX = new ArrayList<>();
		int max=0;
		for(List<Cell> list:listAll) {
			List<Cell> temp = new ArrayList<>();
			for (Cell cell:list) {
				if (cell.getState()==State.X) {
					temp.add(cell);
					if (temp.size()>max) {
						listX.clear();
						listX.addAll(temp);
					 	max = listX.size();
										}
				}
				else {
						temp = new ArrayList<>();
				}
			}
		}

		//найдем самую длинную непрерывную строку O
		List<Cell> listO = new ArrayList<>();
		max=0;
		for(List<Cell> list:listAll) {
			List<Cell> temp = new ArrayList<>();
			for (Cell cell:list) {
				if (cell.getState()==State.O) {
					temp.add(cell);
						if (temp.size()>max) {
							listO.clear();
							listO.addAll(temp);
							max = listO.size();
					}
				}
				else {
						temp = new ArrayList<>();
					}
			}
		}
		int x = listX.size();
		int o = listO.size();
		// сравним
		if (x>o) {
			drawWin(listX, State.X);
			info.setText("YOU WON!");
			//JOptionPane.showMessageDialog(null, "Победил Крестик!");
		} else if (o>x) {
			drawWin(listO, State.O);
			info.setText("YOU LOSE!");
			//JOptionPane.showMessageDialog(null, "Победил Нолик!");
		} else  {
			//JOptionPane.showMessageDialog(null, "Ничья!");
			info.setText("DRAW!");
			
		}

	}

	/**
	 * конвертирует PlayingField в Cell[][]
	 * @param fields
	 * @return Cell[][]
	 */
	private Cell[][] toCellFieild(PlayingField fields) {
		Cell[][] cells = new Cell[fields.getSize()][fields.getSize()];
		for (int i = 0; i < fields.getSize(); i++)
			for (int j = 0; j < fields.getSize(); j++)
				cells[i][j] = new Cell(i, j, fields.getRow(i, j));
		return cells;
	}
     /**
      * возвращает строки  Cell[][]
      * @param cell
      * @return
      */
	private List<List<Cell>> getRows(Cell[][] cell) {
		List<List<Cell>> listAll = new ArrayList<List<Cell>>();
		for (int i = 0; i < cell.length; i++) {
			List<Cell> list = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				list.add(cell[i][j]);
			}
			listAll.add(list);
		}
		return listAll;
	}
	/**
     * возвращает столбцы  Cell[][]
     * @param cell
     * @return
     */
	private List<List<Cell>> getColls(Cell[][] cell) {
		List<List<Cell>> listAll = new ArrayList<List<Cell>>();
		for (int i = 0; i < cell.length; i++) {
			List<Cell> list = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				list.add(cell[j][i]);
			}
			listAll.add(list);
		}
		return listAll;
	}
	/**
     * над побочной диагональю
     */
	private List<List<Cell>> getDiagSideTop(Cell[][] cells) {
		List<List<Cell>> listAll = new ArrayList<List<Cell>>();
	
		// над побочной
        for (int k = 1; k < size - 1; k++) {
            int i = 0;
            int j = k;
            List<Cell> list = new ArrayList<>();
            while ((j >= 0)) {
            	list.add(cells[i][j]);
                i++;
                j--;
            }
            listAll.add(list);
        }
		return listAll;
	}
	
	/*
	 * под побочной
	 */
	private List<List<Cell>> getDiagMainBottom(Cell[][] cells) {
		List<List<Cell>> listAll = new ArrayList<List<Cell>>();
	
	// под побочной!!

		for (int k = 0; k < size - 2; k++) {
			List<Cell> list = new ArrayList<>();
			int i = k + 1;
			int j = 0;
			while (i < size && j < size) {
				list.add(cells[i][j]);
				i++;
				j++;
			}
			listAll.add(list);
		}
	
		return listAll;
	}
	
	/*
	 * над главной
	 */
	private List<List<Cell>> getDiagMainTop(Cell[][] cells) {
		List<List<Cell>> listAll = new ArrayList<List<Cell>>();
	
		// над главной!!
			for (int k = 0; k < size - 2; k++) {
			int j = k + 1;
			int i = 0;
			List<Cell> list = new ArrayList<>();
			while (i < size && j < size) {
				list.add(cells[i][j]);
				i++;
				j++;
			}
			listAll.add(list);	
		}
		return listAll;
	}
	
	/*
	 * под главной
	 */
	private List<List<Cell>> getDiagSideBottom(Cell[][] cells) {
		List<List<Cell>> listAll = new ArrayList<List<Cell>>();
		for (int k = 1; k < size - 1; k++) {
			int i = size - 1;
			int j = k;
			List<Cell> list = new ArrayList<>();
			while (i < size && j < size) {
				list.add(cells[i][j]);
				i--;
				j++;
			}
			listAll.add(list);
		}

		return listAll;
	}
	/*
	 * главная диагональ
	 */
	private List<List<Cell>> getDiagMain(Cell[][] cells) {
		List<List<Cell>> listAll = new ArrayList<List<Cell>>();
		List<Cell> list = new ArrayList<>();
		for (int i = 0; i < cells.length; i++) {
			list.add(cells[i][i]);
		}
		listAll.add(list);

		return listAll;
	}
	/*
	 * побочная диагональ
     */
	private List<List<Cell>> getDiagSide(Cell[][] cells) {
		List<List<Cell>> listAll = new ArrayList<List<Cell>>();
		// побочная
		List<Cell> list = new ArrayList<>();
		 int i = size;
	        for (int j = 0; j < size; j++) {
	            i--;
	            list.add(cells[i][j]);
	        }
		listAll.add(list);
		return listAll;
	}

	

	/**
	 * ход компютера
	 * 
	 */
	public void computeMove(){
		try {
			
			Cell cell = service.nextMove(this.fields);
			fields.setRow(cell.getX(), cell.getY(), State.O);
			draw();
			if (fields.isFull())
				checkWin();
			isComputer = false;
			info.setText("YOUR TURN");
		} catch (RemoteException e1) {
			info.setText("service ERROR");
			JOptionPane.showMessageDialog(null, "service ERROR");
			//dispose();
		}
		
		
	}

	/*
	 * отрисовка игрового поля
	 */
	public void draw() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				if (fields.getRow(i, j) == State.EMPTY) {
					buttons[i][j].setIcon(new ImageIcon(white));
				}
				if (fields.getRow(i, j) == State.X) {
					buttons[i][j].setIcon(new ImageIcon(cross));
				}
				if (fields.getRow(i, j) == State.O) {
					buttons[i][j].setIcon(new ImageIcon(zerro));
				}

			}
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gridPanel.updateUI();
			}
		});
	}

}
