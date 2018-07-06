import { setStorage, setStorageSync, getStorage, getStorageSync, removeStorageSync } from '../common/service/storage-service';
import {
    GLOBAL_STORE_TYPE_STORAGE,
    GLOBAL_STORE_TYPE_OBJECT
} from '../common/constants';

/**
 * 获取或修改全局数据。全局数据的存放方式，根据实际使用情况调整。
 * 如果存储的数据是引用类型，只会存储引用类型的指针
 * @param {string} key 全局数据的唯一标示
 * @param {any} value 存储在全局的数据内容，可以是任何数据类型。
 * @param {object} opts 处理数据的参数
 */
export const globalData = (key, value) => {
    if (!key) {
        throw Error('缺少获取全局数据的key');
    };

    const app = getApp();
    if (value) {
        // 设置全局数据，如果key已经存在，直接覆盖原有数据
        app.globalData[key] = value;
    } else {
        // 根据key获取全局数据
        return app.globalData[key];
    }
};

/**
 * 向全局数据缓存添加数据
 * @param {string} key 数据的唯一key
 * @param {any} value 数据实体
 * @param {object} opts 操作选项。
 *          storageType：全局缓存位置storage或globalData，默认：globalData。
 *          isSync: 是否使用同步模式, 异步模式全部返回Promise， 默认：false
 */
export const setGlobalStore = (key, value, opts = {}) => {
    const defaultOpts = {
        storageType: GLOBAL_STORE_TYPE_OBJECT,
        isSync: false
    };

    opts = Object.assign({}, defaultOpts, opts);

    if (opts.storageType === GLOBAL_STORE_TYPE_OBJECT) {
        if (opts.isSync === false) {
            return new Promise((resolve, reject) => {
                try {
                    return resolve(globalData(key, value));
                } catch (e) {
                    reject(e);
                }
            });
        } else {
            return globalData(key, value);
        }
    } else if (opts.storageType === GLOBAL_STORE_TYPE_STORAGE) {
        if (opts.isSync === false) {
            return setStorage(key, value);
        } else {
            return setStorageSync(key, value);
        }
    }
};

/**
 * 获取全局数据缓存数据
 * @param {*} key 数据的唯一key
 * @param {*} opts 操作选项
 * @return Promise
 */
export const getGlobalStore = (key, opts = {}) => {
    const defaultOpts = {
        storageType: GLOBAL_STORE_TYPE_OBJECT,
        isSync: false
    };

    opts = Object.assign({}, defaultOpts, opts);

    if (opts.storageType === GLOBAL_STORE_TYPE_OBJECT) {
        if (opts.isSync === false) {
            return new Promise((resolve, reject) => {
                try {
                    return resolve(globalData(key));
                } catch (e) {
                    reject(e);
                }
            });
        } else {
            return globalData(key);
        }
    } else if (opts.storageType === GLOBAL_STORE_TYPE_STORAGE) {
        if (opts.isSync === false) {
            return getStorage(key);
        } else {
            return getStorageSync(key);
        }
    }
};

export const removeGlobalStore = (key, opts = {}) => {
    const defaultOpts = {
        storageType: GLOBAL_STORE_TYPE_OBJECT,
        isSync: false
    };

    opts = Object.assign({}, defaultOpts, opts);

    if (opts.storageType === GLOBAL_STORE_TYPE_OBJECT) {
        let globalData = getApp().globalData;
        if (opts.isSync === false) {
            return new Promise((resolve, reject) => {
                try {
                    delete globalData[key];
                    return resolve(true);
                } catch (e) {
                    reject(e);
                }
            });
        } else {
            return delete globalData[key];
        }
    } else if (opts.storageType === GLOBAL_STORE_TYPE_STORAGE) {
        if (opts.isSync === false) {
            return removeStorageSync(key);
        } else {
            return removeStorageSync(key);
        }
    }
};
