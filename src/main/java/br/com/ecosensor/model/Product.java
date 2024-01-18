package br.com.ecosensor.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Product {
	
	@EqualsAndHashCode.Include
	private Long id;
	private String name;
	private String description;
	private Float price = 0F;
	private Category category;
	private Date dateCreate;
	private Date dateUpdate;
	
}
