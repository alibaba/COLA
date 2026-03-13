package com.huawei.charging.domain.gateway;

import com.huawei.charging.domain.charge.ChargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeGateway extends JpaRepository<ChargeRecord, Long> {
    public List<ChargeRecord> findBySessionId(String sessionId);

    public ChargeRecord getBySessionId(String sessionId);

    public List<ChargeRecord> findByPhoneNo(long phoneNo);
}
