package com.food.khaaonow.repo;

import com.food.khaaonow.model.otp.EmailVerificationToken;
import com.food.khaaonow.model.otp.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmailVerificationTokenRepo  extends JpaRepository<EmailVerificationToken,Long> {
    @Query("""
    SELECT e 
    FROM EmailVerificationToken e 
    WHERE e.email = :email 
      AND e.tokenStatus = com.food.khaaonow.model.otp.TokenStatus.SENT
    ORDER BY e.createdAt DESC
""")
    List<EmailVerificationToken> findLatestSentTokenByEmail(@Param("email") String email);


    @Query("""
    SELECT COUNT(e) > 0
    FROM EmailVerificationToken e
    WHERE e.email = :email
      AND e.tokenStatus = com.food.khaaonow.model.otp.TokenStatus.VERIFIED
""")
    boolean isEmailVerified(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("""
     UPDATE EmailVerificationToken e 
     SET e.tokenStatus = :toStatus
     where e.email = :email AND e.tokenStatus = :fromStatus
            """)
    Integer updateAllPreviousTokensByEmail(@Param("email") String email ,@Param("fromStatus")TokenStatus fromStatus,@Param("toStatus")TokenStatus toStatus);

}
