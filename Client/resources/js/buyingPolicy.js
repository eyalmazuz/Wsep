

//TODO
async function viewStoreBuyingPolicies(){

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


function showDeleteBuyingPolicy(){

    
    document.getElementById('deleteBuyingPolicyToStoreForm').style.display='block'
}



async function deleteBuyingPolicy(idx){

    var type = '';

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var deletePolicyURL;

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
        deletePolicyURL = 'https:/localhost:8443/OwnerDeletePolicyFromStore?'
    }
    else{
        deletePolicyURL = 'https:/localhost:8443/ManagerDeletePolicyFromStore?'
    }
    
    var policyId = document.getElementById('deletepolicyIdText').value

    deletePolicyURL += 'sessionId=' + sessionStorage['sessionId']
    deletePolicyURL += "&storeId=" + storeId;
    deletePolicyURL += "&policyId=" + policyId;

    console.log(deletePolicyURL)


}

function showEditBuyingPolicyProduct(){

    
    document.getElementById('editBuyingPolicyToStoreForm').style.display='block'
}


async function editBuyingPolicy(idx){


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

function showAddBuyingPolicy(){

    document.getElementById('addBuyingPolicyToStoreForm').style.display='block'
}

async function addBuyingPolicy(){



}


  async function CreateAmountPolicy(){
    
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
    var addAmountPolicyURL;

    if(type === 'Owner'){
        addAmountPolicyURL = 'https://localhost:8443/OwnerAddAmountPolicy?'
    }
    else{
        addAmountPolicyURL = 'https://localhost:8443/ManagerAddAmountPolicy?'
    }


    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');


    var radios = document.getElementsByName('type');
    var type = ''
    for (var i = 0, length = radios.length; i < length; i++) {
      if (radios[i].checked) {
        // do whatever you want with the checked radio
        type = radios[i].value;
    
        // only one radio can be logically checked, don't check the rest
        break;
      }
    }

    var productId = document.getElementById('idText').value
    var amount = document.getElementById('amountText').value

    addAmountPolicyURL += 'storeId=' + storeId
    addAmountPolicyURL += '&prodcutId' + productId
    addAmountPolicyURL += '&minmax=' + type
    addAmountPolicyURL += '&amount=' + amount

    console.log(addAmountPolicyURL)
}


async function CreateCountryPolicy(){
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

    var addCountryPolicyURL;


    if(type === 'Owner'){
        addCountryPolicyURL = 'https://localhost:8443/OwnerAddCountryPolicy?'
    }
    else{
        addCountryPolicyURL = 'https://localhost:8443/ManagerAddCountryPolicy?'
    }

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');



    var country = document.getElementById('countryText').value

    addCountryPolicyURL += 'storeId=' + storeId
    addCountryPolicyURL += '&country' + country

    console.log(addCountryPolicyURL)
}

async function CreateDayPolicy(){
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
    var addDaysPolicyURL;
    if(type === 'Owner'){
        addDaysPolicyURL = 'https://localhost:8443/OwnerAddDayPolicy?'

    }
    else{
        addDaysPolicyURL = 'https://localhost:8443/ManagerAddDayPolicy?'
    }

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    
    var radios = document.getElementsByName('days');
    var day = ''
    for (var i = 0, length = radios.length; i < length; i++) {
      if (radios[i].checked) {
        // do whatever you want with the checked radio
        day = radios[i].value;
    
        // only one radio can be logically checked, don't check the rest
        break;
      }
    }

    addDaysPolicyURL += 'storeId=' + storeId
    addDaysPolicyURL += '&day=' + day

    console.log(addDaysPolicyURL)
}


async function CreateAdvancePolicy(){
    
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
    var addAdvancePolicyURL;

    if(type === 'Owner'){
        addAdvancePolicyURL = 'https://localhost:8443/OwnerAddAdvancePolicy?'

    }
    else{
        addAdvancePolicyURL = 'https://localhost:8443/ManagerAddAdvancePolicy?'
    }

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    
    var radios = document.getElementsByName('operator');
    var operator = ''
    for (var i = 0, length = radios.length; i < length; i++) {
      if (radios[i].checked) {
        // do whatever you want with the checked radio
        operator = radios[i].value;
    
        // only one radio can be logically checked, don't check the rest
        break;
      }
    }

    var ids = document.getElementById('idsText').value

    addAdvancePolicyURL += 'storeId=' + storeId
    addAdvancePolicyURL += '&operator=' + operator
    addAdvancePolicyURL += '&ids=' + ids.split(' ').join(',')

    console.log(addAdvancePolicyURL)
}
 