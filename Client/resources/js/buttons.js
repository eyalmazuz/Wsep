headers = {
    headers: {          
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
}

async function register(){


        var username = document.getElementById("registerUserText").value;

        var password = document.getElementById("registerPasswordText").value;

        registerURL = 'https://localhost:8443/register?sessionId=' + sessionStorage['sessionId'] + '&username=' + username + '&password=' + password

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

        registerURL = 'https://localhost:8443/login?sessionId=' + sessionStorage['sessionId'] + '&username=' + username + '&password=' + password

        var result; 
        await fetch(registerURL).then(response => response.json()).then(response => result = response)

        console.log(result)

        if (result['resultCode'] === 'SUCCESS'){
            
            sessionStorage['loggedin'] = true
            sessionStorage['username'] = username
            sessionStorage['subId'] = result['id']
            connect()
            if(username === 'admin' && password === "admin"){
                sessionStorage['isAdmin'] = true
            }
            document.getElementById('loginForm').style.display = 'none'
            document.getElementById("loginUserText").value = ''
            document.getElementById("loginPasswordText").value = ''
            alert("successfully logged in")
            sendReadyForNotificaitons();

        
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
        stompClient.subscribe('/storeUpdate/' + sessionStorage['subId'], function (message) {
            recieveNotification(message)
        });
    });
}

async function sendReadyForNotificaitons(){

    pullNotificationURL = 'https://localhost:8443/ready?'

    pullNotificationURL += 'subId=' + sessionStorage['subId']

    await fetch(pullNotificationURL, headers).then(response => console.log("send ready message"))
}

async function recieveNotification(message){
    message = JSON.parse(message.body)
    alert(message['massage'])
    var id = message['id']

    ackURL = "https://localhost:8443/notificationAck?"

    ackURL += 'subId=' + sessionStorage['subId']
    ackURL += '&notification=' + id

    await fetch(ackURL, headers).then(response => console.log("message sent"))

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

    if(sessionStorage['loggedin'] === 'false'){
        console.log('popup')
        alert("Can't logout when not logged in");
    }
    else{
        var logoutURL = "https://localhost:8443/logout?"

        logoutURL += 'sessionId=' + sessionStorage['sessionId']

        if(sessionStorage['isAdmin'] === 'true'){
            sessionStorage['isAdmin'] = false
        }

        var result; 
        await fetch(logoutURL).then(response => response.json()).then(response => result = response)
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            alert("successfully logged out")
            sessionStorage['loggedin'] = false
            sessionStorage['username'] = ''

        }
        location.reload()

    }
}


function showLogin(){
    if(sessionStorage['loggedin'] === 'true'){
        console.log('popup')
        alert("Can't login already logged in");
    }
    else{
        document.getElementById('loginForm').style.display='block'
    }
}

function showRegister(){
    if(sessionStorage['loggedin'] === 'true'){
        alert("Can't register already logged in");
    }
    else{
        document.getElementById('registerForm').style.display='block'
    }
}


async function openStore(){
    var result;

    if(sessionStorage['loggedin'] === 'false'){
        alert("To open a store you need to be logged in")
    }
    else{
        openStoreURL = "https://localhost:8443/openStore?"
        
        openStoreURL += 'sessionId=' + sessionStorage['sessionId']
        await fetch(openStoreURL, headers).then(response => response.json()).then(response => result = response)
        alert("successfully openned store")
        location.reload()

    }  
}


async function moveToAdminPage(){
    if(sessionStorage['isAdmin'] === 'true'){
        location.href = 'admin.html'
    }
    else{
        alert("admins only")
    }
}
