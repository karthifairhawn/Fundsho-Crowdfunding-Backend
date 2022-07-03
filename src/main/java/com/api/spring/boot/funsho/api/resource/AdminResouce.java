package com.api.spring.boot.funsho.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.spring.boot.funsho.api.entity.users;
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

    @GetMapping("/admin/requests") 
    public List<usersRequest> getUsersRequests2(@RequestParam("page") int page, @RequestParam("size") int size,@RequestParam("status") Long status, @RequestParam("sessionKey") String sessionKey){                
        return UsersRequestRepository.findForAdmin(status,size,(page*size)-size);        
    } 


    @GetMapping("/admin/users/{userId}")
    public MappingJacksonValue getUser(@PathVariable Long userId,@RequestParam("sessionKey") String sessionKey){
        MappingJacksonValue mapping = new MappingJacksonValue(UserRepository.findByUserId(userId));
        mapping.setFilters(privateUserFilter());
        return mapping;
    }

    @GetMapping("/admin/users/{userId}/wallet")
    public wallet getWallet(@PathVariable int userId,@RequestParam("sessionKey") String sessionKey){
        return WalletRepository.findByUserId(userId);
    }

    @GetMapping("/admin/users/{userId}/donations")
    public List<transaction> getDonations(@PathVariable Long userId,@RequestParam("sessionKey") String sessionKey){
        wallet userWallet = WalletRepository.findByUserId(userId);
        return userWallet.getTransaction();    
    }

    @GetMapping("/admin/users/{userId}/requests")
    public List<usersRequest> getRequests(@PathVariable Long userId,@RequestParam("sessionKey") String sessionKey){
        return UsersRequestRepository.findByUserId(userId);
    }

    @DeleteMapping("/admin/users/{requestId}/requests/id")
    public void deleteRequests(@PathVariable Long requestId,@RequestParam("sessionKey") String sessionKey){
        UsersRequestRepository.deleteById(requestId);
    }

    @PutMapping("/admin/users/{userId}/status")
    public void putBlockedUser(@PathVariable Long userId,@RequestParam("blockStatus") Long blockStatus,@RequestParam("sessionKey") String sessionKey){
        users User = UserRepository.findByUserId(userId);
        User.setBlocked(blockStatus);
        UserRepository.save(User);
    }

    // FUdnaraises

    @PutMapping("/admin/requests/{requestId}/status") //Set the request status 0-blocked 1-active 2-succeeded 3-userWithdrawn 4-Expired 
    public void updateRequestStatus(@PathVariable("requestId") Long requestId,@RequestParam("status") Long status,@RequestParam("sessionKey") String sessionKey){
        int updateStatus = UsersRequestRepository.updateRequestStatus(status,requestId);                
        System.out.println(updateStatus);        
    }

    @RequestMapping(value = "/admin/requests/{requestId}/status",method = RequestMethod.OPTIONS)
    public void updateRequestStatus(@PathVariable("requestId") Long requestId){
        ;
    }
    


    public FilterProvider privateUserFilter(){
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(
            "fname","lname","dob","email","phNumber","username","userId","sessionKey","avatarUrl","role","blocked"
        );

        FilterProvider filters = new SimpleFilterProvider().addFilter("userFilter", filter);        
        return filters;
    }
    
}
