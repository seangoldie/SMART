package algomusic2021.seangoldie.jmsl;

import java.util.Scanner;

public class CSVReader implements HarmonicDataModel
{
	Scanner scanner;
	double[] data;
	
	public CSVReader(String fileName)
	{
		this.scanner = new Scanner(fileName);
		scanner.useDelimiter(",");
	}
	
	public void readNextLine()
	{
		char[] s = scanner.nextLine().toCharArray();
		for (int i=0; i<s.length; i++)
		{
			if (java.lang.Character.isDigit(s[i]))
			{
				data[i] = (double) s[i];
			}
		}
	}
	
	public void readNextValue()
	{
		char[] s = scanner.next().toCharArray();
		for (int i=0; i<s.length; i++)
		{
			if (java.lang.Character.isDigit(s[i]))
			{
				data[i] = (double) s[i];
			}
		}
	}
	
	@Override
	public double[] getValues()
	{
		return data;
	}

	@Override
	public double getValue()
	{
		readNextValue();
		return data[0];
	}
}
