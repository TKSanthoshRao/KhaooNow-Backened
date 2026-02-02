package com.food.khaaonow.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.food.khaaonow.model.auth.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepo extends JpaRepository<Role,Long> {

    public Role findByName(String name);

}
