

function returnToStore(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    location.href = 'store.html?storeId=' + storeId
}


async function viewManagers(){
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
            if(user['type'] === 'Manager'){
                var row = managerTable.insertRow(ridx)
                var managerId = row.insertCell(0)
                var managerName = row.insertCell(1)

                managerId.innerHTML = user['id']
                managerName.innerHTML = user['username']
            }

        }
    
    }
}

async function showAddManager(){

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
        var x = document.getElementById("managerSelect");
        var option = document.createElement("option");
        option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
        x.add(option); 
    }

    document.getElementById('addManagerToStoreForm').style.display='block'
}

async function showRemoveManager(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var possibleManagers;
    possibleManagersURL = 'https://localhost:8443/getAllManagers?'
    
    possibleManagersURL += 'sessionId=' + sessionStorage['sessionId']
    possibleManagersURL += '&storeId=' + storeId
    await fetch(possibleManagersURL, headers).then(response => response.json()).then(response => possibleManagers = response)
    console.log(possibleManagers)
    for(managerIdx in possibleManagers['subscribers']){
        manager = possibleManagers['subscribers'][managerIdx]
        console.log(manager)
        if(manager['type'] === 'Manager'){
            var x = document.getElementById("removeManagerSelect");
            var option = document.createElement("option");
            option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
            x.add(option);
        } 
    }

    document.getElementById('removeManagerForm').style.display='block'
}

async function showEditManager(){

    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    var possibleManagers;
    possibleManagersURL = 'https://localhost:8443/getAllManagers?'
    
    possibleManagersURL += 'sessionId=' + sessionStorage['sessionId']
    possibleManagersURL += '&storeId=' + storeId
    await fetch(possibleManagersURL, headers).then(response => response.json()).then(response => possibleManagers = response)
    console.log(possibleManagers)
    for(managerIdx in possibleManagers['subscribers']){
        manager = possibleManagers['subscribers'][managerIdx]
        console.log(manager)
        if(manager['type'] === 'Manager'){
            var x = document.getElementById("editManagerSelect");
            var option = document.createElement("option");
            option.innerHTML = "Id: " + manager['id'] + " Name: "+ manager['username']
            x.add(option);
        } 
    }

    document.getElementById('editManagerForm').style.display='block'
}



async function addManagerToStore(){

    var addManagerToStoreURL;
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');
    addManagerToStoreURL = 'https://localhost:8443/addStoreManager?'


    addManagerToStoreURL += 'sessionId=' + sessionStorage['sessionId']
    addManagerToStoreURL += "&storeId=" + storeId;
    var user = document.getElementById('managerSelect').value.split(' ')
    console.log(user)
    addManagerToStoreURL += "&userId=" + user[1];

    var result;
    await fetch(addManagerToStoreURL, headers).then(response => response.json()).then(response => result = response)
    
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        alert('successfully added ${user[3]} to the Manager ranks')
        location.reload()
    }

}



async function removeManagerFromStore(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    removeManagerURL = 'https://localhost:8443/deleteManager?'


    removeManagerURL += 'sessionId=' + sessionStorage['sessionId']
    removeManagerURL += "&storeId=" + storeId;
    var user = document.getElementById('removeManagerSelect').value.split(' ')
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


async function editManager(){
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = urlParams.get('storeId');

    editManagerURL = 'https://localhost:8443/editManageOptions?'


    editManagerURL += 'sessionId=' + sessionStorage['sessionId']
    editManagerURL += "&storeId=" + storeId;

    var user = document.getElementById('editManagerSelect').value.split(' ')
    editManagerURL += "&userId=" + user[1];
    console.log(user[1])
    var option = document.getElementById('editOptionsSelect').value
    editManagerURL += "&options=" + option;

    var result;
    await fetch(editManagerURL, headers).then(response => response.json()).then(response => result = response)
    
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        alert('successfully edited ${user[3]} options')
        location.reload()
    }

}