import PubSub from '../vendors/pubsub'
/**
 * 调用方式:
 * 绑定监听
 * eventBus.$on('key', function)
 * 解绑监听
 * eventBus.$off('key', function)
 * 监听一次
 * eventBus.$once('key', function)
 * 发射事件
 * eventBus.$emit('key', data)
 */

exports.eventBusRegister = new PubSub();

exports.eventBusGetter = () => {
    return getApp().globalData.eventbus || {};
};
