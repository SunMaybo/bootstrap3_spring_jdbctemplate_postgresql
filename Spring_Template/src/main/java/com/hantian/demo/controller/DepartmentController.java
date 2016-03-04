package com.hantian.demo.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hantian.demo.pojo.Department;
import com.hantian.demo.pojo.DepartmentTree;
import com.hantian.demo.service.IDepartmentService;

@Controller
@RequestMapping("/department")
public class DepartmentController {
	@Autowired
	private IDepartmentService departmentService;

	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Integer edit(@ModelAttribute Department department) {

		int status = 0;

		try {
			departmentService.saveOrUpdate(department);
			status = 1;
		} catch (Exception e) {
			e.printStackTrace();
			status = -1;
		}
		return status;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Integer delete(Integer id) {
		int status = 0;
		try {

			departmentService.delete(id);
			status = 1;
		} catch (Exception e) {
			e.printStackTrace();
			status = -1;
		}
		return status;
	}

	@RequestMapping("/list")
	@ResponseBody
	public List<Department> list() {
		List<Department> departments = departmentService.list();
		return departments;
	}

	@RequestMapping("/index")
	public ModelAndView index() {
		ModelAndView andView = new ModelAndView("department/index");
		return andView;
	}

	@RequestMapping("/listTree")
	@ResponseBody
	public List<DepartmentTree> listTree() {
		List<Department> departments = departmentService.list();
		List<DepartmentTree> departmentTrees = new ArrayList<DepartmentTree>();
		int i = 0;
		while (true) {
			int flag = 0;
			toDepartmentTrees(departmentTrees, departments.get(i));
			for (int j = 0; null != departments && j < departments.size(); j++) {
				if (departments.get(j).getId() >= 0) {
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				break;
			}
			i++;
			if (i >= departments.size()) {
				i = 0;
			}
		}

		return departmentTrees;
	}

	private void toDepartmentTrees(List<DepartmentTree> departmentTrees,
			Department department) {
		if (null == department) {
			return;
		} else {
			if (null == department.getParent()
					|| department.getParent().getId() == 0) {
				DepartmentTree departmentTree = new DepartmentTree();
				departmentTree.setTitle(department.getName());
				departmentTree.setId(department.getId());
				departmentTree.setKey(department.getId() + "");
				departmentTrees.add(departmentTree);
				department.setId(-1);
				return;
			} else {
				for (int i = 0; null != departmentTrees
						&& i < departmentTrees.size(); i++) {
					if (null != department.getParent()
							&& departmentTrees.get(i).getId() == department
									.getParent().getId()) {
						DepartmentTree departmentTree = new DepartmentTree();
						departmentTree.setTitle(department.getName());
						departmentTree.setKey(department.getId() + "");
						departmentTree.setId(department.getId());
						departmentTrees.get(i).getChildren()
								.add(departmentTree);
						department.setId(-1);

					} else {
						toDepartmentTrees(departmentTrees.get(i).getChildren(),
								department);
					}
				}
			}
		}
	}

	@RequestMapping("find")
	@ResponseBody
	public Department find(Integer id) {
		Department department = departmentService.findById(id);
		return department;
	}

	@RequestMapping("/editView")
	public ModelAndView editView(String state, Integer id) {
		ModelAndView modelAndView = new ModelAndView("/department/edit");
		modelAndView.addObject("state", state);
		modelAndView.addObject("id", id);
		return modelAndView;
	}
}
