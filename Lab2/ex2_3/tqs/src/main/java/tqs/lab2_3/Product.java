package tqs.lab2_3;

public class Product {
    private Integer id;
    private String title;
    private String description;
    private String category;
    private String image;
    private Double price;

    public Product() {
    }

    public Product(Integer id, String title, String description, String category, String image, Double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.image = image;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public Double getPrice() {
        return price;
    }
}
