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

    @Query(value = "select u from User u inner join Address a on u.id = a.user.id where a.city =:city")
    List<User> getAllUser(String city);


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


    // before and after
    //    @Query(value = "select u from User u where u.createdAt > :date")
    List<User> findByCreatedAtBefore(Date date);

    //    @Query(value = "select u from User u where u.createdAt < :date")
    List<User> findByCreatedAtAfter(Date date);

    // is null
    //    @Query(value = "select u from User u where u.email is null")
    List<User> findByEmailIsNull();

    // is not null
    //    @Query(value = "select u from User u where u.email is not null")
    List<User> findByEmailIsNotNull();

    // like
    //    @Query(value = "select u from User u where u.firstName like %:name%")
    List<User> findByFirstNameLike(String name);

    // not like
    //    @Query(value = "select u from User u where u.firstName not like %:name%")
    List<User> findByFirstNameNotLike(String name);

    // starting with
    //    @Query(value = "select u from User u where u.firstName like :name%")
    List<User> findByFirstNameStartingWith(String name);

    // ending with
    //    @Query(value = "select u from User u where u.firstName like %:name")
    List<User> findByFirstNameEndingWith(String name);

    // containing
    //    @Query(value = "select u from User u where u.firstName like %:name%")
    List<User> findByFirstNameContaining(String name);

    // Not
    //    @Query(value = "select u from User u where u.firstName <> :name")
    List<User> findByFirstNameNot(String name);

    // And
    //    @Query(value = "select u from User u where u.firstName =:firstName and u.lastName =:lastName")
    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    // Or
    //    @Query(value = "select u from User u where u.firstName =:firstName or u.lastName =:lastName")
    List<User> findByFirstNameOrLastName(String firstName, String lastName);

    // Order By
    //    @Query(value = "select u from User u order by u.firstName")
    List<User> findByOrderByFirstNameAsc();

    // In
    //    @Query(value = "select u from User u where u.firstName in (:names)")
    List<User> findByFirstNameIn(List<String> names);

    // Not In
    //    @Query(value = "select u from User u where u.firstName not in (:names)")
    List<User> findByFirstNameNotIn(List<String> names);

    // id in 10 - 20
    //    @Query(value = "select u from User u where u.id between 10 and 20")
    List<User> findByIdBetween(Long min, Long max);

    // true/false
    //    @Query(value = "select u from User u where u.activated = true")
    List<User> findByActivatedTrue();

    // ignoreCase
    //    @Query(value = "select u from User u where lower(u.firstName) =lower(:name) ")
    List<User> findByFirstNameIgnoreCase(String name);

    // Order By
    //    @Query(value = "select u from User u where u.firstName =:name order by u.createdAt desc")
    List<User> findByFirstNameOrderByCreatedAtDesc(String name);

    // And
    //    @Query(value = "select u from User u where u.firstName =:firstName and u.lastName =:lastName")
    List<User> findByFirstNameAndLastNameAllIgnoreCase(String firstName, String lastName);

}
