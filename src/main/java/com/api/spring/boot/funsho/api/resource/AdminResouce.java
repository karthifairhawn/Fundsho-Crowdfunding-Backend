package com.api.spring.boot.funsho.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.spring.boot.funsho.api.entity.requestsEntity.usersRequest;
import com.api.spring.boot.funsho.api.entity.wallet.transaction;
import com.api.spring.boot.funsho.api.entity.wallet.wallet;
import com.api.spring.boot.funsho.api.repository.userRepository;
import com.api.spring.boot.funsho.api.repository.usersRequestRepository;
import com.api.spring.boot.funsho.api.repository.walletRepository;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
@CrossOrigin
public class AdminResouce {

    @Autowired
    userRepository UserRepository;

    @Autowired
    walletRepository WalletRepository;

    @Autowired
    usersRequestRepository UsersRequestRepository;

    @GetMapping("/admin/users")
    public MappingJacksonValue getUsers(@RequestParam("sessionKey") String sessionKey){
        MappingJacksonValue mapping = new MappingJacksonValue(UserRepository.findAll());
        mapping.setFilters(privateUserFilter());
        return mapping;
    }

    @GetMapping("/admin/users/{userId}")
    public MappingJacksonValue getUser(@PathVariable Long userId,@RequestParam("sessionKey") String sessionKey){
        MappingJacksonValue mapping = new MappingJacksonValue(UserRepository.findByUserId(userId));
        mapping.setFilters(privateUserFilter());
        return mapping;
    }

    @GetMapping("/admin/users/{userId}/wallet")
    public wallet getWallet(@PathVariable("id") int userId,@RequestParam("sessionKey") String sessionKey){
        return WalletRepository.findByUserId(userId);
    }

    @GetMapping("/admin/users/{userId}/donations")
    public List<transaction> getDonations(@PathVariable Long id,@RequestParam("sessionKey") String sessionKey){
        wallet userWallet = WalletRepository.findByUserId(id);
        return userWallet.getTransaction();    
    }

    @GetMapping("/admin/users/{userId}/requests")
    public List<usersRequest> getRequests(@PathVariable Long userId,@RequestParam("sessionKey") String sessionKey){
        return UsersRequestRepository.findByUserId(userId);
    }

    @DeleteMapping("/admin/users/{userId}/requests/id")
    public void deleteRequests(@PathVariable Long requestId,@RequestParam("sessionKey") String sessionKey){
        UsersRequestRepository.deleteById(requestId);
    }


    


    public FilterProvider privateUserFilter(){
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(
            "fname","lname","dob","email","phNumber","username","userId","sessionKey","avatarUrl"
        );

        FilterProvider filters = new SimpleFilterProvider().addFilter("userFilter", filter);        
        return filters;
    }
    
}