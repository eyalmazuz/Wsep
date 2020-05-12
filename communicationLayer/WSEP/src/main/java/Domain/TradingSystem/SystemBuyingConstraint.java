package Domain.TradingSystem;

import java.util.Calendar;

public class SystemBuyingConstraint extends SimpleBuying {

    public class NotOnDayConstraint extends SystemBuyingConstraint {

        private int forbiddenDay;

        public NotOnDayConstraint(int forbiddenDay) {
            this.forbiddenDay = forbiddenDay;
        }

        public boolean canBuy() {
            return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != forbiddenDay;
        }
    }

}
