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
public class Account {
    private String accountName;
    private String identification;
    private String schemeName;
    private String address;
}
