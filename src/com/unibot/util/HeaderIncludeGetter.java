package com.unibot.util;

import java.io.BufferedReader;
import java.io.FileReader;

public class HeaderIncludeGetter {

	BufferedReader br;
	
	
	public HeaderIncludeGetter()
	{
		// TODO Auto-generated constructor stub
	}
	
	public String getIncludes(String file)
	{
		////System.out.println(file);
		String res="";
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
				line = line.trim();
				if (line.startsWith("#include"))
					res+=line+"\n";
			//	//System.out.println(res+" "+line);
			}
		}
		
		catch (Exception e)
		{
		}
		return res;
		}
	}


