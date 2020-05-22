package Domain.TradingSystem;

import DTOs.DiscountPolicyActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import DTOs.SimpleDTOS.DiscountTypeDTO;

import java.util.*;

public class DiscountPolicy {

    private String details;
    private Map<Integer, DiscountType> discountTypes = new HashMap<>();

    public DiscountPolicy(String details) {
        this.details = details;
    }

    public double getBasketDiscountedPrice(User user, ShoppingBasket basket) {
        List<DiscountBasket.DiscountProduct> discountProducts = new ArrayList<>();

        Map<ProductInfo, Integer> products = basket.getProducts();

        for (Map.Entry<ProductInfo, Integer> entry : products.entrySet()) {
            double productPrice = basket.getProductPrice(entry.getKey().getId());

            //java.lang.System.out.println(entry.getValue());
            //system.println(entry.getValue());
            discountProducts.add(new DiscountBasket.DiscountProduct(entry.getKey().getId(), entry.getKey().getCategory(), entry.getValue(), entry.getValue() * productPrice));
        }


        DiscountBasket discountBasket = new DiscountBasket(discountProducts);


        for (DiscountType discount: discountTypes.values()) {
            discountBasket = discount.getDiscountedBasket(user, discountBasket);
        }

        return discountBasket.getTotalPrice();
    }


    @Override
    public String toString() {
        String output = "";
        for (DiscountType type : discountTypes.values()) {
            output += type.toString() + "\n";
        }
        return output;
    }

    public int addDiscountType(DiscountType discount) {
        int id = 0;
        if (!discountTypes.isEmpty()) id = Collections.max(discountTypes.keySet()) + 1;
        discountTypes.put(id, discount);
        return id;
    }

    public IntActionResultDto addAdvancedDiscountType(List<Integer> discountTypeIDs, String logicalOperationStr) {
        synchronized (discountTypes) {
            List<DiscountType> relevantDiscountTypes = new ArrayList<>();
            for (Integer typeID : discountTypeIDs) {
                if (discountTypeIDs.get(typeID) == null) return new IntActionResultDto(ResultCode.ERROR_STORE_DISCOUNT_POLICY_CHANGE, "There is no discount type ID" + typeID, -1);
                relevantDiscountTypes.add(discountTypes.get(typeID));
                discountTypeIDs.remove(typeID);
            }
            AdvancedDiscount.LogicalOperation logicalOperation = AdvancedDiscount.LogicalOperation.AND;
            if (logicalOperationStr.toLowerCase().equals("or")) logicalOperation = AdvancedDiscount.LogicalOperation.OR;
            else if (logicalOperationStr.toLowerCase().equals("xor")) logicalOperation = AdvancedDiscount.LogicalOperation.XOR;
            DiscountType advanced = new AdvancedDiscount.LogicalDiscount(relevantDiscountTypes, logicalOperation);
            return new IntActionResultDto(ResultCode.SUCCESS, null, addDiscountType(advanced));
        }
    }

    public void removeDiscountType(int discountTypeId) {
        discountTypes.remove(discountTypeId);
    }

    public void clearDiscountTypes() {
        discountTypes.clear();
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public DiscountPolicyActionResultDTO getDTO() {
        List<DiscountTypeDTO> dtos = new ArrayList<>();
        for (Integer id : discountTypes.keySet()) {
            DiscountType type = discountTypes.get(id);
            dtos.add(new DiscountTypeDTO(id, type.toString()));
        }
        return new DiscountPolicyActionResultDTO(ResultCode.SUCCESS, null, dtos);
    }




}
