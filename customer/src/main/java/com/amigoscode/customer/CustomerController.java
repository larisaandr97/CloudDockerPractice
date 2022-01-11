package com.amigoscode.customer;

import com.amigoscode.customer.dto.CustomerRequest;
import com.amigoscode.customer.entity.Customer;
import com.amigoscode.customer.entity.Role;
import com.amigoscode.customer.mapper.CustomerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("new customer registration {}", customerRegistrationRequest);
        customerService.registerCustomer(customerRegistrationRequest);
    }

    @GetMapping("/authUser")
    public String getAuthenticatedUser(Principal principal) {
        Customer user = (Customer) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        log.info("Getting authenticated user... {}", user.getUsername());
        return user.getUsername();
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new Customer());
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
        }
        return "login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String registerForm(Model model, HttpServletRequest request) {
        model.addAttribute("userRequest", new Customer());
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
        }
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid
                                 @RequestBody
                                 @ModelAttribute
                                         CustomerRequest customerRequest,
                                 BindingResult bindingResult) {
        ModelAndView viewRegister = new ModelAndView("register");
        if (bindingResult.hasErrors()) {
            return viewRegister;
        }
        Customer customer = customerMapper.customerRequestToCustomer(customerRequest);
        customer.setRole(new Role(1, "ROLE_USER"));
        customer.setEnabled(true);
        customerService.registerNewCustomer(customer);
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("user", new Customer());
        return modelAndView;
    }
}
