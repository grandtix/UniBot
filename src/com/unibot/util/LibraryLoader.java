package com.unibot.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

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

	boolean isInPublic = true;

	private String file;
	private String className;
	StringBuffer contenuFichier = new StringBuffer();
	StringBuffer contenuFamily = new StringBuffer();
	boolean firstGroup = true;
	ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();
	boolean isClass = false;
	// private boolean asGroup = false;

	private int current = -1;

	private boolean firstConstructor = true;

	private boolean inComment = false;
	String groupConstantes = "constantes";
	String groupVarFunctions="variables et fonctions";

	Map<String, String> connectorMap = new HashMap<String, String>() {
		{
			put("number", "number"); // 1 fumble per 100 hand-offs
			put("int", "number"); // 1 fumble per 100 hand-offs
			put("long", "number");
			put("double", "number");
			put("boolean", "boolean");
			put("char", "string");
			put("float", "number");
			put("String", "string-list");
			put("string", "string-list");
			put("char *", "string-list");
			put("byte", "boolean-list");
			put("object", "poly");

		}
	};;

	Map<String, String> aliasGeniusTypeMap = new HashMap<String, String>() {
		{
			put("int", "number"); // 1 fumble per 100 hand-offs
			put("long", "number");
			put("double", "number");
			put("boolean", "boolean");
			put("char", "string");
			put("float", "number");
			put("String", "string");
			put("char *", "string");
			put("byte", "byte");
			put("uint8_t", "number");
			put("uint16_t", "number");
			put("uint32_t", "number");
			put("object", "object");
		}
	};
	Map<String, String> externObjectGenius = new HashMap<String, String>();

	private boolean isDefine = false;

	private String lastname = "";

	private boolean hasContructor = false;

	private ArrayList<Boolean> useNoInstance = new ArrayList<Boolean>();

	ArrayList<String> getTypes(String text) {
		ArrayList<String> liste = new ArrayList<String>();
		text = text.replace(", ", ",");
		String[] types = text.trim().split(",");
		// System.out.println("text :" + text);
		if (text.trim().length() > 0)
			for (String t : types) {
				String tmp = t.trim().split(" ")[0];
				String lbl = "";
				// System.out.println("tmp1 :" + tmp);
				if (!tmp.startsWith("void")) {
					if (t.contains(" "))
						lbl = t.trim().split(" ")[1];
					String ty = "";
					if (aliasGeniusTypeMap.get(tmp) != null)
						ty = aliasGeniusTypeMap.get(tmp);
					else {
						contenuFichier.append(createBlockGenus("variable", tmp, tmp.toLowerCase(), "data", "poly", null,
								false, "variable de l'objet " + tmp, ""));
						groups.get(current).add("<BlockGenusMember>" + tmp + "</BlockGenusMember>\n");
						aliasGeniusTypeMap.put(tmp, "object");
						ty = "variable_object";

					}
					ty += "/";
					ty += lbl;
					ty += "/";
					ty += connectorMap.get(aliasGeniusTypeMap.get(tmp)) != null
							? connectorMap.get(aliasGeniusTypeMap.get(tmp))
							: "poly";
					// System.out.println("--->" + ty);
					liste.add(ty);
				}
			}
		return liste;
	}

	String createBlockGenus(String template_type, String name, String label, String typeGenus, String typeOutput,
			ArrayList<String> typesInput, boolean isconstructor, String description, String imagepath) {
		return createBlockGenus(template_type, name, label, typeGenus, typeOutput, typesInput, isconstructor, false,
				description, imagepath);

	}

	String createBlockGenus(String template_type, String name, String label, String typeGenus, String typeOutput,
			ArrayList<String> typesInput, boolean isconstructor, boolean isPlug, String description, String imagepath) {
		StringBuffer string = new StringBuffer();
		String typename = "";
		int couleurT0 = 1, couleurT1 = 1, couleurT2 = 1;
		/*
		 * if (current == -1) { if (current == -1 || className.length() < 3) {
		 * current++; groups.add(new ArrayList<String>());
		 * groups.get(current).add(className); }
		 * 
		 * } else if (current == 0 && !isDefine) { current++; groups.add(new
		 * ArrayList<String>()); groups.get(current).add(className);
		 * 
		 * }
		 */
		String couleur = (((int) (groups.get(current).get(0).charAt(0))) % 256) + " "
				+ (((int) (groups.get(current).get(0).charAt(1)) * 10 + 127) % 256) + " "
				+ (((int) (groups.get(current).get(0).charAt(0)) * 10 + 127) % 256);

		// System.out.println("in create block genus :"+groups.get(current).get(0));

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

			string.append("<BlockConnector connector-type=\"" + typeOutput
					+ "\" connector-kind=\"plug\" position-type=\"mirror\" label-editable=\"yes\" is-expandable=\"true\" />\n");
		}

		if (!template_type.equals("instanceClasse")) {

			String position = "";
			if (!template_type.equals("constructeur"))
				position = " position-type=\"bottom\"";
			else
				position = " position-type=\"SINGLE\"";

			// -----------create instance de classe dans le socket bottom pour une entete de
			// classe, sinon c du C non objet

			if (isClass) {
				string.append(
						"<BlockConnector connector-type=\"poly\"  is-expandable=\"true\"  label-editable=\"yes\" connector-kind=\"socket\""
								+ position + " >");
				string.append("\n");
				string.append("<DefaultArg genus-name=\"" + className.replace("()", "") + "\" label=\""
						+ className.replace("()", "").toLowerCase()

						+ "\" editable-label=\"yes\"/>");
				string.append("\n");
				string.append("</BlockConnector>");
				string.append("\n");
			}

			// ---------ajout des parametres dans les sockets
			int inc = 0;

			if (typesInput != null)
				for (String it : typesInput) {
					if (!(template_type.equals("constructeur") && inc == 0)) {
						string.append("<BlockConnector connector-type=\"");

						string.append(it.split("/")[2]);
						string.append("\" connector-kind=\"socket\" position-type=\"SINGLE\" label=\"");
						string.append(it.split("/").length > 1 ? it.split("/")[1] : "");
						string.append("\" label-editable=\"false\"  is-expandable=\"true\" >");
						string.append("\n");
						string.append("<DefaultArg genus-name=\"");
						if (it.equals("boolean"))
							string.append("true\" label=\"VRAI\"");
						else
							string.append(it.split("/")[0] + "\" label=\"\"");

						string.append("  />");
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

		return string.toString();
	}

	String readFile2(String file) {
		className = new File(file).getName().replace(".h", "");

		current = -1;
		String imagePath = "";
		String description = "";
		this.file = file;
		isClass = false;
		contenuFichier.append("<GenusMembers>");
		contenuFichier.append("\n");

		ArrayList<String> listNames = new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			int idLine = 0;
			int currentIdBalise = 1;
			String name = "";
			lastname = "";
			while ((line = br.readLine()) != null) {
				isDefine = false;
				// clean the the line.
				line = line.replace("  ", " ");
				line = line.trim();
				line = line.replace("unsigned ", "");
				line = line.replace("virtual ", "");
				line = line.replace("const ", "");
				line = line.replace("static ", "");
				line = line.replace("void (*)(void)", "String fonction");
				// replace types for parsing after on " "
				line = line.replace("char *", "String ");
				line = line.replace("String &", "String ");
				line = line.replace("uint8_t *", "int ");
				/*
				 * if (line.contains("void")) { boolean isvoidtype = line.startsWith("void");
				 * line = line.replace("void", ""); if (isvoidtype) line = "void" + line; }
				 */
				// remove function body
				if (line.contains("{") && line.length() > 1)
					line = line.substring(0, line.indexOf("{")) + ";";
				// remove inline comments
				if (line.contains(";"))
					line = line.substring(0, line.indexOf(";") + 1);
				if (line.startsWith("/*"))
					inComment = true;

				// System.out.println(line);

				if (inComment) {
					// do nothing
				} else if (line.startsWith("#") && !line.startsWith("#define")) {
					// do nothing
				} else if (line.startsWith("extern")) {
		//			System.err.println("**************************" + line + "---" + line.split(" ").length);
					if (line.split(" ").length == 3)
						externObjectGenius.put(line.split(" ")[1], line.split(" ")[2].replaceAll(";", ""));
					isClass = false;
					// do nothing
				} else if (line.startsWith("void loop") && !line.startsWith("void setup")) {
					// do nothing
				} else if (line.startsWith("#define")) {
					if (line.contains("("))
						line = line.substring(0, line.indexOf("("));
					if (line.split(" ").length > 1) {
						String type = "number";
						name = line.split(" ")[1];
						isDefine = true;
						if (current == -1) {
							current++;
							groups.add(new ArrayList<String>());

							groups.get(current).add(className + " - " + groupConstantes);

						}
						contenuFichier.append(createBlockGenus("variable", name, name, "data", type, null, false,
								idLine == currentIdBalise + 1 ? description : "variable de l'objet " + name,
								idLine == currentIdBalise + 1 ? imagePath : ""));
						groups.get(current).add("<BlockGenusMember>" + name + "</BlockGenusMember>\n");

					}

				} else if (line.length() == 0) {
					// do nothing
				} else if (line == "{") {
					// do nothing
				} else if (line.startsWith("};") || line.equals("};")) {

					if (!isClass || (isClass && !hasContructor))
						useNoInstance.add(new Boolean(true));
					else
						useNoInstance.add(new Boolean(false));

					if (isClass && !hasContructor) {

						PropertiesReader.addValue(className, "com.unibot.translator.block.CustomVariableBlock");
						if (externObjectGenius.get(className) != null)
							PropertiesReader.addValue(externObjectGenius.get(className),
									"com.unibot.translator.block.CustomVariableBlock");

					}
					isClass = false;
					// System.out.println(PropertiesReader.p.toString());
				} else if (line.startsWith("}")) {
					// do nothing
				} else if (line.startsWith("class ")) {
					hasContructor = false;
					className = line.split(" ")[1].trim();
					// if (isClass) {
					current++;
					groups.add(new ArrayList<String>());
					groups.get(current).add(className + " - "+groupVarFunctions);

					// }

					contenuFichier.append(createBlockGenus("instanceClasse", className, className.toLowerCase(), "data",
							"poly", null, false, true,
							idLine == currentIdBalise + 1 ? ("objet de type " + className) : "", ""));
					groups.get(current).add("<BlockGenusMember>" + className + "</BlockGenusMember>\n");

					isClass = true;

				} else if (line.startsWith("private:") || line.startsWith("protected:")) {
					isInPublic = false;

				} else if (line.startsWith("public:")) {
					isInPublic = true;

				} else if (line.startsWith("//@")) {
					///////////////////////////////////////////////
					// description du bloc
					///////////////////////////////////////////////
					description = "";
					imagePath = null;
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
						groups.get(current).add(className + " - " + line.replaceAll("//@", ""));
					}
				} else if (line.startsWith("//")) {
					// do nothing
				} else if (line.trim().contains(" ") && !line.trim().contains("(") && line.trim().contains(";")
						&& isInPublic) {

					// c'est un attribut de classe
					String type = line.substring(0, line.indexOf(" "));
					type = connectorMap.get(type);
					if (type == null)
						type = "poly";
					if (line.contains("="))
						name = line.substring(0, line.indexOf("=")).replace(type, "").trim();
					else
						name = line.replace(";", "").replace(type, "").trim();

					if (!groups.get(current).get(0).equals(className+" - "+groupVarFunctions))
					{
						current++;
						groups.add(new ArrayList<String>());
						groups.get(current).add(className + " - " + groupVarFunctions);
					}
					contenuFichier.append(createBlockGenus("variable", name, name, "data", type, null, false,
							idLine == currentIdBalise + 1 ? description : "variable de type " + name,
							idLine == currentIdBalise + 1 ? imagePath : ""));
				} else if (line.contains("(") && isInPublic) {
					if (!line.substring(0, line.indexOf('(')).contains(" ")) {
						///////////////////////////////////////////////
						// constructeur
						///////////////////////////////////////////////
						boolean ok = true;
						hasContructor = true;
						int i = 0;
						name = line.substring(0, line.indexOf('('));
						for (String str : listNames) {
							if (str.trim().equals(name)) {
								ok = false;
							}
						}
						String nametemp = name;
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
						ArrayList<String> typesInput = new ArrayList<String>();
						// prepare string to parse args
						line = line.substring(line.indexOf("("));
						line = line.replace("(", "");
						line = line.replace(")", "");
						line = line.replace(";", "");
						line = line.trim();
						typesInput.add(name);
						typesInput.addAll(getTypes(line));
						contenuFichier.append(createBlockGenus("constructeur", nametemp + "()", name + "()", "command",
								null, typesInput, true, idLine == currentIdBalise + 1 ? description : "",
								idLine == currentIdBalise + 1 ? imagePath : ""));
						groups.get(current)
								.add("<BlockGenusMember>" + "nouveau " + nametemp + "()" + "</BlockGenusMember>\n");

					} else if (line.startsWith("void") && isInPublic) {
						///////////////////////////////////////////////
						// is command (return void function)
						///////////////////////////////////////////////
						boolean ok = true;
						int i = 0;

						name = line.substring(0, line.indexOf('(')).replaceFirst("void ", "").trim();
						for (String str : listNames) {
							if (str.trim().equals(name)) {
								ok = false;
							}
						}
						String nametemp = name;
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
						line = line.substring(line.indexOf('(') + 1);
						line = line.replace("(", "");
						line = line.replace(")", "");
						line = line.replace(";", "");
						line = line.replace("void", "");
						line = line.trim();
						ArrayList<String> typesInput = getTypes(line);
						contenuFichier.append(createBlockGenus("method", nametemp, name, "command", null, typesInput,
								false, idLine == currentIdBalise + 1 ? description : name,
								idLine == currentIdBalise + 1 ? imagePath : ""));
						if (groups.get(current).get(0).equals(className+" - "+groupConstantes))
						{
							current++;
							groups.add(new ArrayList<String>());
							groups.get(current).add(className + " - " + groupVarFunctions);
						}
						groups.get(current).add("<BlockGenusMember>" + nametemp + "</BlockGenusMember>\n");

					} else if (isInPublic) { // aliasGeniusTypeMap.get(line.split(" ")[0]) != null &&
						String type = connectorMap.get(aliasGeniusTypeMap.get(line.substring(0, line.indexOf(" "))));
						if (type == null)
							type = "poly";

						boolean ok = true;
						int i = 0;
						name = line.substring(line.indexOf(" "), line.indexOf('(')).trim();
						String nametemp = name;
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
						line = line.substring(line.indexOf("(") + 1);
						line = line.replace("(", "");
						line = line.replace(")", "");
						line = line.replace(";", "");
						line = line.trim();

						ArrayList<String> typesInput = getTypes(line);
						if (!groups.get(current).get(0).equals(className+" - "+groupVarFunctions))
						{
							current++;
							groups.add(new ArrayList<String>());
							groups.get(current).add(className + " - " + groupVarFunctions);
						}
						contenuFichier.append(createBlockGenus("methodreturn", nametemp, name, "data", type, typesInput,
								false, idLine == currentIdBalise + 1 ? description : name,
								idLine == currentIdBalise + 1 ? imagePath : ""));
						groups.get(current).add("<BlockGenusMember>" + nametemp + "</BlockGenusMember>\n");

					}
				}
				if (line.trim().contains("*/"))
					inComment = false;

				// System.out.println("last :"+lastname +" name :"+name);
				lastname = name;
			}
			br.close();

			contenuFichier.append("</GenusMembers>");
			contenuFichier.append("\n");
			return contenuFichier.toString();
		} catch (

		Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * void readFile(String file) {
	 * 
	 * this.file = file; contenuFichier.append("<GenusMembers>");
	 * contenuFichier.append("\n");
	 * 
	 * className = new File(file).getName().replace(".h", "");
	 * 
	 * String imagePath = ""; String description = ""; BufferedReader br;
	 * ArrayList<String> listNames = new ArrayList<String>(); try { br = new
	 * BufferedReader(new FileReader(file)); String line; int idLine = 0; int
	 * currentIdBalise = 1; while ((line = br.readLine()) != null) { // process the
	 * line. line = line.trim(); while (line.contains("  ")) { line =
	 * line.replace("  ", " "); } idLine++;
	 * 
	 * if (line.startsWith("/*")) inComment = true; if (line.contains("class "))
	 * className = line.replace("class ", "").replace("{", "").split(":")[0].trim();
	 * if (line.length() > 0 && !inComment) { if (line.contains("public:")) {
	 * isInPublic = true;
	 * 
	 * } if (line.contains("private:") || line.contains("protected:")) { isInPublic
	 * = false;
	 * 
	 * } if (isInPublic) { String name = "";
	 * 
	 * // si c'est une metabalise if (line.startsWith("//@")) { description = "";
	 * imagePath = null; if (line.replace("//@", "").startsWith("bloc")) { String[]
	 * maligne = (line.replace("//@bloc", "").replace("png=", "#png=")
	 * .replace("texte=", "#texte=").trim()).split("#"); currentIdBalise = idLine;
	 * for (int i = 0; i < maligne.length; i++) { if
	 * (maligne[i].split("=")[0].equals("png")) imagePath = new
	 * File(file).getParentFile().getAbsolutePath() + "/img/" +
	 * maligne[i].split("=")[1]; if (maligne[i].split("=")[0].equals("texte"))
	 * description = maligne[i].split("=")[1]; // @bloc texte="ms" png="delay.png" }
	 * 
	 * } else { current++; groups.add(new ArrayList<String>());
	 * groups.get(current).add(line.replaceAll("//@", "")); }
	 * 
	 * } else {
	 * 
	 * if (!line.startsWith("//") && !line.contains("operator") &&
	 * !line.startsWith("using")) {
	 * 
	 * if (line.contains("{")) line = line.replace(line.substring(line.indexOf("{"),
	 * line.indexOf("}")), ";"); line = line.substring(0, line.indexOf(";") + 1);
	 * 
	 * if (line.startsWith("static ")) line = line.replace("static ", "");
	 * 
	 * line = line.replace("const ", "");
	 * 
	 * line = line.replace("unsigned", "");
	 * 
	 * line = line.replace("uint8_t", "int"); line = line.replace("uint16_t",
	 * "int"); line = line.replace("uint32_t", "int"); line = line.replace("char *",
	 * "String "); line = line.replace("String &", "String "); line =
	 * line.replace("virtual ", "");
	 * 
	 * // si c'est un constructeur ou une methode if (line.contains("(")) { if
	 * (!line.substring(0, line.indexOf('(')).contains(" ")) {// is // constructor
	 * name = line.substring(0, line.indexOf('(')); line = line.replace(name, "");
	 * line = line.replace("(", ""); line = line.replace(")", ""); line =
	 * line.replace(";", ""); line = line.trim(); ArrayList<String> typesInput = new
	 * ArrayList<String>(); typesInput.add(className);
	 * 
	 * System.out.println("types input: " + line);
	 * typesInput.addAll(getTypes(line));
	 * contenuFichier.append(createBlockGenus("constructeur", name + "()", name +
	 * "()", "command", null, typesInput, true, idLine == currentIdBalise + 1 ?
	 * description : "", idLine == currentIdBalise + 1 ? imagePath : "")); if
	 * (firstConstructor) { contenuFichier.append(createBlockGenus("instanceClasse",
	 * className, className, "data", "object", null, false, true, idLine ==
	 * currentIdBalise + 1 ? ("objet de type " + name) : "", ""));
	 * 
	 * // groups.get(current).add(className); firstConstructor = false;
	 * 
	 * }
	 * 
	 * } else if (line.startsWith("void")) { boolean ok = true; int i = 0;
	 * 
	 * name = line.substring(0, line.indexOf('(')).replaceFirst("void ", "").trim();
	 * for (String str : listNames) { if (str.trim().equals(name)) { ok = false; } }
	 * String nametemp = name; while (!ok) { ok = true; for (String str : listNames)
	 * { if (str.trim().equals(nametemp)) { ok = false; } } nametemp = name + "" +
	 * i; i++; }
	 * 
	 * listNames.add(nametemp); System.out.println("nametemp void : " + nametemp);
	 * line = line.substring(line.indexOf('(') + 1); line = line.replace("(", "");
	 * line = line.replace(")", ""); line = line.replace(";", "");
	 * 
	 * line = line.trim(); ArrayList<String> typesInput = getTypes(line);
	 * contenuFichier.append( createBlockGenus("method", nametemp, name, "command",
	 * null, typesInput, false, idLine == currentIdBalise + 1 ? description : name,
	 * idLine == currentIdBalise + 1 ? imagePath : ""));
	 * 
	 * } else { String type = line.substring(0, line.indexOf(" "));
	 * 
	 * boolean ok = true; int i = 0; name = line.substring(line.indexOf(" "),
	 * line.indexOf('(')).trim(); String nametemp = name; for (String str :
	 * listNames) { if (str.trim().equals(name)) { ok = false;
	 * 
	 * }
	 * 
	 * } while (!ok) { ok = true; nametemp = name + "" + i; for (String str :
	 * listNames) { if (str.trim().equals(nametemp)) { ok = false; i++; }
	 * 
	 * } nametemp = name + "" + i; i++; }
	 * 
	 * listNames.add(nametemp);
	 * 
	 * System.out.println("nametemp other :" + nametemp); line =
	 * line.substring(line.indexOf("(") + 1); line = line.replace("(", ""); line =
	 * line.replace(")", ""); line = line.replace(";", ""); line = line.trim();
	 * type=connectorMap.get(aliasGeniusTypeMap.get(type));
	 * 
	 * ArrayList<String> typesInput = getTypes(line);
	 * contenuFichier.append(createBlockGenus("methodreturn", nametemp, name,
	 * "data", type, typesInput, false, idLine == currentIdBalise + 1 ? description
	 * : name, idLine == currentIdBalise + 1 ? imagePath : ""));
	 * 
	 * }
	 * 
	 * } else if (line.trim().contains(" ") && line.trim().contains(";")) { // c'est
	 * un attribut de classe String type = line.substring(0, line.indexOf(" "));
	 * type=connectorMap.get(aliasGeniusTypeMap.get(type)); if (line.contains("="))
	 * name = line.substring(0, line.indexOf("=")).replace(type, "").trim(); else
	 * name = line.replace(";", "").replace(type, "").trim();
	 * 
	 * contenuFichier .append(createBlockGenus("variable", name, name, "data", type,
	 * null, false, idLine == currentIdBalise + 1 ? description :
	 * "variable de l'objet " + name, idLine == currentIdBalise + 1 ? imagePath :
	 * "")); } } } } } // if (line.trim().contains("")) inComment = false; }
	 * br.close(); contenuFichier.append("</GenusMembers>");
	 * contenuFichier.append("\n");
	 * 
	 * } catch (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } }
	 */
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
			if (liste != null) {
				// System.out.println(groups.get(i).get(0).toLowerCase().charAt(0));

				String couleur = (((int) (liste.get(0).charAt(0))) % 256) + " "
						+ (((int) (liste.get(0).charAt(1)) * 10 + 127) % 256) + " "
						+ (((int) (liste.get(0).charAt(0)) * 10 + 127) % 256);
				// System.out.println("couleur :"+couleur);
				i++;
				contenuFichier.append("<BlockDrawer button-color=\"" + couleur + "\" name=\"" + liste.get(0) + "\">");
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
		readFile2(name);
		setpied();

		// extern name for class
		String xml = contenuFichier.toString();
		Iterator it = externObjectGenius.entrySet().iterator();
		Iterator it2 = useNoInstance.iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			boolean val = ((Boolean) (it2.hasNext())).booleanValue();

			xml = xml.replace((String) pair.getKey(), (String) pair.getValue());
			if (val)// use no instance, so class name with same case
			{

				xml = xml.replace(((String) pair.getKey()).toLowerCase(), (String) pair.getValue());
				PropertiesReader.addValue((String) pair.getValue(), PropertiesReader.getValue((String) pair.getKey()));
			}

		}

		//System.out.println(xml);

		byte[] bytes = xml.getBytes();

		/*
		 * Get ByteArrayInputStream from byte array.
		 */
		// System.out.println(contenuFichier.toString());
		return new ByteArrayInputStream(bytes);
	}

	public LibraryLoader() {
		// TODO Auto-generated constructor stub

	}

}
