import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import org.math.plot.*;
import org.math.plot.plotObjects.*;
import org.math.io.*;

/**
 * This will plot daily and cumulative deaths by countries provided by the user
 * @author William Nash
 * C:/Users/rsasw/Desktop/Code/covidPlotter/countries_deaths.txt
 */
public class covidPlotter
{
	/**
	 * This is a nice formated welcome line
	 */
	public static void welcome()
	{
		System.out.println("*****************************************************");
		System.out.println("*        International Covid Mortality Rates        *");
		System.out.println("*****************************************************");
	}

	public static void main(String[] args)
	{
		welcome();
		Scanner sc = new Scanner(System.in);
		String path;
		String countriesGraph;
		String graphRate;
		LinkedHashMap<String, ArrayList<Integer>> covidData = new LinkedHashMap<String, ArrayList<Integer>>();
		String[] parts;
		ArrayList<Integer> dataArray;
		double[] days;
		
		try
		{
			System.out.print("Enter the path to the file: ");
			path = sc.nextLine();//gets path
			
			Scanner file = new Scanner(new File(path));// finds file
			covidData = lineReader(file,covidData); //<Country Name, ArrayList<Data>> 

			do
			{
				System.out.print("Enter countries to plot, seperated by commas (or quit to end):");
				countriesGraph = sc.nextLine();
				if(!countriesGraph.equalsIgnoreCase("quit"))
				{
					System.out.print("[D]aily or [C]umulative? ");
					graphRate = sc.nextLine();
					
					parts = countriesGraph.split(", ");
					Plot2DPanel plot = new Plot2DPanel();
					for(String part: parts)
					{
						part = part.trim();
						if(covidData.containsKey(part)==false)
						{
							System.out.printf("%s is not in the Data set.\n", part);
						}
						else
						{
							dataArray = covidData.get(part);
							double[] data = new double[dataArray.size()];
							for(int i = 0; i < dataArray.size(); i++)
							{
								data[i] = dataArray.get(i);
							}
							days = getDays(dataArray);
							
							
							if(graphRate.equalsIgnoreCase("C"))
							{
								plot.addLinePlot(part,days,data);
								BaseLabel title = new BaseLabel("Cumulative", Color.RED, 0.5, 1.1);
								plot.addPlotable(title);
							}
							else if(graphRate.equalsIgnoreCase("D"))
							{
								plot.addLinePlot(part, days, dailyDeathCalc(data));
								BaseLabel title = new BaseLabel("Daily", Color.RED, 0.5, 1.1);
								plot.addPlotable(title);
							}
						}
					}
					plot.addLegend("SOUTH");
					plot.setAxisLabel(0, "Day");
					plot.setAxisLabel(1, "Deaths");
					frameSetup(plot);
				}
				else
				{
					break;
				}
			}while(!countriesGraph.equalsIgnoreCase("quit"));
			
		}catch(FileNotFoundException ex) {
			System.out.println("The file could not be found");
			//ex.printStackTrace();
			sc.next();
		}catch(Exception ex){
			System.out.println("Error");
			ex.printStackTrace();
			sc.next();
		}
		
		sc.close(); //IMPORTANT**
		System.out.println("Your only task is to wear a mask!!!");
	}//main
	
	/**
	 * This function takes the file proved by the user and puts the data into a linked Hash map formated like
	 * <String country name, ArrayList<Deaths by day>>
	 * @param file The file with all the related covid Data
	 * @param covidData The linked hashmap with no data
	 * @return covidData hashmap with all the related covid Data
	 */
	public static LinkedHashMap lineReader(Scanner file, LinkedHashMap covidData)
	{
		String line;
		String[] parts;
		String countryName;
		while(file.hasNextLine())
		{
			ArrayList<Integer> deaths = new ArrayList<Integer>();
			line = file.nextLine();// takes one line from file
			parts = line.split("\t");// splits line into parts
			countryName = parts[0];//gets countries names
			//System.out.println(parts.length);
			for(int i = 0; i < parts.length-1; i++)
			{
				deaths.add(Integer.parseInt(parts[i+1]));
			}
			covidData.put(countryName, deaths);
		}
		return covidData;
	}
	
	/**
	 * This function creates the JFrame needed to display the plot panel
	 */
	public static void frameSetup(Plot2DPanel plot)
	{
		JFrame frame = new JFrame();
		frame.setBounds(100,100,500,500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(plot,BorderLayout.CENTER);
		frame.setVisible(true);	
	}

	/**
	 * This function gets the amount of days to show based on the amount of data entries
	 * @param dataArray This is an ArrayList of integer for the use to find out how many entries
	 * @return The total number of entries in the dataArray list
	 */
	public static double[] getDays(ArrayList<Integer> dataArray)
	{
		double[] days = new double[dataArray.size()];
		for(int i = 0; i < days.length; i++)
		{
			days[i] = i;
		}
		return days;
	}
	
	/**
	 * This function calculates the amount of deaths daily by looking at the day before and subtracting that from today
	 * @param data contain the data needed to perform the math
	 * @return returns a double[] containing the daily deaths
	 */
	public static double[] dailyDeathCalc(double[] data)
	{
		double[] daysDeaths = new double[data.length];
		for(int i = 1; i < daysDeaths.length; i++)
		{
			daysDeaths[i] = data[i]-data[i-1];
		}
		
		return daysDeaths;
	}
}
