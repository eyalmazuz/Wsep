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
    await fetch("https://localhost:8443/setup?supplyConfig=supplyConfig&paymentConfig=paymentConfig")
        if(!('loggedin' in localStorage)){
        localStorage['loggedin'] = false
    }

    if(!('productID' in localStorage)){
        localStorage['productID'] = 0
    }

    if(!('isAdmin' in localStorage)){
        localStorage['isAdmin'] = false
    }
    if(localStorage['loggedin'] === 'true'){
        connect()
    }
}

async function getSessionId(){

    await fetch("https://localhost:8443/startSession")
    .then(response => response.json())
    .then(response => localStorage['sessionId']=response['id'])
    console.log("session id: " + localStorage['sessionId'])


}

function connect() {
    var socket = new SockJS('https://localhost:8443/notifications');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/storeUpdate/' + localStorage['subId'], function (message) {
            alert(message.body)
        });
    });
}