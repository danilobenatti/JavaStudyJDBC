package br.com.ecosensor.repository;

import java.sql.SQLException;
import java.util.List;

public interface Repository<T> {
	
	List<T> listAll();
	
	T searchById(Long id);
	
	void save(T t);
	
	void delete(Long id);
}
