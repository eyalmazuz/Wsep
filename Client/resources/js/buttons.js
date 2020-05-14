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
            if(username === 'admin' && password === "admin"){
                localStorage['isAdmin'] = true
            }
            document.getElementById('loginForm').style.display = 'none'
            document.getElementById("loginUserText").value = ''
            document.getElementById("loginPasswordText").value = ''
        
        }
        
        else if(result['resultCode'] === 'ERROR_LOGIN'){
            document.getElementById("loginResult").innerHTML = result['details']
            document.getElementById("loginUserText").value = ''
            document.getElementById("loginPasswordText").value = ''
        }
    
}

async function logout(){

    if(localStorage['loggedin'] === 'false'){
        console.log('popup')
        alert("Can't logout when not logged in");
    }
    else{
        var logoutURL = "https://localhost:8443/logout"

        if(localStorage['isAdmin'] === 'true'){
            localStorage['isAdmin'] = false
        }

        var result; 
        await fetch(logoutURL).then(response => response.json()).then(response => result = response)
        console.log(result)
        if(result['resultCode'] === 'SUCCESS'){
            console.log("successfully logged out")
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
        openStoreURL = "https://localhost:8443/openStore"
        await fetch(openStoreURL, headers).then(response => response.json()).then(response => result = response)
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

        addProductURL += 'id=' + productId
        addProductURL += '&name=' + name
        addProductURL += '&category=' + category
        console.log(addProductURL)
        var result;
        // await fetch(addProductURL, headers)
        await fetch(addProductURL, headers).then(response => response.json()).then(response => result = response)
        
        if(result['resultCode'] === 'SUCCESS'){
            console.log("successfully added proudct")            
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