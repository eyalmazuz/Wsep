async function viewStoreDiscountPolicies(){
    hideButtons();
    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }

    var type = '';

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');



    console.log(typeof(storeId))
    var discountPoliciesURL = "https://localhost:8443/OwnerViewDiscountPolicies?"
    
    discountPoliciesURL += 'sessionId=' + sessionStorage['sessionId']

    discountPoliciesURL += "&storeId=" + parseInt(storeId)

    var result
    await fetch(discountPoliciesURL, headers).then(response => response.json()).then(response => result = response)
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


function showDeleteDiscountPolicy(){

    
    document.getElementById('deleteDiscountPolicyToStoreForm').style.display='block'
}



async function deleteDiscountPolicy(idx){

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
        deleteProductURL = 'https:/localhost:8443/OwnerRemoveDiscountType?'
    }
    else{
        deleteProductURL = 'https:/localhost:8443/ManagerRemoveDiscountType?'
    }
    
    var productId = document.getElementById('deletepolicyIdText').value

    deleteProductURL += 'sessionId=' + sessionStorage['sessionId']
    deleteProductURL += "&storeId=" + storeId;
    deleteProductURL += "&discountTypeID=" + productId;

    console.log(deleteProductURL)
    var result;
    await fetch(deleteProductURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        alert("deleted Policy: " + productId)
        location.reload()

    }
    else{
        alert(result['details'])
    }

}

function showEditDiscountPolicyProduct(){

    
    document.getElementById('editDiscountPolicyToStoreForm').style.display='block'
}


async function editDiscountPolicy(idx){


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

function showAddDiscountPolicy(){

    
    document.getElementById('addDiscountPolicyToStoreForm').style.display='block'
}

async function CreateProductDiscount(){
    
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');

    var addProductDiscountURL;

    if(type === 'Owner'){
        addProductDiscountURL = 'https://localhost:8443/OwnerAddSimpleProductDiscount?'
    }
    else{
        addProductDiscountURL = 'https://localhost:8443/ManagerAddSimpleProductDiscount?'
    }

    var productId = document.getElementById('idText').value
    var amount = document.getElementById('discountProductText').value
    console.log(amount)
    addProductDiscountURL += '&sessionId=' + parseInt(sessionStorage['sessionId'])
    addProductDiscountURL += '&storeId=' + parseInt(storeId)
    addProductDiscountURL += '&productId=' + parseInt(productId)
    addProductDiscountURL += '&salePercentage=' + parseFloat(amount)

    console.log(addProductDiscountURL)

    var result;
    await fetch(addProductDiscountURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
        alert("successfully added amount constraint policy with id: " + result['id'])
    }
    else{
        alert(result['details'])
    }    
}


async function CreateCategoryDiscount(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    var type = urlParams.get('type');


    var addCategoryDiscountURL;


    if(type === 'Owner'){
        addCategoryDiscountURL = 'https://localhost:8443/OwnerAddSimpleCategoryDiscount?'
    }
    else{
        addCategoryDiscountURL = 'https://localhost:8443/ManagerAddSimpleCategoryDiscount?'
    }



    var category = document.getElementById('categoryText').value
    var amount = document.getElementById('discountCategoryText').value

    addCategoryDiscountURL += '&sessionId=' + parseInt(sessionStorage['sessionId'])
    addCategoryDiscountURL += '&storeId=' + parseInt(storeId)
    addCategoryDiscountURL += '&categoryName=' + category
    addCategoryDiscountURL += '&salePercentage=' + amount

    console.log(addCategoryDiscountURL)

    var result;
    await fetch(addCategoryDiscountURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
        alert("successfully added country constraint policy with id: " + result['id'])
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
        addAdvancePolicyURL = 'https://localhost:8443/OwnerCreateAdvancedDiscountType?'

    }
    else{
        addAdvancePolicyURL = 'https://localhost:8443/ManagerCreateAdvancedDiscountType?'
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
    addAdvancePolicyURL += '&discountTypeIDs=' + ids.split(' ').join(',')

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