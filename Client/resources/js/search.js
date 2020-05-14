headers = {
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
}


async function searchProducts() {
    searchURL = 'https://localhost:8443/searchProducts'

    var productName = document.getElementById('nameText').value
    var productCategory = document.getElementById('categoryText').value
    var productKeywords = document.getElementById('keywordText').value
    var productItemRating = document.getElementById('itemText').value
    var productStoreRating = document.getElementById('storeText').value
    console.log(productName)
    console.log(productCategory)
    console.log(productKeywords)
    console.log(productItemRating)
    console.log(productStoreRating)

    searchURL += '?sessionId=' + localStorage['sessionId']
    if (productName != undefined) {
        searchURL += "&productName=" + productName
    }
    if (productCategory != '') {
        searchURL += "&categoryName=" + productCategory
    }
    if (productKeywords != '') {
        searchURL += "&keywords=" + productKeywords.split(" ").join(',')
    }
    if (productItemRating != '') {
        searchURL += "&minItemRating=" + productItemRating
    }
    if (productStoreRating != '') {
        searchURL += "&minStoreRating=" + productStoreRating
    }
    var result;
    await fetch(searchURL, headers).then(response => response.json()).then(response => result = response)

    console.log(result)

    createProductTable(result);
}


function createProductTable(productList){
    products = productList['products']

    var productTable = document.getElementById('productTable')

    for(var i = 1; i< productTable.rows.length; i++){
        productTable.deleteRow(i);
    }

    for (productIdx in products) {
        product = products[productIdx]    

        var row = productTable.insertRow(parseInt(productIdx) + 1);
        var storeId = row.insertCell(0);
        var productId = row.insertCell(1);
        var productName = row.insertCell(2);
        var productCategory = row.insertCell(3);
        var productInfo = row.insertCell(4);
        var productAmount = row.insertCell(5);
        var Amount = row.insertCell(6);
        var button = row.insertCell(7);

        storeId.innerHTML = "<a href='store.html'>" + product['storeId'] + "</a>"
        productId.innerHTML = product['productId'] 
        productName.innerHTML = product['name'];
        productCategory.innerHTML = product['category'];
        productInfo.innerHTML = product['info'];
        productAmount.innerHTML = product['amount'];
        Amount.innerHTML = "<input id='cartAmount' type='number' min='1'>"
        button.innerHTML = "<button type='button' id='addToCartButton' onclick='addToCart(" + productIdx + ")'>Add to Cart</button>";
    }
    
}



async function addToCart(idx) {


    var products = document.getElementById('productTable')

    var sessionId = localStorage['sessionId']
    var storeId = products.rows[parseInt(idx) + 1].cells[0].children[0].innerHTML
    var productId = products.rows[parseInt(idx) + 1].cells[1].innerHTML
    var amount = products.rows[parseInt(idx) + 1].cells[6].children[0].value
    console.log(storeId)
    console.log(productId)
    console.log(amount)
    addCartURL = 'https://localhost:8443/addProductToCart?'

    addCartURL += "sessionId=" + sessionId;
    addCartURL += "&storeId=" + storeId;
    addCartURL += "&productId=" + productId;
    addCartURL += "&amount=" + amount;

    var result;
    await fetch(addCartURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
        alert('successfully added product to cart')
    }
    else{
        console.log('fail')
    }

}