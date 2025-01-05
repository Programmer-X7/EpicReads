package com.ardent.epicreads.controller;

import java.util.logging.Logger;

import com.ardent.epicreads.entity.User;
import com.ardent.epicreads.service.UserService;
import com.ardent.epicreads.dto.WebUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	private Logger logger = Logger.getLogger(getClass().getName());

    private final UserService userService;

	@Autowired
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping
	public String showRegistrationForm(Model theModel) {

		// Check if user is not logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:/";
		}

		theModel.addAttribute("webUser", new WebUser());

		return "register/registration-form";
	}

	@PostMapping
	public String processRegistrationForm(
			@Valid @ModelAttribute("webUser") WebUser theWebUser,
			BindingResult theBindingResult,
			RedirectAttributes redirectAttributes,
			HttpSession session, Model theModel) {

		String userName = theWebUser.getUserName();
		logger.info("Processing registration form for: " + userName);

		// form validation
		 if (theBindingResult.hasErrors()){
			 return "register/registration-form";
		 }

		// check the database if user already exists
        User existing = userService.findByUserName(userName);
        if (existing != null){
        	theModel.addAttribute("webUser", new WebUser());
			theModel.addAttribute("registrationError", "User name already exists.");

			logger.warning("User name already exists.");
        	return "register/registration-form";
        }

        // create user account and store in the database
        userService.save(theWebUser);

        logger.info("Successfully created user: " + userName);

		// place user in the web http session for later use
		session.setAttribute("user", theWebUser);

		// Add success message to redirect attributes
		redirectAttributes.addFlashAttribute("signupSuccessMessage", "You have successfully signed up. Please log in.");

//        return "register/registration-confirmation";
        return "redirect:/login";
	}
}
