package md5.end.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEditForm {
    @NotEmpty(message = "Must be not empty.")
    private String fullName;

    private String birthday;

    @Pattern(regexp = "\\S+", message = "Containing whitespaces.")
    @Pattern(regexp = "^[A-Za-z0-9]+[A-Za-z0-9._%+-]*@[a-z]+(\\.[a-z]+)$", message = "Invalid email format.")
    private String email;

    @Pattern(regexp = "\\S+", message = "Containing whitespaces.")
    @Pattern(regexp = "^0\\d{9}$",message = "Invalid phone number format.")
    private String tel;

    private String address;


}
