/**
 * Toast通用函数 注意文字不要超过7个字 所有toast均显示loading图标
 */
function showToast (title = '正在加载', duration = 2000, mask = false, image = '', icon = 'loading') {
    wx.showToast({
        title: title,
        icon: icon,
        image: image,
        duration: (duration <= 0) ? 2000 : duration,
        mask: mask
    });
}

// 菊花图标
function showLoadingToast (title = '正在加载', duration = 2000, mask = false) {
    showToast(title, duration, mask, '', 'loading');
}

// 对号图标
function showSuccessToast (title = '提交成功', duration = 2000, mask = false) {
    showToast(title, duration, mask, '', 'success');
}

function hideToast () {
    wx.hideToast();
}

function showLoading (title = '加载中...', mask = true) {
    wx.showLoading({
        title: title,
        mask: mask
    });
}

function hideLoading () {
    wx.hideLoading();
}

module.exports = {
    showLoadingToast: showLoadingToast,
    hideToast: hideToast,
    showLoading: showLoading,
    hideLoading: hideLoading,
    showSuccessToast: showSuccessToast
};
