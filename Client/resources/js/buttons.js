
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
			alert('Could not contact database. Please try again later.');
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
            await connect() //.then(result => sendReadyForNotificaitons())
            
            registerURL = 'https://localhost:8443/isAdmin?sessionId=' + sessionStorage['sessionId']

            var result; 
            await fetch(registerURL).then(response => response.json()).then(response => result = response)
            console.log(result)
            
            if(result['resultCode'] === 'SUCCESS'){
                sessionStorage['isAdmin'] = true
            }
            document.getElementById('loginForm').style.display = 'none'
            document.getElementById("loginUserText").value = ''
            document.getElementById("loginPasswordText").value = ''
            alert("successfully logged in")
			location.reload();

            ;

        
        }
        
        else if(result['resultCode'] === 'ERROR_LOGIN'){
            document.getElementById("loginResult").innerHTML = result['details']
            document.getElementById("loginUserText").value = ''
            document.getElementById("loginPasswordText").value = ''
        }

    
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
            sessionStorage['subId'] = '-1'

        }
        location.href = 'index.html'

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


function hideButtons(){
    if(sessionStorage['loggedin'] === 'true'){
        document.getElementById('logoutButton').style.visibility = '';
        document.getElementById('registerButton').style.visibility = 'hidden';
        document.getElementById('loginButton').style.visibility = 'hidden';
    }
    else{

        document.getElementById('logoutButton').style.visibility = 'hidden';
        document.getElementById('registerButton').style.visibility = '';
        document.getElementById('loginButton').style.visibility = '';
    }
}