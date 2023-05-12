package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/customers")
    public ResponseEntity<?> customerList
            (@RequestParam Integer pageOffset, @RequestParam Integer pageSize, @RequestParam String sortBy) {
        return adminService.findAllCustomer(pageOffset, pageSize, sortBy);
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> sellerList
            (@RequestParam Integer pageOffset, @RequestParam Integer pageSize, @RequestParam String sortBy) {
        return adminService.findAllSeller(pageOffset, pageSize, sortBy);
    }

    @PutMapping("/customer/{id}/activate")
    public ResponseEntity<?> activateCustomer(@PathVariable Long id) {
        return adminService.activateUser(id);
    }

    @PutMapping("/customer/{id}/deactivate")
    public ResponseEntity<?> deactivateCustomer(@PathVariable Long id) {
        return adminService.deactivateUser(id);
    }


}
