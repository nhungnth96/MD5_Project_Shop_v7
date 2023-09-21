package md5.end.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderRequest {
//    @NotEmpty (message = "Must be not empty")
    private String receiver;
//    @NotEmpty (message = "Must be not empty")
    private String address;
//    @NotEmpty (message = "Must be not empty")
    private String tel;
    private String note;
//    @NotNull(message = "Must be have a value.")
    private Long paymentId;
//    @NotNull(message = "Must be have a value.")
    private Long shippingId;

}
