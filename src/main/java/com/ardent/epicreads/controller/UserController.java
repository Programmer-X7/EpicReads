package com.ardent.epicreads.controller;

import com.ardent.epicreads.dto.WebUser;
import com.ardent.epicreads.entity.User;
import com.ardent.epicreads.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    public final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getProfileDetails(HttpSession session, Model model) {
        User theUser = (User) session.getAttribute("user");
        if (theUser != null) {
            User user = userService.findByUserName(theUser.getUserName());
            model.addAttribute("user", user);
            model.addAttribute("editable", false);
        }
        return "user-profile";
    }

    @GetMapping("/edit")
    public String getEditProfileForm(HttpSession session, Model model) {
        User theUser = (User) session.getAttribute("user");
        if (theUser != null) {
            User user = userService.findByUserName(theUser.getUserName());
            model.addAttribute("user", user);
            model.addAttribute("editable", true);
        }
        return "edit-user-profile-form";
    }

    @PostMapping("/edit")
    public String editProfile(@ModelAttribute("user") User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser != null) {
            user.setId(currentUser.getId());
            userService.update(user);
            session.setAttribute("user", user);
        }

        return "redirect:/user/profile?updatedprofile=true";
    }

    @PostMapping("/delete")
    public String deleteAccount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.delete(user.getId());
        session.invalidate();
        return "redirect:/?deletedprofile=true";
    }
}
