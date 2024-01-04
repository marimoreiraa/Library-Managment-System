package br.com.marianadmoreira.bibliotecaob.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.marianadmoreira.bibliotecaob.model.BookStatus;
import br.com.marianadmoreira.bibliotecaob.model.Loan;
import br.com.marianadmoreira.bibliotecaob.model.LoanStatus;
import br.com.marianadmoreira.bibliotecaob.model.UserModel;
import br.com.marianadmoreira.bibliotecaob.service.BookService;
import br.com.marianadmoreira.bibliotecaob.service.LoanService;
import br.com.marianadmoreira.bibliotecaob.service.UserService;

@Controller
public class LoanController {
    
    @Autowired
    LoanService loanService;

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @GetMapping("/loans")
    public String loans(Model model){
        
        var loans = this.loanService.activeLoans();
        var totalActiveLoans = loans.size();

        model.addAttribute("loans", loans);
        model.addAttribute("totalActiveLoans", totalActiveLoans);

        return "loans/loans";
    }

    @GetMapping("/loans/add")
    public String loanBook(Model model){
        Loan loan = new Loan();
        loan.setStatus(LoanStatus.REGULAR);
        loan.setLoanDate(LocalDate.now());
        loan.setReturnLimitDate(loan.getLoanDate().plusDays(10));
        loan.setFine(0.0);
        model.addAttribute("loan", loan);
        model.addAttribute("books", bookService.listBooks());
        model.addAttribute("users", userService.listUsers());
        return "loans/add";
    }

    @PostMapping("/loans/add")
    public String processLoanForm(@ModelAttribute Loan loan,Errors errors){
        if(errors.hasErrors()){
            return "add";
        }

        UserModel user = userService.searchUserById(loan.getUser().getIdUser());
        int qtdUserLoans = loanService.qtdLoansByUser(user.getIdUser());
        System.err.println(qtdUserLoans);
        if(loan.getBook().getStatus().equals(BookStatus.EMPRESTADO)){
            return "error/bookUnavailable";
        }
        else if(qtdUserLoans > Loan.LIMIT_BORROWS){
            return "error/userLoansOnLimit";
        }
        else{
           loan.getBook().setStatus(BookStatus.EMPRESTADO);
            loanService.addLoan(loan);
            return "redirect:/loans"; 
        }

        
    }

    @GetMapping("/myLoans/{userId}")
    public String myLoans(@PathVariable Long userId, Model model) {
        UserModel user = userService.searchUserById(userId);

        if (user != null) {
            var loans = loanService.loansByUser(user.getIdUser());
            var activeLoans = loanService.activeLoansByUser(user.getIdUser());
            var totalActiveLoans = activeLoans.size();
            var totalAvailableLoans = Loan.LIMIT_BORROWS - totalActiveLoans;

            model.addAttribute("loans", loans);
            model.addAttribute("activeLoans", activeLoans);
            model.addAttribute("totalActiveLoans", totalActiveLoans);
            model.addAttribute("totalAvailableLoans", totalAvailableLoans);
        } 
        
        return "loans/myLoans";
    }

    @GetMapping("/loans/return/{idLoan}")
    public String showReturnConfirmation(@PathVariable Long idLoan, Model model) {
        Optional<Loan> loan = loanService.searchLoanById(idLoan);

        model.addAttribute("loan", loan.orElse(null));
        return "loans/returnConfirmation";
    }

    @PostMapping("/loans/return/{idLoan}")
    public String returnLoan(@PathVariable Long idLoan) {
        Optional<Loan> loan = this.loanService.searchLoanById(idLoan);
        loanService.returnBook(loan);

        System.out.println(loan);
        return "redirect:/loans";
    }
    @GetMapping("/loans/overdue")
    public String overDueList(Model model){
        var loans = this.loanService.overdueLoans();
        var totalOverdue = loans.size();

        model.addAttribute("loans", loans);
        model.addAttribute("totalOverdue", totalOverdue);
        return "loans/overdue";
    }

    @GetMapping("/returns")
    public String returns(Model model){
        var loans = this.loanService.returnedLoans();
        var totalReturns = loans.size();

        model.addAttribute("loans", loans);
        model.addAttribute("totalReturns", totalReturns);

        return "returns/returns";
    }
    

    @GetMapping("/returns/overdue")
    public String overDueReturns(Model model){
        var loans = this.loanService.overdueReturned();
        var totalOverdue = loans.size();

        model.addAttribute("loans", loans);
        model.addAttribute("totalOverdue", totalOverdue);
        return "returns/overdue";
    }

    @GetMapping("/returns/onTime")
    public String onTimeReturns(Model model){
        var loans = this.loanService.onTimeReturned();
        var totalOnTime = loans.size();

        model.addAttribute("loans", loans);
        model.addAttribute("totalOnTime", totalOnTime);
        return "returns/onTime";
    }
}
