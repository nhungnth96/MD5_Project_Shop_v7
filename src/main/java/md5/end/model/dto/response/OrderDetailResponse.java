package md5.end.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetailResponse {
    private String buyerName;
    private String receiver;
    private String address;
    private String tel;
    private String note;
    private String total;
    private String shippingFee;
    private String subTotal;
    private String orderDate;
    private String status;
    private String payment;
    private String shipping;
    private List<ItemResponse> items;
}
