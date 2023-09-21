package md5.end.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CategoryRequest {
//    @NotEmpty(message = "Must be not empty")
    private String name;

    private Long parentCategoryId;

}
