
async function startSystem(){
    
    if(!('sessionId' in sessionStorage)){
        getSessionId()
    }
    await fetch("https://localhost:8443/setup?supplyConfig=supplyConfig&paymentConfig=paymentConfig")
        if(!('loggedin' in sessionStorage)){
        sessionStorage['loggedin'] = false
    }

    if(!('isAdmin' in sessionStorage)){
        sessionStorage['isAdmin'] = false
    }
    if(sessionStorage['loggedin'] === 'true'){
        connect()
    }
}

async function getSessionId(){
    headers = {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    }
    
    
    await fetch("https://localhost:8443/startSession", headers)
    .then(response => response.json())
    .then(response => sessionStorage['sessionId']=response['id']).catch(err => console.log(err))
    console.log("session id: " + sessionStorage['sessionId'])


}