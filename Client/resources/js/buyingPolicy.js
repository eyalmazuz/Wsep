

//TODO
async function viewStoreBuyingPolicies(){

    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }

    var type = '';

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');



    console.log(typeof(storeId))
    var policiesURL = "https://localhost:8443/viewBuyingPolicies?"
    
    policiesURL += 'sessionId=' + sessionStorage['sessionId']

    policiesURL += "&storeId=" + parseInt(storeId)

    var result
    await fetch(policiesURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    
    if(result['resultCode'] === 'SUCCESS'){

        
        var policies = result['dtos']
        console.log(policies)
        var policyTable = document.getElementById('storePolicies')

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


    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');
    var deletePolicyURL;

    if(type === 'Owner'){
        deletePolicyURL = 'https:/localhost:8443/OwnerRemoveBuyingType?'
    }
    else{
        deletePolicyURL = 'https:/localhost:8443/ManagerDeletePolicyFromStore?'
    }
    
    var policyId = document.getElementById('deletepolicyIdText').value

    deletePolicyURL += 'sessionId=' + sessionStorage['sessionId']
    deletePolicyURL += "&storeId=" + storeId;
    deletePolicyURL += "&buyingTypeID=" + policyId;

    var result;
    await fetch(deletePolicyURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
        alert("successfully added advance constraint policy with id: " + result['id'])
    }
    else{
        alert(result['details'])
    }  

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
    
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');

    var addAmountPolicyURL;

    if(type === 'Owner'){
        addAmountPolicyURL = 'https://localhost:8443/OwnerAddSimpleBuyingTypeBasketConstraint?'
    }
    else{
        addAmountPolicyURL = 'https://localhost:8443/ManagerAddSimpleBuyingTypeBasketConstraint?'
    }


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

    addAmountPolicyURL += '&sessionId=' + parseInt(sessionStorage['sessionId'])
    addAmountPolicyURL += '&storeId=' + parseInt(storeId)
    addAmountPolicyURL += '&productId=' + parseInt(productId)
    addAmountPolicyURL += '&minmax=' + type
    addAmountPolicyURL += '&amount=' + parseInt(amount)

    console.log(addAmountPolicyURL)

    var result;
    await fetch(addAmountPolicyURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
        alert("successfully added amount constraint policy with id: " + result['id'])
    }
    else{
        alert(result['details'])
    }    
}


async function CreateCountryPolicy(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');


    var addCountryPolicyURL;


    if(type === 'Owner'){
        addCountryPolicyURL = 'https://localhost:8443/OwnerAddSimpleBuyingTypeUserConstraint?'
    }
    else{
        addCountryPolicyURL = 'https://localhost:8443/ManagerAddSimpleBuyingTypeUserConstraint?'
    }



    var country = document.getElementById('countryText').value

    addCountryPolicyURL += '&sessionId=' + parseInt(sessionStorage['sessionId'])
    addCountryPolicyURL += '&storeId=' + parseInt(storeId)
    addCountryPolicyURL += '&country' + country

    console.log(addCountryPolicyURL)

    var result;
    await fetch(addCountryPolicyURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
        alert("successfully added country constraint policy with id: " + result['id'])
    }
    else{
        alert(result['details'])
    }  
}

async function CreateDayPolicy(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');

    var addDaysPolicyURL;
    if(type === 'Owner'){
        addDaysPolicyURL = 'https://localhost:8443/OwnerAddSimpleBuyingTypeSystemConstraint?'

    }
    else{
        addDaysPolicyURL = 'https://localhost:8443/ManagerAddSimpleBuyingTypeSystemConstraint?'
    }

    
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

    addDaysPolicyURL += '&sessionId=' + parseInt(sessionStorage['sessionId'])
    addDaysPolicyURL += '&storeId=' + parseInt(storeId)
    addDaysPolicyURL += '&dayOfWeek=' + parseInt(day)

    console.log(addDaysPolicyURL)

    var result;
    await fetch(addDaysPolicyURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
        alert("successfully added day constraint policy with id: " + result['id'])
    }
    else{
        alert(result['details'])
    }  
}


async function CreateAdvancePolicy(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');

    var addAdvancePolicyURL;

    if(type === 'Owner'){
        addAdvancePolicyURL = 'https://localhost:8443/OwnerCreateAdvancedBuyingType?'

    }
    else{
        addAdvancePolicyURL = 'https://localhost:8443/ManagerCreateAdvancedBuyingType?'
    }


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

    addAdvancePolicyURL += 'sessionId=' + parseInt(sessionStorage['sessionId'])
    addAdvancePolicyURL += '&storeId=' + parseInt(storeId)
    addAdvancePolicyURL += '&logicalOperation=' + operator
    addAdvancePolicyURL += '&buyingTypeIDs=' + ids.split(' ').join(',')

    console.log(addAdvancePolicyURL)

    var result;
    await fetch(addAdvancePolicyURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
        alert("successfully added advance constraint policy with id: " + result['id'])
    }
    else{
        alert(result['details'])
    }  
}
 

function openTab(evt, cityName) {
    var i, x, tablinks;
    x = document.getElementsByClassName("city");
    for (i = 0; i < x.length; i++) {
      x[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablink");
    for (i = 0; i < x.length; i++) {
      tablinks[i].classList.remove("w3-light-grey");
    }
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.classList.add("w3-light-grey");
  }