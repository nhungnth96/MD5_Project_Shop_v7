package md5.end.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoginForm {
    @NotEmpty(message = "Must be not empty.")
    @Pattern(regexp = "\\S+", message = "Containing whitespaces.")
    @Pattern(regexp = "^[a-zA-Z0-9._#?!@$%^&*-]{4,15}$",message = "Username must be at least 4 characters and not over 15 characters.")
    private String username;

    @NotEmpty(message = "Must be not empty")
    @Pattern(regexp = "\\S+", message = "Containing whitespaces.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,12}$",message = "Password must be at least 6 characters, not over 12 characters. include 1 uppercase letter, 1 lowercase letter and 1 symbol character.")
    private String password;
}
