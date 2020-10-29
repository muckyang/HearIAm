// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here, other Firebase libraries
// are not available in the service worker.
importScripts('https://www.gstatic.com/firebasejs/8.0.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.0.0/firebase-messaging.js');
//import config from '@/assets/firebaseConfig';
// Initialize the Firebase app in the service worker by passing in the
// messagingSenderId.


const firebaseConfig = {
  apiKey: "AIzaSyBlXUwp6KUZ6ntouxrCdtktNyu441ySZ44",
  authDomain: "fcm-hear-iam.firebaseapp.com",
  databaseURL: "https://fcm-hear-iam.firebaseio.com",
  projectId: "fcm-hear-iam",
  storageBucket: "fcm-hear-iam.appspot.com",
  messagingSenderId: "69668597355",
  appId: "1:69668597355:web:660c978f1ec77eb410cb2e",
  measurementId: "G-V5EVSX2YFX"
};

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

// 백그라운드 상태에서 받은 알림 처리
messaging.setBackgroundMessageHandler((payload) => {
  console.log('[firebase-messaging-sw.js] Received background message ', payload)
  // Customize notification here
  const notificationTitle = 'Hear I am';
  const notificationOptions = {
    body: '알림이 왔습니다. 확인해주세요!',
    icon: '../src/assets/logo.png',
  }

  return self.registration.showNotification(notificationTitle,
    notificationOptions);
})


