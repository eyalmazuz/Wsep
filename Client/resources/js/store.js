headers = {
    headers: {          
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
}


async function viewStore(){

    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }

    var type = '';

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    storeManagersURL = 'https://localhost:8443/getAllManagers?'
    

    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(sessionStorage['username'] === manager['username']){
            type = manager['type']
        }
    }



    console.log(typeof(storeId))
    var storeURL = "https://localhost:8443/getStore?"
    
    storeURL += 'sessionId=' + sessionStorage['sessionId']

    storeURL += "&storeId=" + parseInt(storeId)

    var result
    await fetch(storeURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    
    if(result['resultCode'] === 'SUCCESS'){

        var policies = document.getElementById('storePolicies')
        policies.rows[1].cells[0].innerHTML = result['stores'][0]['buyingPolicy']
        policies.rows[1].cells[1].innerHTML = result['stores'][0]['discountPolicy']

        
        var products = result['stores'][0]['products']
        var productsTable = document.getElementById('storeProducts')

        for(var i = 1; i< productsTable.rows.length; i++){
            productsTable.deleteRow(i);
        }

        var ridx = 1;
        for(productIdx in products){
            var product = products[productIdx]
            var row = productsTable.insertRow(ridx)
            var productId = row.insertCell(0)
            var productName = row.insertCell(1)
            var productCategory = row.insertCell(2)
            var productInfo = row.insertCell(3)
            var productAmount = row.insertCell(4)
            var DeleteProduct = row.insertCell(5)

            productId.innerHTML = product['productId']
            productName.innerHTML = product['name']
            productCategory.innerHTML = product['category']
            productInfo.innerHTML = product['info']
            productAmount.innerHTML = product['amount']
            DeleteProduct.innerHTML = "<button type='button' id='deleteProductButton' onclick='deleteProduct(" + ridx + ")'>Delete</button>";
            
        }

    }
    if(type != ''){
        document.getElementById('optionTitle').style.visibility = '';
        document.getElementById('addProduct').style.visibility = '';
        document.getElementById('addManager').style.visibility = '';
        document.getElementById('addOwner').style.visibility = '';
        document.getElementById('getHistory').style.visibility = '';
        document.getElementById('editProduct').style.visibility = '';
        document.getElementById('editManager').style.visibility = '';
        document.getElementById('deleteManager').style.visibility = '';
        document.getElementById('changeBuyingPolicy').style.visibility = '';
        document.getElementById('storeHistory').style.visibility = '';
        document.getElementById('deleteProductButton').style.visibility = '';


    }

    else{
        document.getElementById('optionTitle').style.visibility = 'hidden';
        document.getElementById('addProduct').style.visibility = 'hidden';
        document.getElementById('addManager').style.visibility = 'hidden';
        document.getElementById('addOwner').style.visibility = 'hidden';
        document.getElementById('getHistory').style.visibility = 'hidden';
        document.getElementById('editProduct').style.visibility = 'hidden';
        document.getElementById('editManager').style.visibility = 'hidden';
        document.getElementById('deleteManager').style.visibility = 'hidden';
        document.getElementById('changeBuyingPolicy').style.visibility = 'hidden';
        document.getElementById('storeHistory').style.visibility = 'hidden';
        document.getElementById('deleteProductButton').style.visibility = 'hidden';

    }
    
}


async function deleteProduct(idx){

    var type = '';
    var addProductToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var editProductURL;

    storeManagersURL = 'https://localhost:8443/getAllManagers?'
    

    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(sessionStorage['username'] === manager['username']){
            type = manager['type']
        }
    }

    if(type != ''){

        if(type === 'Owner'){
            editProductURL = 'https:/localhost:8443/OwnerDeleteProductFromStore?'
        }
        else{
            editProductURL = 'https:/localhost:8443/ManagerDeleteProductFromStore?'
        }
        
        var products = document.getElementById('storeProducts')

        var productId = products.rows[idx].cells[0].innerHTML

        editProductURL += 'sessionId=' + sessionStorage['sessionId']
        editProductURL += "&storeId=" + storeId;
        editProductURL += "&productId=" + idx;

        var result;
        await fetch(editProductURL, headers).then(response => response.json()).then(response => result = response)
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            console.log('success')
        }
        else{
            console.log('fail')
        }
        location.reload()
    }
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }

}

async function showEditProduct(){

    
    document.getElementById('editProductToStoreForm').style.display='block'
}


async function editProduct(idx){


    var type = '';
    var addProductToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var editProductURL;

    storeManagersURL = 'https://localhost:8443/getAllManagers?'
    
    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(sessionStorage['username'] === manager['username']){
            type = manager['type']
        }
    }

    if(type != ''){

        if(type === 'Owner'){
            editProductURL = 'https:/localhost:8443/editProductToStore?'
        }
        else{
            editProductURL = 'https:/localhost:8443/ManagerEditProductToStoreManager?'
        }
        
        var products = document.getElementById('storeProducts')

        var productId = document.getElementById('productIdText').value

        var productInfo = document.getElementById('productInfoText').value;

        editProductURL += 'sessionId=' + sessionStorage['sessionId']
        editProductURL += "&storeId=" + storeId;
        editProductURL += "&productId=" + productId;
        editProductURL += "&info=" + productInfo;
        console.log(editProductURL)
        var result;
        await fetch(editProductURL, headers).then(response => response.json()).then(response => result = response)
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            console.log('success')
            alert("successfully edited product")
            location.reload()
        }
        else{
            console.log('fail')
        }
    }
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }
}

async function showAddProduct(){

    
    document.getElementById('addProductToStoreForm').style.display='block'
}

async function addProduct(){

    var managers;
    var type = '';
    var addProductToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');


    storeManagersURL = 'https://localhost:8443/getAllManagers?'

    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(sessionStorage['username'] === manager['username']){
            type = manager['type']
        }
    }

    if(type != ''){
        var productId = document.getElementById('idText').value
        var amount = document.getElementById('amountText').value
        var urlParams = new URLSearchParams(window.location.search);
        var storeId = urlParams.get('storeId');

        if(type === 'Owner'){
            addProductToStoreURL = 'https://localhost:8443/addProductToStore?'
        }
        else{
            addProductToStoreURL = 'https://localhost:8443/ManagerAddProductToStore?'
        }

        addProductToStoreURL += 'sessionId=' + sessionStorage['sessionId']
        addProductToStoreURL += "&storeId=" + storeId;
        addProductToStoreURL += "&productId=" + productId;
        addProductToStoreURL += "&amount=" + amount;

        var result;
        await fetch(addProductToStoreURL, headers).then(response => response.json()).then(response => result = response)
        
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert('successfully added proudct ${productId}')
            location.reload()
        }
        console.log(addProductToStoreURL)
    }
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }
}



async function showAddManager(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var possibleManagers;
    possibleManagersURL = 'https://localhost:8443/getOptionalManagers?'
    
    possibleManagersURL += 'sessionId=' + sessionStorage['sessionId']
    possibleManagersURL += '&storeId=' + storeId

    await fetch(possibleManagersURL, headers).then(response => response.json()).then(response => possibleManagers = response)
    console.log(possibleManagers)
    for(managerIdx in possibleManagers['subscribers']){
        manager = possibleManagers['subscribers'][managerIdx]
        console.log(manager)
        var x = document.getElementById("managerSelect");
        var option = document.createElement("option");
        option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
        x.add(option); 
    }

    document.getElementById('addManagerToStoreForm').style.display='block'
}

async function showAddOwner(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var possibleManagers;
    possibleManagersURL = 'https://localhost:8443/getOptionalManagers?'
    
    possibleManagersURL += 'sessionId=' + sessionStorage['sessionId']
    possibleManagersURL += '&storeId=' + storeId
    await fetch(possibleManagersURL, headers).then(response => response.json()).then(response => possibleManagers = response)
    console.log(possibleManagers)
    for(managerIdx in possibleManagers['subscribers']){
        manager = possibleManagers['subscribers'][managerIdx]
        console.log(manager)
        var x = document.getElementById("ownerSelect");
        var option = document.createElement("option");
        option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
        x.add(option); 
    }

    document.getElementById('addOwnerToStoreForm').style.display='block'

}

async function showRemoveManager(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var possibleManagers;
    possibleManagersURL = 'https://localhost:8443/getAllManagers?'
    
    possibleManagersURL += 'sessionId=' + sessionStorage['sessionId']
    possibleManagersURL += '&storeId=' + storeId
    await fetch(possibleManagersURL, headers).then(response => response.json()).then(response => possibleManagers = response)
    console.log(possibleManagers)
    for(managerIdx in possibleManagers['subscribers']){
        manager = possibleManagers['subscribers'][managerIdx]
        console.log(manager)
        if(manager['type'] === 'Manager'){
            var x = document.getElementById("employeeSelect");
            var option = document.createElement("option");
            option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
            x.add(option);
        } 
    }

    document.getElementById('removeManagerForm').style.display='block'
}

async function showEditManager(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var possibleManagers;
    possibleManagersURL = 'https://localhost:8443/getAllManagers?'
    
    possibleManagersURL += 'sessionId=' + sessionStorage['sessionId']
    possibleManagersURL += '&storeId=' + storeId
    await fetch(possibleManagersURL, headers).then(response => response.json()).then(response => possibleManagers = response)
    console.log(possibleManagers)
    for(managerIdx in possibleManagers['subscribers']){
        manager = possibleManagers['subscribers'][managerIdx]
        console.log(manager)
        if(manager['type'] === 'Manager'){
            var x = document.getElementById("editManagerSelect");
            var option = document.createElement("option");
            option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
            x.add(option);
        } 
    }

    document.getElementById('editManagerForm').style.display='block'
}



async function addManagerToStore(){
    var managers;
    var type = '';
    var addManagerToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');


    storeManagersURL = 'https://localhost:8443/getAllManagers?'
    
    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(sessionStorage['username'] === manager['username']){
            type = manager['type']
        }
    }


    if(type != ''){

        addManagerToStoreURL = 'https://localhost:8443/addStoreManager?'


        addManagerToStoreURL += 'sessionId=' + sessionStorage['sessionId']
        addManagerToStoreURL += "&storeId=" + storeId;
        var user = document.getElementById('managerSelect').value.split(' ')
        console.log(user)
        addManagerToStoreURL += "&userId=" + user[1];

        var result;
        await fetch(addManagerToStoreURL, headers).then(response => response.json()).then(response => result = response)
        
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert('successfully added ${user[3]} to the Manager ranks')
            location.reload()
        }
    }
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }
}


async function addOwnerToStore(){
    var managers;
    var type = '';
    var addManagerToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');


    storeManagersURL = 'https://localhost:8443/getAllManagers?'
    
    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(sessionStorage['username'] === manager['username']){
            type = manager['type']
        }
    }


    if(type != ''){

        addOwnerToStoreURL = 'https://localhost:8443/addStoreOwner?'


        addOwnerToStoreURL += 'sessionId=' + sessionStorage['sessionId']
        addOwnerToStoreURL += "&storeId=" + parseInt(storeId);
        var user = document.getElementById('ownerSelect').value.split(' ')
        console.log(user)
        addOwnerToStoreURL += "&subId=" +parseInt(user[1]);

        console.log(addOwnerToStoreURL)
        var result;
        await fetch(addOwnerToStoreURL, headers).then(response => response.json()).then(response => result = response)
        
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert('successfully added ${user[3]} to the Owners ranks')
            location.reload()
        }
    }
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }
}


async function removeManagerFromStore(){
    var managers;
    var type = '';
    var addManagerToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');


    storeManagersURL = 'https://localhost:8443/getAllManagers?'
    
    
    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(sessionStorage['username'] === manager['username'] && manager['type'] === 'Owner'){
            type = manager['type']
        }
    }


    if(type != ''){

        removeManagerURL = 'https://localhost:8443/deleteManager?'


        removeManagerURL += 'sessionId=' + sessionStorage['sessionId']
        removeManagerURL += "&storeId=" + storeId;
        var user = document.getElementById('employeeSelect').value.split(' ')
        console.log(user)
        removeManagerURL += "&userId=" + user[1];

        var result;
        await fetch(removeManagerURL, headers).then(response => response.json()).then(response => result = response)
        
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert('successfully fired ${user[3]} that piece of shit')
            location.reload()
        }
    }
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }
}


async function editManager(){
    var managers;
    var type = '';
    var addManagerToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');


    storeManagersURL = 'https://localhost:8443/getAllManagers?'
    
    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        if(sessionStorage['username'] === manager['username'] && manager['type'] === 'Owner'){
            type = manager['type']
        }
    }


    if(type != ''){

        editManagerURL = 'https://localhost:8443/editManageOptions?'


        editManagerURL += 'sessionId=' + sessionStorage['sessionId']
        editManagerURL += "&storeId=" + storeId;

        var user = document.getElementById('editManagerSelect').value.split(' ')
        editManagerURL += "&userId=" + user[1];
        console.log(user[1])
        var option = document.getElementById('editOptionsSelect').value
        editManagerURL += "&options=" + option;

        var result;
        await fetch(editManagerURL, headers).then(response => response.json()).then(response => result = response)
        
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert('successfully edited ${user[3]} options')
            location.reload()
        }
    }
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }
}

async function viewStoreHistory(){

    var managers;
    var type = '';
    var addManagerToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');


    storeManagersURL = 'https://localhost:8443/getAllManagers?'
    
    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        if(sessionStorage['username'] === manager['username'] && manager['type'] === 'Owner'){
            type = manager['type']
        }
    }


    if(type != ''){

        var historyURL = 'https://localhost:8443/viewPurchaseHistory?'


        historyURL += 'sessionId=' + sessionStorage['sessionId']
        historyURL += "&storeId=" + storeId;

        var result;
        await fetch(historyURL, headers).then(response => response.json()).then(response => result = response)
        
        if(result['resultCode'] === 'SUCCESS'){
            histroyTable = document.getElementById('storePruchaseHistory')
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
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }


}


function connect() {
    var socket = new SockJS('https://localhost:8443/notifications');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/storeUpdate/' + sessionStorage['subId'], function (message) {
            alert(message.body)
        });
    });
}