
async function loadpage(){
    hideButtons();
    viewCart()
    getHisotry();
    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }
}

async function viewCart(){
    
    cartURL = 'https://localhost:8443/viewCart'


    cartURL += '?sessionId=' + sessionStorage['sessionId']
    
    var result;
    await fetch(cartURL, headers).then(response => response.json()).then(response => result = response)

    console.log(result)

    createCartTable(result);
}

async function createCartTable(productList){
    products = productList['baskets']

    var productTable = document.getElementById('cartTable')
    var totalPrice = 0;
    for(var i = 1; i< productTable.rows.length; i++){
        productTable.deleteRow(i);
    }
    
    var ridx = 1;

    for (basketIdx in products) {
        basket = products[basketIdx]    
        
        for(productIdx in basket['productsAndAmounts']){
            
            var product = basket['productsAndAmounts'][productIdx]
            console.log(product)
            var row = productTable.insertRow(ridx);
            var storeId = row.insertCell(0);
            var productId = row.insertCell(1);
            var productName = row.insertCell(2);
            var productPrice = row.insertCell(3);
            var productAmount = row.insertCell(4);
            var updateButton = row.insertCell(5);
            var deleteButton = row.insertCell(6);

            storeId.innerHTML = "<a href='store.html'>" + basket['storeId'] + "</a>"
            productId.innerHTML = product['pid'] 
            productName.innerHTML = product['name'];
            productPrice.innerHTML = product['price'];
            productAmount.innerHTML = "<input id='cartAmount' type='number' min='1' value='" + product['amount'] + "'>"
            updateButton.innerHTML = "<button type='button' id='editProductButton' onclick='editProduct(" + ridx + ")'>Update Amount</button>";
            deleteButton.innerHTML = "<button type='button' id='deleteProductButton' onclick='deleteProduct(" + ridx + ")'>Delete</button>";
            ridx += 1;
            totalPrice += product['amount'] * product['price']

        }
        
    }
    var row = productTable.insertRow(ridx);
    var storeId = row.insertCell(0);
    var productId = row.insertCell(1);
    var productName = row.insertCell(2);
    var productPrice = row.insertCell(3);
    var productAmount = row.insertCell(4);
    var updateButton = row.insertCell(5);
    var deleteButton = row.insertCell(6);
    var sum = row.insertCell(7);

    sum.innerHTML = totalPrice;
    console.log('done cart')
}



async function deleteProduct(idx){

    deleteProductURL = 'https:/localhost:8443/removeProductInCart?'
    
    var products = document.getElementById('cartTable')

    var sessionId = sessionStorage['sessionId']
    var storeId = products.rows[parseInt(idx)].cells[0].children[0].innerHTML
    var productId = products.rows[parseInt(idx)].cells[1].innerHTML
    console.log(storeId)
    console.log(productId)

    deleteProductURL += "sessionId=" + sessionId;
    deleteProductURL += "&storeId=" + storeId;
    deleteProductURL += "&productId=" + productId;

    var result;
    await fetch(deleteProductURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
    }
    else{
        console.log('fail')
    }

    products.deleteRow(idx)
    location.reload()

}


async function editProduct(idx){
    console.log('in edit')
    editProductURL = 'https:/localhost:8443/editProductInCart?'
    
    var products = document.getElementById('cartTable')

    var sessionId = sessionStorage['sessionId']
    var storeId = products.rows[parseInt(idx)].cells[0].children[0].innerHTML
    var productId = products.rows[parseInt(idx)].cells[1].innerHTML
    var amount = products.rows[parseInt(idx)].cells[4].children[0].value
    console.log(storeId)
    console.log(productId)
    console.log(amount)

    editProductURL += "sessionId=" + sessionId;
    editProductURL += "&storeId=" + parseInt(storeId);
    editProductURL += "&productId=" + parseInt(productId);
    editProductURL += "&amount=" + parseInt(amount);

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


async function clearCart(){

    clearCartURL = 'https://localhost:8443/clearCart?'
    var sessionId = sessionStorage['sessionId']

    clearCartURL += "sessionId=" + sessionId;

    var result;
    await fetch(clearCartURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
    }
    else{
        console.log('fail')
    }
    var products = document.getElementById('cartTable')
    for(var i = 1; i< products.rows.length; i++){
        products.deleteRow(i);
    }
    
    location.reload()
}


async function purchaseCart(){


    requestPurchaseURL = 'https://localhost:8443/requestPurchase?'

    var sessionId = sessionStorage['sessionId']

    requestPurchaseURL += 'sessionId=' + sessionId

    var result;
    await fetch(requestPurchaseURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        if(confirm(result['details'])){
            confirmPurchaseURL = 'https://localhost:8443/confirmPurchase?'
            confirmPurchaseURL += 'sessionId=' + sessionId
            
            var cardNumber = document.getElementById('cardNumberText').value
            var cardMonth = document.getElementById('cardMonthText').innerHTML
            var cardYear = document.getElementById('cardYearText').innerHTML
            var cardHolder = document.getElementById('cardHolderText').value
            var cardCcv = document.getElementById('cardCcvText').value
            var id = document.getElementById('idText').value
        
            var buyerNamer = document.getElementById('buyerNameText').value
            var address = document.getElementById('addressText').value
            var city = document.getElementById('cityText').value
            var country = document.getElementById('countryText').value
            var zip = document.getElementById('zipText').value
        
            confirmPurchaseURL += "&cardNumber=" + cardNumber
            confirmPurchaseURL += "&cardMonth=" + cardMonth
            confirmPurchaseURL += "&cardYear=" + cardYear
            confirmPurchaseURL += "&cardHolder=" + cardHolder
            confirmPurchaseURL += "&cardCcv=" + cardCcv
            confirmPurchaseURL += "&cardId=" + id
            confirmPurchaseURL += "&buyerName=" + buyerNamer
            confirmPurchaseURL += "&address=" + address
            confirmPurchaseURL += "&city=" + city
            confirmPurchaseURL += "&country=" + country
            confirmPurchaseURL += "&zip=" + zip

            console.log(confirmPurchaseURL)
            await fetch(confirmPurchaseURL, headers).then(response => response.json()).then(response => result = response)
            console.log(result)
            if(result['resultCode'] === 'SUCCESS'){
                alert('purchase successfull')
                location.reload()

            }
            else{
                alert(result['details'])
            }
        }
        else{
            alert('purchase canceled')
        }

    }
    else{
        alert(result['details'])
    }


}


async function getHisotry(){

    if(sessionStorage['loggedin'] === 'true'){
        historyURL = "https://localhost:8443/getHistory?"
        
        historyURL += 'sessionId=' + sessionStorage['sessionId']
        var result;
        await fetch(historyURL, headers).then(response => response.json()).then(response => result = response);
        console.log(result)
        
        if(result['resultCode'] === 'SUCCESS'){

            histroyTable = document.getElementById('historyTable')
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
                        var productPrice = row.insertCell(6);

                        storeId.innerHTML = storeIdx
                        purchaseId.innerHTML = basket['id']

                        var product = basket['mapProductsAmount'][productIdx]
                        productId.innerHTML = product['productInfo']['id']
                        productName.innerHTML = product['productInfo']['name']
                        productCategory.innerHTML = product['productInfo']['category']
                        productAmount.innerHTML = product['amount']
                        productPrice.innerHTML = product['price']
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
                basketPrice.innerHTML = 'total: ' + basket['price']
                ridx++;
                }

            }
            

        }

    }

}

function openPurchaseCart(){
    document.getElementById('purchaseForm').style.display='block'

}