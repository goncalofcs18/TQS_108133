package lab2_1.project;

import java.util.ArrayList;
import java.util.List;

public class StocksPortofolio {

    private IStockMarketService service;
    private List<Stock> stocks;



    public StocksPortofolio(IStockMarketService service){
        this.service = service;
        this.stocks = new ArrayList<Stock>();
        
    }

    public void addStock(Stock stock){

        stocks.add(stock);
    }

    public double totalValue(){
        double totalValue = 0.00;
        for (Stock stock: stocks) {
            totalValue = totalValue + stock.getQuantity()*service.lookUpPrice((stock.getLabel()));
        }
        return totalValue;
    }

    
}
