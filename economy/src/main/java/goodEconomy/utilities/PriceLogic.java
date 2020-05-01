package goodEconomy.utilities;

public class PriceLogic {
	public static String SellLogic(String howmuchsell, int currentprice )  {
		int newprice = Integer.parseInt(howmuchsell) * 5 ;
		newprice = currentprice - newprice;
		return String.valueOf(newprice);
		//y = mx +b
	}
	public static String BuyLogic(String howmuchsell, int currentprice )  {
		int newprice = Integer.parseInt(howmuchsell) * 5 ;
		newprice += currentprice;
		return String.valueOf(newprice);
		//y = mx +b
	}
}
