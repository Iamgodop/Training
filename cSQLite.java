// CRUDS operation using SQLite3

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class cSQLite implements iCRUDS
{
    Connection connection = null;
	private static final String host = "jdbc:sqlite:D:\\Training\\Java\\Item";
    ResultSet resultSet;
    Statement cursor;
    Scanner stream = new Scanner(System.in);
	int fields_count = 3;

    public cSQLite()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(host);
            cursor = connection.createStatement();
			if (connection != null)
			{
				System.out.println("Sucessfully connected to database.");				
			}
			else
			{
				System.out.println("Failed to connect to database.");
			}
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }


	private void print_success_message(String id, String operation)
	{
		System.out.println(id + ": Sucessfully " + operation);
	}

	public void add_item(String[] item_details)
	{
		String query = "insert into Item values(?, ?, ?)";

		try
		{
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, item_details[0]);
            statement.setString(2, item_details[1]);
            statement.setString(3, item_details[2]);

            if (statement.execute())
            {
				print_success_message(item_details[0], "added");
            }
            else 
            {
            	System.out.println(item_details[0] + ": Failed to insert!");
            }
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	public ArrayList<String[]> get_all_items()
	{
		ArrayList<String[]> rows = new ArrayList<>();
		try 
		{
			resultSet = cursor.executeQuery("select * from Item");
			while (resultSet.next())
			{
				String[] values = new String[fields_count];
				for (int counter = 0; counter < fields_count; counter++)
				{
					values[counter] = resultSet.getString(counter + 1);
				}
				rows.add(values);
			}
			return rows;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}
}