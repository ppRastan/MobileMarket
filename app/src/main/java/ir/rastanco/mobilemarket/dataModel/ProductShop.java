package ir.rastanco.mobilemarket.dataModel;

/**
 * Created by ShaisteS on 1394/11/3.
 * This Class is Packaging Product that user shop from site
 */
public class ProductShop {

    private String invoiceNumber;
    private String timeStamp;
    private String invoiceImageLink;
    private String invoiceStatus;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getInvoiceImageLink() {
        return invoiceImageLink;
    }

    public void setInvoiceImageLink(String invoiceImageLink) {
        this.invoiceImageLink = invoiceImageLink;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}
