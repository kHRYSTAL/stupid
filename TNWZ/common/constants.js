/**
 * 此文件用户配置常量，如果没有特殊需求，所有常量都分段写在这里
 */
import {CHANNEL, ENVIRONMENT} from './config';

// APP_ID TODO
export const APP_ID = '';

/**
 * 格式化url占位符
 * eg: http://localhost:8080/user/{id}/name/{name}
 * urlFormat(url, {id: '111', name: 'yc'})
 * => http://localhost:8080/user/111/name/aa
 * @param url
 * @param param
 * @returns url
 */
export const urlFormat = (url, param) => {
    if (param === undefined || param === null || JSON.stringify(param) === '{}') {
        return url;
    }
    let keys = Object.keys(param);
    for (let key of keys) {
        url = url.replace(new RegExp('\\{' + key + '\\}', 'g'), param[key]);
    }
    return url;
};

/** 基础URL，方面切换开发环境和线上环境 TODO */
export function BASE_URL () {
    let channel = CHANNEL;
    if (channel === ENVIRONMENT.Dev) {
        return '';
    } else if (channel === ENVIRONMENT.Test) {
        return '';
    } else if (channel === ENVIRONMENT.Pre) {
        return '';
    } else if (channel === ENVIRONMENT.Product) {
        return '';
    } else if (channel === ENVIRONMENT.Mock) {
        return '';
    }
}

/** 上传图片 TODO */
export const API_UPLOAD_IMG = ``;

export const PAY_ERROR = {
    USER_NOT_LOGIN: 1,
    GET_PAY_OBJ_ERR: 2,
    WX_PAY_ERR: 3,
    GET_PAY_STATUS_ERR: 4,
    REPEAT_PURCHASES: 5
};

/** 全局缓存类型 - 数据缓存在storage中 */
export const GLOBAL_STORE_TYPE_STORAGE = 'store_type_storage';
/** 全局缓存类型 - 数据缓存在全局变量global中 */
export const GLOBAL_STORE_TYPE_OBJECT = 'store_type_object';
