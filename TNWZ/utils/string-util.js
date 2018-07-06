/**  获取字符数，英文占1个中文占2个 */
function getLength (str) {
    let strlen = 0;
    for (let i = 0; i < str.length; i++) {
        if (str.charCodeAt(i) > 255) { // 非英文字符算2位
            strlen += 2;
        } else {
            strlen++;
        }
    }
    return strlen;
}

module.exports = {
    getLength: getLength
};
