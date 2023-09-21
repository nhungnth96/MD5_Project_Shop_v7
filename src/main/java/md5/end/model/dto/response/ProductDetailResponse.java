package md5.end.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDetailResponse {
    private Long id;
    private String brandName;
    private String categoryName;
    private String productName;
    private String description;
    private String price;
    private Map<String,String> specifications;
    private String image;
    private List<String> subImages;
}
