package br.com.ecosensor.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Category {
	
	@EqualsAndHashCode.Include
	private Long id;
	private String name;
	@ToString.Exclude
	private List<Product> products = new ArrayList<>();
	private Date dateCreate;
	private Date dateUpdate;
}
