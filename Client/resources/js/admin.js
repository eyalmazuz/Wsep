headers = {
    headers: {          
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
}



async function showViewUserHistory(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var result;
    subscribersURL = 'https://localhost:8443/getAllSubscribers?'
    
    subscribersURL += 'sessionId=' + sessionStorage['sessionId']
    
    await fetch(subscribersURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result);
    
    var x = document.getElementById("userHistorySelect");
    
    for(i = x.length-1; i >= 0; i--) {
        x.remove(i);
     }

    for(subscriberIdx in result['subscribers']){
        subscriber = result['subscribers'][subscriberIdx]
        console.log(subscriber)
        var option = document.createElement("option");
        option.innerHTML = "Id: " + subscriber['id'] + " Name: "+ subscriber['username']
        x.add(option); 
    }

    document.getElementById('viewUserHistoryForm').style.display='block'

}


async function getUserHistory(){

    userHistoryURL = 'https://localhost:8443/getSubscriberHistory?'

    userHistoryURL += 'sessionId=' + sessionStorage['sessionId']

    var user = document.getElementById('userHistorySelect').value.split(' ')
    console.log(user)
    userHistoryURL += "&subId=" +parseInt(user[1]);

    console.log(userHistoryURL)
    var result;
    await fetch(userHistoryURL, headers).then(response => response.json()).then(response => result = response)
    
    histroyTable = document.getElementById('adminUserHistoryTable')

    console.log(result)
    
    if(result['resultCode'] === 'SUCCESS'){
        document.getElementById('viewUserHistoryForm').style.display='none'
        for(i = histroyTable.rows.length-1; i >= 1; i--) {
            histroyTable.deleteRow(i);
        }
        
        ridx = 1;
        baskets = result['store2details']
        for (storeIdx in baskets){
            for(purchase in baskets[storeIdx]){
                basket = baskets[storeIdx][purchase]

                console.log(basket)
                for (productIdx in basket['mapProductsAmount']){
                    var row = histroyTable.insertRow(ridx);
                    var storeId = row.insertCell(0)
                    var purchaseId = row.insertCell(1);
                    var productId = row.insertCell(2);
                    var productName = row.insertCell(3);
                    var productCategory = row.insertCell(4);
                    var productAmount = row.insertCell(5);
                    row.insertCell(6);

                    storeId.innerHTML = storeIdx
                    purchaseId.innerHTML = basket['id']

                    var product = basket['mapProductsAmount'][productIdx]
                    productId.innerHTML = product['productInfo']['id']
                    productName.innerHTML = product['productInfo']['name']
                    productCategory.innerHTML = product['productInfo']['category']

                    productAmount.innerHTML = product['amount']
                    ridx++;
                    
                }

            var row = histroyTable.insertRow(ridx);
            row.insertCell(0);
            row.insertCell(1);
            row.insertCell(2);
            row.insertCell(3);
            row.insertCell(4);
            row.insertCell(5);
            var basketPrice = row.insertCell(6);
            basketPrice.innerHTML = basket['price']
            ridx++;
            }

        }
        
    }
    
}



async function showViewShopHistory(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var result;
    subscribersURL = 'https://localhost:8443/viewStoreProductInfo?'

    subscribersURL += 'sessionId=' + sessionStorage['sessionId']
    
    await fetch(subscribersURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result);
    
    var x = document.getElementById("shopHistorySelect");
    
    for(i = x.length-1; i >= 0; i--) {
        x.remove(i);
     }

    for(storeIdx in result['stores']){
        store = result['stores'][storeIdx]
        console.log(store)
        console.log(store['storeId'])
        var option = document.createElement("option");
        option.innerHTML = "Id: " + store['storeId']
        x.add(option); 
    }

    document.getElementById('viewStoreHistoryForm').style.display='block'

}


async function getShopHistory(){

    userHistoryURL = 'https://localhost:8443/getStoreHistory?'

    userHistoryURL += 'sessionId=' + sessionStorage['sessionId']

    var store = document.getElementById('shopHistorySelect').value.split(' ')
    console.log(store)
    userHistoryURL += "&storeId=" +parseInt(store[1]);

    console.log(userHistoryURL)
    var result;
    await fetch(userHistoryURL, headers).then(response => response.json()).then(response => result = response)
    
    histroyTable = document.getElementById('adminStoreHistoryTable')

    console.log(result)
    
    if(result['resultCode'] === 'SUCCESS'){
        document.getElementById('viewStoreHistoryForm').style.display='none'
        for(i = histroyTable.rows.length-1; i >= 1; i--) {
            histroyTable.deleteRow(i);
        }
        
        ridx = 1;
        purchases = result['purchases']
        for (purchaseIdx in purchases){
            purchase = purchases[purchaseIdx]
            console.log(purchase)
            for(itemIdx in purchase['mapProductsAmount']){
                item = purchase['mapProductsAmount'][itemIdx]
                console.log(item)
                var row = histroyTable.insertRow(ridx);
                var purchaseId = row.insertCell(0);
                var productId = row.insertCell(1);
                var productName = row.insertCell(2);
                var productAmount = row.insertCell(3);
                row.insertCell(4);

                purchaseId.innerHTML = purchaseIdx

                productId.innerHTML = item['productInfo']['id']
                productName.innerHTML = item['productInfo']['name']
                productAmount.innerHTML = item['amount']

                ridx++;
            }                        

            var row = histroyTable.insertRow(ridx);
            row.insertCell(0);
            row.insertCell(1);
            row.insertCell(2);
            row.insertCell(3);
            var basketPrice = row.insertCell(4);
            basketPrice.innerHTML = purchase['price']
            ridx++;
        }
        
    }
    
}

async function loadAdminPage(){
    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }
}

async function logoutAdmin(){
    await logout()
    location.href = 'index.html'
}