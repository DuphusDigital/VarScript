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
	
	public ArrayList<VariableToken> Parse(String filename, ParseStyle style, boolean allowComments) throws Exception {
		if (filename.substring(filename.indexOf('.')) != "var") {
			throw new Exception("[VariableScript]: Bad Script Extension");
		}
		
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
}
