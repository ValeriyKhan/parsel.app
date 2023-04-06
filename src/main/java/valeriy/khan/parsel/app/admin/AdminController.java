package valeriy.khan.parsel.app.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import valeriy.khan.parsel.app.admin.dto.AddUserByAdminRequest;


@RestController
@RequestMapping("v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    @PostMapping("add-user")
    public ResponseEntity<?> addUser(@Valid @RequestBody AddUserByAdminRequest response){
        return adminService.addUser(response);
    }
}
