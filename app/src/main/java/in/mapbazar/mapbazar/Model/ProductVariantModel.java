package in.mapbazar.mapbazar.Model;

public class ProductVariantModel {

    String id, product_id, attribute_name, attribute_value, attribute_mrp, attribute_rewards;

    public ProductVariantModel() {
    }

    public ProductVariantModel(String id, String product_id, String attribute_name, String attribute_value, String attribute_mrp, String attribute_rewards) {
        this.id = id;
        this.product_id = product_id;
        this.attribute_name = attribute_name;
        this.attribute_value = attribute_value;
        this.attribute_mrp = attribute_mrp;
        this.attribute_rewards = attribute_rewards;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public String getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }

    public String getAttribute_mrp() {
        return attribute_mrp;
    }

    public void setAttribute_mrp(String attribute_mrp) {
        this.attribute_mrp = attribute_mrp;
    }

    public String getAttribute_rewards() {
        return attribute_rewards;
    }

    public void setAttribute_rewards(String attribute_rewards) {
        this.attribute_rewards = attribute_rewards;
    }
}