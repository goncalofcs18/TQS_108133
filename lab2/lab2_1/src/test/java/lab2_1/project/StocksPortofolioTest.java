package lab2_1.project;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * StocksPortofolio
 */
public class StocksPortofolioTest {

	private StocksPortofolio portofolio;
	private IStockMarketService service;

	@BeforeEach
	public void SetUp() throws Exception {
		service = mock(IStockMarketService.class);
		portofolio = new StocksPortofolio(service);
	}

	@Test
	public void totalValueTest() {
		// arrange
		when(service.lookUpPrice("batatas")).thenReturn(5.0);
		when(service.lookUpPrice("queijo")).thenReturn(2.0);

		// act
		portofolio.addStock(new Stock("batatas", 5));
		portofolio.addStock(new Stock("queijo", 1));
		double price = portofolio.totalValue();

		// assert
		assertNotNull(price);
		assertEquals(27.00, price);
			

	}


}