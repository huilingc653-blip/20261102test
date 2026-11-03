var form, element,countFlag = 0;
bt.use(["jquery", "form","element"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element();
//监听提交
    form.on("submit(submit)", function (data) {
        bt.loading.show();
        // 提交近端交易子页面
        var data1 = $("#swapIframe1")[0].contentWindow.subData();
        if (typeof data1 == "boolean") {
            bt.loading.hide();
            return false;
        }
        // 提交远端交易子页面
        var data2 = $("#swapIframe2")[0].contentWindow.subData();
        if (typeof data2 == "boolean") {
            bt.loading.hide();
            return false;
        }
        // 提交近端交易数据
        var sub1 = subData(data1);
        // 提交远端交易数据
        var sub2 = subData(data2);
        var updateinfo = updateParner(sub1.obj.id, sub2.obj.id);
        if (!updateinfo.success) {
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            bt.loading.hide();
            return false;
        }
        bt.loading.hide();
        parent.closeTab(updateinfo);
        return false;
    });
    //这里填写新的js代码，请参考前端文档
});

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}


/**
 * 编辑时更新Parner信息
 * @param id1
 * @param id2
 * @returns {*}
 */
function updateParner(id1,id2) {
    var url = basePath + "/foreign/fexTranApp/editUpdateParner";
    var result = null;
    $.ajax({
        url: url,
        data:{
            "id1": id1,
            "id2": id2
        },
        type:"post",
        dataType:"json",
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}

/**
 * 返回主页面信息
 * @returns {*}
 */
function getMainInfo() {
    return parent.getMainInfo();
}


/**
 * 提交数据
 * @param data
 */
function subData(data) {
    var returnData = null;
    var action = "/foreign/fexTranApp/saveDtl";
    $.ajax({
        url: basePath + action,
        type: "post",
        data: data,
        dataType: "json",
        async: false,
        success: function (result) {
            if (result != null) {
                returnData = result;
            }
        }
    });
    return returnData;
}

/**
 *掉期交易数据复制
 */
function getCopyData() {
    return $("#swapIframe1")[0].contentWindow.getCopyData()
}