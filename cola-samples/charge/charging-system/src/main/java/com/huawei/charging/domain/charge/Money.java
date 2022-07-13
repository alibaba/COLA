package com.huawei.charging.domain.charge;


import lombok.Data;

import java.util.Objects;

/**
 * 这个Money是简化版的，真实场景应该用BigDecimal
 */
@Data
public class Money {

    /**
     * 单位是角，1代表0.1元， 10代表1元
     */
    private int amount;

    public Money(int amount) {
        this.amount = amount;
    }

    public static Money of(int amount){
        return new Money(amount);
    }

    public boolean isLessThan(Money money){
        return this.amount <= money.getAmount();
    }

    public void minus(Money money){
        this.amount =  this.amount - money.getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
