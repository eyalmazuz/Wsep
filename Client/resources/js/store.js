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

    if(type != ''){
        document.getElementById('productPageButton').style.visibility = '';
        document.getElementById('managersPageButton').style.visibility = '';
        document.getElementById('ownersPageButton').style.visibility = '';
        document.getElementById('buyingPolicyPageButton').style.visibility = '';
        document.getElementById('discountPolicyPageButton').style.visibility = '';
        document.getElementById('storeHistoryButton').style.visibility = '';
        document.getElementById('storeHistory').style.visibility = '';
        document.getElementById('optionTitle').style.visibility = '';
        document.getElementById('historyTitle').style.visibility = '';

        

        

    }

    else{
        document.getElementById('productPageButton').style.visibility = 'hidden';
        document.getElementById('managersPageButton').style.visibility = 'hidden';
        document.getElementById('ownersPageButton').style.visibility = 'hidden';
        document.getElementById('buyingPolicyPageButton').style.visibility = 'hidden';
        document.getElementById('discountPolicyPageButton').style.visibility = 'hidden';
        document.getElementById('storeHistoryButton').style.visibility = 'hidden';
        document.getElementById('storeHistory').style.visibility = 'hidden';
        document.getElementById('optionTitle').style.visibility = 'hidden';
        document.getElementById('historyTitle').style.visibility = 'hidden';




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
            
        }

    }
    
}


function moveToProducts(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    location.href = 'products.html?storeId=' + storeId  
}

function moveToManagers(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    location.href = 'manager.html?storeId=' + storeId  
}

function moveToOwners(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    location.href = 'owners.html?storeId=' + storeId  
}


function moveToBuyingPolicy(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    location.href = 'buyingpolicy.html?storeId=' + storeId  
}


function moveToDiscountPolicy(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    location.href = 'discountpolicy.html?storeId=' + storeId  
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