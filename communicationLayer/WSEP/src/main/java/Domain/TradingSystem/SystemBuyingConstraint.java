package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

import java.util.Calendar;

public class SystemBuyingConstraint extends SimpleBuying {

    public ActionResultDTO canBuy() {
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public static class NotOnDayConstraint extends SystemBuyingConstraint {

        private int forbiddenDay;

        public NotOnDayConstraint(int forbiddenDay) {
            this.forbiddenDay = forbiddenDay;
        }

        public ActionResultDTO canBuy() {
            return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != forbiddenDay? new ActionResultDTO(ResultCode.SUCCESS, null) :
                    new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Today is " + forbiddenDay + " and therefore purchase is rejected.");
        }

        public String toString() {
            return "Cannot buy on day " + forbiddenDay + " of the week";
        }
    }

}
