package md5.end.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderStatusUpdate {
    @Min(value = 1, message = "Value must be from 1-4")
    @Max(value = 4, message = "Value must be from 1-4")
    private int statusCode;
}
