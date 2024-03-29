


async function viewStores(){
    hideButtons();
    storesURL = 'https://localhost:8443/viewStoreProductInfo'

    var stores = await fetch(storesURL, headers)
    .then(response => response.json())
    
    if (stores['resultCode'] === 'SUCCESS'){
        buildStoresTable(stores)
    }
    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }
}

function buildStoresTable(stores){

    storeList = stores['stores']
    console.log(storeList)
    var storesTable = document.getElementById('storesTable')
    
    for(var i = 1; i< storesTable.rows.length; i++){
        storesTable.deleteRow(i);
    }
    var ridx = 1;

    for (storeidx in storeList) {
        store = storeList[storeidx]    


        var row = storesTable.insertRow(ridx);
        var storeId = row.insertCell(0);
        var buyingPolicy = row.insertCell(1);
        var discountPolicy = row.insertCell(2);
        row.insertCell(3);
        row.insertCell(4);
        row.insertCell(5);
        row.insertCell(6);
        row.insertCell(7);
        storeId.innerHTML = "<a href='store.html?storeId=" + store['storeId'] + "'>" + store['storeId'] + "</a>"
        buyingPolicy.innerHTML = store['buyingPolicy']
        discountPolicy.innerHTML = store['discountPolicy'];

        ridx += 1;

        for (productidx in store['products']){

            product = store['products'][productidx]
            console.log(product)

            var row = storesTable.insertRow(ridx);
            row.insertCell(0);
            row.insertCell(1);
            row.insertCell(2);
            var productId = row.insertCell(3);
            var productName = row.insertCell(4);
            var productCategory = row.insertCell(5);
            var productAmount = row.insertCell(6);
           
            productId.innerHTML = product['productId'] + "</td>";
            productName.innerHTML = product['name'];
            productCategory.innerHTML = product['category'];
            productAmount.innerHTML =  product['amount']
            console.log(ridx)
            ridx += 1;

        }

    }
}