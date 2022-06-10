package com.api.spring.boot.funsho.api.resource;

import java.util.Date;
import java.util.List;
// import java.util.Map;

import com.api.spring.boot.funsho.api.dto.requests.donateDTO;
import com.api.spring.boot.funsho.api.dto.requests.newReqDTO;
import com.api.spring.boot.funsho.api.dto.requests.updateRequestDTO;
import com.api.spring.boot.funsho.api.entity.users;
import com.api.spring.boot.funsho.api.entity.requestsEntity.usersRequest;
import com.api.spring.boot.funsho.api.entity.wallet.transaction;
import com.api.spring.boot.funsho.api.entity.wallet.wallet;
import com.api.spring.boot.funsho.api.exceptions.notAcceptableException;
import com.api.spring.boot.funsho.api.exceptions.unauthorizedException;
import com.api.spring.boot.funsho.api.exceptions.userNotFoundException;
import com.api.spring.boot.funsho.api.repository.transactionRepository;
import com.api.spring.boot.funsho.api.repository.userRepository;
import com.api.spring.boot.funsho.api.repository.usersRequestRepository;
import com.api.spring.boot.funsho.api.repository.walletRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@CrossOrigin
public class usersRequestResource {


    @Autowired
    usersRequestRepository UsersRequestRepository;

    @Autowired
    userRepository UserRepository;

    @Autowired
    walletRepository WalletRepository;

    @Autowired
    transactionRepository TransactionRepository;

    @PostMapping("/requests")
    public usersRequest saveUsersRequests(@RequestBody newReqDTO obj,@RequestParam("sessionKey") String sessionKey){
        
        usersRequest usersRequest = new usersRequest(); 

        users user = UserRepository.findBySessionKey(sessionKey);
        System.out.println(user.getUserId());
        Long userId = user.getUserId();
        usersRequest.setUserId(userId);    
        usersRequest.setVotes(0l);
        usersRequest.setAmountRecieved(0l);
        usersRequest.setFname(obj.getFname());
        usersRequest.setLname(obj.getLname());
        usersRequest.setGender(obj.getGender());
        usersRequest.setBackground(obj.getBackground());
        usersRequest.setDateOfBirth(obj.getDateOfBirth());
        usersRequest.setInstitutionName(obj.getInstitutionName());
        usersRequest.setStudyProgram(obj.getStudyProgram());
        usersRequest.setInstitutePlace(obj.getInstitutePlace());        
        usersRequest.setAdditionalEdInfo(obj.getAdditionalEdInfo());
        usersRequest.setPhoneNumber(obj.getPhoneNumber());
        usersRequest.setAddress(obj.getAddress());
        usersRequest.setCity(obj.getCity());
        usersRequest.setPinCode(obj.getPinCode());
        usersRequest.setStateRegion(obj.getStateRegion());
        usersRequest.setPersonalEmail(obj.getPersonalEmail());
        usersRequest.setEventImageUrl(obj.getEventImageUrl());
        usersRequest.setEventDescription(obj.getEventDescription());
        usersRequest.setEventTitle(obj.getEventTitle());
        usersRequest.setAmountRequired(obj.getAmountRequired());
        usersRequest.setDeadLine(obj.getDeadLine());
        usersRequest.setAddtionalFilesUrl(obj.getAddtionalFilesUrl());
        usersRequest.setEventDescription(obj.getEventDescription());
                
        System.out.println(usersRequest.toString());
        UsersRequestRepository.save(usersRequest);
        return usersRequest;
    }
    
    @GetMapping("/requests") 
    public List<usersRequest> getUsersRequests(@RequestParam("page") int page, @RequestParam("size") int size,@RequestParam("featured") boolean featured){
        
        if(featured){
            return UsersRequestRepository.findAll(PageRequest.of(page, size, Sort.by("votes"))).getContent();
        }
        else{
            return UsersRequestRepository.findAll(PageRequest.of(page, size, Sort.by("votes").descending())).getContent();
        }           
    } 


    @GetMapping("/requests/{id}")
    public usersRequest getUsersRequestsById(@PathVariable("id") Long id){
        usersRequest a =  UsersRequestRepository.findByRequestId(id);
        return a;
    }

    @RequestMapping(value ="/requests/{id}", method = RequestMethod.PUT)
    public usersRequest updateUsersRequests(@PathVariable("id") Long id, @RequestBody updateRequestDTO obj,@RequestParam("sessionKey") String sessionKey){

        usersRequest usersRequest = UsersRequestRepository.findByRequestId(id);
        
        if(usersRequest.getUserId() != UserRepository.findBySessionKey(sessionKey).getUserId()) throw new userNotFoundException("You are not authorized to update this request");

        if(obj.getPhoneNumber().length() != 0) usersRequest.setPhoneNumber(obj.getPhoneNumber());        
        if(obj.getPersonalEmail().length() != 0) usersRequest.setPersonalEmail(obj.getPersonalEmail());
        if(obj.getEventImageUrl().length() != 0) usersRequest.setEventImageUrl(obj.getEventImageUrl());
        if(obj.getEventDescription().length() != 0) usersRequest.setEventDescription(obj.getEventDescription());
        if(obj.getEventTitle().length() != 0) usersRequest.setEventTitle(obj.getEventTitle());
        if(obj.getAmountRequired()!= 0) usersRequest.setAmountRequired(obj.getAmountRequired());
        if(obj.getDeadLine()!=null) usersRequest.setDeadLine(obj.getDeadLine());
        if(obj.getAddtionalFilesUrl().length() != 0) usersRequest.setAddtionalFilesUrl(obj.getAddtionalFilesUrl());
        if(obj.getAdditionalEdInfo().length() != 0) usersRequest.setAdditionalEdInfo(obj.getAdditionalEdInfo());

        UsersRequestRepository.save(usersRequest);
        return usersRequest;
    }
    
    @RequestMapping(value ="/requests/{id}", method = RequestMethod.DELETE)
    public void deleteUsersRequests(@PathVariable("id") Long id,@RequestParam("sessionKey") String sessionKey){

        users user = UserRepository.findBySessionKey(sessionKey);
        if(user == null){
            throw new userNotFoundException("User Not Found for this sessionKey");
        }
        usersRequest usersRequest = UsersRequestRepository.findByRequestId(id);

        if(usersRequest == null) throw new userNotFoundException("Request Not Found");
        if(user.getUserId()-usersRequest.getUserId()!=0) throw new userNotFoundException("You are not authorized to delete this request");
        
        UsersRequestRepository.deleteById(id);
                    
    }
    
    @PostMapping("/requests/{id}/donate")
    public ResponseEntity<String> donate(@PathVariable("id") Long id,@RequestBody donateDTO obj,@RequestParam("sessionKey") String sessionKey){      

        usersRequest usersRequest = UsersRequestRepository.findByRequestId(id);
        if(usersRequest == null) throw new userNotFoundException("Request Not Found");

        users user = UserRepository.findBySessionKey(sessionKey);
        if(user == null) throw new userNotFoundException("User Not Found for this sessionKey");

        if(user.getUserId()-usersRequest.getUserId()==0) throw new unauthorizedException("You are not authorized to donate your own request");

        wallet userWallet = WalletRepository.findByUserId(user.getUserId());
        if(userWallet == null) throw new userNotFoundException("User Wallet is locked contact admin");

        if(userWallet.getBalance() < obj.getDonationAmount()) throw new notAcceptableException("Insufficient Funds");

        userWallet.setBalance(userWallet.getBalance() - obj.getDonationAmount());
        usersRequest.setAmountRecieved(usersRequest.getAmountRecieved()+obj.getDonationAmount());

        transaction t = new transaction();

        t.setTransactionAmount(obj.getDonationAmount());
        t.setTransactionType("Donation from wallet");
        t.setTransactionDateTime(new Date());
        t.setRequestId(usersRequest.getRequestId());
        t.setTransactionDescription(obj.getDonationDescription());
        t.setTransactionStatus("Success");      

        userWallet.getTransaction().add(t);

        WalletRepository.save(userWallet);
        UsersRequestRepository.save(usersRequest);
        TransactionRepository.save(t);


        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Donation Successful");
        
    }


    @GetMapping("/requests/{id}/donations")
    public List<transaction> getDonations(@PathVariable("id") Long id){        
        usersRequest usersRequest = UsersRequestRepository.findByRequestId(id);
        if(usersRequest == null) throw new userNotFoundException("Request Not Found");
        
        return TransactionRepository.findByRequestId(id);
    }

    
}
