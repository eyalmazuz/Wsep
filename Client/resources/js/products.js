headers = {
    headers: {          
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
}


async function viewStoreProducts(){

    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }

    var type = '';

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');



    console.log(typeof(storeId))
    var storeURL = "https://localhost:8443/getStore?"
    
    storeURL += 'sessionId=' + sessionStorage['sessionId']

    storeURL += "&storeId=" + parseInt(storeId)

    var result
    await fetch(storeURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    
    if(result['resultCode'] === 'SUCCESS'){

        
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
    
}

function returnToStore(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    location.href = 'store.html?storeId=' + storeId
}


function showDeleteProduct(){

    
    document.getElementById('deleteProductToStoreForm').style.display='block'
}



async function deleteProduct(idx){

    var type = '';

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var deleteProductURL;

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
    if(type === 'Owner'){
        deleteProductURL = 'https:/localhost:8443/OwnerDeleteProductFromStore?'
    }
    else{
        deleteProductURL = 'https:/localhost:8443/ManagerDeleteProductFromStore?'
    }
    
    var productId = document.getElementById('deleteproductIdText').value

    deleteProductURL += 'sessionId=' + sessionStorage['sessionId']
    deleteProductURL += "&storeId=" + storeId;
    deleteProductURL += "&productId=" + productId;

    console.log(deleteProductURL)
    var result;
    await fetch(deleteProductURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        alert("deleted product: " + productId)
        location.reload()

    }
    else{
        alert(result['details'])
    }

}

function showEditProduct(){

    
    document.getElementById('editProductToStoreForm').style.display='block'
}


async function editProduct(idx){


    var type = '';
    var managers;
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

function showAddProduct(){

    
    document.getElementById('addProductToStoreForm').style.display='block'
}

async function addProduct(){


    var type = '';
    var managers;
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

    var addProductToStoreURL;

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var productId = document.getElementById('idText').value
    var amount = document.getElementById('amountText').value
    var price = document.getElementById('priceText').value
    var category = document.getElementById('categoryText').value
    var name = document.getElementById('nameText').value
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
    addProductToStoreURL += "&category=" + category;
    addProductToStoreURL += "&price=" + price;
    addProductToStoreURL += "&name=" + name;


    console.log(addProductToStoreURL)
    var result;
    await fetch(addProductToStoreURL, headers).then(response => response.json()).then(response => result = response)
    
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        alert('successfully added proudct ${productId}')
        location.reload()
    }
    console.log(addProductToStoreURL)

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