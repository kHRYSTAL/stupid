/**
 * 源码摘自Vue.js
 * 地址 https://github.com/vuejs/vue/blob/f865b0d7678beb70765ec8fd560008bebb51b923/packages/weex-vue-framework/factory.js#L2317
 */

let hookRE = /^hook:/;

let PubSub = function () {
    this._events = [];
};

PubSub.prototype.$on = function (event, fn) {
    let this$1 = this;

    let vm = this;
    if (Array.isArray(event)) {
        for (let i = 0, l = event.length; i < l; i++) {
            this$1.$on(event[i], fn);
        }
    } else {
        (vm._events[event] || (vm._events[event] = [])).push(fn);
        // optimize hook:event cost by using a boolean flag marked at registration
        // instead of a hash lookup
        if (hookRE.test(event)) {
            vm._hasHookEvent = true;
        }
    }
    return vm;
};

PubSub.prototype.$once = function (event, fn) {
    let vm = this;

    function on () {
        vm.$off(event, on);
        fn.apply(vm, arguments);
    }

    on.fn = fn;
    vm.$on(event, on);
    return vm;
};

PubSub.prototype.$off = function (event, fn) {
    var this$1 = this;

    var vm = this;
    // all
    if (!arguments.length) {
        vm._events = Object.create(null);
        return vm;
    }
    // array of events
    if (Array.isArray(event)) {
        for (var i = 0, l = event.length; i < l; i++) {
            this$1.$off(event[i], fn);
        }
        return vm;
    }
    // specific event
    var cbs = vm._events[event];
    if (!cbs) {
        return vm;
    }
    if (arguments.length === 1) {
        vm._events[event] = null;
        return vm;
    }
    if (fn) {
        // specific handler
        var cb;
        var i$1 = cbs.length;
        while (i$1--) {
            cb = cbs[i$1];
            if (cb === fn || cb.fn === fn) {
                cbs.splice(i$1, 1);
                break;
            }
        }
    }
    return vm;
};

PubSub.prototype.$emit = function (event) {
    let vm = this;

    let cbs = vm._events[event];
    if (cbs) {
        cbs = cbs.length > 1 ? toArray(cbs) : cbs;
        let args = toArray(arguments, 1);
        for (let i = 0, l = cbs.length; i < l; i++) {
            try {
                cbs[i].apply(vm, args);
            } catch (e) {
                console.log(e, vm, ('event handler for "' + event + '"'));
            }
        }
    }
    return vm;
};

export function toArray (list, start) {
    start = start || 0;
    let i = list.length - start;
    const ret = new Array(i);
    while (i--) {
        ret[i] = list[i + start];
    }
    return ret;
}

export default PubSub;
