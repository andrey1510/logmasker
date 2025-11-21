package com.logtest.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardEntity {

    private Integer id;

    private String cardNumber;

    private String description;

}
