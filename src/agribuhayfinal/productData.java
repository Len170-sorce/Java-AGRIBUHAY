/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agribuhayfinal;

/**
 *
 * @author PC
 */
public class productData {
    
    private String productID;
    private String productName;
    private String productCategory;
    private Integer productStock;
    private Double productCost;
    private Double productPrice;
    private String productStatus;
    
    public productData(String productID, String productName
            , String productCategory, Integer productStock
            , Double productCost, Double productPrice
            , String productStatus) {
        this.productID = productID;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productStock = productStock;
        this.productCost = productCost;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
    }
    
    public productData(String productID, String productName, Double productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
    }
    
    public String getProductID() {
	return productID;
    }

    public String getProductName() {
            return productName;
    }

    public String getProductCategory() {
            return productCategory;
    }

    public Integer getProductStock() {
            return productStock;
    }

    public Double getProductCost() {
            return productCost;
    }

    public Double getProductPrice() {
            return productPrice;
    }

    public String getProductStatus() {
            return productStatus;
    }
    
    private String customerID;
        private String customerName;
        private String costumerNo;
        private String currentAddress;
        private String deliveryAddress;
        private String customerType;

        // Constructor
        public productData(String customerID, String customerName, String costumerNo, String currentAddress, String deliveryAddress, String customerType) {
            this.customerID = customerID;
            this.customerName = customerName;
            this.costumerNo = costumerNo;
            this.currentAddress = currentAddress;
            this.deliveryAddress = deliveryAddress;
            this.customerType = customerType;
        }

        // Getters and Setters
        public String getCustomerID() {
            return customerID;
        }

        public void setCustomerID(String customerID) {
            this.customerID = customerID;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getContactNo() {
            return costumerNo;
        }

        public void setContactNo(String contactNo) {
            this.costumerNo = contactNo;
        }

        public String getCurrentAddress() {
            return currentAddress;
        }

        public void setCurrentAddress(String currentAddress) {
            this.currentAddress = currentAddress;
        }

        public String getDeliveryAddress() {
            return deliveryAddress;
        }

        public void setDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
        }

        public String getCustomerType() {
            return customerType;
        }

        public void setCustomerType(String customerType) {
            this.customerType = customerType;
        }
}

