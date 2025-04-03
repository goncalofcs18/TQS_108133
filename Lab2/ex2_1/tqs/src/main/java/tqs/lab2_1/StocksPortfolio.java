package tqs.lab2_1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class StocksPortfolio {
    private IStockMarketService market;
    private List<Stock> stocks = new ArrayList<>();

    public void setMarketService(IStockMarketService market) {
        this.market = market;
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public double getTotalValue() {
        return stocks.stream()
                .mapToDouble(stock -> stock.getQuantity() * market.lookUpPrice(stock.getLabel()))
                .sum();
    }

    public List<Stock> mostValuableStocks(int topN) {
        return stocks.stream()
                .sorted((s1, s2) -> {
                    double value1 = s1.getQuantity() * market.lookUpPrice(s1.getLabel());
                    double value2 = s2.getQuantity() * market.lookUpPrice(s2.getLabel());
                    return Double.compare(value2, value1); // descending
                })
                .limit(topN)
                .collect(Collectors.toList());
    }


}
