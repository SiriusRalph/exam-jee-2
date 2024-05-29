
package Controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.Employee;
import Service.EmployeeService;

import javax.inject.Inject;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/employees")
public class EmployeeController extends HttpServlet {

    @Inject
    private EmployeeService employeeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Employee employee = objectMapper.readValue(request.getInputStream(), Employee.class);
        employeeService.save(employee);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(employeeService.findAll()));
        } else {
            Long id = Long.valueOf(pathInfo.substring(1));
            Employee employee = employeeService.findById(id);
            if (employee != null) {
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(employee));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
}
