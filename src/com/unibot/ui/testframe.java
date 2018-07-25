package com.unibot.ui;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Panel;

import javax.swing.JFrame;

public class testframe extends JFrame {

	/**
	 * 
	 */
	 private Button button;
	  private Panel panel;
	  private Checkbox checkbox;
	  private Button button_1;
	  private Choice choice;
	  public testframe() {
	    initComponents();
	  }
	  private void initComponents() {
	    setBounds(100, 100, 450, 300);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    getContentPane().add(getButton(), BorderLayout.NORTH);
	    getContentPane().add(getPanel(), BorderLayout.CENTER);
	  }
	  private Button getButton() {
	    if (button == null) {
	    	button = new Button("New button");
	    }
	    return button;
	  }
	  private Panel getPanel() {
	    if (panel == null) {
	    	panel = new Panel();
	    	panel.add(getCheckbox());
	    	panel.add(getButton_1());
	    	panel.add(getChoice());
	    }
	    return panel;
	  }
	  private Checkbox getCheckbox() {
	    if (checkbox == null) {
	    	checkbox = new Checkbox("New check box");
	    }
	    return checkbox;
	  }
	  private Button getButton_1() {
	    if (button_1 == null) {
	    	button_1 = new Button("New button");
	    }
	    return button_1;
	  }
	  private Choice getChoice() {
	    if (choice == null) {
	    	choice = new Choice();
	    }
	    return choice;
	  }
	}
