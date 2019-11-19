package in.mapbazar.mapbazar.Model.RecentlyProduct;

import java.io.Serializable;

public class RevcentlyProductAttributeItem implements Serializable {
	private String attributeId;
	private String attributeName;
	private String attributeValue;

	public void setAttributeId(String attributeId){
		this.attributeId = attributeId;
	}

	public String getAttributeId(){
		return attributeId;
	}

	public void setAttributeName(String attributeName){
		this.attributeName = attributeName;
	}

	public String getAttributeName(){
		return attributeName;
	}

	public void setAttributeValue(String attributeValue){
		this.attributeValue = attributeValue;
	}

	public String getAttributeValue(){
		return attributeValue;
	}

}
