package in.mapbazar.mapbazar.Model.MatchWithProduct;

import java.io.Serializable;
import java.util.List;

public class MatchProductItem implements Serializable {
	private List<ProductImageItem> productImage;
	private List<MatchProductAttributeItem> productAttribute;
	private String productStatus;
	private List<MatchProductSizeItem> productSize;
	private String productColor;
	private String productPrice;
	private String productName;
	private String product_name_hindi;
	private String productPrimaryImage;
	private String isProductCustomizable;
	private String productId;
	private String productOldPrice;
	private String productStock;
	private String productBrand;
	private String productDescription;
	private int favoriteFlag;
	private String quantity;
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}


	public int getSelectedProductItem() {
		return selectedProductItem;
	}

	public void setSelectedProductItem(int selectedProductItem) {
		this.selectedProductItem = selectedProductItem;
	}

	private int selectedProductItem = -1;

	public void setProductImage(List<ProductImageItem> productImage){
		this.productImage = productImage;
	}

	public List<ProductImageItem> getProductImage(){
		return productImage;
	}

	public void setProductAttribute(List<MatchProductAttributeItem> productAttribute){
		this.productAttribute = productAttribute;
	}

	public List<MatchProductAttributeItem> getProductAttribute(){
		return productAttribute;
	}

	public void setProductStatus(String productStatus){
		this.productStatus = productStatus;
	}

	public String getProductStatus(){
		return productStatus;
	}

	public void setProductSize(List<MatchProductSizeItem> productSize){
		this.productSize = productSize;
	}

	public List<MatchProductSizeItem> getProductSize(){
		return productSize;
	}

	public void setProductColor(String productColor){
		this.productColor = productColor;
	}

	public String getProductColor(){
		return productColor;
	}

	public void setProductPrice(String productPrice){
		this.productPrice = productPrice;
	}

	public String getProductPrice(){
		return productPrice;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setProductPrimaryImage(String productPrimaryImage){
		this.productPrimaryImage = productPrimaryImage;
	}

	public String getProductPrimaryImage(){
		return productPrimaryImage;
	}

	public void setIsProductCustomizable(String isProductCustomizable){
		this.isProductCustomizable = isProductCustomizable;
	}

	public String getIsProductCustomizable(){
		return isProductCustomizable;
	}

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setProductOldPrice(String productOldPrice){
		this.productOldPrice = productOldPrice;
	}

	public String getProductOldPrice(){
		return productOldPrice;
	}

	public void setProductStock(String productStock){
		this.productStock = productStock;
	}

	public String getProductStock(){
		return productStock;
	}

	public void setProductBrand(String productBrand){
		this.productBrand = productBrand;
	}

	public String getProductBrand(){
		return productBrand;
	}

	public void setProductDescription(String productDescription){
		this.productDescription = productDescription;
	}

	public String getProductDescription(){
		return productDescription;
	}

	public void setFavoriteFlag(int favoriteFlag){
		this.favoriteFlag = favoriteFlag;
	}

	public int getFavoriteFlag(){
		return favoriteFlag;
	}


	public String getProduct_name_hindi() {
		return product_name_hindi;
	}

	public void setProduct_name_hindi(String product_name_hindi) {
		this.product_name_hindi = product_name_hindi;
	}
}