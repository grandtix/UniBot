package com.unibot.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.unibot.core.Context;

public class ManageLibraries extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8420461817119819969L;
	private final Action actionSuppLib = new SwingActionSuppLib();
	private final Action actionAddLib = new SwingActionAddLib();
	private JTable table;
	private JFrame parentFrame;
	public String nomLibToAdd;
	public String pathLibToAdd;
	private final Action action = new SwingAction();
	protected boolean changed = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ManageLibraries dialog = new ManageLibraries(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * 
	 * @param context
	 * @param parentFrame
	 */
	public ManageLibraries(JFrame _parentFrame, Context context) {
		// setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		setTitle("gestion des librairies");
		this.parentFrame = _parentFrame;
		setBounds(100, 100, 600, 400);
		
		

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 100, 300, 80, 0 };
		gridBagLayout.rowHeights = new int[] { 226, 35, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0,Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		DefaultTableModel tblModel = new DefaultTableModel(0, 3);
		tblModel.setColumnIdentifiers(new String[] { "nom", "chemin", "activé" });
		table = new JTable(tblModel) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3779756050940865671L;

			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;

				default:
					return Boolean.class;
				}
			}

			public boolean isCellEditable(int row, int column) {
				int modelColumn = convertColumnIndexToModel(column);

				if (modelColumn == 2) {
					changed = true;

					return true;
				} else
					return super.isCellEditable(row, column);
			}

		};
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null,
				null));

		JScrollPane scrollPane = new JScrollPane(table);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		getContentPane().add(scrollPane, gbc_scrollPane);

		JButton btnImporter = new JButton("importer");
		GridBagConstraints gbc_btnImporter = new GridBagConstraints();
		gbc_btnImporter.fill = GridBagConstraints.BOTH;
		gbc_btnImporter.insets = new Insets(5, 5, 5, 5);
		gbc_btnImporter.gridx = 0;
		gbc_btnImporter.gridy = 1;
		getContentPane().add(btnImporter, gbc_btnImporter);
		btnImporter.setAction(actionAddLib);

		JButton btnSupprimer = new JButton("supprimer");
		GridBagConstraints gbc_btnSupprimer = new GridBagConstraints();
		gbc_btnSupprimer.fill = GridBagConstraints.BOTH;
		gbc_btnSupprimer.insets = new Insets(5, 5, 5, 5);
		gbc_btnSupprimer.gridx = 1;
		gbc_btnSupprimer.gridy = 1;
		getContentPane().add(btnSupprimer, gbc_btnSupprimer);
		btnSupprimer.setAction(actionSuppLib);
		JButton okButton = new JButton("OK");
		GridBagConstraints gbc_okButton = new GridBagConstraints();
		gbc_okButton.insets = new Insets(5, 5, 5, 5);
		gbc_okButton.fill = GridBagConstraints.BOTH;
		gbc_okButton.gridx = 2;
		gbc_okButton.gridy = 1;
		getContentPane().add(okButton, gbc_okButton);
		okButton.setAction(action);
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);

		fillTable();

	}

	void fillTable() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		BufferedReader br;
		try {
			File file = new File(((OpenblocksFrame) parentFrame).getPathConf());
			if (!file.exists())
				file.createNewFile();

			br = new BufferedReader(new FileReader(((OpenblocksFrame) parentFrame).getPathConf()));
			String line;
	
			while ((line = br.readLine()) != null) {
				// process the line.
				line = line.trim();
				model.addRow(new Object[] { line.split("=")[0],
						line.split("=")[1],
						line.split("=")[2].equals("true") ? true : false });

			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
	 e.printStackTrace();
		}
	}

	private class SwingActionSuppLib extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7499123042258833729L;

		public SwingActionSuppLib() {
			putValue(NAME, "supprimer");
			putValue(SHORT_DESCRIPTION,
					"décharge la librairie de l'application");
		}

		public void actionPerformed(ActionEvent e) {

			String libToSupp = (String) table.getValueAt(
					table.getSelectedRow(), 0);

			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(((OpenblocksFrame) parentFrame).getPathConf()));

				String line;
				String alllines = "";
				while ((line = br.readLine()) != null) {
					// process the line.
					line = line.trim();
					if (!libToSupp.equals(line.split("=")[0])
							&& !line.equals("") && line.length() != 0)
						alllines = alllines + line + "\n";
				}
				br.close();

				BufferedWriter output = new BufferedWriter(new FileWriter(
						((OpenblocksFrame) parentFrame).getPathConf(), false));
				System.out.println(" a la suppression " + alllines);
				output.append(alllines);
				output.close();

				DefaultTableModel model = (DefaultTableModel) table.getModel();

				model.removeRow(table.getSelectedRow());
				JOptionPane
						.showMessageDialog(null,
								"Vous devez relancer UniBot pour que les changements prennent effet");

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	private class SwingActionAddLib extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 949646669620057431L;

		public SwingActionAddLib() {
			putValue(NAME, "importer une librairie");
			putValue(SHORT_DESCRIPTION,
					"genere les blocks d'une librairie (.h)");
		}

		public void actionPerformed(ActionEvent e) {

			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter ffilter = new FileNameExtensionFilter(
					"fichier d'entête (c/c++)", "h");

			fileChooser.setFileFilter(ffilter);
			int result = fileChooser.showOpenDialog(ManageLibraries.this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File savedFile = fileChooser.getSelectedFile();
				nomLibToAdd = savedFile.getName();
				pathLibToAdd = savedFile.getAbsolutePath();

				boolean toAppend = true;
				BufferedReader br;
				try {

					File file = new File(((OpenblocksFrame) parentFrame).getPathConf());
					if (!file.exists())
						file.createNewFile();

					br = new BufferedReader(new FileReader(((OpenblocksFrame) parentFrame).getPathConf()));
					String line;
			
					while ((line = br.readLine()) != null) {
						// process the line.
						line = line.trim();

						if (nomLibToAdd.equals(line.split("=")[0]))
							toAppend = false;
					}
					br.close();
					if (toAppend) {

						BufferedWriter output = new BufferedWriter(
								new FileWriter(file, true));
						output.append(nomLibToAdd + "=" + pathLibToAdd
								+ "=true");
						output.newLine();
						output.close();
						DefaultTableModel model = (DefaultTableModel) table
								.getModel();
						model.addRow(new Object[] { nomLibToAdd, pathLibToAdd,
								true });
						((OpenblocksFrame) parentFrame).getFile(pathLibToAdd);

					}
				}

				catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
	}

	private class SwingAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3124449878498561447L;

		public SwingAction() {
			putValue(NAME, "Fermer");
			putValue(SHORT_DESCRIPTION, "Fermer la boite de dialogue");
		}

		public void actionPerformed(ActionEvent e) {

		
			try {

				File file = new File(((OpenblocksFrame) parentFrame).getPathConf());
				if (file.exists())
					file.delete();

				file.createNewFile();

				BufferedWriter output = new BufferedWriter(new FileWriter(file,
						false));
				for (int i = 0; i < table.getRowCount(); i++) {
/*
					System.out.println(table.getValueAt(i, 0) + "="
							+ table.getValueAt(i, 1) + "="
							+ table.getValueAt(i, 2));
					System.out.println("----" + table.getValueAt(i, 0));
					System.out.println("----" + table.getValueAt(i, 1));
					System.out.println("----" + table.getValueAt(i, 2));*/
					output.append(table.getValueAt(i, 0) + "="
							+ table.getValueAt(i, 1) + "="
							+ table.getValueAt(i, 2));
					output.newLine();
				}
				output.close();

			}

			catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (changed)
				JOptionPane
						.showMessageDialog(null,
								"Vous devez relancer UniBot pour que les changements prennent effet");

			// ((OpenblocksFrame)
			// parentFrame).context.getWorkspaceController().loadFreshWorkspace();
			// ((OpenblocksFrame) parentFrame).loadLibs();
			dispose();
		}
	}
}
