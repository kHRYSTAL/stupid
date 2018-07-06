import {API_UPLOAD_IMG} from '../common/constants';

export default class Upload {
    constructor () {
        this._imagePics = [];
    }

    /**
     * 选择图片并执行上传操作
     * @param maxCount 选择图片最大个数
     * @param selectCb 选中图片后的回调
     * @param uploadCb 上传图片成功失败的回调
     */
    chooseImageUpload (maxCount, selectCb, uploadCb) {
        let that = this;
        wx.chooseImage({
            count: maxCount, // 默认9
            sizeType: ['original'], // 可以指定是原图还是压缩图，默认二者都有 'compressed' 'original'
            sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
            success: function (res) {
                if (res.tempFilePaths && res.tempFilePaths.length > 0) {
                    that._imagePics = res.tempFilePaths;
                    if (selectCb) {
                        selectCb(that._imagePics);
                    }
                    that._uploadFile({
                        url: API_UPLOAD_IMG,
                        path: res.tempFilePaths,
                        selectCb: selectCb,
                        uploadCb: uploadCb
                    });
                }
            }
        });
    }

    _uploadFile (data) {
        console.log(data);
        let that = this;
        let i = data.i ? data.i : 0;
        let success = data.success ? data.success : 0;
        let fail = data.fail ? data.fail : 0;
        let selectCb = data.selectCb;
        let uploadCb = data.uploadCb;
        console.log('开始上传文件');
        wx.uploadFile({
            url: data.url,
            filePath: data.path[i],
            name: new Date().getMilliseconds().toString(),
            formData: null,
            success: (res) => {
                console.log(res);
                if (res.statusCode === 200) {
                    let data = JSON.parse(res.data);
                    console.log('上传文件返回data:', data);
                    success++;
                    console.log('第' + i + '个文件上传成功, 成功数' + success);
                    if (data.result) {
                        that._imagePics.splice(i, 1, data.data);
                        uploadCb({index: i, success: true, imgUrl: data.data});
                    } else {
                        console.log(data.msg);
                        uploadCb({index: i, success: false});
                    }
                } else {
                    fail++;
                    console.log('第' + i + '个上传非200, 失败数' + fail);
                    that._imagePics.splice(i, 1);
                    uploadCb({index: i, success: false});
                }
            },
            fail: (err) => {
                fail++;
                console.log('第' + i + '张上传失败, 失败数' + fail);
                console.log(err);
                that._imagePics.splice(i, 1);
                uploadCb({index: i, success: false});
            },
            complete: () => {
                i++;
                if (i >= data.path.length) { // 当图片传完时停止调用
                    console.log('执行完毕');
                    console.log('成功：' + success + ' 失败：' + fail);
                    if (selectCb) {
                        selectCb(that._imagePics);
                    }
                } else { // 若图片还没有传完，则继续调用函数
                    data.i = i;
                    data.success = success;
                    data.fail = fail;
                    that._uploadFile(data);
                }
            }
        });
    }

    /**
     * 调整图片宽高
     * @param event 从wxml的image标签中获取的原始图片宽高
     * @returns 按照屏幕宽高比例对图片进行宽高适配后的图片宽高
     */
    imageResize (event) {
        let originalWidth = event.detail.width;
        let originalHeight = event.detail.height;
        let windowWidth = 0;
        let result = {};
        wx.getSystemInfo({
            success (res) {
                windowWidth = res.windowWidth;
                // 按照iphone6比例进行宽高缩放
                let ratio = 375 / windowWidth;
                result.width = originalWidth / ratio;
                result.height = originalHeight / ratio;
            }
        });
        return result;
    }
}
