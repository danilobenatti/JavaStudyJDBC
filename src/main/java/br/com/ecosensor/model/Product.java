package br.com.ecosensor.model;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(builderMethodName = "productBuilder", setterPrefix = "with")
public class Product {
	
	@EqualsAndHashCode.Include
	private Long id;
	private String name;
	private String description;
	@Builder.Default
	private Float price = 0F;
	private Category category;
	private Date dateCreate;
	private Date dateUpdate;
	private String sku;
}


