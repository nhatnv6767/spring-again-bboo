package com.ra.repository;

import com.ra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // distinct
    //    @Query(value = "SELECT DISTINCT u FROM User u where u.firstName =:firstName and u.lastName =:lastName")
    List<User> findDistinctByFirstNameAndLastName(String firstName, String lastName);

    //    @Query(value = "SELECT u FROM User u where u.email = ?1")
    List<User> findByEmail(String email);

    //    @Query(value = "select u from User u where u.firstName =:name or u.lastName =:name")
//    List<User> findByFirstNameOrLastName(String name);

    // Is, Equals
    //    @Query(value = "select u from User u where u.firstName =:name")
    List<User> findByFirstNameIs(String name);

    //    @Query(value = "select u from User u where u.firstName =:name")
    List<User> findByFirstNameEquals(String name);

    //    @Query(value = "select u from User u where u.firstName =:name")
    List<User> findByFirstName(String name);

    // Between
    //    @Query(value = "select u from User u where u.createdAt between :min and :max")
    List<User> findByCreatedAtBetween(Date min, Date max);

    // LessThan
    //    @Query(value = "select u from User u where u.createdAt < :date")
    List<User> findByCreatedAtLessThan(Date date);

    // LessThanEqual
    //    @Query(value = "select u from User u where u.createdAt <= :date")
    List<User> findByCreatedAtLessThanEqual(Date date);

    // GreaterThan
    //    @Query(value = "select u from User u where u.createdAt > :date")
    List<User> findByCreatedAtGreaterThan(Date date);

    // GreaterThanEqual
    //    @Query(value = "select u from User u where u.createdAt >= :date")
    List<User> findByCreatedAtGreaterThanEqual(Date date);

}
