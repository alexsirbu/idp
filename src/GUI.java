import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

/**
 * @author cristian
 *
 */
public class GUI extends JPanel {
	/*
	 * 
	 */
	private DefaultListModel	peersModel;
	/*
	 * 
	 */
	private DefaultListModel	filesModel;
	/*
	 * 
	 */
	private JList				peersList, filesList;
	/*
	 * 
	 */
	private JTable				downloadsTable;
	/*
	 * 
	 */
	private CustomTableModel	downloadsModel;
	
	/*
	 * 
	 */
	public GUI() {
		init();
	}
	
	/*
	 * 
	 */
	public void init() {
	
	}
	
	/*
	 * 
	 */
	public void updateProgress() {
		
	}
	
	/*
	 * 
	 */
	public static void build(String appName) {
		JFrame frame = new JFrame(appName);
		frame.setContentPane(new GUI());
		frame.setSize(800, 640);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
