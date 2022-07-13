package com.huawei.charging.domain.charge.chargeplan;

public class FixedTimeChangePlan extends ChargePlan<FixedTimeChangePlan.FreeCallTime>{

    public FixedTimeChangePlan() {
        this.priority=1;
    }

    @Override
    public FreeCallTime getResource() {
        return new FreeCallTime();
    }

    @Override
    public ChargePlanType getType() {
        return ChargePlanType.FIXED_TIME;
    }

    public static class FreeCallTime implements Resource{
        public static int FREE_CALLING_TIME = 200;
        public static int FREE_CALLED_TIME = 200;

        public boolean isCallingTimeRemaining(){
            return FREE_CALLING_TIME > 0;
        }

        /**
         * 扣减固定时长套餐的费用
         * @param duration 扣减时长
         * @return 剩余还需要扣减的时长
         */
        public int chargeFreeCallingTime(int duration){
            if(duration > FREE_CALLING_TIME){
                int durationToCharge = duration - FREE_CALLING_TIME;
                FREE_CALLING_TIME = 0;
                return durationToCharge;
            }
            else{
                FREE_CALLING_TIME = FREE_CALLING_TIME - duration;
                return 0;
            }
        }

        public boolean isCalledTimeRemaining(){
            return FREE_CALLED_TIME > 0;
        }

        /**
         * 扣减固定时长套餐的费用
         * @param duration 扣减时长
         * @return 剩余还需要扣减的时长
         */
        public int chargeFreeCalledTime(int duration){
            if(duration > FREE_CALLED_TIME){
                int durationToCharge = duration - FREE_CALLED_TIME;
                FREE_CALLED_TIME = 0;
                return durationToCharge;
            }
            else{
                FREE_CALLED_TIME = FREE_CALLED_TIME - duration;
                return 0;
            }
        }
    }
}
