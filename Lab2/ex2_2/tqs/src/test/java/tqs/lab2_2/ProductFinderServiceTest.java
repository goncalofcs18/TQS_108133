package tqs.lab2_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductFinderServiceTest {

    @Mock
    private ISimpleHttpClient httpClient;

    @InjectMocks
    private ProductFinderService service;

    @BeforeEach
    void setUp() {
        service = new ProductFinderService(httpClient);
    }

    @Test
    void whenProductExists_thenReturnValidProduct() {
        String json = """
            {
              "id": 3,
              "title": "Mens Cotton Jacket",
              "price": 55.99,
              "description": "Great jacket for any season",
              "category": "men's clothing",
              "image": "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg"
            }
        """;

        Mockito.lenient().when(httpClient.doHttpGet("https://fakestoreapi.com/products/3"))
                .thenReturn(json);

        Optional<Product> result = service.findProductDetails(3);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(3);
        assertThat(result.get().getTitle()).isEqualTo("Mens Cotton Jacket");
    }

    @Test
    void whenProductDoesNotExist_thenReturnEmpty() {
        String notFoundJson = "{}";

        Mockito.lenient().when(httpClient.doHttpGet("https://fakestoreapi.com/products/300"))
                .thenReturn(notFoundJson);

        Optional<Product> result = service.findProductDetails(300);

        assertThat(result).isEmpty();
    }
}
