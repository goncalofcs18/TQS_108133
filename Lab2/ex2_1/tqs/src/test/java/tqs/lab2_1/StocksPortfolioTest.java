package tqs.lab2_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StocksPortfolioTest {

    @Mock
    private IStockMarketService market;

    private StocksPortfolio portfolio;

    @BeforeEach
    public void setUp() {
        portfolio = new StocksPortfolio();
        portfolio.setMarketService(market);
    }

    @Test
    public void testGetTotalValue() {
        Stock microsoft = new Stock("MSFT", 2);
        Stock apple = new Stock("AAPL", 3);

        portfolio.addStock(microsoft);
        portfolio.addStock(apple);

        when(market.lookUpPrice("MSFT")).thenReturn(250.0);
        when(market.lookUpPrice("AAPL")).thenReturn(100.0);

        double result = portfolio.getTotalValue();

        assertThat(result).isEqualTo(250.0 * 2 + 100.0 * 3);

        verify(market, times(1)).lookUpPrice("MSFT");
        verify(market, times(1)).lookUpPrice("AAPL");
    }

    @Test
    public void testMostValuableStocks() {
        Stock s1 = new Stock("TSLA", 2);   // 2 × 800 = 1600
        Stock s2 = new Stock("AMZN", 5);   // 5 × 150 = 750
        Stock s3 = new Stock("GOOG", 3);   // 3 × 300 = 900
        Stock s4 = new Stock("NFLX", 1);   // 1 × 200 = 200

        portfolio.addStock(s1);
        portfolio.addStock(s2);
        portfolio.addStock(s3);
        portfolio.addStock(s4);

        when(market.lookUpPrice("TSLA")).thenReturn(800.0);
        when(market.lookUpPrice("AMZN")).thenReturn(150.0);
        when(market.lookUpPrice("GOOG")).thenReturn(300.0);
        when(market.lookUpPrice("NFLX")).thenReturn(200.0);

        List<Stock> top2 = portfolio.mostValuableStocks(2);

        assertThat(top2).hasSize(2);
        assertThat(top2.get(0).getLabel()).isEqualTo("TSLA");
        assertThat(top2.get(1).getLabel()).isEqualTo("GOOG");
    }

    @Test
    public void testTopNMoreThanPortfolioSize() {
        Stock s1 = new Stock("A", 1);
        Stock s2 = new Stock("B", 2);

        portfolio.addStock(s1);
        portfolio.addStock(s2);

        when(market.lookUpPrice("A")).thenReturn(100.0); // total = 100
        when(market.lookUpPrice("B")).thenReturn(200.0); // total = 400

        List<Stock> top5 = portfolio.mostValuableStocks(5); // só temos 2 ações

        assertThat(top5).hasSize(2);
        assertThat(top5.get(0).getLabel()).isEqualTo("B");
        assertThat(top5.get(1).getLabel()).isEqualTo("A");
    }

    @Test
    public void testMostValuableStocksEmptyPortfolio() {
        List<Stock> result = portfolio.mostValuableStocks(3);
        assertThat(result).isEmpty();
    }

    @Test
    public void testMostValuableStocksWithZero() {
        Stock s = new Stock("X", 10);
        portfolio.addStock(s);
        List<Stock> result = portfolio.mostValuableStocks(0);
        assertThat(result).isEmpty();
    }

    @Test
    public void testMostValuableStocksWithTies() {
        Stock s1 = new Stock("A", 2);
        Stock s2 = new Stock("B", 4);
        Stock s3 = new Stock("C", 1);

        portfolio.addStock(s1);
        portfolio.addStock(s2);
        portfolio.addStock(s3);

        when(market.lookUpPrice("A")).thenReturn(100.0);
        when(market.lookUpPrice("B")).thenReturn(50.0);
        when(market.lookUpPrice("C")).thenReturn(100.0);

        List<Stock> top2 = portfolio.mostValuableStocks(2);

        assertThat(top2).containsExactly(s1, s2);
    }



}
