package com.ttn.ecommerce.service.customer;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.ChangePasswordDto;
import com.ttn.ecommerce.dto.MessageDto;
import com.ttn.ecommerce.dto.customer.CustomerDto;
import com.ttn.ecommerce.dto.customer.CustomerProfileDto;
import com.ttn.ecommerce.enums.Authority;
import com.ttn.ecommerce.exception.GenericException;
import com.ttn.ecommerce.model.Address;
import com.ttn.ecommerce.model.Customer;
import com.ttn.ecommerce.model.Role;
import com.ttn.ecommerce.repository.*;
import com.ttn.ecommerce.service.EmailSenderService;
import com.ttn.ecommerce.util.JwtUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    public CustomerRepository customerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    AddressRepository addressRepository;

    @Value("${url}")
    private String url;

    private ResponseEntity<MessageDto> createResponse(HttpStatus httpStatus, String message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message);
        return ResponseEntity.status(httpStatus).body(messageDto);
    }
    

    @Override
    public ResponseEntity<MessageDto> createAccount(CustomerDto customerDto) {
        if (!customerDto.getPassword().equals(customerDto.getConfirmPassword())) {
            return createResponse(HttpStatus.BAD_REQUEST, "Password and confirm password do not match");
        }

        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            return createResponse(HttpStatus.CONFLICT, "Email address is already registered");
        }

        Customer customer = new Customer();
        customer.setEmail(customerDto.getEmail());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setContact(customerDto.getContact());

        Role role = roleRepository.findByAuthority(Authority.CUSTOMER);
        customer.setRole(List.of(role));

        customerRepository.save(customer);

        String token = jwtUtils.generateToken(customer.getEmail(), 180);
        sendEmail(customer.getEmail(), token);

        return createResponse(HttpStatus.CREATED, "Thank you for registering!");
    }

    public void sendEmail(String email, String token) {
        String verificationUrl = url + "/customer/activate?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account Verification");
        message.setText("Please click the link below to activate your account:\n" + verificationUrl);

        emailSenderService.sendEmail(message);
    }


    @Override
    public ResponseEntity<String> activateAccount(String activeToken) {
        if (StringUtils.isBlank(activeToken)) {
            throw new GenericException("Activation token cannot be blank.", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtils.validateTokenAndRetrieveSubject(activeToken);
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        if (customer == null) {
            throw new GenericException("Invalid activation token.", HttpStatus.BAD_REQUEST);
        }

        if (customer.getIsActive()) {
            String message = "Account is already activated.";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }

        customer.setIsActive(true);
        customerRepository.save(customer);

        String message = "Account has been successfully activated.";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }



    @Override
    public ResponseEntity<String> resendActivationMail(String email) {
        Customer customer = customerRepository.findByEmailIgnoreCase(email);
        customerNotFound(customer);

        if (customer.getIsActive()) {
            String message = "Your account is already active.";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }

        String token = jwtUtils.generateToken(customer.getEmail(), 180);
        sendEmail(customer.getEmail(), token);

        String message = "Verification link has been resent.";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Override
    public ResponseEntity<CustomerProfileDto> viewProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        if (customer == null) {
            throw new GenericException("Customer not found.", HttpStatus.NOT_FOUND);
        }

        CustomerProfileDto customerProfileDto = new CustomerProfileDto();
        customerProfileDto.setId(customer.getId());
        customerProfileDto.setFirstName(customer.getFirstName());
        customerProfileDto.setLastName(customer.getLastName());
        customerProfileDto.setIsActive(customer.getIsActive());
        customerProfileDto.setContact(customer.getContact());

        return ResponseEntity.ok(customerProfileDto);
    }



    @Override
    public ResponseEntity<?> updateUserProfile(CustomerProfileDto customerDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        customerNotFound(customer);

        if (customerDto.getFirstName() != null) {
            customer.setFirstName(customerDto.getFirstName());
        }
        if (customerDto.getMiddleName() != null) {
            customer.setMiddleName(customerDto.getMiddleName());
        }
        if (customerDto.getLastName() != null) {
            customer.setLastName(customerDto.getLastName());
        }
        if (customerDto.getContact() != null) {
            customer.setContact(customerDto.getContact());
        }

        // Check for null values
        if (customerDto.getFirstName() == null || customerDto.getMiddleName() == null ||
                customerDto.getLastName() == null || customerDto.getContact() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Fields cannot be null.");
        }

        customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.OK).body("Customer updated");
    }




    @Override
    public ResponseEntity<String> updatePassword(ChangePasswordDto passwordDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmailIgnoreCase(email);
        customerNotFound(customer);

        if (!passwordDto.getPassword().equals(passwordDto.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password and confirm password do not match.");
        }

        if (!passwordEncoder.matches(passwordDto.getOldPassword(), customer.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid old password.");
        }

        String newPassword = passwordEncoder.encode(passwordDto.getPassword());
        customer.setPassword(newPassword);
        customerRepository.save(customer);

        String message = "Password updated successfully.";
        return ResponseEntity.ok(message);
    }


    @Override
    public ResponseEntity<Address> createAddress(AddressDto addressDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmailIgnoreCase(email);
        customerNotFound(customer);

        Address address = new Address();
        address.setCustomer(customer);
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setAddressLine(addressDto.getAddressLine());
        address.setZipCode(addressDto.getZipCode());

        if (addressDto.getLabel() != null) {
            address.setLabel(addressDto.getLabel());
        }

        Address savedAddress = addressRepository.save(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

    @Override
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (StringUtils.isBlank(email)) {
            throw new GenericException("Invalid user email", HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        if (customer == null) {
            throw new GenericException("Customer not found", HttpStatus.NOT_FOUND);
        }

        List<Address> addresses = addressRepository.findByCustomer(customer);

        if (addresses.isEmpty()) {
            throw new GenericException("No addresses found for the customer", HttpStatus.NOT_FOUND);
        }

        List<AddressDto> addressResponseDtoList = new ArrayList<>();

        for (Address address : addresses) {
            AddressDto addressDto = new AddressDto();
            addressDto.setId(address.getId());
            addressDto.setCity(address.getCity());
            addressDto.setState(address.getState());
            addressDto.setCountry(address.getCountry());
            addressDto.setAddressLine(address.getAddressLine());
            addressDto.setZipCode(address.getZipCode());
            addressDto.setLabel(address.getLabel());

            addressResponseDtoList.add(addressDto);
        }

        return ResponseEntity.ok().body(addressResponseDtoList);
    }



    @Override
    public ResponseEntity<?> updateAddress(Long id, AddressDto addressDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        customerNotFound(customer);

        Address address = addressRepository.findAddressByCustomerAndId(customer, id);

        addressNotFound(address);

        if (addressDto.getCity() != null) {
            address.setCity(addressDto.getCity());
        }
        if (addressDto.getState() != null) {
            address.setState(addressDto.getState());
        }
        if (addressDto.getCountry() != null) {
            address.setCountry(addressDto.getCountry());
        }
        if (addressDto.getZipCode() != null) {
            address.setZipCode(addressDto.getZipCode());
        }
        if (addressDto.getAddressLine() != null) {
            address.setAddressLine(addressDto.getAddressLine());
        }

        Address savedAddress = addressRepository.save(address);
        return ResponseEntity.ok(savedAddress);
    }


    @Override
    public ResponseEntity<?> deleteAddress(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        customerNotFound(customer);

        Address address = addressRepository.findAddressByCustomerAndId(customer, id);

        if (address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Address not found for the given ID: " + id);
        }

        addressRepository.delete(address);

        return ResponseEntity.ok().body("Address with ID " + id + " has been deleted successfully.");
    }



    private void customerNotFound(Customer customer) {
        if (Objects.isNull(customer)) {
            throw new GenericException("Customer not found", HttpStatus.NOT_FOUND);
        }
    }

    private void addressNotFound(Address address) {
        if (Objects.isNull(address)) {
            throw new GenericException("Address not found", HttpStatus.NOT_FOUND);
        }
    }


}
