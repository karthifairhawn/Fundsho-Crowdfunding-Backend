package com.api.spring.boot.funsho.api.resource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
// import java.util.Collections;
import java.util.Date;
import java.util.List;
// import java.util.Map;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
@CrossOrigin
public class usersRequestResource {

    public static final String folderPath =  "incoming-files//";
    public static final Path filePath = Paths.get(folderPath);    
    
    @Autowired
    usersRequestRepository UsersRequestRepository;

    @Autowired
    userRepository UserRepository;

    @PostMapping("/requests")
    public usersRequest saveUsersRequests(@RequestBody usersRequest obj){
        System.out.println();
        System.out.println(obj.toString());
        System.out.println();
        obj.setVotes(0l);
        obj.setAmountRecieved(0l);        
        return UsersRequestRepository.save(obj);
    }


    @GetMapping("/requests")
    public List<usersRequest> getAllUsersRequests(){
        return UsersRequestRepository.findAll();
    }


    @GetMapping("/requests/{id}")
    public usersRequest getUsersRequestsById(@PathVariable("id") Long id){
        usersRequest a =  UsersRequestRepository.findByRequestId(id);
        return a;
    }

    @GetMapping("/requests") 
    public List<usersRequest> getUsersRequests(@RequestParam("page") int page, @RequestParam("size") int size){
        Pageable pageFormat = PageRequest.of(page, size, Sort.by("requestId").descending());    
        Page<usersRequest>  res = UsersRequestRepository.findAll(pageFormat);  
        return res.getContent();            
    } 

    @GetMapping("/requests/featured/{page}/{size}")
    public List<usersRequest> getUsersRequests(){
        Pageable firstPage = PageRequest.of(0, 3, Sort.by("amountRecieved").descending());
        List<usersRequest>  page = UsersRequestRepository.findAll(firstPage).getContent();        
        return page;
    }    

    // @GetMapping("/requests/search/{page}/{size}")
    // public List<usersRequest> getUsersRequests(@RequestParam("search") String search, @PathVariable("page") int page, @PathVariable("size") int size){
    //     Pageable pageFormat = PageRequest.of(page, size, Sort.by("requestId").descending());    
    //     Page<usersRequest>  res = UsersRequestRepository.findByRequestNameContaining(search, pageFormat);  
    //     return res.getContent();            
    // }

    


    @PostMapping("/donatereq")
    public wallet donateToRequest(@RequestBody donateRequest request){

        
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


  

    // Save File and return url Starts 
    @PostMapping("/savefile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            createDirIfNotExist();

            byte[] bytes = new byte[0];
            bytes = file.getBytes();
            String newName = new Date().hashCode()+file.getOriginalFilename();
            Files.write(Paths.get(folderPath + newName), bytes);
            return newName;
            // return ResponseEntity.status(HttpStatus.OK)
            //         .body(newName);
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
            //         .body("Exception occurred for: " + file.getOriginalFilename() + "!");
            return "failed";
        }
    }

    private void createDirIfNotExist() {
        //create directory to save the files
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    @GetMapping("/files")
    public ResponseEntity<String[]> getListFiles() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new java.io.File(folderPath).list());
    }

    // Save File Ends...
}
