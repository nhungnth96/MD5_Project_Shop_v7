package md5.end.model.entity.order;

public enum ShippingType {
    ECONOMY(5),
    FAST(10),
    EXPRESS(15);

    private double price;

    ShippingType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
