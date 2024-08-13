package org.example.mvcobjectmapper.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Имя должно быть заполнено")
    @Column(unique = true)
    private String name;

    @NotBlank(message = "Описание должно быть заполнено")
    private String description;

    @Positive(message = "Цена должна быть больше нуля")
    private BigDecimal price;

    @Min(value = 0, message = "Количество должно быть 0 или более")
    private int quantityInStock;

}
