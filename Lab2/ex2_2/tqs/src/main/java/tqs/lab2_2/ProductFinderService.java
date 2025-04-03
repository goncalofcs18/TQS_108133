package tqs.lab2_2;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.Optional;

public class ProductFinderService {

    private ISimpleHttpClient httpClient;
    private final static String API_PRODUCTS = "https://fakestoreapi.com/products/";

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<Product> findProductDetails(int id) {
        String response = httpClient.doHttpGet(API_PRODUCTS + id);
        if (response == null || response.isEmpty()) {
            return Optional.empty();
        }

        try {
            JSONObject json = new JSONObject(response);
            Product product = new Product();

            product.setId(json.getInt("id"));
            product.setTitle(json.getString("title"));
            product.setDescription(json.getString("description"));
            product.setCategory(json.getString("category"));
            product.setImage(json.getString("image"));
            product.setPrice(json.getDouble("price")); // conversão correta

            return Optional.of(product);

        } catch (JSONException e) {
            return Optional.empty();
        }
    }
}
