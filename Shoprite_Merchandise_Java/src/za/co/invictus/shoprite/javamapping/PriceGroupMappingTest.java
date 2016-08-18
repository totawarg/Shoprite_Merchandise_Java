package za.co.invictus.shoprite.javamapping;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

import za.co.invictus.shoprite.MerchandiseException;

public class PriceGroupMappingTest {

	public static void main(String[] args) throws ParseException, IOException, MerchandiseException {
		// TODO Auto-generated method stub
		FileInputStream in=new FileInputStream("C:/Users/i045193/Documents/Shoprite/560/idoc1.xml");
		FileOutputStream out=new FileOutputStream("C:/Users/i045193/Documents/Shoprite/560/idoc1out.xml");
		PriceGroupJavaMapping pgjm=new PriceGroupJavaMapping();
		pgjm.execute(in, out);
		in.close();
		out.close();

	}

}
