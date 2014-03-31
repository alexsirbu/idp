import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel {
	
	private DefaultListModel	peersModel, filesModel;										// list model
	private JList				peersList, filesList;						
	private JTable				downloadsTable;
	private CustomTableModel	downloadsModel;
	
	public Main() {
		init();
	}
	
	public void init() {
		
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
		
		peersModel.addElement("first peer");
		peersModel.addElement("second peer");
	
		top.add(new JScrollPane(peersList));
		top.add(new JScrollPane(filesList));
		bottom.add(new JScrollPane(downloadsTable));
		
		peersList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
			
				System.out.println(peersList.getSelectedIndex());
				filesModel.clear();
				String value = (String)peersList.getSelectedValue();
				filesModel.addElement(value+" first file");
				filesModel.addElement(value+" second file");
				
				
			}
		}
		);
		
		filesList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				
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
							"none",
							"Receiving"
						};
					downloadsModel.addRow(rowData);
				}
				
			}
			
		});
		
		/*
		// initialize model
		model = new DefaultListModel();
		
		// DONE TODO 1: populate model
		model.addElement("MP");
		model.addElement("SO");
		model.addElement("IDP");
		
		// initialize lists, based on the same model
		list	= new JList(model);
		mirror	= new JList(new ReverseListModel(model));
		
		// TODO 6: redefine mirror so as to use a ReverseListModel instance on top of 'model'
		
		// main panel: top panel, bottom panel
		JPanel top = new JPanel(new GridLayout(1, 0)); // 1 row, any number of columns
		JPanel bottom = new JPanel(new FlowLayout());
		this.setLayout(new BorderLayout());
		this.add(top, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
		
		// top panel: the two lists (scrollable)
		top.add(new JScrollPane(list));
		top.add(new JScrollPane(mirror));
		
		// bottom panel: name field, add button, remove button
		bottom.add(tName);
		bottom.add(bAdd);
		bottom.add(bRemove);
		bottom.add(bUndo);
		bottom.add(bRedo);

		
		final UndoManager manager = new UndoManager();
		
		bUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//done TODO 2: call the metmovehod for obtaining the text field's content
				manager.undo();
			}
		});
		
		bRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//done TODO 2: call the metmovehod for obtaining the text field's content
				manager.redo();
			}
		});
		
		bAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//done TODO 2: call the metmovehod for obtaining the text field's content
				String text = "";
				text = tName.getText();
				if (text.isEmpty()) {
					JOptionPane.showMessageDialog(
							null, "Name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// done - TODO 3: add new element to model
				if (!model.contains(text))
					model.addElement(text);
				manager.addCommand(new UndoAdd(model, model.indexOf(text), text));
			}
		});
		
		bRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = "";
				
				text = tName.getText();
				if (list.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(
							null, "No element is selected", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				manager.addCommand(new UndoRemove(model, list.getSelectedIndex(), model.elementAt(list.getSelectedIndex())));
				model.removeElement(model.elementAt(list.getSelectedIndex()));

			}
		});
		
		// TODO 4: add listener for Remove button
		*/
	}
	
	public static void buildGUI() {
		JFrame frame = new JFrame("IDP File Sharing");
		frame.setContentPane(new Main());
		frame.setSize(800, 640);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	public static void main(String[] args) {
		// run on EDT (event-dispatching thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buildGUI();
			}
		});
	}

}
