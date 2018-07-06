
function setStorage (key, value) {
    return new Promise((resolve, reject) => {
        wx.setStorage({ key: key, data: value, success: resolve, fail: reject });
    });
}

function getStorage (key) {
    return new Promise((resolve, reject) => {
        wx.getStorage({ key: key, success: resolve, fail: reject });
    });
}

function setStorageSync (key, value) {
    wx.setStorageSync(key, value);
}

function getStorageSync (key) {
    return wx.getStorageSync(key);
}

function removeStorageSync (key) {
    wx.removeStorageSync(key);
}

function clearStorage () {
    wx.clearStorage();
}

module.exports = {
    setStorage,
    getStorage,
    setStorageSync: setStorageSync,
    getStorageSync: getStorageSync,
    removeStorageSync: removeStorageSync,
    clearStorage: clearStorage
};
