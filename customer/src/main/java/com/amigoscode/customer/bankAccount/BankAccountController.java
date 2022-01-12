package com.amigoscode.customer.bankAccount;

import com.amigoscode.customer.entity.Customer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@Validated
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountMapper bankAccountMapper;
    private final BankAccountServiceImpl bankAccountService;

    public BankAccountController(BankAccountMapper bankAccountMapper, BankAccountServiceImpl bankAccountService) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/new")
    public String newAccount(Model model) {
        model.addAttribute("bankAccountRequest", new BankAccountRequest());
        return "addBankAccount";
    }

    @PostMapping()
    public ModelAndView createBankAccount(
            @Valid
            @RequestBody
            @ModelAttribute
                    BankAccountRequest bankAccountRequest,
            BindingResult bindingResult,
            Principal principal) {
        ModelAndView modelAndView = new ModelAndView("bankAccounts");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }
        Customer customer = (Customer) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        bankAccountRequest.setCustomer(customer);
        BankAccount bankAccount = bankAccountMapper.bankAccountRequestToBankAccount(bankAccountRequest);
        bankAccountService.createBankAccount(bankAccount);

        List<BankAccount> accountsFound = bankAccountService.getBankAccountsForUser(customer);
        modelAndView.addObject("accounts", accountsFound);
        return modelAndView;
    }

    @GetMapping()
    public ModelAndView getBankAccountsForUser(Principal principal) {
        Customer customer = (Customer) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        ModelAndView modelAndView = new ModelAndView("bankAccounts");
        List<BankAccount> accountsFound = bankAccountService.getBankAccountsForUser(customer);
        modelAndView.addObject("accounts", accountsFound);
        return modelAndView;
    }

}
