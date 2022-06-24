package com.api.spring.boot.funsho.api.repository;

import java.util.List;

import com.api.spring.boot.funsho.api.entity.requestsEntity.usersRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface usersRequestRepository extends JpaRepository<usersRequest,Long>{

    usersRequest findByRequestId(long requestId);

    List<usersRequest> findByRequestIdNot(Long requestId,Pageable pageable);

    List<usersRequest> findByUserIdNot(long id, Pageable pageFormat);    
    
    Page<usersRequest> findAll(Pageable pageFormat);

    List<usersRequest> findByUserId(Long userId);

    @Query(value="select * from users_request where req_status = ?1 order by votes desc limit ?2 offset ?3",nativeQuery=true)
    List<usersRequest> findByReqStatusIs(Long i,int size,int page);

    @Query(value="select * from users_request where req_status = ?1 limit ?2 offset ?3",nativeQuery=true)
    List<usersRequest> findByReqStatusIsNonFeatured(Long i,int size,int page);

    
    
}
