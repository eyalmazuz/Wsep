headers = {
    headers: {          
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
}

async function register(){


        var username = document.getElementById("registerUserText").value;

        var password = document.getElementById("registerPasswordText").value;

        registerURL = 'https://localhost:8443/register?sessionId=' + localStorage['sessionId'] + '&username=' + username + '&password=' + password

        var result; 
        await fetch(registerURL).then(response => response.json()).then(response => result = response)

        console.log(result)

        if (result['resultCode'] === 'SUCCESS'){
            document.getElementById('registerForm').style.display='none'
            document.getElementById("registerUserText").value = ''
            document.getElementById("registerPasswordText").value = ''
            alert("successfully register")
        }
        else if(result['resultCode'] === 'ERROR_REGISTER'){
            console.log('in else if')
            document.getElementById("registerResult").innerHTML = result['details']
            document.getElementById("registerUserText").value = ''
            document.getElementById("registerPasswordText").value = ''
        }
    
}


async function login(){


        var username = document.getElementById("loginUserText").value;

        var password = document.getElementById("loginPasswordText").value;

        registerURL = 'https://localhost:8443/login?sessionId=' + localStorage['sessionId'] + '&username=' + username + '&password=' + password

        var result; 
        await fetch(registerURL).then(response => response.json()).then(response => result = response)

        console.log(result)

        if (result['resultCode'] === 'SUCCESS'){
            
            localStorage['loggedin'] = true
            localStorage['username'] = username
            localStorage['subId'] = result['id']
            connect()
            if(username === 'admin' && password === "admin"){
                localStorage['isAdmin'] = true
            }
            document.getElementById('loginForm').style.display = 'none'
            document.getElementById("loginUserText").value = ''
            document.getElementById("loginPasswordText").value = ''
            alert("successfully logged in")
        
        }
        
        else if(result['resultCode'] === 'ERROR_LOGIN'){
            document.getElementById("loginResult").innerHTML = result['details']
            document.getElementById("loginUserText").value = ''
            document.getElementById("loginPasswordText").value = ''
        }
    
}


function connect() {
    var socket = new SockJS('https://localhost:8443/notifications');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/storeUpdate/' + localStorage['subId'], function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


function showGreeting(message) {
    console.log(message)
}

async function logout(){

    if(localStorage['loggedin'] === 'false'){
        console.log('popup')
        alert("Can't logout when not logged in");
    }
    else{
        var logoutURL = "https://localhost:8443/logout?"

        logoutURL += 'sessionId=' + localStorage['sessionId']

        if(localStorage['isAdmin'] === 'true'){
            localStorage['isAdmin'] = false
        }

        var result; 
        await fetch(logoutURL).then(response => response.json()).then(response => result = response)
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert("successfully logged out")
            localStorage['loggedin'] = false
            localStorage['username'] = ''

        }
    }
}


function showLogin(){
    if(localStorage['loggedin'] === 'true'){
        console.log('popup')
        alert("Can't login already logged in");
    }
    else{
        document.getElementById('loginForm').style.display='block'
    }
}

function showRegister(){
    if(localStorage['loggedin'] === 'true'){
        alert("Can't register already logged in");
    }
    else{
        document.getElementById('registerForm').style.display='block'
    }
}


async function openStore(){
    var result;

    if(localStorage['loggedin'] === 'false'){
        alert("To open a store you need to be logged in")
    }
    else{
        openStoreURL = "https://localhost:8443/openStore?"
        
        openStoreURL += 'sessionId=' + localStorage['sessionId']
        await fetch(openStoreURL, headers).then(response => response.json()).then(response => result = response)
        alert("successfully openned store")
        location.reload()

    }  
}


function showAddProduct(){
    if(localStorage['isAdmin'] === 'false'){
        alert("Only Admins can add products to the system");
    }
    else{
        document.getElementById('addProductForm').style.display='block'
    }
}

async function addProduct(){


        addProductURL = "https://localhost:8443/addProductInfo?";
        
        localStorage['productID'] = parseInt(localStorage['productID']) + 1;
        var name = document.getElementById("nameText").value
        var category = document.getElementById("categoryText").value
        var productId = document.getElementById('idText').value

        addProductURL += 'sessionId=' + localStorage['sessionId']
        addProductURL += 'id=' + productId
        addProductURL += '&name=' + name
        addProductURL += '&category=' + category
        console.log(addProductURL)
        var result;
        await fetch(addProductURL, headers).then(response => response.json()).then(response => result = response)
        
        if(result['resultCode'] === 'SUCCESS'){
            alert("successfully added proudct")            
            document.getElementById('addProductForm').style.display='none'
            document.getElementById("nameText").value = ''
            document.getElementById("categoryText").value = ''

        }
        else{
            alert(result['details'])
            document.getElementById("nameText").value = ''
            document.getElementById("categoryText").value = ''
        
        }


}


async function moveToAdminPage(){
    if(localStorage['isAdmin'] === 'true'){
        location.href = 'admin.html'
    }
    else{
        alert("admins only")
    }
}


async function logoutAdmin(){
    await logout()
    location.href = 'index.html'
}