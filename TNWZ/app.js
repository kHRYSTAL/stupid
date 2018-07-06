import {eventBusRegister, eventBusGetter} from './utils/eventbus';
import {routerRegister} from './utils/router';
import qcloud from './vendors/wafer2-client-sdk/index';

App({
    appData: {
        tunnelStatus: 'close',//统一管理唯一的信道连接的状态：connect、close、reconnecting、reconnect、error
        friendsFightingRoom: undefined,//好友对战时创建的唯一房间名,作为好友匹配的标识
    },
    onLaunch(options) {
        console.log('=====App====onLaunch============', JSON.stringify(options));
        // TODO 执行微信用户登录授权操作
        // TODO 如果有Router 需要执行注册回调
    },
    onShow(options) {
        console.log('=====App====onShow============', JSON.stringify(options));
    },
    tunnelCreate() { // 创建一个信道, 监听相关数据变化
        const that = this;
        // TODO 配置信道服务器地址
        const tunnel = that.tunnel = new qcloud.Tunnel('')
        tunnel.open();
        tunnel.on('connect', () => { // 监听信道连接
            this.appData.tunnelStatus = 'connect'; //改变信道状态为已连接
            if (that.tunnelConnectCallback) {//设置回调
                that.tunnelConnectCallback();
            }
        });
        tunnel.on('close', () => {//监听信道断开
            console.info("tunnelStatus = 'close'");
            this.appData.tunnelStatus = 'close'; //改变信道状态为已断开
            if (that.tunnelCloseCallback) {//设置回调
                that.tunnelCloseCallback();
            }
        });
        tunnel.on('reconnecting', () => {//监听信道重新链接
            console.info("tunnelStatus = 'reconnecting'");
            this.appData.tunnelStatus = 'reconnecting'; //改变信道状态为重新连接中
            if (that.tunnelReconnectingCallback) {//设置回调
                that.tunnelReconnectingCallback()
            }
        });
        tunnel.on('reconnect', () => {//监听信道重新连接成功
            console.info("tunnelStatus = 'reconnect'");
            console.info('重连后的信道为:' + tunnel.socketUrl.slice(tunnel.socketUrl.indexOf('tunnelId=') + 9, tunnel.socketUrl.indexOf('&')))
            this.appData.tunnelStatus = 'reconnect'; //改变信道状态为重新连接成功
            if (that.tunnelReconnectCallback) {//设置回调
                that.tunnelReconnectCallback();
            }
        });
        tunnel.on('error', () => {//监听信道发生错误
            console.info("tunnelStatus = 'error'");
            this.appData.tunnelStatus = 'error' //改变信道状态为发生错误
            console.log('您已断线，请检查联网');
            // TODO 返回首页
            if (that.tunnelErrorCallback) {//设置回调
                that.tunnelErrorCallback()
            }
        });
        tunnel.on('PING', () => {//PING-PONG机制:监听服务器PING
            console.info("接收到PING");
            tunnel.emit('PONG', {//给出回应
                openId: this.appData.openId
            });
            console.info("发出了PONG");
        });
    },
    globalData: {
        eventbus: eventBusRegister,
        router: routerRegister
    }
});
