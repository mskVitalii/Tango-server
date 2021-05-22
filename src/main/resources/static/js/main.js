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
var client = 1;
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();
    client = username;
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
            chatUserId: client,//1,
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
        message = makeMessage(messageElement, message);
    }
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.message ?? message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function makeMessage(messageElement, message) {
    messageElement.classList.add('chat-message');
    var avatarElement = document.createElement('img');

    const username = message.username
    const messageValue = message.message
    const posted = message.posted
    const avatarValue = message.avatar ?? message.username

    console.log(messageValue, avatarValue, username, posted)

    var avatarText = document.createTextNode(username[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(username);
    avatarElement.style['background-position'] = "top";
    avatarElement.style['object-fit'] = 'cover';
    avatarElement.src = avatarValue;
    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode("[" + posted + "]\t" + username);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    return message
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
    let myHeaders = new Headers();
    myHeaders.append("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJWaXRhbHkiLCJpYXQiOjE2MjE2NDI0NjcsImV4cCI6MTYyMTcyODg2N30.45gWgwKkE8IEYLWFItYA1Uavp5DHZU_MsCU7GYTF2Jjx2-bGiuvVqE8Qe8CGmbZvifbwiP8lt27u7zctBXPelw");

    let result = await fetch("/api/chats/history/1?page=0&size=250", {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    })
        .then(response => response.json())
        .catch(error => console.log('error', error));

    console.log(result.result)

    for (let i = 0; i < result.result?.length ; i++) {
        let textElement = document.createElement('p');
        let messageElement = document.createElement('li');

        let message = makeMessage(messageElement, result.result[i])

        console.log(message)
        let messageText = document.createTextNode(message.message ?? message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
    }
    messageArea.scrollTop = messageArea.scrollHeight;
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
