'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


async function onConnected() {
    // Subscribe to the Public Topic
    await stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    await stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
    await getHistory();
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            chatUserId: 1,
            chatId: 1,
            message: messageInput.value,
            messageType: 'CHAT',
            posted: new Date().toJSON(),
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    console.log(message);
    var messageElement = document.createElement('li');

    if (message.type && message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        // {type: "JOIN", content: null, sender: "fv"}
        message.content = message.sender + ' joined!';
        console.log(message.sender + ' joined!', message.content);
    } else if (message.type && message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        makeMessage();
    }
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.message ?? message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function makeMessage() {
    messageElement.classList.add('chat-message');
    var avatarElement = document.createElement('img');

    const username = message.username
    const messageValue = message.message
    const posted = message.posted
    // new Date(message.posted)
    //     .toLocaleString('ru', {
    //     day: '2-digit',
    //     month: '2-digit',
    //     year: '2-digit'
    // });
    const avatarValue = message.avatar ?? message.username

    console.log(messageValue, avatarValue, username, posted)

    var avatarText = document.createTextNode(username[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(username);
    avatarElement.style['background-position'] = "top";
    avatarElement.src = avatarValue;
    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode("[" + posted + "]\t" + username);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

async function getHistory() {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJWaXRhbHkiLCJpYXQiOjE2MjE1MTM1NDAsImV4cCI6MTYyMTU5OTk0MH0.fN0QUErDu1e3wgJIz_XY2JlgXNhtHxIsgs3G0z9A2kl9lkTM4ycBpOz6XBDCuSxPHbGLQ2Gd9QGvbYxxP0E-XQ");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    await fetch("localhost:8080/api/chats/history/1?page=0&size=250", requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
