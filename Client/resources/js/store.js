headers = {
    headers: {          
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
}


async function viewStore(){


    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    console.log(typeof(storeId))
    var storeURL = "https://localhost:8443/getStore?storeId=" + parseInt(storeId)

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
            productAmount.innerHTML = "<input id='cartAmount' type='number' min='1' value='" + product['amount'] + "'>"
            DeleteProduct.innerHTML = "<button type='button' id='deleteProductButton' onclick='deleteProduct(" + ridx + ")'>Delete</button>";
            
        }

    }
    
}


async function deleteProduct(idx){

    var type = '';
    var addProductToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var editProductURL;

    storeManagersURL = 'https://localhost:8443/getAllManagers?storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(localStorage['username'] === manager['username']){
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

        editProductURL += "storeId=" + storeId;
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
        document.reload()
    }
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }

}


async function editProduct(idx){


    var type = '';
    var addProductToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var editProductURL;

    storeManagersURL = 'https://localhost:8443/getAllManagers?storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(localStorage['username'] === manager['username']){
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

        var productId = products.rows[idx].cells[0].innerHTML

        var productInfo = document.getElementById('productInfoText');

        editProductURL += "storeId=" + storeId;
        editProductURL += "&productId=" + idx;
        editProductURL += "&info=" + productInfo;

        var result;
        await fetch(editProductURL, headers).then(response => response.json()).then(response => result = response)
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            console.log('success')
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


    storeManagersURL = 'https://localhost:8443/getAllManagers?storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(localStorage['username'] === manager['username']){
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

        addProductToStoreURL += "storeId=" + storeId;
        addProductToStoreURL += "&productId=" + productId;
        addProductToStoreURL += "&amount=" + amount;

        var result;
        await fetch(addProductToStoreURL, headers).then(response => response.json()).then(response => result = response)
        
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert('successfully added proudct ${productId}')
            document.reload()
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
    possibleManagersURL = 'https://localhost:8443/getOptionalManagers?storeId=' + storeId
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
    possibleManagersURL = 'https://localhost:8443/getOptionalManagers?storeId=' + storeId
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




async function addManagerToStore(){
    var managers;
    var type = '';
    var addManagerToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');


    storeManagersURL = 'https://localhost:8443/getAllManagers?storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(localStorage['username'] === manager['username']){
            type = manager['type']
        }
    }


    if(type != ''){

        addManagerToStoreURL = 'https://localhost:8443/addStoreManager?'


        addManagerToStoreURL += "storeId=" + storeId;
        var user = document.getElementById('managerSelect').value.split(' ')
        console.log(user)
        addManagerToStoreURL += "&userId=" + user[1];

        var result;
        await fetch(addManagerToStoreURL, headers).then(response => response.json()).then(response => result = response)
        
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert('successfully added ${user[3]} to the Manager ranks')
            document.reload()
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


    storeManagersURL = 'https://localhost:8443/getAllManagers?storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => managers = response)
    console.log(managers)
    for(managerIdx in managers['subscribers']){
        manager = managers['subscribers'][managerIdx]
        console.log(manager)
        if(localStorage['username'] === manager['username']){
            type = manager['type']
        }
    }


    if(type != ''){

        addOwnerToStoreURL = 'https://localhost:8443/addStoreOwner?'


        addOwnerToStoreURL += "storeId=" + parseInt(storeId);
        var user = document.getElementById('ownerSelect').value.split(' ')
        console.log(user)
        addOwnerToStoreURL += "&subId=" +parseInt(user[1]);

        console.log(addOwnerToStoreURL)
        var result;
        await fetch(addOwnerToStoreURL, headers).then(response => response.json()).then(response => result = response)
        
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert('successfully added ${user[3]} to the Owners ranks')
            document.reload()
        }
    }
    else{
        alert('ONLY MANAGER/OWNERS ALLOWED TO DO THIS ACTION')
    }
}