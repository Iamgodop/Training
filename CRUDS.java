// Perform CRUD operations for Item domain

import java.sql.*;
import java.util.Scanner;

class CRUDS
{
	private static final String host = "jdbc:mysql://138.68.140.83/dbBhaskar";
	private static final String username = "Bhaskar";
	private static final String password = "Bhaskar@123";

	Connection connection = null;
	Statement cursor;
	ResultSet resultSet;
	Scanner stream = new Scanner(System.in);

	CRUDS()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(host, username, password);
			cursor = connection.createStatement();
			System.out.println("Sucessfully connected to database!");
		}
		catch (Exception e)
		{
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

	private String[] get_item_details()
	{
		String[] item_details = new String[5];

		System.out.print("Enter Item Id: ");
		item_details[0] = stream.nextLine();

		System.out.print("Enter Item name: ");
		item_details[1] = stream.nextLine();
		
		System.out.print("Enter Unit price: ");
		item_details[2] = stream.nextLine();
		
		System.out.print("Enter Supplier Id: ");
		item_details[3] = stream.nextLine();
		
		System.out.print("Enter Stock quantity: ");
		item_details[4] = stream.nextLine();

		return item_details;
	}

	public void add_item()
	{
		String[] item_details;
		try
		{
			item_details = get_item_details();
			String query = "insert into Item values(?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, item_details[0]);
            statement.setString(2, item_details[1]);
            statement.setString(3, item_details[2]);
            statement.setString(4, item_details[3]);
            statement.setString(5, item_details[4]);

            if (statement.executeUpdate() == 1)
            {
				System.out.println(item_details[0] + ": Sucessfully added to inventory");
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

    private void print_details(ResultSet resultSet) throws SQLException
    {
    	System.out.printf("| %-6s | %-25s | %10d | %-10s | %15d |\n", resultSet.getString("ItemId"), resultSet.getString("Name"), resultSet.getInt("UnitPrice"), resultSet.getString("SupplierId"), resultSet.getInt("StockQty"));
    }

	private void print_all_items(ResultSet resultSet) throws SQLException
	{
		System.out.println("----------------------------------------------------------------------------------");
		System.out.printf("| %-6s | %-25s | %-10s | %-10s | %-15s |\n", "ItemId", "Item Name", "Unit Price", "SupplierId", "Stock Quantity");
		System.out.println("----------------------------------------------------------------------------------");
		while (resultSet.next())
		{
			print_details(resultSet);
		}
		System.out.println("----------------------------------------------------------------------------------");
	}

	public void view_all_items()
	{
		try 
		{
			resultSet = cursor.executeQuery("select * from Item");
			print_all_items(resultSet);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	private String get_id(String operation)
	{
		System.out.print("Enter Item Id to " + operation + ": ");

		return stream.nextLine();
	}

	private void print_not_found_message(String id)
	{
		System.out.println(id + ": not found");
	}

	public void search()
	{
		String id = get_id("Search");
		String query = String.format("Select * from Item where ItemId = '%s'", id);
		try 
		{
			resultSet = cursor.executeQuery(query);
			if (resultSet.next())
			{
				print_details(resultSet);
			}
			else
			{
				print_not_found_message(id);
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	private Boolean get_item_status(String id)
	{
		try
		{
			resultSet = cursor.executeQuery(String.format("Select * from Item where ItemId = '%s'", id));

			if (resultSet.next())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return false;
	}

	private int get_new_price()
	{
		System.out.print("Enter new price: ");
		return stream.nextInt();
	}

	public void update()
	{
		String id = get_id("update");

		if (get_item_status(id))
		{
			int new_price = get_new_price();
			String query = String.format("update Item SET UnitPrice = %d where ItemId = '%s'", new_price, id);

			try
			{
				cursor.execute(query);
				System.out.println(id + ": Sucessfully updated.");
			}
			catch (SQLException e)
			{
				System.out.println(e);
			}
		}
		else
		{
			print_not_found_message(id);
		}
	}

	public void delete()
	{
		String id = get_id("delete");
		String query = String.format("delete from Item where ItemId = '%s'", id);

		if (get_item_status(id))
		{
			System.out.print("Are your sure that you want to delete item with Id " + id + "? [Y/N]: ");
			char confirm_delete = (stream.nextLine()).charAt(0);

			if (confirm_delete == 'Y' || confirm_delete == 'y')
			{
				try
				{
					System.out.println(cursor.execute(query));
					System.out.println(id + ": Sucessfully removed from inventory.");
				}
				catch (SQLException e)
				{
					System.out.println(e);
				}
			}
			else
			{
				System.out.println(id + ": Failed to remove");
			}
		}
		else
		{
			print_not_found_message(id);
		}
	}

	public void sort()
	{
		String[] headers = {"ItemId", "Name", "UnitPrice", "SupplierId", "StockQty"};
		
		System.out.println("Sort by:\n1. Item Id\n2. Description\n3. Unit price\n4. Supplier Id\n5. Stock");
		System.out.print("Enter your choice: ");

		int choice = stream.nextInt();

		String query = String.format("select * from Item ORDER BY %s", headers[choice - 1]);

		try
		{
			resultSet = cursor.executeQuery(query);
			print_all_items(resultSet);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
}

class test_CRUDS
{
	public static void main(String[] args)
	{
		Scanner stream = new Scanner(System.in);
		CRUDS manager = new CRUDS();
		while (true)
		{
			System.out.println("---------------------------GUGU STORE---------------------------");
			System.out.println("0. Exit");
			System.out.println("1. Add item");
			System.out.println("2. View all items");
			System.out.println("3. Update price");
			System.out.println("4. Remove item");
			System.out.println("5. Search item");
			System.out.println("6. Sort");
			System.out.print("Enter your choice: ");

			int choice = stream.nextInt();

			switch (choice)
			{
				case 0: System.exit(0);
						break;
				case 1: manager.add_item();
						break;
				case  2: manager.view_all_items();
						break;
				case 3: manager.update();
						break;
				case 4: manager.delete();
						break;
				case 5: manager.search();
						break;
				case 6: manager.sort();
						break;
				default: System.out.println("Enter a valid option!");
						 break;
			}
		}
	}
}

// javac -cp "D:/Training/Java/mysql-connector-j-8.3.0.jar" CRUDS.java

// java -cp "D:/Training/Java/mysql-connector-j-8.3.0.jar;D:/Training/Java" test_CRUDS