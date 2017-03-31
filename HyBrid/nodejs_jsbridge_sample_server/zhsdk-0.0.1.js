(function(global, callback){//向客户端发送请求 注入jsBridge
    if (global.WebViewJavascriptBridge) {
        return callback(WebViewJavascriptBridge);
    }
    if (global.WVJBCallbacks) {
        return global.WVJBCallbacks.push(callback);
    }
    //ready event alpha
    global.ZHHybridCallbacks = [];
    global.zh = {
        ready:function(handler){
            global.ZHHybridCallbacks.push(handler);
        }
    };

    global.WVJBCallbacks = [callback];

    //此段外层setTimeout 影响浏览器后退事件
    //bug描述：未加时 浏览器后退动作会导致scheme请求被取消 jsBridge不被注入
    //修复原因：未知
    setTimeout(function(){
        var WVJBIframe = document.createElement('iframe');
        WVJBIframe.style.display = 'none';
        WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
        document.documentElement.appendChild(WVJBIframe);
        setTimeout(function() {
            document.documentElement.removeChild(WVJBIframe)
        }, 0)
    }, 0);
})(typeof window !== "undefined" ? window : this, function(bridge){
    (function(global, factory) {//UMD加载模块
        typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
        typeof define === 'function' && define.amd ? define(factory) :
        (global.zh = factory(global));
        //ready event alpha
        if(global.ZHHybridCallbacks.length > 0){
            for (var i = 0; i < global.ZHHybridCallbacks.length; i++) {
                global.ZHHybridCallbacks[i]()
            }
        }
    }(typeof window !== "undefined" ? window : this, (function(global) {
        'use strict';

        // var _bridge = window.WebViewJavascriptBridge;

        /**
         * [ready 构建完成时]
         * @param  {Function} callback 完成后回调函数
         */
         function ready (handler){
             handler();
             // if(x){
                //  if(window.zh){
                //      handler();
                //  }else{
                //      if(document.addEventListener){
                //          document.addEventListener("ZHHybridReady", handler, false);
                //      }
                //  }
             // }
         }


         // Browser environment sniffing
        var inBrowser =
          typeof window !== 'undefined' &&
          Object.prototype.toString.call(window) !== '[object Object]';

        var UA = inBrowser && window.navigator.userAgent.toLowerCase();
        var isIE = UA && /msie|trident/.test(UA);
        var isIE9 = UA && UA.indexOf('msie 9.0') > 0;
        var isEdge = UA && UA.indexOf('edge/') > 0;
        var isAndroid = UA && UA.indexOf('android') > 0;
        var isIOS = UA && /iphone|ipad|ipod|ios/.test(UA);

        /* istanbul ignore next */
        function isNative (Ctor) {
          return /native code/.test(Ctor.toString())
        }

        /**
         * [_formatParam description]
         * @param  {[type]} param [待处理object]
         * @return {[object]}     [返回处理后的param 为以后非同源处理param做准备]
         */
        function _formatParam(param){

            if(typeof param === 'object'){
                for(var key in param){
                    param[key] = _formatParam(param[key]);
                }
                param = JSON.stringify(param);
            }
            else if(typeof param === 'string'){
                param = encodeURI(param);
            }
            else if(typeof param !== 'function'){
                param = param.toString();
            }

            return param;
        }

        /*test _formatParam*/
        // var a = {
        //     str: " 换服dd",
        //     b: {
        //         bstr: "换地方sss"
        //     }
        // }
        // console.log(a);
        // console.log("------------");
        // console.log(_formatParam(a));
        // console.log("------------");
        // console.log(JSON.stringify(_formatParam(a)));
        // console.log("------------");
        // console.log("结果 tpye = string ",typeof JSON.stringify(_formatParam(a)));

        function error(txt){
            console.error("[ZHHybrid] "+txt);
        }

        /**
         * [_registerHandler description]
         * @param  {[type]} name [description]
         * @param  {[type]} func [description]
         * @return {[type]}      [description]
         */
        function _registerHandler(name, func){
            bridge.registerHandler(name, function(data, responseCallback) {
                func();
                responseCallback();
            })
        };

        /**
         * [_callHandler 封装底层bridge调用]
         */
        function _callHandler(taskName, param, callback){
            if(!(taskName && typeof taskName === "string")){
                error("_callHandler() 缺少参数");
                return;
            }

            var config = {
                taskId : taskName,
            };

            if(!!param){
                config["param"] = _formatParam(param);
            }

            bridge.callHandler('NativeHybrid', config, function(response){
                if(typeof callback === "function"){
                    callback(response);
                }
            });
        };

        /**
         * [titleBar description]
         * @param  {[type]} config
         *     {
         *      backgroundColor {String} : '0xFFFFFF' or '#FFFFFF',
         *      text {String} : "",
         *      fontSize {Number} : 12,
         *      color {String} : '0xFFFFFF' or '#FFFFFF',
         *     }
         */
        function setNavigationBar (config){
            if(config.backgroundColor){
                _callHandler('zhhybrid/titleBar/background/color',config.backgroundColor);
            }
            if(config.text){
                _callHandler('zhhybrid/titleBar/title/text',config.text);
            }
            if(config.fontSize){
                _callHandler('zhhybrid/titleBar/title/fontSize',config.fontSize);
            }
            if(config.color){
                _callHandler('zhhybrid/titleBar/title/textColor',config.color);
            }
        }

        /**
         * [setNavigationBarTitle 设置导航栏标题]
         * @param {String}   text     [标题文本]
         * @param {Function} callback [设置完成后的回调]
         */
        function setNavigationBarTitle (text, callback){
            _callHandler('zhhybrid/titleBar/title/text', text, callback);
        }
        /**
         * [setNavigationBarColor 设置导航栏字体颜色]
         * @param {String}   color    [颜色字符串 格式为‘0xFFFFFF’ 或 '#FFFFFF']
         * @param {Function} callback [设置完成后的回调]
         */
        function setNavigationBarColor (color, callback){
            _callHandler('zhhybrid/titleBar/title/textColor', color, callback);
        }
        /**
         * [setNavigationBarFontSize 设置导航栏字号]
         * @param {Number}   fontSize [字号]
         * @param {Function} callback [设置完成后的回调]
         */
        function setNavigationBarFontSize (fontSize, callback){
            _callHandler('zhhybrid/titleBar/title/fontSize', fontSize, callback);
        }
        /**
         * [setNavigationBarBackgroundColor 设置导航栏背景颜色]
         * @param {[type]}   backgroundColor [颜色字符串 格式为‘0xFFFFFF’ 或 '#FFFFFF']
         * @param {Function} callback        [设置完成后的回调]
         */
        function setNavigationBarBackgroundColor (backgroundColor, callback){
            _callHandler('zhhybrid/titleBar/background/color', backgroundColor, callback);
        }

        /**
         * _createMenuButtonEventName 为button 创建固定格式的毁掉函数名
         */
        function _createMenuButtonEventName(txt){
            txt = encodeURI(txt).replace(/%/g,"");//替换中文编码
            return '_'+txt+'_Callback';
        }

        /**
         * [createMenuButton navigationBar增加导航按钮]
         * @param  {String}   buttonName      [button下方文本]
         * @param  {Function} callback        [正确执行后的回调]
         * @param  {Function} createdCallback [创建button完成后的回调]
         */
        function createMenuButton (buttonName, callback, createdCallback){
            var config = {
                txt : buttonName,
                handlerName : _createMenuButtonEventName(buttonName),
            }

            _registerHandler(config.handlerName, callback);//注册回调函数
            _callHandler('zhhybrid/titleBar/button', config, createdCallback);
        }

        /**
         * [refreshButton 定义一个刷新功能的menuButton //test]
         */
        function showRefreshButton (){
            createMenuButton ('刷新', function(){
                location.replace("");
            })
        }

        /**
         * [navigateTo 保留当前页面，在新层中打开新页面。
         * 注意：调用 navigateTo 跳转时，调用该方法的页面会被加入堆栈，而 redirectTo 方法则不会。]
         * @param  {object} config [url值为有效的http地址]
         *       {
         *          url {String} : "",
         *       }
         */
        function navigateTo (config){
            _callHandler('zhhybrid/navigator/openGroup', config);
        }

        /**
         * [redirectTo 替换当前页面，打开新页面]
         * @param  {object} config
         *       {
         *          url {String} : "",
         *       }
         */
        function redirectTo (config){
            _callHandler('zhhybrid/navigator/openPage', config);
        }

        /**
         * [navigateBack 关闭当前多级页面]
         */
        function navigateBack (){
            _callHandler('zhhybrid/navigator/closeCurGroup');
        }

        /**
         * [redirectTo 后退当前页面]
         */
        function redirectBack (){
            _callHandler('zhhybrid/navigator/closeCurPage');
        }



        var ZHHybrid = {
            ready : ready,
            //导航栏相关api
            setNavigationBar : setNavigationBar,
            setNavigationBarTitle : setNavigationBarTitle,
            setNavigationBarColor : setNavigationBarColor,
            setNavigationBarFontSize : setNavigationBarFontSize,
            setNavigationBarBackgroundColor : setNavigationBarBackgroundColor,
            //导航相关api
            navigateTo : navigateTo,
            navigateBack : navigateBack,
            redirectTo : redirectTo,
            redirectBack : redirectBack,
            //预设menuButton api
            showRefreshButton : showRefreshButton,
        }

        return ZHHybrid;
    })));
});
