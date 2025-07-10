package com.example.swp_smms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
// import com.example.swp_smms.model.enums.RoleEnum; // Uncomment if using @Enumerated

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    
    @Column(name = "role_name")
    private String roleName;
    
    @Column(name = "description")
    private String description;
    
    @OneToMany(mappedBy = "role")
    private List<Account> accounts;
} 