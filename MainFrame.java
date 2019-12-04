package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Аргументы командной строки - коэффициенты многочлена , пример :
 *
 * <p>Аргементы командной строки : 5 4 3 2 1</p>
 * <p>Многочлен : 5*x^4 + 4*x^3 + 3*x^2 + 2*x + 1</p>
 */

public class MainFrame extends JFrame {

	private static final int WIDTH = 1200;
	private static final int HEIGHT = 500;

	private static int numPhoto = 2;

	private Double[] coefficients;

	private JFileChooser fileChooser = null;

	private JMenuItem saveToTextMenuItem;
	private JMenuItem saveToGraphicsMenuItem;
	private JMenuItem searchValueMenuItem;
	private JMenuItem aboutProgramItem;
	private JMenuItem searchRangeItem;
	private JMenuItem saveToCSVFileItem;

	private JTextField textFieldFrom;
	private JTextField textFieldTo;
	private JTextField textFieldStep;

	private Box hboxResult;

	private GornerTableCellRenderer renderer = new GornerTableCellRenderer();

	private GornerTableModel data;

	public MainFrame(Double[] coefficients) {
		super("Табулирование многочлена на отрезке по схеме Горнера");
		this.coefficients = coefficients;

		setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();


		setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

		JMenuBar menuBar = new JMenuBar();

		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Файл");

		menuBar.add(fileMenu);

		JMenu tableMenu = new JMenu("Таблица");

		menuBar.add(tableMenu);

		JMenu referenceMenu = new JMenu("Справка");

		menuBar.add(referenceMenu);

		Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
			public void actionPerformed(ActionEvent event) {
				if (fileChooser == null) {
					fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("."));
				}
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					saveToTextFile(fileChooser.getSelectedFile());
				}
			}
		};

		saveToTextMenuItem = fileMenu.add(saveToTextAction);
		saveToTextMenuItem.setEnabled(false);

		Action saveToGraphicsAction = new AbstractAction("Сохранить данные для просмотра графика") {
			public void actionPerformed(ActionEvent event) {
				if (fileChooser == null) {
					fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("."));
				}
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					saveToGraphicsFile(fileChooser.getSelectedFile());
				}
			}
		};

		saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
		saveToGraphicsMenuItem.setEnabled(false);

		Action saveToCSVAction = new AbstractAction("Сохранить в CSV файл") {
			public void actionPerformed(ActionEvent event) {
				if (fileChooser == null) {
					fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("."));
				}
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					saveToCSVFile(fileChooser.getSelectedFile());
				}
			}
		};

		saveToCSVFileItem = fileMenu.add(saveToCSVAction);
		saveToCSVFileItem.setEnabled(false);

		Action searchValueAction = new AbstractAction("Найти значение многочлена") {
			public void actionPerformed(ActionEvent ev) {
				String value = JOptionPane.showInputDialog(MainFrame.this,
						"Введите значение для поиска", "Поиск Значения",
						JOptionPane.QUESTION_MESSAGE);

				renderer.setNeedle(value);
				getContentPane().repaint();
			}
		};

		searchValueMenuItem = tableMenu.add(searchValueAction);
		searchValueMenuItem.setEnabled(false);

		Action aboutProgramAction = new AbstractAction("О программе") {
			public void actionPerformed(ActionEvent ev) {

				Box mainPanel = Box.createVerticalBox();

				Box title1 = Box.createHorizontalBox();
				JLabel l1 = new JLabel("Автор: Сиваков Иван");
				l1.setFont(new Font("TimesRoman", Font.ITALIC, 20));
				title1.add(Box.createHorizontalGlue());
				title1.add(l1);
				title1.add(Box.createHorizontalGlue());


				Box title2 = Box.createHorizontalBox();
				JLabel l2 = new JLabel("Группа #6");
				JLabel l3 = new JLabel(":3");
				l3.setForeground(Color.RED);
				l3.setFont(new Font("TimesRoman", Font.ITALIC, 20));
				l2.setFont(new Font("TimesRoman", Font.ITALIC, 20));
				title2.add(Box.createHorizontalGlue());
				title2.add(l2);
				title2.add(Box.createHorizontalStrut(10));
				title2.add(l3);
				title2.add(Box.createHorizontalGlue());

				ImageIcon icon = new ImageIcon("src/com/company/images/photo_2.jpg");
				JLabel image = new JLabel(icon);
				image.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (numPhoto == 2) {
							image.setIcon(new ImageIcon("src/com/company/images/photo_1.jpg"));
							numPhoto = 1;
						} else if (numPhoto == 1) {
							image.setIcon(new ImageIcon("src/com/company/images/photo_2.jpg"));
							numPhoto = 2;
						}
					}

				});


				mainPanel.add(Box.createVerticalGlue());
				mainPanel.add(title1);
				mainPanel.add(Box.createVerticalStrut(10));
				mainPanel.add(title2);
				mainPanel.add(Box.createVerticalStrut(10));
				mainPanel.add(image);
				mainPanel.add(Box.createVerticalGlue());


				JOptionPane.showMessageDialog(MainFrame.this,
						mainPanel, "О программе", JOptionPane.INFORMATION_MESSAGE);
			}
		};

		aboutProgramItem = referenceMenu.add(aboutProgramAction);

		Action searchRangeAction = new AbstractAction("Найти из диапазона") {
			@Override
			public void actionPerformed(ActionEvent ev) {
				Box rangeBox = Box.createVerticalBox();
				JLabel from = new JLabel("от:");
				JLabel to = new JLabel("до");
				JTextField searchFrom = new JTextField("0.0", 10);
				searchFrom.setMaximumSize(searchFrom.getPreferredSize());
				JTextField searchTo = new JTextField("0.0", 10);
				searchFrom.setMaximumSize(searchFrom.getPreferredSize());
				rangeBox.add(Box.createVerticalGlue());

				Box fromBox = Box.createHorizontalBox();
				fromBox.add(from);
				fromBox.add(Box.createHorizontalStrut(10));
				fromBox.add(searchFrom);

				rangeBox.add(fromBox);
				rangeBox.add(Box.createVerticalStrut(20));

				Box toBox = Box.createHorizontalBox();
				toBox.add(to);
				toBox.add(Box.createHorizontalStrut(10));
				toBox.add(searchTo);

				rangeBox.add(toBox);
				rangeBox.add(Box.createVerticalGlue());

				JOptionPane.showMessageDialog(MainFrame.this,
						rangeBox, "" +
								"Найти из диапазона", JOptionPane.QUESTION_MESSAGE);
				renderer.setRanges(searchFrom.getText(), searchTo.getText());
				getContentPane().repaint();
			}
		};

		searchRangeItem = tableMenu.add(searchRangeAction);
		searchRangeItem.setEnabled(false);

		JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
		textFieldFrom = new JTextField("0.0", 10);
		textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());

		JLabel labelForTo = new JLabel("до:");
		textFieldTo = new JTextField("1.0", 10);
		textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());

		JLabel labelForStep = new JLabel("с шагом:");
		textFieldStep = new JTextField("0.1", 10);
		textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());

		Box hboxRange = Box.createHorizontalBox();
		hboxRange.setBorder(BorderFactory.createBevelBorder(1));
		hboxRange.add(Box.createHorizontalGlue());
		hboxRange.add(labelForFrom);
		hboxRange.add(Box.createHorizontalStrut(10));
		hboxRange.add(textFieldFrom);
		hboxRange.add(Box.createHorizontalStrut(20));
		hboxRange.add(labelForTo);
		hboxRange.add(Box.createHorizontalStrut(10));
		hboxRange.add(textFieldTo);
		hboxRange.add(Box.createHorizontalStrut(20));
		hboxRange.add(labelForStep);
		hboxRange.add(Box.createHorizontalStrut(10));
		hboxRange.add(textFieldStep);
		hboxRange.add(Box.createHorizontalGlue());

		hboxRange.setPreferredSize(new Dimension(
				new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
				new Double(hboxRange.getMinimumSize().getHeight()).intValue() * 2));
		getContentPane().add(hboxRange, BorderLayout.NORTH);

		JButton buttonCalc = new JButton("Вычислить");

		buttonCalc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					Double from = Double.parseDouble(textFieldFrom.getText());
					Double to = Double.parseDouble(textFieldTo.getText());
					Double step = Double.parseDouble(textFieldStep.getText());

					data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);

					JTable table = new JTable(data);
					table.setDefaultRenderer(Double.class, renderer);
					table.setRowHeight(30);

					hboxResult.removeAll();
					hboxResult.add(new JScrollPane(table));

					getContentPane().validate();

					saveToTextMenuItem.setEnabled(true);
					saveToGraphicsMenuItem.setEnabled(true);
					searchValueMenuItem.setEnabled(true);
					searchRangeItem.setEnabled(true);
					saveToCSVFileItem.setEnabled(true);

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(MainFrame.this,
							"Ошибка в формате записи числа с плавающей точкой",
							"Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		JButton buttonReset = new JButton("Очистить поля");

		buttonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				textFieldFrom.setText("0.0");
				textFieldTo.setText("1.0");
				textFieldStep.setText("0.1");

				hboxResult.removeAll();
				hboxResult.add(new JPanel());

				saveToTextMenuItem.setEnabled(false);
				saveToGraphicsMenuItem.setEnabled(false);
				searchValueMenuItem.setEnabled(false);
				searchRangeItem.setEnabled(false);
				saveToCSVFileItem.setEnabled(false);

				getContentPane().validate();

			}
		});

		Box hboxButtons = Box.createHorizontalBox();
		hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
		hboxButtons.add(Box.createHorizontalGlue());
		hboxButtons.add(buttonCalc);
		hboxButtons.add(Box.createHorizontalStrut(30));
		hboxButtons.add(buttonReset);
		hboxButtons.add(Box.createHorizontalGlue());
		hboxButtons.setPreferredSize(new Dimension(
				new Double(hboxButtons.getMaximumSize().getWidth()).intValue(),
				new Double(hboxButtons.getMinimumSize().getHeight()).intValue() * 2));

		getContentPane().add(hboxButtons, BorderLayout.SOUTH);

		hboxResult = Box.createHorizontalBox();
		hboxResult.add(new JPanel());

		getContentPane().add(hboxResult, BorderLayout.CENTER);
	}

	protected void saveToGraphicsFile(File selectedFile) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));

			for (int i = 0; i < data.getRowCount(); i++) {
				out.writeDouble((Double) data.getValueAt(i, 0));
				out.writeDouble((Double) data.getValueAt(i, 1));
				out.writeDouble((Double) data.getValueAt(i, 2));
				out.writeDouble((Double) data.getValueAt(i, 3));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void saveToTextFile(File selectedFile) {
		try {
			PrintStream out = new PrintStream(selectedFile);
			out.println("Результаты табулирования многочлена по схеме Горнера");
			out.println();
			out.print("Многочлен 1: ");

			for (int i = 0; i < coefficients.length; i++) {
				out.print(coefficients[i] + "*X^" + (coefficients.length - 1 - i));
				if (i != coefficients.length - 1) {
					out.print(" + ");
				}
			}

			out.println("");
			out.println("Интервал от " + data.getFrom() + " до " + data.getTo() +
					" с шагом " + data.getStep());

			out.println("====================================================");

			for (int i = 0; i < data.getRowCount(); i++) {
				out.println("Значение в точке " + String.format("%.2f", data.getValueAt(i, 0)) +
						" равно " + String.format("%.2f", data.getValueAt(i, 1)));
			}


			out.println("");
			out.print("Многочлен 2: ");

			for (int i = 0; i < coefficients.length; i++) {
				out.print(coefficients[coefficients.length - i - 1] + "*X^" + (coefficients.length - 1 - i));
				if (i != coefficients.length - 1) {
					out.print(" + ");
				}
			}

			out.println("");
			out.println("Интервал от " + data.getFrom() + " до " + data.getTo() +
					" с шагом " + data.getStep());

			out.println("====================================================");

			for (int i = 0; i < data.getRowCount(); i++) {
				out.println("Значение в точке " + String.format("%.2f", data.getValueAt(i, 0)) +
						" равно " + String.format("%.2f", data.getValueAt(i, 2)));
			}

			out.println("");
			out.println("Разница между 'Многочлен 1' и 'Многочлен 2'");

			for (int i = 0; i < data.getRowCount(); i++) {
				out.println("Значение равно " + String.format("%.2f", data.getValueAt(i, 3)));
			}
			out.close();
		} catch (FileNotFoundException e) {
		}
	}

	protected void saveToCSVFile(File selectedFile) {
		try {
			PrintStream out = new PrintStream(selectedFile);

			for (int i = 0; i < data.getRowCount(); i++) {
				for (int j = 0; j < data.getColumnCount(); j++) {
					out.print(String.format("%.2f", data.getValueAt(i, j)));
					if (j != data.getColumnCount() - 1) {
						out.print(" , ");
					}
				}
				out.println();
			}

			out.close();
		} catch (FileNotFoundException ex) {
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
			System.exit(-1);
		}

		Double[] coefficients = new Double[args.length];
		int i = 0;

		try {
			for (String arg : args) {
				coefficients[i++] = Double.parseDouble(arg);
			}
		} catch (NumberFormatException ex) {
			System.out.println("Ошибка преобразования строки '" + args[i] + "' в число типа Double");
			System.exit(-2);
		}

		MainFrame frame = new MainFrame(coefficients);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}