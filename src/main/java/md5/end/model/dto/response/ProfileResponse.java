package md5.end.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProfileResponse {
    private String fullName;
    private String birthday;
    private String email;
    private String tel;
    private String address;
}
