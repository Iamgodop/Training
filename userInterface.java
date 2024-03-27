// User interface for CRUDS

import java.util.Scanner;
import java.util.ArrayList;

class userInterface
{
    Scanner stream;
    userInterface()
    {
        stream = new Scanner(System.in);
    }

    public String[] read_item_details()
    {
        String[] item_details = new String[5];

		System.out.print("Enter Item Id: ");
		item_details[0] = stream.nextLine();

		System.out.print("Enter Item name: ");
		item_details[1] = stream.nextLine();
		
		System.out.print("Enter Unit price: ");
		item_details[2] = stream.nextLine();

		return item_details;
    }

    public void print_all_items(ArrayList<String[]> items)
    {
        if (items != null)
        {
            for (String[] item: items)
            {
                System.out.println("-".repeat(40));
                System.out.println("Item Id: " + item[0]);
                System.out.println("Description: " + item[1]);
                System.out.println("Unit Price: " + item[2]);
                System.out.println("-".repeat(40));
            }
        }
    }
}