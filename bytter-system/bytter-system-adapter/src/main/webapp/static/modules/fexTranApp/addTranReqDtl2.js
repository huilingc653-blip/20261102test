var form, element, count = 0;
bt.use(["jquery", "form","element"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element();

//监听提交
    form.on("submit(submit)", function (data) {
        bt.loading.show();
        var data1 = $("#swapIframe1")[0].contentWindow.getData();
        if (typeof data1 == "boolean") {
            showVerifyFailTab(1);
            // 显示校验的相关信息
            $("#swapIframe1")[0].contentWindow.showVerifyMsg()
            bt.loading.hide();
            return false;
        }
        var data2 = $("#swapIframe2")[0].contentWindow.getData();
        if (typeof data2 == "boolean") {
            showVerifyFailTab(2);
            // 显示校验的相关信息
            $("#swapIframe2")[0].contentWindow.showVerifyMsg()
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
        var obj = {};
        obj.id = sub1.obj.id + "," + sub2.obj.id;
        updateinfo.obj = obj;
        bt.loading.hide();
        parent.closeTab(updateinfo);
        return false;
    });
    //这里填写新的js代码，请参考前端文档
});

// 显示校验失败的页签
function showVerifyFailTab(layId) {
    // 如果指定的layid没有显示
    var $tab = $('[lay-id="'+layId+'"]')
    if(!$tab.hasClass('layui-this')){
        $tab.addClass('layui-this');
        $tab.siblings('li').removeClass('layui-this');
        $(".layui-tab-item").removeClass('layui-show');
        $("#tabItem"+layId).addClass('layui-show');
    }
}

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 新增时更新Parner信息
 * @param id1
 * @param id2
 * @returns {*}
 */
function updateParner(id1,id2) {
    var url = basePath + "/foreign/fexTranApp/addUpdateParner";
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
 * 提交数据
 * @param data
 */
function subData(data) {
    var returnData = null;
    var action = "/foreign/fexTranApp/saveDtlTmp";
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
    return $("#swapIframe1")[0].contentWindow.getCopyData();
}
