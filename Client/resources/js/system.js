headers = {
    headers: {          
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
}


async function startSystem(){
    
    // window.addEventListener('beforeunload', (event) => {
    //     // Cancel the event as stated by the standard.
    //     event.preventDefault();
    //     // Chrome requires returnValue to be set.
    //     event.returnValue = '';

    //     localStorage.clear()
    //   });
      

    if(!('sessionId' in localStorage)){
        getSessionId()
    }
    await startControllers()
    if(!('loggedin' in localStorage)){
        localStorage['loggedin'] = false
    }

    if(!('productID' in localStorage)){
        localStorage['productID'] = 0
    }

    if(!('isAdmin' in localStorage)){
        localStorage['isAdmin'] = false
    }
}

async function startControllers(){
    await fetch("https://localhost:8443/setup?supplyConfig=supplyConfig&paymentConfig=paymentConfig")
    await fetch("https://localhost:8443/AdminStateHandler?sessionId=" + localStorage['sessionId'])
    await fetch("https://localhost:8443/ManagerHandler?sessionId=" + localStorage['sessionId'])
    await fetch("https://localhost:8443/OwnerHandler?sessionId=" + localStorage['sessionId'])
    await fetch("https://localhost:8443/SubscriberStateHandler?sessionId=" + localStorage['sessionId'])
}

async function getSessionId(){

    await fetch("https://localhost:8443/startSession")
    .then(response => response.json())
    .then(response => localStorage['sessionId']=response['id'])
    console.log("session id: " + localStorage['sessionId'])


}