package tqs.lab2_3;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductFinderServiceIT {

    @Test
    public void testFindProductDetails_validId() throws IOException {
        ISimpleHttpClient httpClient = new TqsBasicHttpClient();
        ProductFinderService service = new ProductFinderService(httpClient);

        Optional<Product> result = service.findProductDetails(3);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(3);
        assertThat(result.get().getTitle()).isEqualTo("Mens Cotton Jacket");
    }

    @Test
    public void testFindProductDetails_invalidId() throws IOException {
        ISimpleHttpClient httpClient = new TqsBasicHttpClient();
        ProductFinderService service = new ProductFinderService(httpClient);

        Optional<Product> result = service.findProductDetails(300); // ID inválido

        assertThat(result).isEmpty();
    }
}
