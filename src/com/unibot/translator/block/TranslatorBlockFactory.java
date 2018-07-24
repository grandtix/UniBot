package com.unibot.translator.block;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.unibot.translator.Translator;
import com.unibot.util.PropertiesReader;

public class TranslatorBlockFactory
{
	private static final String BLOCK_MAPPING = "com/unibot/block/block-mapping.properties";


	private Map<String, String> shortClassName;
	
	public TranslatorBlockFactory()
	{
		shortClassName = new HashMap<String, String>();
	
	}
	
	
	public TranslatorBlock buildTranslatorBlock(Translator translator, Long blockId, String blockName, String codePrefix, String codeSuffix, String label,  boolean forGlobal, String description)
	{
		
		String className = PropertiesReader.getValue(blockName, BLOCK_MAPPING);
		//System.out.println("className: " + className);
		String longName = shortClassName.get(className);
		if (longName != null)
		{
			className = longName;
		}
		
		try
		{
			Class<?> blockClass = Class.forName(className);
			//System.out.println("piopio-->"+blockClass.toString()+"-"+className);
			if (className.equals("com.unibot.translator.block.CustomConstructorBlock"))
			{	
				Constructor<?> constructor = blockClass.getConstructor(Long.class, Translator.class, String.class, String.class, String.class, String.class);
			TranslatorBlock ret = (TranslatorBlock)constructor.newInstance(blockId, translator, codePrefix, codeSuffix, label, description);
			ret.setForGlobal(forGlobal);
			//System.out.println("for name 3");
			return ret;
			}
			else
			{			//	System.out.println("for name 4");

				
					Constructor<?> constructor = blockClass.getConstructor(Long.class, Translator.class, String.class, String.class, String.class);
					TranslatorBlock ret = (TranslatorBlock)constructor.newInstance(blockId, translator, codePrefix, codeSuffix, label);
					ret.setForGlobal(forGlobal);

					return ret;
				
					
			}
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println(blockName + " not suitable class!");
		}		

		System.err.println(blockName + " not found!");
		
		return null;
	}

	public TranslatorBlock buildTranslatorBlock(Translator translator, Long blockId, String blockName, String codePrefix, String codeSuffix, String label, String desc)
	{
//		System.out.println("block name : " + blockName + " captured");
		
		String className = PropertiesReader.getValue(blockName, BLOCK_MAPPING);
	PropertiesReader.getValue(blockName);
//		if (className==null)
//			if (className.startsWith("template_constructor"))
//				className="";
//		System.out.println("className: " + className);
		String longName = shortClassName.get(className);
		if (longName != null)
		{
			className = longName;
		}
		
		try
		{
			//System.out.println("class name "+className+ " "+blockName);
			

	//		System.out.println("2--> -"+className+"-"+blockName);
		//	
			Class<?> blockClass = Class.forName(className);
			if (className==("com.unibot.translator.block.CustomConstructorBlock"))
			{	
				Constructor<?> constructor = blockClass.getConstructor(Long.class, Translator.class, String.class, String.class, String.class, String.class);
			TranslatorBlock ret = (TranslatorBlock)constructor.newInstance(blockId, translator, codePrefix, codeSuffix, label,   desc);
			//ret.setForGlobal(forGlobal);
	//	System.out.println("forname 1");
			return ret;
			}
			else
			{
	
					Constructor<?> constructor = blockClass.getConstructor(Long.class, Translator.class, String.class, String.class, String.class);
					TranslatorBlock ret = (TranslatorBlock)constructor.newInstance(blockId, translator, codePrefix, codeSuffix, label);
//	ret.setForGlobal(forGlobal);
					
//System.out.println("forname 2");	
return ret;

					
			}
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println(blockName + " not suitable class!");
		}		

		System.err.println(blockName + " not found!");
		
		return null;
	}

}
