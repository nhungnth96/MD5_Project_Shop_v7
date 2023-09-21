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
public class CartItemRequest {
//    @NotNull(message = "Must be have a value")
    private Long productId;
    private int quantity = 1;
    private Long userId;
}
