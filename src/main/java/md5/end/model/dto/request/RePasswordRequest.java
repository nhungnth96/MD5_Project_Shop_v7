package md5.end.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RePasswordRequest {
    @NotEmpty (message = "must not be empty")
    @Pattern(regexp = "\\S+", message = "Containing whitespaces")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,12}$",message = "Password must be at least 6 characters, not over 12 characters. include 1 uppercase letter, 1 lowercase letter and 1 symbol character.")
    private String oldPass;

    @NotEmpty (message = "must not be empty")
    @Pattern(regexp = "\\S+", message = "Containing whitespaces")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,12}$",message = "Password must be at least 6 characters, not over 12 characters. include 1 uppercase letter, 1 lowercase letter and 1 symbol character.")
    private String newPass;

    @NotEmpty (message = "must not be empty")
    @Pattern(regexp = "\\S+", message = "Containing whitespaces")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,12}$",message = "Password must be at least 6 characters, not over 12 characters. include 1 uppercase letter, 1 lowercase letter and 1 symbol character.")

    private String rePass;

}
