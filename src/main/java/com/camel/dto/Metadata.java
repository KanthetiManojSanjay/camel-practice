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
public class Metadata {
    private String fileName;
    private String initiatingChannel;
    private String postingPlatform;
    private String preDebited;
    private String additionalProperties;
    private String paymentJourney;
}
