package com.example.publicoffering.ipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ipo {

    private String ipoName;
    private String offeringPeriod;
    private String finalPrice;
    private String expectedPrice;
    private String company;
    private String link;
}
