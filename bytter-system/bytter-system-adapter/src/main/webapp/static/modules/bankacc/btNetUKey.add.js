bt.use(['form','dateType','corp','selectType','dicts','btUpload','selectTable','mulSelectUserPage',
    'mulViewUserPage'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.corp.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        data.field.attachmentId = up.getGroupId();
        var action = "/bankacc/btnetukey/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshParent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

    bt.selectTable.init({
        elem: "#custodianId",
        url: basePath + "/sys/user/listAllUserByUnDelete",
        searchInput: ".custodianId",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "loginName",},
            {data: "username",},
        ],
        clickCallback: function (data, $tr) {
            $(".custodianId").val(data.username);
            $("input[name='custodianId']").val(data.id);
        },
    })

    bt.selectTable.init({
        elem: "#uKeyUserId",
        url: basePath + "/sys/user/listAllUserByUnDelete",
        searchInput: ".uKeyUserId",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "loginName",},
            {data: "username",},
        ],
        clickCallback: function (data, $tr) {
            $(".uKeyUserId").val(data.username);
            $("input[name='ukeyUserId']").val(data.id);
        },
    })

    bt.selectTable.init({
        elem: "#supervisorId",
        url: basePath + "/sys/user/listAllUserByUnDelete",
        searchInput: ".supervisorId",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "loginName",},
            {data: "username",},
        ],
        clickCallback: function (data, $tr) {
            $(".supervisorId").val(data.username);
            $("input[name='supervisorId']").val(data.id);
        },
    })

    bt.permission = function (data) {
        var personalLimit = select.get("personalLimit");
        if(data.value === "1"){
            select.get("personalLimit").attr({
                'data-filterData':'',
                'data-disabled':'true',
                'data-defaultvalue':'1'
            });
        }else{
            select.get("personalLimit").attr({
                'data-filterData':'1',
                'data-disabled':'',
                'data-defaultvalue':'4'
            });
        }
        select.render()
        personalLimit.render()
        form.render()
    }

    var mulSelect = bt.mulSelectUserPage.init();
    var viewInfo = bt.mulViewUserPage.init();
    bt.selectInfo = function () {
        var corp = bt.corp.get("corpId").val();
        var bankType = selectType.get("bankType").val();
        if(bt.empty(corp)){
            layer.msg(bt.lang('nis.pay.message.corp.null'));
            return false;
        }
        if(bt.empty(bankType)){
            layer.msg(bt.lang('cms.provide.loaninfo.bankIdnull'));
            return false;
        }

        mulSelect.set({
            "corpId": corp,
            "bankTypeId": bankType,
        });
        return true;
    }
    bt.viewBank = function () {
        viewInfo.set({"id": $("input[name='bankAccIds']").val()});
        return true;
    }
    bt.selectData = function (datas, rom) {
        var bankAccId = "";
        $.each(datas.data, function (index, data) {
            if (bankAccId == "") {
                bankAccId += data.id;
            } else {
                bankAccId += "," + data.id;
            }
        });
        $("input[name='bankAccIds']").val(bankAccId);
    }
    var selectType = bt.selectType.init();
    var select = bt.dicts.init();
})

/**判断字符串是否为空*/
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}

function resetCallback() {
    window.location.reload();
}