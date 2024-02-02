package com.sudol.SudolAPI.Repositories;

import com.sudol.SudolAPI.models.ERole;
import com.sudol.SudolAPI.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
