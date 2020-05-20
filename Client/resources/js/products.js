
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
    console.log('asdasdasd')
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


async function showDeleteProduct(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var res = await checkPermission(storeId, 'delete product') 
    if(res){
        document.getElementById('deleteProductToStoreForm').style.display='block'
    }
    else{
        alert("NO PERMISSION")
    }
}



async function deleteProduct(idx){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');

    var deleteProductURL;


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

async function showEditProduct(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var res = await checkPermission(storeId, 'edit product') 
    if(res){
        document.getElementById('editProductToStoreForm').style.display='block'
    }
    else{
        alert("NO PERMISSION")
    }
    
}


async function editProduct(idx){


    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');

    var editProductURL;

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

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var res = await checkPermission(storeId, 'add product') 
    if(res){
        document.getElementById('addProductToStoreForm').style.display='block'
    }
    else{
        alert("NO PERMISSION")
    }
}

async function addProduct(){


    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');


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

async function checkPermission(storeId, perm){
        
    permissionURL = 'https://localhost:8443/getPermission?'
    var permissions;

    console.log('permission')
    permissionURL += 'storeId=' + parseInt(storeId)
    permissionURL += '&subId=' + parseInt(sessionStorage['subId'])
    await fetch(permissionURL, headers).then(response => response.json()).then(response => permissions = response)
    var isOwner = permissions['permission']['details'] === 'Simple' && permissions['permission']['type'] === 'Owner'
    var isManager = (permissions['permission']['details'] === perm || permissions['permission']['details'] === 'any') && permissions['permission']['type'] === 'Manager' 

     return isOwner || isManager  
}
