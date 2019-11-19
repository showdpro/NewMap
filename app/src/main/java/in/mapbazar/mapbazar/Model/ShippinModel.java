package in.mapbazar.mapbazar.Model;

public class ShippinModel {

    String min_quantity,max_quantity,shipping_charges;

    public ShippinModel() {
    }

    public ShippinModel(String min_quantity, String max_quantity, String shipping_charges) {
        this.min_quantity = min_quantity;
        this.max_quantity = max_quantity;
        this.shipping_charges = shipping_charges;
    }

    public String getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(String min_quantity) {
        this.min_quantity = min_quantity;
    }

    public String getMax_quantity() {
        return max_quantity;
    }

    public void setMax_quantity(String max_quantity) {
        this.max_quantity = max_quantity;
    }

    public String getShipping_charges() {
        return shipping_charges;
    }

    public void setShipping_charges(String shipping_charges) {
        this.shipping_charges = shipping_charges;
    }
}
