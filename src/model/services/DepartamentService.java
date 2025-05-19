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
	
	public void saveOrUpdate (Departament obj) {
		if(obj.getId()==null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Departament obj) {
		dao.deleteById(obj.getId());
	}

}
