import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

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
	private static String FRAME_NAME = "File Sharer";
	/*
	 * 
	 */
	private static int FRAME_WIDTH = 800;
	/*
	 * 
	 */
	private static int FRAME_HEIGHT = 600;
	
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
	private JList				peersList;
	/*
	 * 
	 */
	private JList				filesList;
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
		final GUI gui = this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui.build();
			}
		});
	}
	
	/*
	 * 
	 */
	public void build() {
		this.buildContent();
		this.buildFrame();
	}
	
	/*
	 * 
	 */
	public void buildContent() {
		JPanel top = new JPanel(new GridLayout(1, 0));
		JPanel bottom = new JPanel(new GridLayout(1, 1));
		this.setLayout(new BorderLayout());
		this.add(top, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
		
		peersModel = new DefaultListModel();
		filesModel = new DefaultListModel();
		
		Object[] columnNames = {
			"Source",
			"Destination",
			"Name",
			"Progress",
			"State"
		};
		downloadsModel = new CustomTableModel();
		downloadsModel.setColumnIdentifiers(columnNames);
		
		peersList = new JList(peersModel);
		filesList = new JList(filesModel);
		downloadsTable = new JTable(downloadsModel);
		
		TableColumnModel tableColumnModel = downloadsTable.getColumnModel();
		TableColumn progressBarColumn = tableColumnModel.getColumn(3);
		progressBarColumn.setCellRenderer(new CustomCellRenderer());
		
		peersModel.addElement("first peer");
		peersModel.addElement("second peer");
	
		top.add(new JScrollPane(peersList));
		top.add(new JScrollPane(filesList));
		bottom.add(new JScrollPane(downloadsTable));
				
		peersList.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(peersList.getSelectedIndex());
				filesModel.clear();
				String value = (String)peersList.getSelectedValue();
				filesModel.addElement(value+" first file");
				filesModel.addElement(value+" second file");
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		}
		);
		
		filesList.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2)
				{
				
					int rows = downloadsModel.getRowCount();
					
					boolean alreadyDownloading = false;
					
					String peerName = ((String)(peersList.getSelectedValue()));
					String fileName = ((String)(filesList.getSelectedValue()));
					
					for(int i=0; i<rows; i++)
					{
						String presentPeerName = (String)(downloadsModel.getValueAt(i, 0));
						String presentFileName = (String)(downloadsModel.getValueAt(i, 2));
						
						if (presentPeerName.equals(peerName) && presentFileName.equals(fileName))
						{
							alreadyDownloading = true;
							break;
						}
					}
					if (!alreadyDownloading)
					{
						Object[] rowData =
							{
								peerName,
								"__MYSELF__",
								fileName,
								new JProgressBar(0, 10),
								"Receiving"
							};
						downloadsModel.addRow(rowData);
					}
				}
					
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
		});
	}
	
	/*
	 * 
	 */
	public void buildFrame() {
		JFrame frame = new JFrame(FRAME_NAME);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setContentPane(this);
	}
	
	/*
	 * 
	 */
	public void updateProgress() {
		
	}
}
