/**
 * 
 */

stomp.connect({}, () => {
	// 방 구독
	stomp.subscribe(`/topic/room/${roomTest}`, e => {
		const msg = JSON.parse(e.body);
		appendChat(msg);
	});

	// 입장 알림 (선택)
	stomp.send('/pub/chat.enter', {}, JSON.stringify({
		roomId: roomTest, sender: idTest, content: ''
	}));
});

// ------------------------------------
// 2) 메시지 전송
// ------------------------------------
document.querySelector('#sendBtn').onclick = () => {
	const content = document.querySelector('#msg').value.trim();
	if (!content) return;
	stomp.send('/pub/chat.send', {}, JSON.stringify({
		roomId: roomTest, sender: idTest, content, type: 'TALK'
	}));
	document.querySelector('#msg').value = '';
};

// ------------------------------------
// 3) 화면 표시
// ------------------------------------
function appendChat({ type, sender, content }) {
	const li = document.createElement('li');
	if (type === 'ENTER') li.textContent = `👋 ${sender} 님 입장`;
	else if (type === 'LEAVE') li.textContent = `🚪 ${sender} 님 퇴장`;
	else li.textContent = `[${sender}] ${content}`;
	document.querySelector('#log').append(li);
}