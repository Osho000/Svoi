package com.example.Svoi.repository;

import com.example.Svoi.entity.User;
import com.example.Svoi.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    void deleteByUser(User user);
    
    List<UserInterest> findByUser(User user);
    
    @Query("SELECT DISTINCT ui.user FROM UserInterest ui WHERE ui.interest.name IN :interestNames")
    List<User> findUsersByInterestNames(@Param("interestNames") List<String> interestNames);
    
    @Query("SELECT ui.interest.name FROM UserInterest ui WHERE ui.user = :user")
    List<String> findInterestNamesByUser(@Param("user") User user);
    
    @Query("SELECT ui.interest.name FROM UserInterest ui WHERE ui.user.id = :userId")
    List<String> findInterestNamesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT DISTINCT ui.user FROM UserInterest ui WHERE ui.interest.name IN :interestNames AND ui.user.id != :excludeUserId")
    List<User> findUsersByInterestNamesExcludingUser(@Param("interestNames") List<String> interestNames, 
                                                     @Param("excludeUserId") Long excludeUserId);
    
    @Query("SELECT ui.interest.name FROM UserInterest ui WHERE ui.user.id IN :userIds")
    List<String> findInterestNamesByUserIds(@Param("userIds") List<Long> userIds);
    
    @Query("SELECT COUNT(ui) FROM UserInterest ui WHERE ui.user.id = :userId AND ui.interest.name IN :interestNames")
    Long countCommonInterests(@Param("userId") Long userId, @Param("interestNames") List<String> interestNames);
    
    @Query("SELECT DISTINCT ui.interest.name FROM UserInterest ui WHERE ui.user.id = :userId")
    Set<String> findInterestNamesSetByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ui FROM UserInterest ui WHERE ui.user.id = :userId")
    List<UserInterest> findByUserId(@Param("userId") Long userId);
}
