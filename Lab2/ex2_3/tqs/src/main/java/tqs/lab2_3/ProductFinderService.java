package tqs.lab2_3;

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
        try {
            String response = httpClient.doHttpGet("https://fakestoreapi.com/products/" + id);

            if (response == null || response.isBlank() || response.trim().equals("{}")) {
                return Optional.empty();
            }

            JSONObject json = new JSONObject(response);

            // verifica se tem qualquer campo obrigatório)
            if (!json.has("title")) {
                return Optional.empty();
            }

            Product product = new Product(
                    json.getInt("id"),
                    json.getString("title"),
                    json.getString("description"),
                    json.getString("category"),
                    json.getString("image"),
                    json.getDouble("price")
            );

            return Optional.of(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }



}
