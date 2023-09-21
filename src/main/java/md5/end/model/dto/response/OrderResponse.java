package md5.end.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import md5.end.model.entity.order.OrderDetail;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private String orderDate;
    private String buyer;
    private String note;
    private String total;
    private String payment;
    private String shipping;
    private String status;
}
