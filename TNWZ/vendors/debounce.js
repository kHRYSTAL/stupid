const FUNC_ERROR_TEXT = 'Expected a function';
const NAN = 0 / 0;
const symbolTag = '[object Symbol]';
const reTrim = /^\s+|\s+$/g;
const reIsBadHex = /^[-+]0x[0-9a-f]+$/i;
const reIsBinary = /^0b[01]+$/i;
const reIsOctal = /^0o[0-7]+$/i;
const freeParseInt = parseInt;
const objectProto = Object.prototype;
const objectToString = objectProto.toString;
let nativeMax = Math.max;
let nativeMin = Math.min;
let now = function () {
    return Date.now();
};

function debounce (func, wait, options) {
    let lastArgs;
    let lastThis;
    let maxWait;
    let result;
    let timerId;
    let lastCallTime;
    let lastInvokeTime = 0;
    let leading = false;
    let maxing = false;
    let trailing = true;

    if (typeof func !== 'function') {
        throw new TypeError(FUNC_ERROR_TEXT);
    }
    wait = toNumber(wait) || 0;
    if (isObject(options)) {
        leading = !!options.leading;
        maxing = 'maxWait' in options;
        maxWait = maxing ? nativeMax(toNumber(options.maxWait) || 0, wait) : maxWait;
        trailing = 'trailing' in options ? !!options.trailing : trailing;
    }

    function invokeFunc (time) {
        let args = lastArgs;
        let thisArg = lastThis;

        lastArgs = lastThis = undefined;
        lastInvokeTime = time;
        result = func.apply(thisArg, args);
        return result;
    }

    function leadingEdge (time) {
        lastInvokeTime = time;
        timerId = setTimeout(timerExpired, wait);
        return leading ? invokeFunc(time) : result;
    }

    function remainingWait (time) {
        let timeSinceLastCall = time - lastCallTime;
        let timeSinceLastInvoke = time - lastInvokeTime;
        let result = wait - timeSinceLastCall;

        return maxing ? nativeMin(result, maxWait - timeSinceLastInvoke) : result;
    }

    function shouldInvoke (time) {
        let timeSinceLastCall = time - lastCallTime;
        let timeSinceLastInvoke = time - lastInvokeTime;
        return (lastCallTime === undefined || (timeSinceLastCall >= wait) ||
            (timeSinceLastCall < 0) || (maxing && timeSinceLastInvoke >= maxWait));
    }

    function timerExpired () {
        var time = now();
        if (shouldInvoke(time)) {
            return trailingEdge(time);
        }
        timerId = setTimeout(timerExpired, remainingWait(time));
    }

    function trailingEdge (time) {
        timerId = undefined;
        if (trailing && lastArgs) {
            return invokeFunc(time);
        }
        lastArgs = lastThis = undefined;
        return result;
    }

    function cancel () {
        if (timerId !== undefined) {
            clearTimeout(timerId);
        }
        lastInvokeTime = 0;
        lastArgs = lastCallTime = lastThis = timerId = undefined;
    }

    function flush () {
        return timerId === undefined ? result : trailingEdge(now());
    }

    function debounced () {
        let time = now();
        let isInvoking = shouldInvoke(time);

        lastArgs = arguments;
        lastThis = this;
        lastCallTime = time;

        if (isInvoking) {
            if (timerId === undefined) {
                return leadingEdge(lastCallTime);
            }
            if (maxing) {
                timerId = setTimeout(timerExpired, wait);
                return invokeFunc(lastCallTime);
            }
        }
        if (timerId === undefined) {
            timerId = setTimeout(timerExpired, wait);
        }
        return result;
    }

    debounced.cancel = cancel;
    debounced.flush = flush;
    return debounced;
}

function isObject (value) {
    var type = typeof value;
    return !!value && (type === 'object' || type === 'function');
}

function isObjectLike (value) {
    return !!value && typeof value === 'object';
}

function isSymbol (value) {
    return typeof value === 'symbol' ||
        (isObjectLike(value) && objectToString.call(value) === symbolTag);
}

function toNumber (value) {
    if (typeof value === 'number') {
        return value;
    }
    if (isSymbol(value)) {
        return NAN;
    }
    if (isObject(value)) {
        var other = typeof value.valueOf === 'function' ? value.valueOf() : value;
        value = isObject(other) ? (other + '') : other;
    }
    if (typeof value !== 'string') {
        return value === 0 ? value : +value;
    }
    value = value.replace(reTrim, '');
    var isBinary = reIsBinary.test(value);
    return (isBinary || reIsOctal.test(value))
        ? freeParseInt(value.slice(2), isBinary ? 2 : 8)
        : (reIsBadHex.test(value) ? NAN : +value);
}

module.exports = debounce;
// export default debounce
