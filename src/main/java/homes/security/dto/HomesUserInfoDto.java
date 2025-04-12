package homes.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class HomesUserInfoDto {
    private long     userNo ;
    private String   email ;
    private String   name ;
//    private String   password ;
    private String   role;
}


