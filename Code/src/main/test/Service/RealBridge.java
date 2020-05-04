package Service;


import DTOs.ActionResultDTO;
import DTOs.ResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.transform.Result;

public class RealBridge implements Bridge {

    ObjectMapper mapper = new ObjectMapper();

    SessionHandler dc = new SessionHandler();

    public RealBridge(){}

    public boolean setupSystem(String supplyConfig, String paymentConfig) {
        return dc.setup(supplyConfig, paymentConfig); }

    public boolean login(int sessionId, String username, String password) {
        GuestUserHandler guh = new GuestUserHandler();
        try {
            return mapper.readValue(guh.login(sessionId, username, password), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int register(int sessionId, String username, String password) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.register(sessionId, username, password);
    }

    public String getAllInfo(int sessionId) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.viewStoreProductInfo();
    }

    public String searchProducts(int sessionId, String productName, String category, String[] keywords, int productRating, int storeRating, int priceFrom, int priceTo) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.searchProducts(sessionId, productName, category, keywords, productRating, storeRating);
    }

    public boolean addToCart(int sessionId, int storeId, int productId, Integer amount) {
        GuestUserHandler guh = new GuestUserHandler();
        try {
            return mapper.readValue(guh.addProductToCart(sessionId, storeId, productId, amount), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAmount(int sessionId, int storeId, int productId, int amount) {
        GuestUserHandler guh = new GuestUserHandler();
        try {
            return mapper.readValue(guh.editProductInCart(sessionId, storeId, productId, amount), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItemInCart(int sessionId, int storeId, int productId) {
        GuestUserHandler guh = new GuestUserHandler();
        try {
            return mapper.readValue(guh.removeProductInCart(sessionId, storeId, productId), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearCart(int sessionId) {
        GuestUserHandler guh = new GuestUserHandler();
        try {
            return mapper.readValue(guh.clearCart(sessionId), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean buyCart(int sessionId, String paymentDetails) {
        GuestUserHandler guh = new GuestUserHandler();
        try {
            if (mapper.readValue(guh.requestPurchase(sessionId), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS) {
                return mapper.readValue(guh.confirmPurchase(sessionId, paymentDetails), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public String viewCart(int sessionId){
        GuestUserHandler guh = new GuestUserHandler();
        return guh.viewCart(sessionId);
    }

    public boolean logout(int sessionId){
        SubscriberStateHandler ssh = new SubscriberStateHandler(sessionId);
        try {
            return mapper.readValue(ssh.logout(), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int openStore(int sessionId) {
        SubscriberStateHandler ssh = new SubscriberStateHandler(sessionId);
        return ssh.openStore();
    }

    public String viewPurchaseHistory(int sessionId){
        SubscriberStateHandler ssh = new SubscriberStateHandler(sessionId);
        return ssh.getHistory();
    }

    public String searchUserHistory(int sessionId, int userId){
        AdminStateHandler ash = new AdminStateHandler(sessionId);
        return ash.getSubscriberHistory(userId);
    }

    public boolean addProduct(boolean flag, int sessionId, int productId, int storeId, int amount) {
        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            try {
                return mapper.readValue(oh.addProductToStore(storeId, productId, amount), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            try {
                return mapper.readValue(mh.addProductToStore(storeId, productId, amount), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean editProduct(boolean flag, int sessionId, int storeId, int productId, String productInfo) {
        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            try {
                return mapper.readValue(oh.editProductToStore(storeId, productId, productInfo), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            try {
                return mapper.readValue(mh.editProductToStore(storeId, productId, productInfo), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean deleteProduct(boolean flag, int sessionId, int storeId, int productId) {
        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            try {
                return mapper.readValue(oh.deleteProductFromStore(storeId, productId), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            try {
                return mapper.readValue(mh.deleteProductFromStore(storeId, productId), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean appointManager(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        try {
            return mapper.readValue(oh.addStoreManager(storeId, userId), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean appointOwner(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        try {
            return mapper.readValue(oh.addStoreOwner(storeId, userId), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeManager(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        try {
            return mapper.readValue(oh.deleteManager(storeId, userId), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editManagerOptions(int sessionId, int storeId, int userId, String option){
        OwnerHandler oh = new OwnerHandler(sessionId);
        try {
            return mapper.readValue(oh.editManageOptions(storeId, userId, option), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String viewShopHistory(int sessionId, int storeId){
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.viewPurchaseHistory(storeId);
    }

    public String getStoreHistory(int sessionId, int storeId) {
        AdminStateHandler ash = new AdminStateHandler(sessionId);
        return ash.getStoreHistory(storeId);
    }

    public void addProductInfo(int sessionId, int id, String name, String category){
        AdminStateHandler ash = new AdminStateHandler(sessionId);
        ash.addProductInfo(id, name, category);
    }

    public int startSession() { return dc.startSession(); }

    public boolean changeBuyingPolicy(int sessionId, boolean flag, int storeId, String newPolicy){

        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            try {
                return mapper.readValue(oh.changeBuyingPolicy(storeId, newPolicy), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            try {
                return mapper.readValue(mh.changeBuyingPolicy(storeId, newPolicy), ActionResultDTO.class).getResultCode() == ResultCode.SUCCESS;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
