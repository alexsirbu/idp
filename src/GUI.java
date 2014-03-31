import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
	private Mediator mediator;
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
	public GUI(Mediator mediator) {
		this.mediator = mediator;
		
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
		
		//peersModel.addElement("first peer");
		//peersModel.addElement("second peer");
	
		top.add(new JScrollPane(peersList));
		top.add(new JScrollPane(filesList));
		bottom.add(new JScrollPane(downloadsTable));
				
		peersList.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				filesModel.clear();
				String peerName = (String)peersList.getSelectedValue();
				
				Peer peer = mediator.getPeerByName(peerName);
				for(int j=0; j<peer.getSharedFiles().size(); j++)
					filesModel.addElement(peer.getSharedFiles().get(j));
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
						Peer peer = mediator.getPeerByName(peerName);
						Peer myself = mediator.getPeerByName(Main.MY_PEER_NAME);
						File file = peer.getSharedFileByName(fileName);
						
						mediator.addTransferRequest(new Transfer(file, peer, myself));
						
						Object[] rowData =
							{
								peerName,
								Main.MY_PEER_NAME,
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
	public void updateTransferProgress(Transfer transfer) {
		int rows = downloadsModel.getRowCount();
		
		for(int i=0; i<rows; i++)
		{
			String currentSenderName = (String)(downloadsModel.getValueAt(i, 0));
			String currentFileName = (String)(downloadsModel.getValueAt(i, 2));
			
			if (currentSenderName.equals(transfer.getSender().getName()) && currentFileName.equals(transfer.getFile().getName()))
			{
				((JProgressBar)downloadsModel.getValueAt(i, 3)).setValue(transfer.getProgress());
				if (transfer.getProgress() == 100)
					downloadsModel.setValueAt("Completed", i, 4);
				break;
			}
		}
	}
	
	/*
	 * 
	 */
	public void addPeer(Peer peer) {
		peersModel.addElement(peer.getName());
	}
	
	/*
	 * 
	 */
	public void deletePeer(Peer peer) {
		if (((String)(peersList.getSelectedValue())).equals(peer.getName()))
			filesModel.clear();		
		
		for(int i=0; i<peersModel.getSize(); i++)
			if (((String)(peersModel.getElementAt(i))).equals(peer.getName()))
			{
				peersModel.remove(i);
				break;
			}
	}
	
	/*
	 * 
	 */
	public void updateFiles(Peer peer) {
		if (((String)(peersList.getSelectedValue())).equals(peer.getName()))
		{
			filesModel.clear();
			
			for(int i=0; i<peer.getSharedFiles().size(); i++)
				filesModel.addElement(peer.getSharedFiles().get(i).getName());
		}
	}
	
	/*
	 * 
	 */
	public void addIncomingTransfer(Transfer transfer) {
		Object[] rowData =
			{
				transfer.getSender().getName(),
				transfer.getReceiver().getName(),
				transfer.getFile().getName(),
				new JProgressBar(0, 10),
				"Sending"
			};
		downloadsModel.addRow(rowData);
	}
}
