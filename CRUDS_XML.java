// CRUDS using XML

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.*;

class cXML implements iCRUDS
{
    String DATAFILE = "Item.xml";
    File fp_data;
    Document document;
    int fields_count = 3;
    String[] field_names = new String[]{"ItemId", "Description", "UnitPrice"}; 

    cXML() throws Exception
    {
        fp_data = new File(DATAFILE);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        document = factory.newDocumentBuilder().parse(fp_data);
    }

    public void add_item(String[] data)
    {
        Element root = document.getDocumentElement();
        Element new_record = document.createElement("item");

        for (int counter = 0; counter < fields_count; counter++)
        {
            Element field = document.createElement(field_names[counter]);
            field.appendChild(document.createTextNode(data[counter]));
            new_record.appendChild(field);
        }
        root.appendChild(new_record);
        try
        {
            save_data_to_file();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void save_data_to_file() throws Exception
    {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer =  factory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult stream = new StreamResult(fp_data);
        transformer.transform(source, stream);
    }
    
    public ArrayList<String[]> get_all_items()
    {
        ArrayList<String[]>  records = new ArrayList<>();
        NodeList items = document.getElementsByTagName("item");

        for (int counter = 0; counter < items.getLength(); counter++)
        {
            Element record = (Element) items.item(counter);
            String[] item_details = new String[fields_count];

            for (int inner_counter = 0; inner_counter < fields_count; inner_counter++)
            {
                item_details[inner_counter] = record.getElementsByTagName(field_names[inner_counter]).item(0).getTextContent();
            }
            records.add(item_details);
        }
        return records;
    }
}