package com.ardent.epicreads.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showMyLoginPage(HttpServletRequest request, Model model) {

        // Check if user is not logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }

        // Check for success message in flash attributes
        String signupSuccessMessage = (String) request.getSession().getAttribute("signupSuccessMessage");

        if (signupSuccessMessage != null) {
            model.addAttribute("successMessage", signupSuccessMessage);

            // Remove the attribute so that it will not display again on refresh
            request.getSession().removeAttribute("signupSuccessMessage");
        }

        return "login";
    }

    // add request mapping for /access-denied

    @GetMapping("/access-denied")
    public String showAccessDenied() {

        return "access-denied";
    }

}










