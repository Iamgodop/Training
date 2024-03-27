// Perform CRUD operations for Item domain

import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;

public class cmySQL implements iCRUDS
{
	private static final String host = "jdbc:mysql://138.68.140.83/dbBhaskar";
	private static final String username = "Bhaskar";
	private static final String password = "Bhaskar@123";

	Connection connection = null;
	Statement cursor;
	ResultSet resultSet;
	Scanner stream = new Scanner(System.in);
	int fields_count = 3;

	public cmySQL()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(host, username, password);
			cursor = connection.createStatement();
		}
		catch (Exception e)
		{
			System.out.println(e);
			System.out.println("Failed to connect to database.");
			System.exit(0);
		}
	}

	private void handle_exception(Exception exp)
	{
		String error_code = (exp.getMessage().split(": ", 2))[0];

		try 
		{
			resultSet = cursor.executeQuery(String.format("select Message from error where ErrorCode = '%s'", error_code));
			resultSet.next();
			System.out.println(resultSet.getString("Message"));
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

	}

	public void add_item(String[] item_details)
	{
		String query = "insert into Items values(?, ?, ?)";

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
			handle_exception(e);
		}
	}

	private void print_success_message(String id, String operation)
	{
		System.out.println(id + ": Sucessfully " + operation);
	}

	public ArrayList<String[]> get_all_items()
	{
		try 
		{
			ArrayList<String[]> rows = new ArrayList<>();
			resultSet = cursor.executeQuery("select * from Items");
			while (resultSet.next())
			{
				String[] item_details = new String[fields_count];
				for (int counter = 0; counter < fields_count; counter++)
				{
					item_details[counter] = resultSet.getString(counter + 1);
				}
				rows.add(item_details);
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