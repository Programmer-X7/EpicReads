package com.ardent.epicreads.controller.admin;

import com.ardent.epicreads.entity.User;
import com.ardent.epicreads.service.AdminService;
import com.ardent.epicreads.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpServletRequest request) {
        model.addAttribute("totalCustomers", adminService.getTotalCustomers());
        model.addAttribute("outOfStockProductCount", adminService.getOutOfStockProductCount());
        model.addAttribute("monthlyRevenue", adminService.getMonthlyRevenue());
        model.addAttribute("totalProducts", adminService.getTotalProducts());
        model.addAttribute("monthlyOrdersCount", adminService.getMonthlyOrdersCount());
        model.addAttribute("totalRevenue", adminService.getTotalRevenue());
        model.addAttribute("last5Orders", adminService.getLast5Orders());
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/admin-dashboard";
    }

    @GetMapping("/profile")
    public String getProfileDetails(Model model, HttpSession session, HttpServletRequest request) {
        User theUser = (User) session.getAttribute("user");
        if (theUser != null) {
            User user = userService.findByUserName(theUser.getUserName());
            model.addAttribute("user", user);
            model.addAttribute("editable", false);
            model.addAttribute("currentPath", request.getRequestURI());
        }
        return "admin/admin-profile";
    }

    @GetMapping("/edit")
    public String getEditProfileForm(HttpSession session, Model model, HttpServletRequest request) {
        User theUser = (User) session.getAttribute("user");
        if (theUser != null) {
            User user = userService.findByUserName(theUser.getUserName());
            model.addAttribute("user", user);
            model.addAttribute("editable", true);
            model.addAttribute("currentPath", request.getRequestURI());
        }
        return "admin/edit-admin-profile-form";
    }

    @PostMapping("/edit")
    public String editProfile(@ModelAttribute("user") User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser != null) {
            user.setId(currentUser.getId());
            userService.update(user);
            session.setAttribute("user", user);
        }

        return "redirect:/admin/profile?updatedprofile=true";
    }

    @PostMapping("/delete")
    public String deleteAccount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.delete(user.getId());
        session.invalidate();
        return "redirect:/?deletedprofile=true";
    }

    // Customer Control
    @GetMapping("/customers")
    public String getAllUsers(Model model, HttpServletRequest request) {
        List<User> customers = userService.findAllCustomers();
        model.addAttribute("customers", customers);
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/customers";
    }

    @PostMapping("/customer/ban")
    public String banCustomer(@RequestParam("customerId") Long customerId, RedirectAttributes redirectAttributes) {
        userService.banUser(customerId);
        redirectAttributes.addFlashAttribute("successMessage", "Customer Ban successfully!");
        return "redirect:/admin/customers";
    }

    @PostMapping("/customer/unban")
    public String unbanCustomer(@RequestParam("customerId") Long customerId, Model model, RedirectAttributes redirectAttributes) {
        userService.unbanUser(customerId);
        redirectAttributes.addFlashAttribute("successMessage", "Customer Unban successfully!");
        return "redirect:/admin/customers";
    }
}
