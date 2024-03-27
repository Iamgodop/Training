// Test CRUDS Interface

import java.util.Scanner;

class test_interface
{
    public static void main(String args[]) throws Exception
    {
        iCRUDS manager = new cSQLite();
		userInterface u_interface = new userInterface();
        Scanner stream = new Scanner(System.in);
		while (true)
		{
			System.out.println("---------------------------GUGU STORE---------------------------");
			System.out.println("0. Exit");
			System.out.println("1. Add item");
			System.out.println("2. View all items");
			System.out.print("Enter your choice: ");

			int choice = stream.nextInt();

			switch (choice)
			{
				case 0: stream.close();
						System.exit(0);
						break;
				case 1: manager.add_item(u_interface.read_item_details());
						break;
				case  2: u_interface.print_all_items(manager.get_all_items());
						break;
                default: System.out.println("Enter a valid choice.");
                         break;
            }
        }
    }
}
