package in.mapbazar.mapbazar.Model;

import java.util.List;

public class PinModel {

    String picode,area;
    List<ShippinModel> shippinModelList;

    public PinModel() {
    }

    public PinModel(String picode, String area, List<ShippinModel> shippinModelList) {
        this.picode = picode;
        this.area = area;
        this.shippinModelList = shippinModelList;
    }

    public String getPicode() {
        return picode;
    }

    public void setPicode(String picode) {
        this.picode = picode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<ShippinModel> getShippinModelList() {
        return shippinModelList;
    }

    public void setShippinModelList(List<ShippinModel> shippinModelList) {
        this.shippinModelList = shippinModelList;
    }
}
