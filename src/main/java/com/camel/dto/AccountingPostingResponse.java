package com.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kansanja on 09/02/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingPostingResponse {
    private String code;
    private String description;
}
