// import firebase from 'firebase/app';
// import '@firebase/messaging';
// import config from './firebaseConfig';


// let token ='';
// const messaging = firebase.messaging();
// messaging.usePublicVapidKey('BCslFJO_va9K_9Qeo8HbYWjwI2rg4CrmcfNt5Uy0ffzDfoLFSB0bug-lxfTyfdRXCxskYgtXTxfh2xAux9kSbRs')

// messaging.getToken().then((currentToken) => {
//     if (currentToken) {
//         console.log(currentToken);
//         token = currentToken;
//         alert(token);
//     } else {
//         // Show permission request.
//         console.log('No Instance ID token available. Request permission to generate one.');
//     }
// });

//포그라운드 상태에서 받은 알림 처리 
 
// messaging.onMessage((payload) => {
//     console.log(payload);
//     const title = 'Title';
//     const options = {
//         body: payload.data.message,
//         icon: './logo.png',
//     };
//     const notification = new Notification(title, options);
//     return notification;
// });



// firebase.initializeApp(config);
// export {
//     token,
//     messaging,
// };