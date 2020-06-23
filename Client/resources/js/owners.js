

function returnToStore(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    location.href = 'store.html?storeId=' + storeId
}


async function viewOwners(){
    hideButtons();
    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }

    var type = '';

    var result;
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    storeManagersURL = 'https://localhost:8443/getAllManagers?'
    

    storeManagersURL += 'sessionId=' + sessionStorage['sessionId']
    storeManagersURL += '&storeId=' + storeId
    await fetch(storeManagersURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    
    if(result['resultCode'] === 'SUCCESS'){
       
        var managerTable = document.getElementById('managerTable')

        for(var i = 1; i< managerTable.rows.length; i++){
            managerTable.deleteRow(i);
        }

        var ridx = 1;
        for(usersIdx in result['subscribers']){
            user = result['subscribers'][usersIdx]
            if(user['type'] === 'Owner'){
                var row = managerTable.insertRow(ridx)
                var managerId = row.insertCell(0)
                var managerName = row.insertCell(1)

                managerId.innerHTML = user['id']
                managerName.innerHTML = user['username']
            }

        }
    
    }
}

async function showAddOwner(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var possibleManagers;
    possibleManagersURL = 'https://localhost:8443/getOptionalManagers?'
    
    possibleManagersURL += 'sessionId=' + sessionStorage['sessionId']
    possibleManagersURL += '&storeId=' + storeId
    await fetch(possibleManagersURL, headers).then(response => response.json()).then(response => possibleManagers = response)
    console.log(possibleManagers)
    
    var x = document.getElementById("ownerSelect");

    for(i = x.length-1; i >= 0; i--) {
        x.remove(i);
     }

    for(managerIdx in possibleManagers['subscribers']){
        manager = possibleManagers['subscribers'][managerIdx]
        console.log(manager)
        var option = document.createElement("option");
        option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
        x.add(option); 
    }

    document.getElementById('addOwnerToStoreForm').style.display='block'

}

async function addOwnerToStore(){

    addOwnerToStoreURL = 'https://localhost:8443/addStoreOwner?'

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    addOwnerToStoreURL += 'sessionId=' + sessionStorage['sessionId']
    addOwnerToStoreURL += "&storeId=" + parseInt(storeId);
    var user = document.getElementById('ownerSelect').value.split(' ')
    console.log(user)
    addOwnerToStoreURL += "&subId=" +parseInt(user[1]);

    console.log(addOwnerToStoreURL)
    var result;
    await fetch(addOwnerToStoreURL, headers).then(response => response.json()).then(response => result = response)
    
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        alert('successfully added ${user[3]} to the Owners ranks')
        location.reload()
    }

}

async function showRemoveOwner(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var possibleManagers;
    possibleManagersURL = 'https://localhost:8443/getAllManagers?'
    
    possibleManagersURL += 'sessionId=' + sessionStorage['sessionId']
    possibleManagersURL += '&storeId=' + storeId
    await fetch(possibleManagersURL, headers).then(response => response.json()).then(response => possibleManagers = response)
    console.log(possibleManagers)

    var x = document.getElementById("removeOwnerSelect");

    for(i = x.length-1; i >= 0; i--) {
        x.remove(i);
     }

    for(managerIdx in possibleManagers['subscribers']){
        manager = possibleManagers['subscribers'][managerIdx]
        console.log(manager)
        if(manager['type'] === 'Owner'){
            var option = document.createElement("option");
            option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
            x.add(option);
        } 
    }

    document.getElementById('removeOwnerForm').style.display='block'
}


async function removeOwnerFromStore(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    removeManagerURL = 'https://localhost:8443/deleteOwner?'


    removeManagerURL += 'sessionId=' + sessionStorage['sessionId']
    removeManagerURL += "&storeId=" + storeId;
    var user = document.getElementById('removeOwnerSelect').value.split(' ')
    console.log(user)
    removeManagerURL += "&userId=" + user[1];

    var result;
    await fetch(removeManagerURL, headers).then(response => response.json()).then(response => result = response)
    
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        alert('successfully fired ${user[3]} that piece of shit')
        location.reload()
    }

}

async function showPendingOwners(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var possibleOwners;
    possibleOwnersURL = 'https://localhost:8443/getGrantings?'
    
    possibleOwnersURL += 'sessionId=' + sessionStorage['sessionId']
    possibleOwnersURL += '&subId=' + sessionStorage['subId']

    var pendingTable = document.getElementById('pendingTable')

    for(var i = 1; i< pendingTable.rows.length; i++){
        pendingTable.deleteRow(i);
    }
    var ridx = 1;

    await fetch(possibleOwnersURL, headers).then(response => response.json()).then(response => possibleOwners = response)
    console.log(possibleOwners)
    for(userIdx in possibleOwners['grantings']){
        user = possibleOwners['grantings'][userIdx]
        if(user['storeId'] === parseInt(storeId) && user['grantor']['id'] != sessionStorage['subId']){
            console.log(user)
            console.log('dasdad')
            var row = pendingTable.insertRow(ridx)
            var userId = row.insertCell(0)
            var userName = row.insertCell(1)
            var Grantor = row.insertCell(2)
            var Add = row.insertCell(3)
            
            userId.innerHTML = user['candidate']['id']
            userName.innerHTML = user['candidate']['username']
            Grantor.innerHTML = user['grantor']['username']
            Add.innerHTML = "<button type='button' id='approveButton' onclick='approveOwner(" + ridx + ")'>Approve</button>";
            
        }
        
    }


    document.getElementById('approveOwnerForm').style.display='block'

}


async function approveOwner(row){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var approveOwnerURL = 'https:/localhost:8443/approveStoreOwner?';
     
    var pendingTable = document.getElementById('pendingTable')

    var userId = pendingTable.rows[parseInt(row)].cells[0].innerHTML
    // var userId = sessionStorage['subId']

    approveOwnerURL += 'sessionId=' + sessionStorage['sessionId']
    approveOwnerURL += "&storeId=" + storeId;
    approveOwnerURL += "&subId=" + userId;
  
    console.log(approveOwnerURL)
    var result;
    await fetch(approveOwnerURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        console.log('success')
        alert("successfully approved owner")
        location.reload()
    }
    else{
        alert(result['details'])
    }
}