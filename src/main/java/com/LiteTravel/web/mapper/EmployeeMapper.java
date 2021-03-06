package com.LiteTravel.web.mapper;

import com.LiteTravel.web.Model.Employee;

import java.util.Collection;

public interface EmployeeMapper {

    public Employee getEmployeeById(Integer id);

    public void InsertEmployee(Employee employee);

    public Collection<Employee> getEmployees();

    public void updateEmployee(Employee employee);

    public void deleteEmployee(Integer id);
}
