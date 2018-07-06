class Router {
    constructor () {
        this._routemap = [];
    }

    register (path, action) {
        this._routemap.push({
            uri: new RegExp(path, 'i'),
            func: action
        });
    }

    execute (uri, openType, from) {
        for (let routeIdx in this._routemap) {
            let route = this._routemap[routeIdx];
            let matchResult = uri.match(route.uri);
            if (matchResult) {
                let matchParam = matchResult.slice(1);
                // 小程序内部uri拼接, 打开方式可以定义未redirect 或 navigateTo
                // 默认为navigateTo
                // 参考具体场景下的业务逻辑
                if (openType !== undefined) {
                    matchParam = matchParam.concat(openType);
                }
                if (from !== undefined) {
                    matchParam = matchParam.concat(from);
                }
                route.func.apply(this, matchParam);
                return;
            }
        }
        console.log(uri, 'not match router map func');
    }
}

exports.routerRegister = new Router();

exports.routerGetter = () => {
    return getApp().globalData.router || {};
};
