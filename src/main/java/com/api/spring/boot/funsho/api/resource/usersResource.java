package com.api.spring.boot.funsho.api.resource;

import java.util.ArrayList;
import java.util.Date;
// import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.api.spring.boot.funsho.api.dto.users.createUser;
import com.api.spring.boot.funsho.api.dto.users.updateUser;
import com.api.spring.boot.funsho.api.entity.users;
import com.api.spring.boot.funsho.api.entity.auth.login;
import com.api.spring.boot.funsho.api.entity.auth.loginData;
import com.api.spring.boot.funsho.api.entity.requestsEntity.usersRequest;
import com.api.spring.boot.funsho.api.entity.wallet.transaction;
import com.api.spring.boot.funsho.api.entity.wallet.wallet;
import com.api.spring.boot.funsho.api.exceptions.unauthorizedException;
import com.api.spring.boot.funsho.api.exceptions.userNotFoundException;
import com.api.spring.boot.funsho.api.repository.loginDataRepository;
import com.api.spring.boot.funsho.api.repository.transactionRepository;
import com.api.spring.boot.funsho.api.repository.userRepository;
import com.api.spring.boot.funsho.api.repository.usersRequestRepository;
import com.api.spring.boot.funsho.api.repository.walletRepository;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class usersResource {

    @Autowired
    userRepository UserRepository;
    private HttpServletRequest request;

    @Autowired
    walletRepository WalletRepository;

    @Autowired
    loginDataRepository LoginDataRepository;

    @Autowired
    transactionRepository TransactionRepository;

    @Autowired
    usersRequestRepository UsersRequestRepository;
    // @Autowired
    // public void setRequest(HttpServletRequest request) {
    //     this.request = request;
    // }


    // @GetMapping("/hello")
    // public ResponseEntity<String> listAllHeaders(
    //     @RequestHeader Map<String, String> headers) {
    //     headers.forEach((key, value) -> {
    //         System.out.println(String.format("Header '%s' = %s", key, value));
    //     });

    //     return new ResponseEntity<String>(String.format("Listed %d headers", headers.size()), HttpStatus.OK);
    // }

    @PostMapping("/users") // Create new user
    public MappingJacksonValue saveUsers(@RequestBody createUser newUserInfo){

        // Save new user to database
        users newUser = new users(newUserInfo);
        UserRepository.save(newUser);        

        // Login data creation for new user
        loginData newLoginData = loginData.builder()
                                .userId(newUser.getUserId())                                
                                .build();
        LoginDataRepository.save(newLoginData);

        // Wallet data creation for new user
        wallet newUserWallet = wallet.builder()
                            .Balance(0l)
                            .user(newUser)
                            .Transaction(new ArrayList<transaction>())
                            .build();
        WalletRepository.save(newUserWallet);
        

        // returning the new user
        MappingJacksonValue mapping = new MappingJacksonValue(newUser);
        mapping.setFilters(privateUserFilter());
        return mapping;
        
    }

    
    @GetMapping("/users")   // Return all Users
    public MappingJacksonValue findAll()
    {        
        MappingJacksonValue mapping = new MappingJacksonValue(UserRepository.findAll());
        mapping.setFilters(publicUserFilter());
        return mapping;
    }

    @GetMapping("/users/{id}") // Return user by id
    public MappingJacksonValue findById(@PathVariable Long id)
    {
        users user = UserRepository.findByUserId(id);
        if(user==null) throw new userNotFoundException("User Not Found");
        else{
            MappingJacksonValue mapping = new MappingJacksonValue(user);
            mapping.setFilters(publicUserFilter());
            return mapping;            
        }

    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)  // Update existing user
    public void updateUser(@RequestBody updateUser updatingUser,@PathVariable Long id,@RequestParam String sessionKey){
        users user = UserRepository.findByUserId(id);
        if(user==null) throw new userNotFoundException("User Not Found");

        if(user.getSessionKey()!=sessionKey) throw new userNotFoundException("User Not Found");
        if(!user.getPassword().equals(updatingUser.getOldPassword())) throw new unauthorizedException("Old password and new password do not match");
        

        if(updatingUser.getChangePassword()){                        
            user.setPassword(updatingUser.getNewPassword());            
            UserRepository.save(user);
        }else{            
            user.setFname(updatingUser.getFname());
            user.setLname(updatingUser.getLname());
            user.setDob(updatingUser.getDob());
            user.setEmail(updatingUser.getEmail());
            user.setPhNumber(updatingUser.getPhNumber());            
            user.setUsername(updatingUser.getUsername());
            UserRepository.save(user);
        }
        
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)  // Delete existing user
    public void deleteUser(@RequestParam String sessionKey,@PathVariable Long   id){

        users user = UserRepository.findByUserId(id);
        if(user==null) throw new userNotFoundException("User Not Found");

        if(user.getSessionKey()!=sessionKey) throw new userNotFoundException("User Not Found");
                
        // Get all obj to delete
        users found = UserRepository.findBySessionKey(sessionKey);        
        long userId = found.getUserId();        
        loginData foundLoginData = LoginDataRepository.findByUserId(userId);
        wallet foundWallet = WalletRepository.findByUserId(userId);
        
        // Delete wallet data
        

        // Delete all found data of user
        TransactionRepository.deleteByWalletId(foundWallet.getWalletId());
        WalletRepository.delete(foundWallet);
        LoginDataRepository.delete(foundLoginData);
        UserRepository.delete(found);


        UserRepository.delete(found);
    }

    @PostMapping("/users/login") // Login user
    public MappingJacksonValue authUsers(@RequestBody @NotNull login user) throws Exception {
        users found = UserRepository.findByEmail(user.getEmail());
        if(found==null){
            throw new userNotFoundException("User Not Found");
        }
        if(found.getPassword().equals(user.getPassword())){
            String ipAddress = "";
            if (request != null) {
                ipAddress = request.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null || "".equals(ipAddress)) {
                    ipAddress = request.getRemoteAddr();
                }
            }
            System.out.println(ipAddress);
            String newSessionKey = (ipAddress+new Date()).hashCode()+"";
            found.setSessionKey(newSessionKey);
            UserRepository.save(found);
            MappingJacksonValue mapping = new MappingJacksonValue(found);
            // mapping.setFilters(userFilter(new String[]{"fname","lname","dob","email","phNumber","username","userId","sessionKey","place"}));
            mapping.setFilters(privateUserFilter());
            return mapping;
        }
        throw new userNotFoundException("User Not Found");
    }

    @GetMapping("/users/{userId}/profile") // Return user by session key
    public MappingJacksonValue findUsingSessionKey(@RequestParam String sessionKey,@PathVariable Long userId)
    {    
        users user = UserRepository.findBySessionKey(sessionKey);
        if(user==null) throw new userNotFoundException("No user found for this session key");
        if(userId-user.getUserId()!=0){
            System.out.println(userId+"   "+user.getUserId());
            throw new userNotFoundException("Given userId & Session Key not matched.");
        }        

        MappingJacksonValue mapping = new MappingJacksonValue(UserRepository.findBySessionKey(sessionKey));
        mapping.setFilters(privateUserFilter());
        return mapping;
       
    }

    @GetMapping("/users/{userId}/wallet") // Return wallet of user
    public wallet getWallet(@RequestParam String sessionKey,@PathVariable Long userId){
        users user = UserRepository.findBySessionKey(sessionKey);
        if(user == null) throw new userNotFoundException("Session key incorrect");
        if(user.getUserId()-userId!=0) throw new userNotFoundException("Given userId & Session Key not matched.");
        return WalletRepository.findByUserId(userId);        
    }

    @GetMapping("/users/{userId}/requests") 
    public List<usersRequest> getAllRequests(@PathVariable Long userId){
        return UsersRequestRepository.findByUserId(userId);        
    }

    @GetMapping("/users/{userId}/loginHistory")
    public loginData getLoginHistory(@PathVariable Long userId,@RequestParam String sessionKey){
        users user = UserRepository.findBySessionKey(sessionKey);
        if(user == null) throw new userNotFoundException("Session key incorrect");
        if(user.getUserId()-userId!=0) throw new userNotFoundException("Given userId & Session Key not matched.");

        return LoginDataRepository.findByUserId(userId);            
    }

    @GetMapping("/users/{userId}/donations")
    public List<transaction> getDonations(@PathVariable Long id){
        wallet userWallet = WalletRepository.findByUserId(id);
        return userWallet.getTransaction();    
    }

    
    public FilterProvider privateUserFilter(){
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(
            "fname","lname","dob","email","phNumber","username","userId","sessionKey","avatarUrl","bio"
        );

        FilterProvider filters = new SimpleFilterProvider().addFilter("userFilter", filter);        
        return filters;
    }
    
    public FilterProvider publicUserFilter(){       
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(
            "fname","lname","email","username","userId","avatarUrl","bio"
        );
        FilterProvider filters = new SimpleFilterProvider().addFilter("userFilter", filter);        
        return filters;
    }






}
