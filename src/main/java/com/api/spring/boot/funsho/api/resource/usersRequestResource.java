package com.api.spring.boot.funsho.api.resource;

import java.util.Date;
import java.util.List;
// import java.util.Map;

import com.api.spring.boot.funsho.api.dto.requests.newReqDTO;
import com.api.spring.boot.funsho.api.entity.users;
import com.api.spring.boot.funsho.api.entity.requestsEntity.donateRequest;
import com.api.spring.boot.funsho.api.entity.requestsEntity.usersRequest;
import com.api.spring.boot.funsho.api.entity.wallet.transaction;
import com.api.spring.boot.funsho.api.entity.wallet.wallet;
import com.api.spring.boot.funsho.api.repository.userRepository;
import com.api.spring.boot.funsho.api.repository.usersRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@CrossOrigin
public class usersRequestResource {


    @Autowired
    usersRequestRepository UsersRequestRepository;

    @Autowired
    userRepository UserRepository;

    @PostMapping("/requests")
    public usersRequest saveUsersRequests(@RequestBody newReqDTO obj){
        
        usersRequest usersRequest = new usersRequest();        
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
                
        return UsersRequestRepository.save(usersRequest);
    }
    
    @GetMapping("/requests") 
    public Page<usersRequest> getUsersRequests(@RequestParam("page") int page, @RequestParam("size") int size,@RequestParam("featured") boolean featured){
        
        if(featured){
            return UsersRequestRepository.findAll(PageRequest.of(page, size, Sort.by("votes").descending()));
        }
        else{
            return UsersRequestRepository.findAll(PageRequest.of(page, size, Sort.by("votes").descending()));
        }           
    } 


    @GetMapping("/requests/{id}")
    public usersRequest getUsersRequestsById(@PathVariable("id") Long id){
        usersRequest a =  UsersRequestRepository.findByRequestId(id);
        return a;
    }


    @PostMapping("/requests/{id}/donate")
    public wallet donateToRequest(@RequestBody donateRequest request,@PathVariable("id") Long id){

        
        users handlingUser = UserRepository.findBySessionKey(request.getSessionId());
        wallet handlingUserWallet = handlingUser.getWallet();
        usersRequest handlingRequests = UsersRequestRepository.findByRequestId(request.getRequestId());

        if(handlingRequests.getAmountRecieved() >=handlingRequests.getAmountRequired()){
            System.out.println("Already Req dumbed");
            return handlingUserWallet;
        }

        users recievingUser = UserRepository.findByUserId(handlingRequests.getUserId());
        wallet recievingUserWallet = recievingUser.getWallet();

        // if(handlingUser.getUserId()==recievingUser.getUserId()) return recievingUserWallet;
        // if(handlingUser.getWallet().getBalance()<request.getDonationAmount()) return handlingUserWallet;

        
        System.out.println();
        System.out.println("HandlingUser Updated");
        System.out.println("HandlingUser Name "+handlingUser.getFname());
        System.out.println("Reciever Name "+recievingUser.getFname());
        System.out.println();

        recievingUserWallet.setBalance(recievingUserWallet.getBalance()+request.getDonationAmount());  // Update Reciever Balance (Working)         
        transaction recieverTransaction = transaction.builder()
                    .reason("Donation Recieved From "+handlingUser.getFname())
                    .amount(request.getDonationAmount())
                    .status(true)
                    .direction("in")
                    .timestamp(new Date())
                    .build();
        recievingUserWallet.getTransaction().add(recieverTransaction);        
        UserRepository.save(recievingUser);




        handlingUserWallet.setBalance(handlingUserWallet.getBalance()-request.getDonationAmount());   
        transaction donorTransaction = transaction.builder()
                    .reason("Donation to "+recievingUser.getFname()+" successfull")
                    .amount(request.getDonationAmount())
                    .status(true)
                    .direction("out")
                    .timestamp(new Date())
                    .build();
        handlingUserWallet.getTransaction().add(donorTransaction);       
        UserRepository.save(handlingUser);


        handlingRequests.setAmountRecieved(handlingRequests.getAmountRecieved()+request.getDonationAmount());
        UsersRequestRepository.save(handlingRequests);

        return handlingUser.getWallet();
    }



}
