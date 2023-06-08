let stompClient = null;

const chatLineElementId = "chatLine";
const roomIdElementId = "roomId";
const messageLabelElementId = "messageLabel";
const messageElementId = "message";
const sendElementId ="send";
const roomAllMessage = 1408;


const setConnected = (connected) => {
    const connectBtn = document.getElementById("connect");
    const disconnectBtn = document.getElementById("disconnect");

    connectBtn.disabled = connected;
    disconnectBtn.disabled = !connected;
    const chatLine = document.getElementById(chatLineElementId);
    chatLine.hidden = !connected;
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);

        const roomId = document.getElementById(roomIdElementId).value;
        console.log(`Connected to roomId: ${roomId} frame:${frame}`);
        if (roomId == roomAllMessage) {
             hideSendMsg(true);
        }
        stompClient.subscribe(`/topic/response.${roomId}`, (message) => showMessage(JSON.parse(message.body).messageStr));
    });
}

const hideSendMsg = (show) => {
   document.getElementById(messageLabelElementId).disabled = show;
   document.getElementById(messageLabelElementId).hidden = show;
   document.getElementById(messageElementId).disabled = show;
   document.getElementById(messageElementId).hidden = show;
   document.getElementById(sendElementId).disabled = show;
   document.getElementById(sendElementId).hidden = show;
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    hideSendMsg(false);
    console.log("Disconnected");
}

const sendMsg = () => {
    const roomId = document.getElementById(roomIdElementId).value;
    const message = document.getElementById(messageElementId).value;
    stompClient.send(`/app/message.${roomId}`, {}, JSON.stringify({'messageStr': message}))
}

const showMessage = (message) => {
    const chatLine = document.getElementById(chatLineElementId);
    let newRow = chatLine.insertRow(-1);
    let newCell = newRow.insertCell(0);
    let newText = document.createTextNode(message);
    newCell.appendChild(newText);
}
