package com.bytter.biz.inneraccount.dto.v1;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class FcInitAccCheckDTO {
    private String ids;

    private String status;
}
