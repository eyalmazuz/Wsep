headers = {
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
}



async function connect() {
    var socket = new SockJS('https://localhost:8443/notifications');
    stompClient = Stomp.over(socket);
    await stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/storeUpdate/' + sessionStorage['subId'], function (message) {
            recieveNotification(message)
        });
        sendReadyForNotificaitons()

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
