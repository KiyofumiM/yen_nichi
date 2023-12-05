function showCurrentTime() {
    const now = new Date(); // 現在の日時を取得

    const hours = now.getHours();

    // 時間に合わせたメッセージを表示
    let message1 = '';
    if (hours < 4) {
        message1 = '遅くまでお疲れ様です';
    } else if (hours < 12) {
        message1 = 'おはようございます!!';
    } else if (hours < 18) {
        message1 = '学習お疲れ様です！';
    } else if (hours < 22) {
        message1 = 'こんばんは！';
    } else {
        message1 = '遅くまでお疲れ様です！';
    }
    document.getElementById('message1').innerText = message1;

    // 分に合わせたメッセージを表示
    let message2 = '';
    if (hours < 4) {
        message2 = '心と体を休めて今日に備えましょう';
    } else if (hours < 12) {
        message2 = '今日も一日頑張りましょう！';
    } else if (hours < 18) {
        message2 = '一息ついて、さらなる成果を目指しましょう！';
    } else if (hours < 22) {
        message2 = '感謝の気持ちを胸に、穏やかな夕べを。';
    } else {
        message2 = '心と体を休めて明日に備えましょう';
    }
    console.log({message2, elm: document.getElementById('message2')})
    document.getElementById('message2').innerText = message2;

    // 1分ごとに時刻と日付を更新
    setTimeout(showCurrentTime, 60000);
}

// 最初の呼び出し
showCurrentTime();