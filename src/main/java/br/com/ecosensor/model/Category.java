package br.com.ecosensor.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Category {
	
	@EqualsAndHashCode.Include
	private Long id;
	private String name;
	@Builder.Default
	@ToString.Exclude
	private List<Product> products = new ArrayList<>();
	private Date dateCreate;
	private Date dateUpdate;
}
