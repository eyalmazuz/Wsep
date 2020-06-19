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


async function connectStatistics() {
    var socket = new SockJS('https://localhost:8443/notifications');
    stompClient = Stomp.over(socket);
    await stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/storeUpdate/' + sessionStorage['subId'], function (message) {
            recieveNotification(message)
        });
        stompClient.subscribe('/statsUpdate/0' , function (message) {
            console.log("got push data")
            data = JSON.parse(message.body)
            var date = new Date()
            var today = '';
            today += date.getFullYear()
            today += date.getMonth() < 10 ? '-0' + (date.getMonth() + 1) : '-' + (date.getMonth()+1)
            today += '-' + date.getDate()
            console.log(today)
            console.log(sessionStorage['toDate'])
            if(sessionStorage['toDate'] === today){
                updateChart(data);
            }
            // drawChart(data)
        });
        sendReadyForNotificaitons()

    });

}