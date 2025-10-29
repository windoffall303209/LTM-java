package com.auction.controller;

import com.auction.model.User;
import com.auction.service.AuctionService;
import com.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller: View (Thymeleaf)
 * Render HTML pages
 */
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final AuctionService auctionService;
    private final UserService userService;

    /**
     * Trang chủ
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("auctions", auctionService.getActiveAuctions());
        return "index";
    }

    /**
     * Dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Lấy thông tin user đã đăng nhập
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("currentUser", user);
            model.addAttribute("isAdmin", user.getRole() == User.Role.ADMIN);
        }
        
        model.addAttribute("auctions", auctionService.getActiveAuctions());
        return "dashboard";
    }

    /**
     * Chi tiết auction
     */
    @GetMapping("/auction/{id}")
    public String auctionDetail(@PathVariable Long id, Model model) {
        // Lấy thông tin user đã đăng nhập
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("currentUser", user);
            model.addAttribute("isAdmin", user.getRole() == User.Role.ADMIN);
        }
        
        model.addAttribute("auction", auctionService.getAuctionDTO(id));
        return "auction-detail";
    }

    /**
     * My Bids
     */
    @GetMapping("/my-bids")
    public String myBids() {
        return "my-bids";
    }

    /**
     * Watchlist
     */
    @GetMapping("/watchlist")
    public String watchlist() {
        return "watchlist";
    }

    /**
     * Admin Dashboard
     */
    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        // Lấy thông tin user đã đăng nhập
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("currentUser", user);
            model.addAttribute("isAdmin", user.getRole() == User.Role.ADMIN);
        }
        return "admin/dashboard";
    }

    /**
     * Admin - Quản lý Auctions
     */
    @GetMapping("/admin/auctions")
    public String adminAuctions(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("currentUser", user);
            model.addAttribute("isAdmin", user.getRole() == User.Role.ADMIN);
        }
        return "admin/auctions";
    }

    /**
     * Admin - Quản lý Users
     */
    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("currentUser", user);
            model.addAttribute("isAdmin", user.getRole() == User.Role.ADMIN);
        }
        return "admin/users";
    }
}

