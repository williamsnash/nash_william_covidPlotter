import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This will plot daily and cumulative deaths by countries provided by the user
 * @author William Nash
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
		try
		{
			System.out.print("Enter the path to the file: ");
			path = sc.nextLine();
			Scanner file = new Scanner(new File(path));
			
			file.close(); //IMPORTANT**
		}catch(FileNotFoundException ex) {
			System.out.println("The file could not be found");
			sc.next();
		}
		sc.close(); //IMPORTANT**
	}

}
