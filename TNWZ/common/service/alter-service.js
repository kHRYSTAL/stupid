function showAlertModel (params) {
    return new Promise(function (resolve, reject) {
        wx.showModal(Object.assign(params, {success: resolve, fail: reject}));
    });
}

module.exports = {
    showAlertModel: showAlertModel
};
