package edu.gwu.graphics2.lindenmayer;

import java.util.Map;

/**
 * Generates a .d model based on a lindenmayer system
 * 
 * @author ian
 *
 */
public class Lindenmayer {
	
	private Map<Character, String> rules;
	
	public Lindenmayer(Map<Character, String> variableRules)
	{
		rules = variableRules; //new HashMap<Character, String>();
		
	}
	
	public String iterate(String original)
	{
		StringBuffer buff = new StringBuffer();
		for(int i=0; i<original.length(); i++)
		{
			Character c = original.charAt(i);
			if(rules.keySet().contains(c))
			{
				buff.append(rules.get(c));
			}else
			{
				buff.append(c);
			}
		}
		return buff.toString();
	}
}
