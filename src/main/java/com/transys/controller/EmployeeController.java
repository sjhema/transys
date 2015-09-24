package com.transys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.Employee;
import com.transys.model.EmployeeStatus;
import com.transys.model.JobTitle;
import com.transys.model.SearchCriteria;
import com.transys.model.State;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/employee")
public class EmployeeController extends CRUDController<Employee> {
	
	public EmployeeController(){
		setUrlContext("employee");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(JobTitle.class, new AbstractModelEditor(JobTitle.class));
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
		binder.registerCustomEditor(EmployeeStatus.class, new AbstractModelEditor(EmployeeStatus.class));
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(Employee.class, criteria, "id", null, null));
		return urlContext + "/list";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(Employee.class, criteria));
		return urlContext + "/list";
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}	
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		Map criterias = new HashMap();
		model.addAttribute("employee", genericDAO.findByCriteria(Employee.class, criterias, "id", false));
				
//		super.create(model, request);
		return urlContext + "/form";
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("jobTitleValues", genericDAO.findByCriteria(JobTitle.class, criterias, "jobTitle", false));
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		model.addAttribute("employeeStatus", genericDAO.findByCriteria(EmployeeStatus.class, criterias, "status", false));
		model.addAttribute("employee", genericDAO.findByCriteria(Employee.class, criterias, "id", false));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Employee entity,
			BindingResult bindingResult, ModelMap model) {
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
		super.save(request, entity,  bindingResult, model);
		
		return urlContext + "/employee";
		
	}
	
}
