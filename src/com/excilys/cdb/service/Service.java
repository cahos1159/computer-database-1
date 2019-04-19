package com.excilys.cdb.service;

import java.util.*;
import java.util.stream.Collectors;

import com.excilys.cdb.dao.Dao;
import com.excilys.cdb.dto.Dto;
import com.excilys.cdb.exception.InvalidIntegerException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.model.Model;


public abstract class Service<T extends Dto, U extends Model> {
	protected Mapper<T, U> mapper;
	protected Dao<U> dao;
	
	protected Service(Mapper<T, U> m, Dao<U> d) {
		this.mapper = m;
		this.dao = d;
	}
	
	public T create(T dtoObject) throws Exception {
		return this.mapper.modelToDto(this.dao.create(this.mapper.dtoToModel(dtoObject)));
	};
	
	public T update(T dtoObject) throws Exception {
		return this.mapper.modelToDto(this.dao.update(this.mapper.dtoToModel(dtoObject)));
	};
	
	public T delete(T dtoObject) throws Exception {
		return this.mapper.modelToDto(this.dao.delete(this.mapper.dtoToModel(dtoObject)));
	};
	
	public T read(String id) throws Exception {
		return this.mapper.modelToDto(this.dao.read(this.mapper.idToInt(id)));
	};
	
	public List<T> listAllElements() throws Exception {
		List<U> modelList = this.dao.listAll();
		return (List<T>) modelList.stream().map(s -> mapper.modelToDto(s)).collect(Collectors.toList());
	};
	
	public List<T> list(String pageStr, String sizeStr) throws Exception {
		int page,size;
		
		try {
			page = Integer.parseInt(pageStr);
		} catch (IllegalArgumentException e) {
			throw new InvalidIntegerException(pageStr);
		}
		try {
			size = Integer.parseInt(sizeStr);
		} catch (IllegalArgumentException e) {
			throw new InvalidIntegerException(sizeStr);
		}
		
		List<U> modelList = this.dao.list(page,size);
		return (List<T>) modelList.stream().map(s -> mapper.modelToDto(s)).collect(Collectors.toList());
	}
}
