package goodEconomy.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceLogic {
	int howmuchsell;
	Double currentprice;
	Double finalcost;
	int intgerprice;
	public PriceLogic(int ammount, Double price) {
		howmuchsell = ammount;
		currentprice = price;
		finalcost=0.00;
		intgerprice = price.intValue();
	}
	// Cheers https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	// TODO: Add a sperate function that does a for loop adding value each loop
	public void doSell() {
		currentprice = this.SellLogic();
		for( int i=1; i<= howmuchsell; i++) {
			currentprice = new PriceLogic(howmuchsell, currentprice).SellLogic();
			finalcost += currentprice;
			
		}
	}
	public  Double SellLogic()  {
		Double newprice = currentprice * 0.98 ;
		return newprice;
		//y = mx +b
	}
	
	public void doBuy() {
		currentprice = this.BuyLogic();
		for( int i=1; i<= howmuchsell; i++) {
			currentprice = new PriceLogic(howmuchsell, currentprice).BuyLogic();
			finalcost += currentprice;
			
		}
	}
	// TODO: Add a sperate function that does a for loop adding value each loop
	public  Double BuyLogic()  {
		Double newprice = currentprice *  1.02;
		return newprice;
		//y = mx +b
	}
	// TODO: Add a getFinalPrice method						
	public Double getFinalPrice() {
		Double newfinalcost = round(finalcost, 2);
		return newfinalcost;
	}
	// TODO: Add get getNewPrice method
	public Double getNewPrice() {
		Double newcurrentprice = round(currentprice, 2);
		return newcurrentprice;
	}
	public Boolean checkInteger() {
		int check = intgerprice- 1;
		if (check >= 0 ) {
			return true;
		} else {
			return false;
		}
	}
}
