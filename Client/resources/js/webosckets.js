// headers = {
//     headers: {          
//             'Content-Type': 'application/json',
//             'Accept': 'application/json'
//         }
// }


// function connect() {
//     var socket = new SockJS('https://localhost:8443/notifications');
//     stompClient = Stomp.over(socket);
//     stompClient.connect({}, function (frame) {
//         console.log('Connected: ' + frame);
//         stompClient.subscribe('/storeUpdate/' + sessionStorage['subId'], function (message) {
//             recieveNotification(message)
//         });
//     });
// }

// async function recieveNotification(mesage){
//     message = JSON.parse(message.body)
//     alert(message['message'])
//     var id = message['id']

//     ackURL = "https://localhost:8443/notificationAck?"

//     ackURL += 'subId=' + sessionStorage['subId']
//     ackURL += '&notification=' + id

//     await fetch(ackURL, headers).then(response => console.log("message sent"))

// }