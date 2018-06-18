import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
/*
 * Cody Tate
 * 6/15/2018
 */
public class connection 
{
	
	/*
	 * Main method to run the program
	 */
	public static void main(String[] args) throws Exception
	{
		
		//getConnection();
		get();
		
	}
	
	/*
	 * This method get a connection to the MYSQL database
	 * Prints out if it was connected successfully or not
	 */
	public static Connection getConnection() throws Exception
	{
		Connection conn = null;
		try
		{
			conn = (Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/Leads","root","Password");
			
			if(conn!=null)
			{
				System.out.println("connected successfully");
				return conn;
				
			}
		}catch(Exception e)
		{
			System.out.println("not connected to database");
		}
		return null;
	}
	
	/*
	 * This get method execute a Query on the database and uses a select statement. It takes in the email and company
	 * name and compares the two to see if they are similar. The method then outputs the records that don't match and outputs
	 * them to a text file.
	 */
	public static ArrayList<String> get() throws Exception
	{
		try 
		{
			Connection con = getConnection(); //get connection to Database
			PreparedStatement statement = (PreparedStatement) con.prepareStatement("SELECT * FROM report2");
			int count = 0; //To count how many records its looped through in the end
			
			ResultSet result = statement.executeQuery();
			//Prepares the text file
			PrintStream o = new PrintStream(new File("A.txt"));
			ArrayList<String> array = new ArrayList<String>();
			while(result.next()) 
			{
				//System.out.print(result.getString("Company / Account"));
				//Set the Email column to a string
				String domain = result.getString("Email");				
				String newDomain = getEmailDomain(domain);
				String company = result.getString("Company / Account");
				
				boolean good = compareString(company, newDomain);
				if(good == false)
				{
					
					System.setOut(o);
					System.out.print(count + "; ");
					System.out.print(result.getString("First Name") + "; ");
					System.out.print(result.getString("Last Name") + "; ");
					System.out.print(result.getString("Title") + "; ");
					System.out.print(result.getString("Company / Account") + "; ");
					System.out.print(result.getString("Email") + "; ");
					System.out.print(result.getString("LinkedIn") + "; ");
					System.out.print(result.getString("Industry") + "; ");
					System.out.print(result.getString("Twitter") + "; ");
					System.out.print(result.getString("City") + "; ");
					System.out.print(result.getString("State/Province") + "; ");
					System.out.print(result.getString("Country") + "; ");
					//System.out.println(newDomain + "  ");
					
					
					count++;
				}//outputs all rows to A.txt that don't have a matching company and email name
				array.add(result.getString("Email"));
				
			}//while loop that goes though all the records in the database
			
			System.out.println("All records have been selected!");
			System.out.println(count + " rows have been queired");
			return array;
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
	
	/*
	 * The method splits the email so that it is just the domain name. Gets rid of everything
	 * before @ and everything before the .com
	 */
	static String getEmailDomain(String email)
	{
		String b =  email.substring(email.indexOf('@') + 1);
		String c = b.substring(0, b.indexOf('.'));
		return c;
	}
	
	/*
	 * Compares the company to the domain
	 * Returns true if the email is good and false if the email is bad
	 */
	public static boolean compareString(String companyName, String domain)
	{
		//Sets both string to upper case
		String newCompanyName = companyName.toUpperCase();
		String newDomainName = domain.toUpperCase();
		
		//Gets rid of white space and special characters in both of the strings
		String whiteCompanyName = newCompanyName.replaceAll(" ", "");
		String finalCompanyName = whiteCompanyName.replaceAll("[-+.^:,]","");
		String finalDomainName = newDomainName.replaceAll("[-+.^:,]","");
		if(finalCompanyName.equals(finalDomainName) || finalCompanyName.contains(finalDomainName) || finalDomainName.equals("GMAIL") || finalDomainName.equals("YAHOO") || finalDomainName.equals("RCCL") || finalDomainName.contains(finalCompanyName) || finalDomainName.equals("OUTLOOK"))
		{
			//System.out.println("Email is good");
			return true;
		}//if statement that returns true if companyName matches domain or domain matches one of the criteria
		System.out.println("Email is bad");
		return false;
	}
	
}
