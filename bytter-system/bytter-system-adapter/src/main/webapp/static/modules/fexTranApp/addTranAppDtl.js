var form, corp, dateType, dicts, selectType;
bt.use(["jquery", "form", "datatable", "corp", "dateType", "dicts", "selectType"], function () {
    $ = bt.jquery;
    form = bt.form();


    corp = bt.corp.init();//单位
    dateType = bt.dateType.init();
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    //这里填写新的js代码，请参考前端文档


    // 模块选择
    $('#templateBtn').click(function () {
        parent.layer.open({
            title:'选择模版',
            type: 2,
            area: ['1000px', '700px'],
            maxmin: true,
            content: basePath + '/foreign/fexTranConfigTemplate/fexConfigTemplateChoose',
        });
    })

    // 清空模块功能
    $('.layui-clear-template .layui-btn').click(function () {
        var templateId = $('#templateId').val();
        if(templateId){
            $('#templateName').val('').attr('title','')
            $('#versionNo').val('')



            var currentTabWin = bt.navtab.getCurrentDom();
            if(currentTabWin){
               currentTabWin.window.frames["tranDtl"].cleanTemplate();
            }


        }
    })
});

// 根据配置更新表单
function updateFormByConfig(row) {
    var templateName = row.templateName
    var id = row.id;
    $('#templateName').val(templateName).attr('title',templateName)
    $('#templateId').val(id)


    // 资金收付
    $.each($("div[name='isSwaptTran']"),function (index,item) {
        $(this).css("display", "none");
    })
    dicts.get("isSwaptTran").attr({"data-value":"0", "data-required": false});

    // 外汇买卖 - 显示配置模板，隐藏产品
    $("#proLabelpl").hide();
    $("#templateLabel").show();
    $("#proDiv").hide();
    $("#templateDiv").show();
/*    selectType.get("templateId").attr({
        "data-value": "",
        "data-url": basePath + "/foreign/fexTranConfigTemplate/configTemplateList"
    });*/

/*    var tranType = dicts.get("tranType").val();
    var isSwaptTran = dicts.get("isSwaptTran").val();
    var templateId = $("#templateId").val();
    var corpId = $("#corpId").val();
    var dtlTmpId = $("#dtlTmpId").val();
    var url = "";
    if (isNotEmpty(isSwaptTran)) {
        url = basePath + "/foreign/fexTranApp/addTranDtl?tranType=" + tranType + "&proId="  + "&corpId=" + corpId + "&isSwaptTran=" + isSwaptTran + "&id=" + dtlTmpId+ "&templateId=" + templateId;
    } else {
        url = basePath + "/foreign/fexTranApp/addTranDtl?tranType=" + tranType + "&proId="  + "&corpId=" + corpId + "&id=" + dtlTmpId+ "&templateId=" + templateId;
    }
    $("#tranDtl").attr("src", url);*/
}


function setData(data) {
    window.frames['tranDtl'].externalBankAccCallback(data)
}

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " ") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}

/**
 * 根据交易类型获取产品
 * @param data
 * @param text
 * @param selectedData
 * @param beforeValue
 */
function getPro(data,text,selectedData,beforeValue) {
    if (selectedData.value == 1) {
        // 外汇买卖
        $.each($("div[name='isSwaptTran']"),function (index,item) {
            $(this).css("display", "");
        })
        dicts.get("isSwaptTran").attr({"data-value":"0", "data-required": true});

        $("#proLabelpl").show();
        $("#templateLabel").hide();
        $("#proDiv").show();
        $("#templateDiv").hide();
        selectType.get("proId").attr({
            "data-value": "",
            "data-url": basePath + "/foreign/fexproduct/listProduct/" + selectedData.value
        });
    } else if (selectedData.value == 4) {
        // 资金收付
        $.each($("div[name='isSwaptTran']"),function (index,item) {
            $(this).css("display", "none");
        })
        dicts.get("isSwaptTran").attr({"data-value":"0", "data-required": false});

        // 外汇买卖 - 显示配置模板，隐藏产品
        $("#proLabelpl").hide();
        $("#templateLabel").show();
        $("#proDiv").hide();
        $("#templateDiv").show();
     /*   selectType.get("templateId").attr({
            "data-value": "",
            "data-url": basePath + "/foreign/fexTranConfigTemplate/configTemplateList"
        });
*/
        addTranDtl();

    }else {
        $("#proLabelpl").show();
        $("#templateLabel").hide();
        $("#proDiv").show();
        $("#templateDiv").hide();
        selectType.get("proId").attr({
            "data-value": "",
            "data-url": basePath + "/foreign/fexproduct/listProduct/" + selectedData.value
        });
    }

    selectType.render();
    dicts.render();
    form.renderIcon();
}

/**
 *  返回产品对应页面
 * @param data
 * @param text
 * @param selectedData
 * @param beforeValue
 */
function addTranDtl() {
    var tranType = dicts.get("tranType").val();
    var isSwaptTran = dicts.get("isSwaptTran").val();
    var proId = selectType.get("proId").val();
    var templateId = $("#templateId").val();
    var corpId = $("#corpId").val();
    var dtlTmpId = $("#dtlTmpId").val();
    var url = "";
    if (isNotEmpty(isSwaptTran)) {
        url = basePath + "/foreign/fexTranApp/addTranDtl?tranType=" + tranType + "&proId=" + proId + "&corpId=" + corpId + "&isSwaptTran=" + isSwaptTran + "&id=" + dtlTmpId+ "&templateId=" + templateId;
    } else {
        url = basePath + "/foreign/fexTranApp/addTranDtl?tranType=" + tranType + "&proId=" + proId + "&corpId=" + corpId + "&id=" + dtlTmpId+ "&templateId=" + templateId;
    }
    $("#tranDtl").attr("src", url);
}

/**
 * 关闭当前标签页
 * @param id
 */
function closeTab(data) {
    var webObj = bt.navtab.getParentDom();
    webObj.setDtlIds(data.obj.id);
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
}


/*function getData() {
    var data={
        bifId:$("#bifId").val(),
        bankTypeId:$("#bankTypeId").val(),
        bankCode:$("#bankCode").val(),
        isDomesticBank:$("#isDomesticBank").val(),
    };
    return data;

}*/

