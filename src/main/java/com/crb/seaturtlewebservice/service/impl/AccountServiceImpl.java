package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.dto.UserDto;
import com.crb.seaturtlewebservice.domain.dto.request.CreateAccountRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.EditAccountRequestDto;
import com.crb.seaturtlewebservice.domain.dto.response.AccountResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Role;
import com.crb.seaturtlewebservice.domain.entity.User;
import com.crb.seaturtlewebservice.exception.EmailAlreadyExistsException;
import com.crb.seaturtlewebservice.exception.PasswordNotMatchException;
import com.crb.seaturtlewebservice.exception.PhoneAlreadyExistsException;
import com.crb.seaturtlewebservice.exception.RoleNotFoundException;
import com.crb.seaturtlewebservice.exception.UserNotFoundException;
import com.crb.seaturtlewebservice.exception.UsernameAlreadyExistsException;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.RoleRepository;
import com.crb.seaturtlewebservice.repository.UserRepository;
import com.crb.seaturtlewebservice.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public ResponsePayload getAllAccounts() {
        LOGGER.info("AccountServiceImpl-> Display account invoked");
        try {
            List<User> users = userRepository.findAllByIsDeletedFalse();
            List<Object> userDtos = users.stream()
                    .map(user -> {
                        UserDto userDto = new UserDto();
                        BeanUtils.copyProperties(user, userDto);
                        if (Optional.ofNullable(user.getRole()).isPresent()) {
                            userDto.setRoleName(user.getRole().getName());
                        }
                        return userDto;
                    }).collect(Collectors.toList());
            return ResponsePayload.builder()
                    .data(userDtos)
                    .status(HttpStatus.OK)
                    .message("List of accounts retrieved successfully")
                    .build();
        } catch (Exception e) {
            LOGGER.error("AccountServiceImpl-> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Failed to retrieve account")
                    .error(e.getMessage())
                    .build();
        }

    }

    @Override
    public ResponsePayload create(CreateAccountRequestDto createAccountRequestDto) {
        LOGGER.info("AccountServiceImpl-> Create account invoked");
        try {
            checkExistsAccount(createAccountRequestDto);
            User user = new User();
            BeanUtils.copyProperties(createAccountRequestDto, user);
            Role role = roleRepository.findByName(createAccountRequestDto.getRoleName())
                    .orElseThrow(() -> new RoleNotFoundException("Role is not found"));
            user.setRole(role);
            user.setPassword(encoder.encode(createAccountRequestDto.getPassword()));
            user.setIsDeleted(false);
            user.setIsActivated(true);
            checkConfirmPassword(createAccountRequestDto);
            userRepository.save(user);
            String roleName = user.getRole().getName();
            AccountResponseDto accountResponseDto = new AccountResponseDto();
            BeanUtils.copyProperties(user, accountResponseDto);
            accountResponseDto.setRole(roleName);
            return ResponsePayload.builder()
                    .data(accountResponseDto)
                    .status(HttpStatus.CREATED)
                    .message("Create account successfully")
                    .build();
        } catch (Exception e) {
            LOGGER.error("AccountServiceImpl-> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Create account failed")
                    .error(e.getMessage())
                    .build();
        }

    }

    private void checkExistsAccount(CreateAccountRequestDto createAccountRequestDto) {
        String username = createAccountRequestDto.getUsername();
        String email = createAccountRequestDto.getEmail();
        String phone = createAccountRequestDto.getPhone();
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username already exists");
        } else if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        } else if (userRepository.existsByPhone(phone)) {
            throw new PhoneAlreadyExistsException("Phone already exists");
        }
    }

    private void checkConfirmPassword(CreateAccountRequestDto createAccountRequestDto) {
        if (Optional.ofNullable(createAccountRequestDto.getPassword()).isPresent()) {
            if (!createAccountRequestDto.getConfirmPassword().equals(createAccountRequestDto.getPassword())) {
                throw new PasswordNotMatchException("Password not match");
            }
        }
    }

    @Override
    public ResponsePayload edit(Long accountId, EditAccountRequestDto editAccountRequestDto) {
        LOGGER.info("AccountServiceImpl-> Edit account invoked");
        try {
            User user = userRepository.findById(accountId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            checkConfirmPasswordEditAccount(user, editAccountRequestDto);
            checkExistsEditAccount(user, editAccountRequestDto);
            user.setFullName(editAccountRequestDto.getFullName());
            user.setEmail(editAccountRequestDto.getEmail());
            user.setPhone(editAccountRequestDto.getPhone());
            user.setIsActivated(editAccountRequestDto.getIsActivated());
            String roleName = editAccountRequestDto.getRoleName();
            Role role;
            if (Optional.ofNullable(roleName).isPresent()) {
                role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException("Role not found"));
            } else {
                role = user.getRole();
            }
            user.setRole(role);
            userRepository.save(user);
            AccountResponseDto accountResponseDto = new AccountResponseDto();
            BeanUtils.copyProperties(user, accountResponseDto);
            accountResponseDto.setRole(roleName);
            return ResponsePayload.builder()
                    .data(accountResponseDto)
                    .status(HttpStatus.OK)
                    .message("Account is edited")
                    .build();
        } catch (Exception e) {
            LOGGER.error("AccountServiceImpl-> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Edit account failed")
                    .error(e.getMessage())
                    .build();
        }
    }

    private void checkExistsEditAccount(User user, EditAccountRequestDto editAccountRequestDto) {
        String newEmail = editAccountRequestDto.getEmail();
        String newPhone = editAccountRequestDto.getPhone();
        if (Optional.ofNullable(newEmail).isPresent()
                && !newEmail.equals(user.getEmail())
                && userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException("Email already exists");
        } else if (Optional.ofNullable(newPhone).isPresent()
                && !newPhone.equals(user.getPhone())
                && userRepository.existsByPhone(newPhone)) {
            throw new PhoneAlreadyExistsException("Phone already exists");
        }
    }

    private void checkConfirmPasswordEditAccount(User user, EditAccountRequestDto editAccountRequestDto) {
        String confirmPassword = editAccountRequestDto.getConfirmPassword();
        if (Optional.ofNullable(confirmPassword).isPresent()) {
            boolean passwordMatch = BCrypt.checkpw(confirmPassword, user.getPassword());
            if (!passwordMatch) {
                throw new PasswordNotMatchException("Password not match");
            }
        }

    }

    @Override
    public ResponsePayload remove(Long accountId) {
        LOGGER.info("AccountServiceImpl-> Remove account invoked");
        try {
            User user = userRepository.findById(accountId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            user.setIsDeleted(true);
            user.setIsActivated(false);
            userRepository.save(user);
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Account is deleted")
                    .build();
        } catch (Exception e) {
            LOGGER.error("AccountServiceImpl-> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Delete account failed")
                    .error(e.getMessage())
                    .build();
        }
    }


}
