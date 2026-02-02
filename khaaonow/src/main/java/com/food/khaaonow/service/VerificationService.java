package com.food.khaaonow.service;

import com.food.khaaonow.dto.EmailRequest;
import com.food.khaaonow.dto.EmailTokenStatus;
import com.food.khaaonow.dto.TokenVerificationRequest;
import com.food.khaaonow.dto.VerificationStatus;
import com.food.khaaonow.model.otp.EmailVerificationToken;
import com.food.khaaonow.model.otp.TokenStatus;
import com.food.khaaonow.repo.EmailVerificationTokenRepo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VerificationService {

    private EmailVerificationTokenRepo emailVerificationTokenRepo;
    private EmailService emailService;
    private UserService userService;

    public VerificationService(EmailVerificationTokenRepo emailVerificationTokenRepo,EmailService emailService,UserService userService) {
        this.emailVerificationTokenRepo = emailVerificationTokenRepo;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Transactional
    public EmailTokenStatus sendVerificationEmailToUser(EmailRequest emailRequest) throws MailException {
        if(isUserEmailVerified(emailRequest.getEmail())){
            EmailTokenStatus emailTokenStatus = new EmailTokenStatus();
            emailTokenStatus.setStatus("Email already verified");
            return emailTokenStatus;
        }
        if(userService.findUserByEmail(emailRequest.getEmail()) == null) {
            String otp = String.valueOf(100000 + new SecureRandom().nextInt(900000));
            EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
            emailVerificationToken.setEmail(emailRequest.getEmail());
            emailVerificationToken.setOtp(otp);
            emailVerificationToken.setTokenStatus(TokenStatus.CREATED);
            emailVerificationTokenRepo.updateAllPreviousTokensByEmail(emailRequest.getEmail(),TokenStatus.SENT,TokenStatus.EXPIRED);
            emailVerificationTokenRepo.updateAllPreviousTokensByEmail(emailRequest.getEmail(),TokenStatus.CREATED,TokenStatus.EXPIRED);
            EmailVerificationToken emailVerificationTokenAfterCreation  = emailVerificationTokenRepo.save(emailVerificationToken);
            try {
                emailService.sendSimpleEmail(emailRequest.getEmail(), "Email verification Code", "Hey User please verify your email by entering the below provided OTP\n OTP is : " + otp + "\n in website\n or \n click the below link to verify\n link....",otp);
            }catch (MailException mailException){
                emailVerificationTokenAfterCreation.setTokenStatus(TokenStatus.FAILED);
                emailVerificationTokenRepo.save(emailVerificationTokenAfterCreation);
                EmailTokenStatus emailTokenStatus = new EmailTokenStatus();
                emailTokenStatus.setStatus("sent Failed");
                return emailTokenStatus;
            }
            emailVerificationTokenAfterCreation.setTokenStatus(TokenStatus.SENT);
            emailVerificationTokenRepo.save(emailVerificationTokenAfterCreation);
            EmailTokenStatus emailTokenStatus = new EmailTokenStatus();
            emailTokenStatus.setStatus("sent");
            return emailTokenStatus;
        }else {
        EmailTokenStatus emailTokenStatus = new EmailTokenStatus();
        emailTokenStatus.setStatus("User already exists in the system");
        return emailTokenStatus;
        }
    }

    @Transactional
    public VerificationStatus verifyEmail(TokenVerificationRequest tokenVerificationRequest) {
        VerificationStatus verificationStatus = new VerificationStatus();
        verificationStatus.setStatus("Cannot verify Now please try Later");

        if(isUserEmailVerified(tokenVerificationRequest.getEmail())){
            verificationStatus.setStatus("Email already verified!!");
            return verificationStatus;
        }

        EmailVerificationToken emailVerificationToken = null;
        List<EmailVerificationToken> emailVerificationTokens = emailVerificationTokenRepo.findLatestSentTokenByEmail(tokenVerificationRequest.getEmail());
        if(!emailVerificationTokens.isEmpty()){
            emailVerificationToken = emailVerificationTokens.get(0);
        }else {
            verificationStatus.setStatus("Token not found please resend");
            return verificationStatus;
        }
        if(emailVerificationToken.getTokenStatus() == TokenStatus.EXPIRED || emailVerificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            emailVerificationToken.setTokenStatus(TokenStatus.EXPIRED);
            emailVerificationTokenRepo.save(emailVerificationToken);
            verificationStatus.setStatus("token expired");
            return verificationStatus;

        }
        else if (emailVerificationToken.getAttemptCount()>=5) {
            verificationStatus.setStatus("token verification Attempts exceeded try again after 5 hours");
        }
        else if (!emailVerificationToken.getOtp().equals(tokenVerificationRequest.getToken())) {
            emailVerificationToken.setAttemptCount(emailVerificationToken.getAttemptCount()+1);
            verificationStatus.setStatus("token verification failed");
        }
        else if(emailVerificationToken.getOtp().equals(tokenVerificationRequest.getToken())) {
            emailVerificationToken.setTokenStatus(TokenStatus.VERIFIED);
            verificationStatus.setStatus("success");
        }
        emailVerificationTokenRepo.save(emailVerificationToken);
        return verificationStatus;
    }

    public boolean isUserEmailVerified(String email) {
        return emailVerificationTokenRepo.isEmailVerified(email);
    }
}
