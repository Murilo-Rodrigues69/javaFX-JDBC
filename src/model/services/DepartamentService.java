package model.services;


import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentDao;

import model.entities.Departament;

public class DepartamentService {
	
	
	private DepartamentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Departament> findAll(){
		return dao.findAll();
	}

}
