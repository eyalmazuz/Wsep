


async function viewStore(){

    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var type = await isManager(storeId, sessionStorage['subId'])


    if(type === 'Manager' || type === 'Owner'){
        document.getElementById('productPageButton').style.visibility = '';
        document.getElementById('managersPageButton').style.visibility = '';
        document.getElementById('ownersPageButton').style.visibility = '';
        document.getElementById('buyingPolicyPageButton').style.visibility = '';
        document.getElementById('discountPolicyPageButton').style.visibility = '';
        document.getElementById('storeHistoryButton').style.visibility = '';
        document.getElementById('storeHistory').style.visibility = '';
        document.getElementById('optionTitle').style.visibility = '';
        document.getElementById('historyTitle').style.visibility = '';
        sessionStorage['storeType'] = type

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
        sessionStorage['storeType'] = 'Normie'




    }

    console.log(typeof(storeId))
    var storeURL = "https://localhost:8443/getStore?"
    
    storeURL += 'sessionId=' + sessionStorage['sessionId']

    storeURL += "&storeId=" + parseInt(storeId)

    var policiesURL = "https://localhost:8443/viewBuyingPolicies?"
    
    policiesURL += 'sessionId=' + sessionStorage['sessionId']

    policiesURL += "&storeId=" + parseInt(storeId)

    var result
    await fetch(policiesURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    
    if(result['resultCode'] === 'SUCCESS'){

        
        var policies = result['dtos']
        console.log(policies)
        var policyTable = document.getElementById('buyingPolicies')

        for(var i = 1; i< policyTable.rows.length; i++){
            policyTable.deleteRow(i);
        }

        var ridx = 1;
        for(productIdx in policies){
            var policy = policies[productIdx]
            console.log(policy)
            var row = policyTable.insertRow(ridx)
            var policyId = row.insertCell(0)
            var policyDescription = row.insertCell(1)

            policyId.innerHTML = policy['id']
            policyDescription.innerHTML = policy['toString']
            
        }

    }


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
            var productPrice = row.insertCell(5)

            productId.innerHTML = product['productId']
            productName.innerHTML = product['name']
            productCategory.innerHTML = product['category']
            productInfo.innerHTML = product['info']
            productAmount.innerHTML = product['amount']
            productPrice.innerHTML = product['price']
            
        }

    }
    
}


async function movePage(event){

    var name = event.target.name
    console.log(name)
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var res = await checkPermission(storeId, name)
    if(res){
        location.href = name + '.html?storeId=' + storeId + '&type=' + sessionStorage['storeType']

    }
    else{
        alert('NO permissions')
    }
}

async function isManager(storeId, subId){
    permissionURL = 'https://localhost:8443/getPermission?'
    var permissions;

    console.log('permission')
    permissionURL += 'storeId=' + parseInt(storeId)
    permissionURL += '&subId=' + parseInt(subId)
    await fetch(permissionURL, headers).then(response => response.json()).then(response => permissions = response) 
    console.log(permissions)
    return permissions['permission']['type']
}


async function checkPermission(storeId, page){
        
    permissionURL = 'https://localhost:8443/getPermission?'
    var permissions;

    console.log('permission')
    permissionURL += 'storeId=' + parseInt(storeId)
    permissionURL += '&subId=' + parseInt(sessionStorage['subId'])
    await fetch(permissionURL, headers).then(response => response.json()).then(response => permissions = response)
    var isOwner = permissions['permission']['details'] === 'Simple' && permissions['permission']['type'] === 'Owner'
    var isManager;
    if(page === 'products'){
      isManager = ['any', 'add product', 'edit product', 'delete product'].includes(permissions['permission']['details']) && permissions['permission']['type'] === 'Manager' 
    }
    if(page === 'buyingPolicy' || page === 'buyingPolicy'){
        isManager = permissions['permission']['details'] === 'any' && permissions['permission']['type'] === 'Manager' 
    }
     return isOwner || isManager  
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