import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.*;

/**
 *
 */
public class GUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
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
	private DefaultListModel<Object> peersModel;
	/*
	 * 
	 */
	private DefaultListModel<Object> filesModel;
	/*
	 * 
	 */
	private CustomTableModel transfersModel;
	/*
	 * 
	 */
	private JList<Object> peersList;
	/*
	 * 
	 */
	private JList<Object> filesList;
	
	/*
	 * 
	 */
	private JTable transfersTable;
	
	
	/*
	 * 
	 */
	public GUI(Mediator mediator) throws Exception {
		this.mediator = mediator;
		
		final GUI gui = this;
		SwingUtilities.invokeAndWait(new Runnable() {
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
		
		peersModel = new DefaultListModel<Object>();
		filesModel = new DefaultListModel<Object>();
		
		transfersModel = new CustomTableModel();
		transfersModel.setColumnIdentifiers(new Object[] {
			"Source",
			"Destination",
			"Name",
			"Progress",
			"State"
		});
		
		peersList = new JList<Object>(peersModel);
		filesList = new JList<Object>(filesModel);
		transfersTable = new JTable(transfersModel);
		
		TableColumnModel tableColumnModel = transfersTable.getColumnModel();
		TableColumn progressBarColumn = tableColumnModel.getColumn(3);
		progressBarColumn.setCellRenderer(new CustomCellRenderer());
	
		top.add(new JScrollPane(peersList));
		top.add(new JScrollPane(filesList));
		
		bottom.add(new JScrollPane(transfersTable));
				
		peersList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String peerName = (String)peersList.getSelectedValue();
				if (peerName.equals(Main.LOCAL_PEER_NAME))
					peerName=mediator.getLocalPeerRealName();
				Peer peer = mediator.getPeer(peerName);
				
				filesModel.clear();
				for(int j = 0; j < peer.getSharedFiles().size(); j++)
					filesModel.addElement(peer.getSharedFiles().get(j).getName());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		
		filesList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)
				{
				
					int rows = transfersModel.getRowCount();
					
					boolean alreadyDownloading = false;
					
					String peerName = ((String)(peersList.getSelectedValue()));
					String fileName = ((String)(filesList.getSelectedValue()));
					if (peerName.equals(Main.LOCAL_PEER_NAME))
						peerName=mediator.getLocalPeerRealName();
					
					for(int i = 0; i < rows; i++)
					{
						String presentPeerName = (String)(transfersModel.getValueAt(i, 0));
						String presentFileName = (String)(transfersModel.getValueAt(i, 2));
						
						if (presentPeerName.equals(Main.LOCAL_PEER_NAME))
							presentPeerName=mediator.getLocalPeerRealName();
						
						if (presentPeerName.equals(peerName) && presentFileName.equals(fileName))
						{
							alreadyDownloading = true;
							break;
						}
					}
					
					if(!alreadyDownloading && !peerName.equals(mediator.getLocalPeerRealName()))
					{						
						Peer peer = mediator.getPeer(peerName);
						Peer myself = mediator.getLocalPeer();
						File file = peer.getSharedFile(fileName);
						
						transfersModel.addRow(new Object[] {
							peerName,
							Main.LOCAL_PEER_NAME,
							fileName,
							new JProgressBar(0, 100),
							"Receiving"
						});
						
						mediator.addTransferOutgoingRequest(new Transfer(file, peer, myself));
					}
				}
					
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
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
	public void addPeer(Peer peer) {
		assert peersModel != null;
		if (peer.getName().equals(this.mediator.getLocalPeerRealName()))
			peersModel.addElement(Main.LOCAL_PEER_NAME);
		else
			peersModel.addElement(peer.getName());
	}
	
	/*
	 * 
	 */
	public void deletePeer(Peer peer) {
		if(peersList.getSelectedValue() != null
			&& ((String)(peersList.getSelectedValue())).equals(peer.getName()))
			filesModel.clear();		
		
		for(int i = 0; i < peersModel.getSize(); i++)
		{
			if(((String)(peersModel.getElementAt(i))).equals(peer.getName()))
			{
				peersModel.remove(i);
				break;
			}
		}
	}
	
	/*
	 * 
	 */
	public void updatePeerFiles(Peer peer) {
		
		if(peersList.getSelectedValue() != null
				&& ((String)(peersList.getSelectedValue())).equals(peer.getName()))
		{
			filesModel.clear();
			
			for(int i = 0; i < peer.getSharedFiles().size(); i++)
				filesModel.addElement(peer.getSharedFiles().get(i).getName());
		}
	}
	
	/*
	 * 
	 */
	public void addTransferIncomingRequest(Transfer transfer) {
		
		String sending = transfer.getSendingPeer().getName();
		String receiving = transfer.getReceivingPeer().getName();
		
		if (sending.equals(mediator.getLocalPeerRealName()))
			sending=Main.LOCAL_PEER_NAME;
		
		if (receiving.equals(mediator.getLocalPeerRealName()))
			receiving=Main.LOCAL_PEER_NAME;
			
		transfersModel.addRow(new Object[] {
			sending,
			receiving,
			transfer.getFile().getName(),
			new JProgressBar(0, 100),
			"Sending"
		});
	}
	
	/*
	 * 
	 */
	public void updateTransferProgress(Transfer transfer) {
		int rows = transfersModel.getRowCount();
		
		for(int i = 0; i < rows; i++)
		{
			String currentSenderName = (String)(transfersModel.getValueAt(i, 0));
			String currentReceiverName = (String)(transfersModel.getValueAt(i, 1));
			String currentFileName = (String)(transfersModel.getValueAt(i, 2));
			
			if (currentSenderName.equals(Main.LOCAL_PEER_NAME))
				currentSenderName=this.mediator.getLocalPeerRealName();
			
			if (currentReceiverName.equals(Main.LOCAL_PEER_NAME))
				currentReceiverName=this.mediator.getLocalPeerRealName();
			
			if(currentSenderName.equals(transfer.getSendingPeer().getName())
					&& currentReceiverName.equals(transfer.getReceivingPeer().getName())
					&& currentFileName.equals(transfer.getFile().getName()))
			{	
				final JProgressBar bar = (JProgressBar)transfersModel.getValueAt(i, 3);
				final Transfer ftransfer = transfer;
				final GUI fgui = this;
				
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						bar.setValue(ftransfer.getProgress());
						fgui.transfersTable.repaint();
					}
					
				});
				if (transfer.getProgress() == 100)
					transfersModel.setValueAt("Completed", i, 4);
				break;
			}
		}
	}
}
