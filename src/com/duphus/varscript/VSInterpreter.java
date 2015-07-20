package com.duphus.varscript;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class VSInterpreter {
	public enum ParseStyle {
		NAME_EQUALS_VAL,
		NAME_COLON_VAL,
		VAL
	}
	
	public ArrayList<VariableToken> parse(String filename, ParseStyle style, boolean allowComments) throws Exception {
		ArrayList<VariableToken> varList = new ArrayList<VariableToken>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Charset.forName("UTF-8")));
			String line;
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {
				if (allowComments && line.startsWith(";")) {
					continue;
				}
				
				if (style == ParseStyle.NAME_COLON_VAL) {
					String[] variableCode = line.split("[a-zA-Z][a-zA-Z0-9]*:[a-zA-Z][a-zA-Z0-9]*");
					if (variableCode.length == 2) {
						varList.add(new VariableToken(variableCode[0], variableCode[1]));
					} else {
						throw new Exception("[VariableScript]: Bad Variable at line " + lineNumber);
					}
				} else if (style == ParseStyle.NAME_EQUALS_VAL) {
					String[] variableCode = line.split("[a-zA-Z][a-zA-Z0-9]*=[a-zA-Z][a-zA-Z0-9]*");
					if (variableCode.length == 2) {
						varList.add(new VariableToken(variableCode[0], variableCode[1]));
					} else {
						throw new Exception("[VariableScript]: Bad Variable at line " + lineNumber);
					}
				} else {
					varList.add(new VariableToken("null", line));
				}
				
				lineNumber++;
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return varList;
	}
	
	public boolean containsVariable(VariableToken variable, ArrayList<VariableToken> variables) {
		return variables.contains(variable);
	}
	
	public boolean getContainsVariable(String name, ArrayList<VariableToken> variables) {
		for(VariableToken var : variables) {
			if (var.name == name) {
				return true;
			}
		}
		return false;
	}
	
	public VariableToken getVariableByName(String name, ArrayList<VariableToken> variables) {
		for(VariableToken var : variables) {
			if (var.name == name) {
				return var;
			}
		}
		return null;
	}

	public ArrayList<VariableToken> getAllVariablesWithValue(String value, ArrayList<VariableToken> variables) {
		ArrayList<VariableToken> vars = new ArrayList<VariableToken>();
		
		for (VariableToken var : vars) {
			if (var.value == value) {
				vars.add(var);
			}
		}
		
		return vars;
	}
}
