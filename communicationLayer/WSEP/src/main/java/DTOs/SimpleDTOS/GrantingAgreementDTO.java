package DTOs.SimpleDTOS;

import java.util.Map;

public class GrantingAgreementDTO {
    int storeId;
    SubscriberDTO grantor;
    SubscriberDTO candidate;
    Map<Integer,Boolean> ownersDecision;

    public GrantingAgreementDTO(int storeId, SubscriberDTO grantor, SubscriberDTO candidate, Map<Integer, Boolean> ownersDecision) {
        this.storeId = storeId;
        this.grantor = grantor;
        this.candidate = candidate;
        this.ownersDecision = ownersDecision;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public SubscriberDTO getGrantor() {
        return grantor;
    }

    public void setGrantor(SubscriberDTO grantor) {
        this.grantor = grantor;
    }

    public SubscriberDTO getCandidate() {
        return candidate;
    }

    public void setCandidate(SubscriberDTO candidate) {
        this.candidate = candidate;
    }

    public Map<Integer, Boolean> getOwnersDecision() {
        return ownersDecision;
    }

    public void setOwnersDecision(Map<Integer, Boolean> ownersDecision) {
        this.ownersDecision = ownersDecision;
    }
}
