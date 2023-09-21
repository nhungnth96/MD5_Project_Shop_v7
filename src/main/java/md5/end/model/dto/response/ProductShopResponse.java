package md5.end.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductShopResponse {
    private Long id;
    private String brandName;
    private String categoryName;
    private String productName;
    private String price;

}
