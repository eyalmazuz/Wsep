

function returnToStore(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    location.href = 'store.html?storeId=' + storeId
}


async function viewOwners(){

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
    for(managerIdx in possibleManagers['subscribers']){
        manager = possibleManagers['subscribers'][managerIdx]
        console.log(manager)
        var x = document.getElementById("ownerSelect");
        var option = document.createElement("option");
        option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
        x.add(option); 
    }

    document.getElementById('addOwnerToStoreForm').style.display='block'

}

async function addOwnerToStore(){

    addOwnerToStoreURL = 'https://localhost:8443/addStoreOwner?'


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