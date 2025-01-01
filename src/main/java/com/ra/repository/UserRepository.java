package com.ra.repository;

import com.ra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // distinct
//    @Query(value = "SELECT DISTINCT u FROM User u where u.firstName =:firstName and u.lastName =:lastName")
    List<User> findDistinctByFirstNameAndLastName(String firstName, String lastName);
}
