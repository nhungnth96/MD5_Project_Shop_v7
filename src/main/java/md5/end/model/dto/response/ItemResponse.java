package md5.end.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemResponse {
    private String productName;
    private String price;
    private int quantity;
    private String amount;
}
