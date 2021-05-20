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


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
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
        messageElement.classList.add('chat-message');
        var avatarElement = document.createElement('img');

        // body:
        //     chat_user:
        //          avatar: null
        //          email: "admin@gmail.com"
        //          info: {rights: "GOD", joined: false, role: null, user: null, chat_user_id: 1}
        //          professional: null
        //          user_id: 2
        //          username: "admin"
        //      content:
        //          attachments: []
        //          chatRoom: {avatar: null, info: null, name: "Новый чатик", lastPosted: "2021-05-20", private: false, …}
        //          message: "fv"
        //          messageType: "CHAT"
        //          message_id: 4
        //          posted: "2021-05-20"
        //---------------------------------//---------------------------------//---------------------------------
        //          message.chat_user.avatar: null
        //          message.chat_user.email: "admin@gmail.com"
        //          message.chat_user.info: {rights: "GOD", joined: false, role: null, user: null, chat_user_id: 1}
        //          message.chat_user.professional: null
        //          message.chat_user.user_id: 2
        //          message.chat_user.username: "admin"
        //
        //          message.content.attachments: []
        //          message.content.chatRoom: {avatar: null, info: null, name: "Новый чатик", lastPosted: "2021-05-20", private: false, …}
        //          message.content.message: "fv"
        //          message.content.messageType: "CHAT"
        //          message.content.message_id: 4
        //          message.content.posted: "2021-05-20"

        message = message.body
        const userId = message.chat_user.user_id
        const messageId = message.content.message_id;
        const messageValue = message.content.message
        const avatarValue = message.chat_user.avatar ?? message.chat_user.info.chat_user_id
        const username = message.chat_user.username
        const posted = new Date(message.content.posted).toLocaleString('ru', {
            day: '2-digit',
            month: '2-digit',
            year: '2-digit'
        });
        console.log(messageValue, avatarValue, username, posted)

        var avatarText = document.createTextNode(username[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.id = message.content.message_id;
        avatarElement.style['background-color'] = getAvatarColor(userId);
        avatarElement.style['background-position'] = "top";
        avatarElement.src = avatarValue;
        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode("[" + posted + "]\t" + username);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content.message ?? message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
