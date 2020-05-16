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

    //     sessionStorage.clear()
    //   });
      

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

    await fetch("https://localhost:8443/startSession")
    .then(response => response.json())
    .then(response => sessionStorage['sessionId']=response['id'])
    console.log("session id: " + sessionStorage['sessionId'])


}

function connect() {
    var socket = new SockJS('https://localhost:8443/notifications');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/storeUpdate/' + sessionStorage['subId'], function (message) {
            alert(message.body)
        });
    });
}