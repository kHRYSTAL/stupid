import {BASE_URL, SERVICE_ERROR_STATUS, APP_ID} from '../common/constants';
import {addRequestKey, getRequestKey, hitRequestKey, removeRequestKey} from './requestRecord';
import {globalData} from '../utils/global-data';

const https = [];

function defaultHeader() {
    let session = globalData('user_session') ? globalData('user_session') : '';
    let headers = {'appId': APP_ID, 'user_session': session};
    return headers;
}

const request = (url, params, method = 'GET', headerData) => {
    return new Promise((resolve, reject) => {
        let ajaxKey = getRequestKey(url, method, params, defaultHeader());
        if (hitRequestKey(ajaxKey)) {
            console.log('重复提交请求:', url);
            return Promise.reject('重复提交请求');
        }
        addRequestKey(ajaxKey);
        // region 请求前处理
        if (https.length === 0) {
            wx.showNavigationBarLoading();
        }
        https.push(url);
        let header;
        if (headerData) {
            header = Object.assign(headerData, defaultHeader());
        } else {
            header = defaultHeader();
        }
        // endregion
        wx.request({
            url: `${BASE_URL()}${url}`,
            method: method,
            header: header,
            data: method === 'GET' ? Object.assign({}, params, {
                t: new Date().getTime()
            }) : params,
            success: function (result) {
                console.log('request:', `${BASE_URL()}${url}`, '\nresponse:', result);
                if (result.statusCode === 200) {
                    // request success
                    resolve(result.data);
                } else {
                    // request fail TODO 错误代码在页面单独处理 cause 错误代码不一定需要弹 toast
                    const error = result || {};
                    if (error && error.header) {
                        const resMsg = error.header.msg ? error.header.msg : SERVICE_ERROR_STATUS[error.statusCode];
                        error.msg = resMsg || '接口调用失败';
                        // TODO 可以在此处判断异常错误码处理登出逻辑
                    }
                    reject(error);
                }
            },
            fail: function (err) {
                reject(err);
            },
            complete: function () {
                // region 请求完成ui处理
                removeRequestKey(ajaxKey);
                if (https.length === 1) {
                    wx.hideNavigationBarLoading();
                }
                https.splice(0, 1);
                // endregion
            }
        });
    });
};

exports.get = function (url, params = {}, headerData) {
    return request(url, params, 'GET', headerData);
};

exports.post = function (url, params = {}, headerData) {
    return request(url, params, 'POST', headerData);
};
