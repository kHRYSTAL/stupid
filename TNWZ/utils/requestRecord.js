let requestList = {}; // api请求记录

// 将当前请求的api记录起来
function addRequestKey (key) {
    requestList[key] = true;
}

// 将请求完成的api从记录中移除
function removeRequestKey (key) {
    delete requestList[key];
}

// 当前请求的api是否已有记录
function hitRequestKey (key) {
    return requestList[key];
}

// 根据请求的地址，请求参数组装成api请求的key,方便记录
function getRequestKey (url, method, data, header) {
    let key = 'Method: ' + method + ',Url: ' + url + ',Data: ' + json2Form(data) + ', header:' + json2Form(header);
    return key;
}

function json2Form (json) {
    var str = [];
    for (var p in json) {
        str.push(encodeURIComponent(p) + '=' + encodeURIComponent(json[p]));
    }
    return str.join('&');
}

module.exports = {
    json2Form: json2Form,
    addRequestKey: addRequestKey,
    removeRequestKey: removeRequestKey,
    hitRequestKey: hitRequestKey,
    getRequestKey: getRequestKey
};
