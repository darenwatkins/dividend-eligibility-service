package com.custody.eligibility.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String clientId;
    private String clientName;
    private String domicile;
    private ClientType clientType;
    private boolean taxExempt;
}
