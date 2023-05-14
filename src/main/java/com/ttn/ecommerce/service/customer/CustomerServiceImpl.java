package com.ttn.ecommerce.service.customer;

import com.ttn.ecommerce.config.JwtFilter;
import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.ChangePasswordDto;
import com.ttn.ecommerce.dto.PasswordDto;
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

    @Autowired
    TokenRepository tokenRepository;


    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    UserRepository userRepository;

    @Value("${url}")
    private String url;

    @Override
    public ResponseEntity<String> createAccount(CustomerDto customerDto) {


        if (!customerDto.getPassword().equals(customerDto.getConfirmPassword())) {
            throw new GenericException("Password and confirm password do not match", HttpStatus.BAD_REQUEST);
        }

        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new GenericException("Email address is already registered", HttpStatus.BAD_REQUEST);
        }

        Customer customer = new Customer();
        customer.setEmail(customerDto.getEmail());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setContact(customerDto.getContact());

        Role role = roleRepository.findByAuthority(Authority.CUSTOMER);
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setRole(List.of(role));

        customerRepository.save(customer);

        String token = jwtUtils.generateToken(customer.getEmail(), 180);
        sendEmail(customer.getEmail(), token);

        return ResponseEntity.status(HttpStatus.CREATED).body("Thank you for registering!");

    }

    public void sendEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account verification");
        message.setText("Please use the following token to activate: " +
                url + "/customer/activate?token=" + token);
        emailSenderService.sendEmail(message);
    }

    @Override
    public ResponseEntity<String> activateAccount(String activeToken) {
        if (StringUtils.isBlank(activeToken)) {
            throw new GenericException("Activation token cannot be blank", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtils.validateTokenAndRetrieveSubject(activeToken);
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        if (customer == null) {
            throw new GenericException("Invalid activation token", HttpStatus.BAD_REQUEST);
        }

        if (customer.getIsActive()) {
            throw new GenericException("Account is already activated", HttpStatus.OK);
        }

        customer.setIsActive(true);
        customerRepository.save(customer);

        String message = "Account verified";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }


    @Override
    public ResponseEntity<String> resendActivationMail(String email) {
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        customerNotFound(customer);

        if (customer.getIsActive()) {
            throw new GenericException("Your account is already active.", HttpStatus.OK);
        }
        String token = jwtUtils.generateToken(customer.getEmail(), 180);
        sendEmail(customer.getEmail(), token);
        return ResponseEntity.status(HttpStatus.OK).body("Resend verification link");
    }

    @Override
    public ResponseEntity<?> viewProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        if (customer == null) {
            throw new GenericException("Customer not found", HttpStatus.NOT_FOUND);
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
        customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.OK).body("Customer updated");
    }



    @Override
    public ResponseEntity<?> updatePassword(ChangePasswordDto passwordDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        customerNotFound(customer);

        if (!passwordDto.getPassword().equals(passwordDto.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password and confirm password do not match.");
        }

        if (!passwordEncoder.matches(passwordDto.getOldPassword(), customer.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Old password is incorrect.");
        }

        customer.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        customerRepository.save(customer);
        return ResponseEntity.ok("Password updated successfully");
    }



    @Override
    public ResponseEntity<?> createAddress(AddressDto addressDto) {
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
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        customerNotFound(customer);

        List<Address> addresses = addressRepository.findByCustomer(customer);

        if (addresses.isEmpty()) {
            throw new GenericException("No addresses found for the customer", HttpStatus.BAD_REQUEST);
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


        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setZipCode(addressDto.getZipCode());
        address.setAddressLine(addressDto.getAddressLine());

        Address savedAddress = addressRepository.save(address);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> deleteAddress(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmailIgnoreCase(email);

        customerNotFound(customer);
        Address address = addressRepository.findAddressByCustomerAndId(customer, id);

        addressNotFound(address);

        addressRepository.delete(address);
        return ResponseEntity.ok().body("Address with id " + id + " has been deleted.");
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
