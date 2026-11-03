var form, moneyType;
bt.use(["jquery", "form","btUpload","moneyType", "conView"], function () {
    $ = bt.jquery;
    form = bt.form();
    moneyType = bt.moneyType.init();
    conView = bt.conView.init();//合同查看组件
    var gid = $("#infoAttachmentId").val();
    var abolishAttachmentId = $("#abolishAttachmentId").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        hasUpload: false,
        hasDelete: false,
        isExpand: true,
        auto: false,
        gid: gid
    });

    var abolishUpload = bt.btUpload.init({
        elem: "#abolishUploader",
        isExpand: true,
        auto: false,
        gid: abolishAttachmentId
    });

    form.on("submit(submit)", function (data) {
        var action = "/inl/abolish/save";
        bt.loading.show();
        data.field.status = 11;
        data.field.attachmentId = abolishUpload.getGroupId();
        jQuery.ajaxSettings.traditional = true;
        $.post(basePath + action, data.field, function (data) {
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            if (data.success) {
                if (!bt.navtab) {
                    bt.navtab.refreshParentTable();
                } else {
                    bt.navtab.closeCurrent();
                }
            } else {
                bt.loading.hide();
            }
        }, 'JSON');
        return false;
    });
});


//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}


/**
 * 设置授信参数
 * @returns {{beginDate, corpId: *, ratItemCode, curCode: *}}
 */
function beforeCpRation(){
    var borrowInCorp = $("#borrowInCorp").val();
    var curId = $("#curId").val();
    var cpRatItemCode = $("#cpRatItemCode").val();
    var startDate = $("#startDate").val();
    var params = {
        corpId: borrowInCorp,
        curCode: curId,
        ratItemCode: cpRatItemCode,
        beginDate: startDate
    };
    return params;
}