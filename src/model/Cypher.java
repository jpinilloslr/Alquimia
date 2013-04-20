package model;

public class Cypher {

	public static String decode(String paramString)
	{
		if ((paramString == null) || (paramString.length() == 0))
			return "";
		
		char[] arrayOfChar = paramString.toCharArray();
		int i = 0;
		int length = arrayOfChar.length;
		int ascii;

		while (i < length)
		{
			ascii = arrayOfChar[i];
			ascii = ascii ^ 21;
			arrayOfChar[i] = (char) ascii;
			i += 1;
		}
		return new StringBuffer(new String(arrayOfChar)).toString();	
	}	
	
	public static String encode(String paramString)
	{
		return decode(paramString);
	}	
}
