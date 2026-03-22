package edu.bbte.idde.vnim2413.model;

public class UsedCarListing extends BaseEntity {
    String make;
    String model;
    int fabricationYear;
    double price;
    String uploadDate;

    public UsedCarListing(String make, String model, int fabricationYear, double price, String uploadDate) {
        super();
        this.make = make;
        this.model = model;
        this.fabricationYear = fabricationYear;
        this.price = price;
        this.uploadDate = uploadDate;
    }

    public UsedCarListing() {
        super();
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getFabricationYear() {
        return fabricationYear;
    }

    public void setFabricationYear(int fabricationYear) {
        this.fabricationYear = fabricationYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

}
