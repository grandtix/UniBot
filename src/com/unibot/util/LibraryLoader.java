package com.unibot.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/*

 <BlockGenus name="setter_variable_string" kind="command"
 color="35 0 220" initlabel="bg.setter_variable_string">
 <description>
 <text>set a number variable</text>
 </description>
 <BlockConnectors>
 <BlockConnector connector-type="string"
 connector-kind="socket" label="bc.variable">
 <DefaultArg genus-name="variable_string" label="mystring" />
 </BlockConnector>
 <BlockConnector connector-type="string"
 connector-kind="socket" label="bc.value">
 <DefaultArg genus-name="string" label="texte" />
 </BlockConnector>
 </BlockConnectors>
 </BlockGenus>


 <BlockGenus name="variable_number" kind="data"
 initlabel="bg.variable_number" editable-label="yes" is-label-value="yes"
 color="0 71 255">
 <description>
 <text>
 Digital Var
 </text>
 </description>
 <BlockConnectors>
 <BlockConnector connector-type="number"
 connector-kind="plug" position-type="mirror" />
 </BlockConnectors>
 </BlockGenus>

 <BlockGenus name="greater" kind="function" color="255 255 102"
 initlabel="bg.greater">
 <description>
 <text>is upper number greater then lower?</text>
 </description>
 <BlockConnectors>
 <BlockConnector connector-type="boolean"
 connector-kind="plug" position-type="mirror" />
 <BlockConnector connector-type="number"
 connector-kind="socket" position-type="bottom" />
 <BlockConnector connector-type="number"
 connector-kind="socket" position-type="bottom" />
 </BlockConnectors>
 </BlockGenus>

 */

public class LibraryLoader {

	boolean isInPublic = false;

	private String file;
	private String className;
	StringBuffer contenuFichier = new StringBuffer();
	StringBuffer contenuFamily = new StringBuffer();
	boolean firstGroup = true;
	ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();

	// private boolean asGroup = false;

	private int current = -1;

	private boolean firstConstructor = true;

	private boolean inComment = false;

	ArrayList<String> getTypes(String text) {
		ArrayList<String> liste = new ArrayList<String>();
		System.out.println("get type in text: " + text);
		String[] types = text.trim().split(",");
		if (text.trim().length() > 0)
			for (String t : types) {
				t=t.replace("unsigned ", "");
				String tmp = t.trim().split(" ")[0];
				String lbl = "";
				if (t.trim().contains(" "))
					lbl = t.trim().split(" ")[1];
				System.out.println("get type: " + tmp);
				if (tmp.equals("int") || tmp.equals("float") || tmp.equals("double") )
					tmp = "number/" + lbl+"/number";
				else if( tmp.equals("byte"))
					tmp = "byte/" + lbl+"/boolean-list";
				else if( tmp.equals("char"))
					tmp = "char/" + lbl+"/string";
				else if( tmp.equals("long"))
					tmp = "long/" + lbl+"/number-list";
				else if (tmp.equals("String") )
					tmp = "string/" + lbl+"/string-list";
				else if (tmp.equals("bool") || tmp.equals("boolean"))
					tmp = "boolean/" + lbl+"/boolean";
				else
					tmp = tmp + "/" + lbl+"/"+tmp;
				liste.add(tmp);
				// System.out.println("\t\tparameter : " +
				// liste.get(liste.size() - 1) + " ");
			}
		return liste;
	}

	String createBlockGenus(String template_type, String name, String label,  String typeGenus, String typeOutput,
			ArrayList<String> typesInput, boolean isconstructor, String description, String imagepath) {
		return createBlockGenus(template_type, name, label,  typeGenus, typeOutput, typesInput, isconstructor, false,
				description, imagepath);

	}


	String createBlockGenus(String template_type, String name, String label,  String typeGenus, String typeOutput,
			ArrayList<String> typesInput, boolean isconstructor, boolean isPlug, String description, String imagepath) {
		StringBuffer string = new StringBuffer();
		String typename = "";

		String couleur = 255 * (-97 + (int) (groups.get(current).get(0).toLowerCase().charAt(0))) / 26 + " "
				+ 255 * (-97 + (int) (groups.get(current).get(0).toLowerCase().charAt(1))) / 26 + " "
				+ 255 * (-97 + (int) (groups.get(current).get(0).toLowerCase().charAt(2))) / 26;

		// contenuFamily.append("<BlockGenusMember>");
		// contenuFamily.append(typename+name);
		// contenuFamily.append("</BlockGenusMember>\n");

		// ------------create BLOCKGENUS

		if (template_type.equals("constructeur")) {
			typename = "nouveau ";
			// System.out.println(typename +
			// name+"=com.unibot.translator.block.CustomConstructorBlock");
			PropertiesReader.addValue(typename + name, "com.unibot.translator.block.CustomConstructorBlock");
			string.append("<BlockGenus name=\"" + typename + name + "\" kind=\"" + typeGenus + "\"   color=\"" + couleur
					+ "\" editable-label=\"no\" initlabel=\"" + typename + label + "\" label-unique=\"no\" >"); //
		} else if (template_type.equals("method")) {
			// System.out.println(typename +
			// name+"=com.unibot.translator.block.CustomCommandBlock");
			PropertiesReader.addValue(name, "com.unibot.translator.block.CustomCommandBlock");
			string.append("<BlockGenus name=\"" + name + "\" kind=\"" + typeGenus + "\"   color=\"" + couleur
					+ "\"  editable-label=\"no\" initlabel=\"" + label + "\" label-unique=\"no\" >");

		} else if (template_type.equals("variable") || template_type.equals("instanceClasse")) {
			// System.out.println(typename +
			// name+"=com.unibot.translator.block.CustomVariableBlock");
			PropertiesReader.addValue(name, "com.unibot.translator.block.CustomVariableBlock");
			string.append("<BlockGenus name=\"" + name + "\" kind=\"" + typeGenus + "\"   color=\"" + couleur
					+ "\" initlabel=\"" + label + "\" editable-label=\"yes\"  label-unique=\"no\">");

		} else if (template_type.equals("methodreturn")) {
			// System.out.println(typename +
			// name+"=com.unibot.translator.block.CustomVariableBlock");
			PropertiesReader.addValue(name, "com.unibot.translator.block.CustomVariableBlock");
			string.append("<BlockGenus name=\"" + name + "\" kind=\"" + typeGenus + "\"   color=\"" + couleur
					+ "\" initlabel=\"" + label + "\" editable-label=\"no\" label-unique=\"no\">");

		}

		// ---------create DESCRIPTION

		string.append("\n");
		string.append("<description>\n<text>");
		string.append(description.equals("") ? this.file : description);
		string.append("</text></description>");

		// --------create BLOCKCONNECTORS
		string.append("<BlockConnectors>");
		string.append("\n");

		// socket ->emplacement pour insererdesblocks
		// plug -> ce block vient se coller à un autre

		// si ce block se PLUG
		if (template_type.equals("variable") || template_type.equals("instanceClasse")
				|| template_type.equals("methodreturn")) {
			String type = "poly";
			if (typeOutput.equals("float") || typeOutput.equals("int") || typeOutput.equals("double")
					|| typeOutput.equals("byte"))
				type = "number";
			if (typeOutput.equals("String") || typeOutput.equals("string"))
				type = "string-list";
			if (typeOutput.equals("char"))
				type = "string";
			if (typeOutput.equals("byte"))
				type = "boolean-list";
			if (typeOutput.equals("boolean") || typeOutput.equals("Boolean") || typeOutput.equals("bool"))
				type = "boolean";

			string.append("<BlockConnector connector-type=\"" + type
					+ "\" connector-kind=\"plug\" position-type=\"mirror\" label-editable=\"yes\" is-expandable=\"true\" />\n");
		}

		if (!template_type.equals("instanceClasse")) {

			String position = "";
			if (!template_type.equals("constructeur"))
				position = " position-type=\"bottom\"";
			else
				position = " position-type=\"SINGLE\"";

			// -----------create instance de classe dans le socket bottom
			string.append(
					"<BlockConnector connector-type=\"poly\"  is-expandable=\"true\"  label-editable=\"yes\" connector-kind=\"socket\""
							+ position + " >");
			string.append("\n");
			string.append("<DefaultArg genus-name=\"" + className + "\" label=\"" + className.toLowerCase()  
					
					+ "\" editable-label=\"yes\"/>");
			string.append("\n");
			string.append("</BlockConnector>");
			string.append("\n");

			// ---------ajout des parametres dans les sockets
			int inc = 0;

			if (typesInput != null)
				for (String it : typesInput) {
					if (!(template_type.equals("constructeur") && inc == 0)) {
						string.append("<BlockConnector connector-type=\"");

						string.append(it.split("/")[2]);
						string.append("\" connector-kind=\"socket\" position-type=\"SINGLE\" label=\"");
						string.append(it.split("/").length >1 ? it.split("/")[1] : "");
						string.append("\" label-editable=\"false\"  is-expandable=\"true\" >");
						string.append("\n");
						string.append("<DefaultArg genus-name=\"");
						if (it.equals("boolean"))
							string.append("true\" label=\"VRAI\"");
						else
							string.append(it.split("/")[0] + "\" label=\"0\"");
						
						string.append( "  />");
						string.append("\n");
						string.append("</BlockConnector>");
						string.append("\n");
					}
					inc++;

				}
		}
		string.append("</BlockConnectors>");
		string.append("\n");

		if (!imagepath.equals("")) {
			string.append("<Images>\n");

			string.append("<Image block-location=\"center\" image-editable=\"no\" wrap-text=\"no\""
					// + " image-editable=\"no\" wrap-text=\"yes\""
					+ ">");
			string.append("<FileLocation>");
			string.append(imagepath);
			string.append("</FileLocation>\n");
			string.append("</Image>\n");
			string.append("</Images>\n");
		}

		string.append("</BlockGenus>");
		string.append("\n");
		groups.get(current).add("<BlockGenusMember>" + typename + name + "</BlockGenusMember>\n");

		return string.toString();
	}

	void readFile(String file) {

		this.file = file;
		contenuFichier.append("<GenusMembers>");
		contenuFichier.append("\n");

		className = new File(file).getName().replace(".h", "");

		String imagePath = "";
		String description = "";
		BufferedReader br;
		ArrayList<String> listNames = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			int idLine = 0;
			int currentIdBalise = 1;
			while ((line = br.readLine()) != null) {
				// process the line.
				line = line.trim();
				while (line.contains("  ")) {
					line = line.replace("  ", " ");
				}
				idLine++;

				if (line.startsWith("/*"))
					inComment = true;

				if (line.length() > 0 && !inComment) {
					if (line.contains("public:")) {
						isInPublic = true;

					}
					if (line.contains("private:") || line.contains("protected:")) {
						isInPublic = false;

					}
					if (isInPublic) {
						String name = "";

						// si c'est une metabalise
						if (line.startsWith("//@")) {
							description="";
							imagePath=null;
							if (line.replace("//@", "").startsWith("bloc")) {
								String[] maligne = (line.replace("//@bloc", "").replace("png=", "#png=")
										.replace("texte=", "#texte=").trim()).split("#");
								currentIdBalise = idLine;
								for (int i = 0; i < maligne.length; i++) {
									if (maligne[i].split("=")[0].equals("png"))
										imagePath = new File(file).getParentFile().getAbsolutePath() + "/img/"
												+ maligne[i].split("=")[1];
									if (maligne[i].split("=")[0].equals("texte"))
										description = maligne[i].split("=")[1];
									// @bloc texte="ms" png="delay.png"
								}

							} else {
								current++;
								groups.add(new ArrayList<String>());
								groups.get(current).add(line.replaceAll("//@", ""));
							}

						} else {

							if (!line.startsWith("//")) {
								if (line.startsWith("static"))
									line=line.replace("static", "");
								if (line.startsWith("const"))
									line=line.replace("const", "");
								if (line.startsWith("unsigned"))
									line=line.replace("unsigned", "");
								
								// si c'est un constructeur ou une methode
								if (line.contains("(")) {
									if (!line.substring(0, line.indexOf('(')).contains(" ")) {// is
																								// constructor
										name = line.substring(0, line.indexOf('('));
										line = line.replace(name, "");
										line = line.replace("(", "");
										line = line.replace(")", "");
										line = line.replace(";", "");
										line = line.trim();
										ArrayList<String> typesInput = new ArrayList<String>();
										typesInput.add(className);

										System.out.println("types input: " + line);
										typesInput.addAll(getTypes(line));
										contenuFichier.append(createBlockGenus("constructeur", name + "()",name + "()", "command",
												null, typesInput, true, description,
												idLine == currentIdBalise + 1 ? imagePath : ""));
										if (firstConstructor) {
											contenuFichier.append(createBlockGenus("instanceClasse", className,className, "data",
													"object", null, false, true,
													idLine == currentIdBalise + 1 ? ("objet de type "+name) : "", ""));

											// groups.get(current).add(className);
											firstConstructor = false;

										}

									} else if (line.startsWith("void")) {
										boolean ok = true;
										int i = 0;

										name = line.substring(0, line.indexOf('(')).replaceFirst("void ", "").trim();
										for (String str : listNames) {
											if (str.trim().equals(name)) {
												ok = false;
												
											}

										}
										String nametemp=name;
										while (!ok) {
											ok = true;
											
											for (String str : listNames) {
												if (str.trim().equals(nametemp)) {
													ok = false;
													
												}
												
											}
											nametemp = name + "" + i;
												i++;
											
										}

										listNames.add(nametemp);
										System.out.println("nametemp void : "+nametemp);
										line = line.substring(line.indexOf('(') + 1);
										line = line.replace("(", "");
										line = line.replace(")", "");
										line = line.replace(";", "");
										line = line.trim();
										ArrayList<String> typesInput = getTypes(line);
										contenuFichier.append(createBlockGenus("method", nametemp,name, "command", null,
												typesInput, false, idLine == currentIdBalise + 1 ? description : name,
												idLine == currentIdBalise + 1 ? imagePath : ""));

									} else {
										String type = line.substring(0, line.indexOf(" "));
										

										boolean ok = true;
										int i = 0;
										name = line.substring(line.indexOf(" "), line.indexOf('(')).trim();
										String nametemp=name;
										for (String str : listNames) {
											if (str.trim().equals(name)) {
												ok = false;
												
											}
											
										}
										while (!ok) {
											ok = true;
											nametemp = name + "" + i;
											for (String str : listNames) {
												if (str.trim().equals(nametemp)) {
													ok = false;
													i++;
												}

											}
											nametemp = name + "" + i;
											i++;
										}

										listNames.add(nametemp);							

										System.out.println("nametemp other :" + nametemp);
										line = line.substring(line.indexOf("(") + 1);
										line = line.replace("(", "");
										line = line.replace(")", "");
										line = line.replace(";", "");
										line = line.trim();
										System.out.println("types inpu34t: " + type);
										if (!(type.equals("int") || type.equals("float") || type.equals("String")
												|| type.equals("byte") || type.equals("char") || type.equals("boolean")
												|| type.equals("bool")))
											type = "poly";
										System.out.println("typeApres: " + type + " from line: " + line);

										ArrayList<String> typesInput = getTypes(line);
										contenuFichier.append(createBlockGenus("methodreturn", nametemp,name, "data", type,
												typesInput, false, idLine == currentIdBalise + 1 ? description : name,
												idLine == currentIdBalise + 1 ? imagePath : ""));

									}

								} else if (line.trim().contains(" ") && line.trim().contains(";")) {
									// c'est un attribut de classe
									String type = line.substring(0, line.indexOf(" "));
									if (!(type.equals("int") || type.equals("float") || type.equals("String")
											|| type.equals("byte") || type.equals("char") || type.equals("boolean")
											|| type.equals("bool")))
										type = "poly";
									if (line.contains("="))
										name = line.substring(0, line.indexOf("=")).replace(type, "").trim();
									else
										name = line.replace(";", "").replace(type, "").trim();

									contenuFichier.append(createBlockGenus("variable", name, name,"data", type, null, false,
											idLine == currentIdBalise + 1 ? description : "variable de l'objet "+name,
											idLine == currentIdBalise + 1 ? imagePath : ""));
								}
							}
						}
					}
				}
				if (line.trim().contains("*/"))
					inComment = false;
			}
			br.close();
			contenuFichier.append("</GenusMembers>");
			contenuFichier.append("\n");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setEntete() {
		contenuFichier.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		contenuFichier.append("\n");
		contenuFichier.append("<BlockLangDef>");
		contenuFichier.append("\n");

	}

	void setpied() {
		contenuFichier.append("<BlockDrawerSets>");
		contenuFichier.append("\n");
		contenuFichier.append(
				"<BlockDrawerSet name=\"factory\" type=\"stack\" location=\"southwest\" window-per-drawer=\"no\" drawer-draggable=\"no\">");
		contenuFichier.append("\n");

		//
		Iterator<ArrayList<String>> it = groups.iterator();
		int i = 0;
		while (it.hasNext()) {
			ArrayList<String> liste = (ArrayList<String>) it.next();

			// System.out.println(groups.get(i).get(0).toLowerCase().charAt(0));

			String couleur = ((groups.get(i).get(0).toLowerCase().charAt(0) - 92) % 26) * 256 / 26 + " "
					+ ((groups.get(i).get(0).toLowerCase().charAt(1) - 92) % 26) * 256 / 26 + " "
					+ ((groups.get(i).get(0).toLowerCase().charAt(2) - 92) % 26) * 256 / 26;
			i++;
			contenuFichier.append(
					"<BlockDrawer button-color=\"" + couleur + "\" name=\"" + className + " - " + liste.get(0) + "\">");
			contenuFichier.append("\n");
			Iterator<String> it2 = liste.iterator();
			it2.next();
			while (it2.hasNext()) {
				contenuFichier.append(it2.next());
				contenuFichier.append("\n");
			}
			contenuFichier.append("</BlockDrawer>");
			contenuFichier.append("\n");
		}
		//
		contenuFichier.append("</BlockDrawerSet>");
		contenuFichier.append("\n");

		contenuFichier.append("</BlockDrawerSets>");
		contenuFichier.append("\n");
		contenuFichier.append("</BlockLangDef>");
		contenuFichier.append("\n");

	}

	public InputStream toInputStream(String name) {

		setEntete();
		readFile(name);
		setpied();
		// System.out.println(contenuFichier);

		byte[] bytes = contenuFichier.toString().getBytes();

		/*
		 * Get ByteArrayInputStream from byte array.
		 */
		System.out.println(contenuFichier.toString());
		return new ByteArrayInputStream(bytes);
	}

	public LibraryLoader() {
		// TODO Auto-generated constructor stub

	}

}
