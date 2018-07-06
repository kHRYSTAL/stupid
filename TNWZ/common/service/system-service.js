import {PAY_ERROR} from '../constants';

function login () {
    return new Promise((resolve, reject) => {
        wx.login({success: resolve, fail: reject});
    });
}

function getShareInfo (ticket) {
    return new Promise((resolve, reject) => {
        wx.getShareInfo({
            shareTicket: ticket,
            success: resolve,
            fail: reject
        });
    });
}

function getWxUserInfo () {
    return new Promise((resolve, reject) => {
        wx.getUserInfo({success: resolve, fail: reject});
    });
}

function getLocation (type) {
    return new Promise((resolve, reject) => {
        wx.getLocation({type: type, success: resolve, fail: reject});
    });
}

function checkSession () {
    return new Promise((resolve, reject) => {
        wx.checkSession({
            success: resolve, fail: reject
        });
    });
}

function removeStorageSync (key) {
    wx.removeStorageSync(key);
}

function addPhoneContact (params) {
    return new Promise((resolve, reject) => {
        let dict = Object.assign(params, {
            success: resolve, fail: reject
        });
        wx.addPhoneContact(dict);
    });
}

function openSetting () {
    return new Promise((resolve, reject) => {
        wx.openSetting({
            success: resolve,
            fail: reject
        });
    });
}

function makePhoneCall (obj) {
    return new Promise((resolve, reject) => {
        wx.makePhoneCall(Object.assign(obj, {success: resolve, fail: reject}));
    });
}

function requestPayment (params) {
    return new Promise((resolve, reject) => {
        // params 应携带的参数
        // 'timeStamp': ''
        // 'nonceStr': ''
        // 'package': ''
        // 'signType': 'MD5'
        // 'paySign': ''
        wx.requestPayment(Object.assign({
            success: resolve,
            fail: () => {
                reject({errorCode: PAY_ERROR.WX_PAY_ERR});
            }
        }, params));
    });
}

/**
 * 获取微信后台音频播放组件对象
 */
function getBackgroundAudioManager () {
    return wx.getBackgroundAudioManager();
}

/**
 * 停止后台音频播放组件
 */
function stopBackgroundAudio () {
    getBackgroundAudioManager().stop();
}

module.exports = {
    login,
    getWxUserInfo,
    getLocation,
    checkSession: checkSession,
    addPhoneContact: addPhoneContact,
    removeStorageSync: removeStorageSync,
    getShareInfo: getShareInfo,
    requestPayment: requestPayment,
    makePhoneCall: makePhoneCall,
    openSetting: openSetting,
    getBackgroundAudioManager,
    stopBackgroundAudio
};
