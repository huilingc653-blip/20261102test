var dicts, selectType, form;
bt.use(['form', 'dicts', 'element',"selectType"], function () {
    $ = bt.jquery, form = bt.form();

    dicts = bt.dicts.init();
    selectType = bt.selectType.init();

    form.on("submit(submit)", function (data) {
        bt.loading.show();
        $.post(basePath + "/bill/intellect/element/update", data.field, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.navtab.refreshParent();
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                }
            }
            bt.loading.hide();
        }, 'JSON');
    });
});

/**
 * 数据字典回调
 * @param value
 * @param el
 */
function sourceTypeCallBack(el, text,obj) {
    setshowBySourceType();
}

/**
 * 设置来源类型设置
 */
function setshowBySourceType() {
    var sourceType = dicts.get("sourceType").val();
    if (!sourceType || sourceType === "1") {
        showDict();
    }else if (sourceType === "2") {
        showfield()
    }else if (sourceType === "3") {
        showComputeMode()
    }else if (sourceType === "4") {
        showSysConstant()
    } else {
        showDict()
    }
}


/**
 * 设置与来源类型为系统常量的相关要素显示，隐藏其他要素
 */
function showSysConstant() {
    $($(".dictTd")[0]).html("系统常量");
    $($(".dictTd")[1]).html(
        "<div class=\"layui-inline\">\n" +
        "    <div class=\"bt-biz-dict\" data-type=\"select\" data-required=\"true\"\n" +
        "         data-group=\"bill.intellect.element.sysConstant\"\n" +
        "         data-name=\"sysConstant\" data-biz-name=\"sysConstant\">\n" +
        "    </div>\n" +
        "</div>"
    );
    dicts.get("dataType").val("date")
    if ($(".field").length > 0 || $(".computeMode").length > 0) {
        $("#sourceTypeTr").next().remove();
    }
    /**
     * 重新初始化表单组件
     */
    initTools();
}

/**
 * 设置与来源类型为字典的相关要素显示，隐藏其他要素
 */
function showDict() {
    $($(".dictTd")[0]).html("字典组");
    $($(".dictTd")[1]).html(
        "                <div class=\"layui-inline dict\">" +
        "                    <div class=\"layui-input-inline\">" +
        "                        <input type=\"text\" class=\"layui-input\" id=\"dictGroupName\" lay-verify=\"required\">" +
        "                        <input type=\"hidden\" class=\"layui-input\" id=\"dictGroup\" name=\"dictGroup\">" +
        "                    </div>" +
        "                    <div class=\"layui-input-inline\">" +
        "                        <button type=\"button\" onclick=\"selectDictGoup()\" class=\"layui-btn\">选择</button>" +
        "                    </div>" +
        "                </div>"
    );

    if ($(".field").length > 0 || $(".computeMode").length > 0) {
        $("#sourceTypeTr").next().remove();
    }
    /**
     * 重新初始化表单组件
     */
    initTools();
}


/**
 * 设置与来源类型为字段的相关要素显示，隐藏其他要素
 */
function showfield() {
    if ($(".field").length > 0 || $(".computeMode").length > 0) {
        $("#sourceTypeTr").next().remove();
    }
    $($(".dictTd")[0]).html("来源名称");
    $($(".dictTd")[1]).html(
        "                <div class=\"layui-inline field\">" +
        "                    <input type=\"text\" class=\"layui-input\" name=\"sourceName\" lay-verify=\"required\">" +
        "                </div>"
    );
    $("#sourceTypeTr").after(
        "<tr class=\"field\">" +
        "            <td class=\"textRight\">" +
        "                表名" +
        "            </td>" +
        "            <td>" +
        "                <div class=\"layui-inline\">" +
        "                    <input type=\"text\" class=\"layui-input\" name=\"tableName\" lay-verify=\"required\">" +
        "                </div>" +
        "            </td>" +
        "            <td class=\"textRight\">" +
        "                字段名" +
        "            </td>" +
        "            <td>" +
        "                <div class=\"layui-inline\">" +
        "                    <input type=\"text\" class=\"layui-input\" name=\"fieldName\" lay-verify=\"required\">" +
        "                </div>" +
        "            </td>" +
        "        </tr>"
    );

    /**
     * 重新初始化表单组件
     */
    initTools();
}

/**
 * 设置与来源类型为计算公式的相关要素显示，隐藏其他要素
 */
function showComputeMode() {
    if ($(".field").length > 0 || $(".computeMode").length > 0) {
        $("#sourceTypeTr").next().remove();
    }
    $($(".dictTd")[0]).html("计算方式");
    $($(".dictTd")[1]).html(
        "                <div class=\"layui-inline computeMode\">" +
        "                    <div class=\"bt-biz-dict\" data-type=\"select\" data-required=\"true\"" +
        "                         data-group=\"bill.intellect.element.computeMode\" data-callback=\"computeModeCallBack\"" +
        "                         data-name=\"computeMode\" data-biz-name=\"computeMode\">" +
        "                    </div>" +
        "                </div>"
    );
    $("#sourceTypeTr").after(
        "<tr class=\"computeMode computeModeTr\">" +
        "            <td class=\"textRight\" id=\"sourceElementName1Title\">" +
        "                来源要素名称" +
        "            </td>" +
        "            <td>" +
        "                <div class=\"layui-inline\">" +
        "                    <div class=\"bt-biz-selectType\" data-name=\"sourceElementName1\" data-biz-name=\"sourceElementName1\"" +
        "                         data-url=\"" + basePath + "/bill/intellect/element/listElement\" data-method=\"post\" data-required=\"true\"" +
        "                         data-valuefield=\"id\" data-textfield=\"elementName\"></div>" +
        "                </div>" +
        "            </td>" +
        "            <td class=\"textRight\"  id=\"sourceElementName2Title\"></td>" +
        "            <td id=\"sourceElementName2Td\"></td>" +
        "        </tr>"
    );

    /**
     * 重新初始化表单组件
     */
    initTools();
}

function initTools() {
    form.render();
    dicts.render();
    selectType.render();
    form.renderIcon()
}

/**
 * 字典选择
 */
function selectDictGoup() {
    layer.open({
        type: 2,
        content: basePath + "/bill/intellect/element/selectDictGroup",
        btn: ["确定", "关闭"],
        btnAlign: "c",
        area: ["900px","1200px"],
        yes: function (index, layero) {
            var iframeWin = window[layero.find('iframe')[0]['name']];
            var selectData = iframeWin.getSelectData();
            $("#dictGroupName").val(selectData.groupName);
            $("#dictGroup").val(selectData.groupCode);
            layer.close(index);
        },
        close: function (index, layero) {
            layer.close(index);
        }
    });
}


/**
 * 计算方式回调
 */
function computeModeCallBack(el, text, data) {
    if (data.value === "add" || data.value === "sub") {
        if (data.value === "add") {
            $("#sourceElementName1Title").html("来源要素名称(加数)");
            $("#sourceElementName2Title").html("来源要素名称(加数)");
        } else if (data.value === "sub") {
            $("#sourceElementName1Title").html("来源要素名称(减数)");
            $("#sourceElementName2Title").html("来源要素名称(被减数)");
        }
        $("#sourceElementName2Td").html(
            "                <div class=\"layui-inline\">" +
            "                    <div class=\"bt-biz-selectType\" data-name=\"sourceElementName2\" data-biz-name=\"sourceElementName2\"" +
            "                         data-url=\"" + basePath + "/bill/intellect/element/listElement\" data-method=\"post\" data-required=\"true\"" +
            "                         data-valuefield=\"id\" data-textfield=\"elementName\"></div>" +
            "                </div>"
        );
    } else {
        $(".computeModeTr").remove();
        $("#sourceTypeTr").after(
            "<tr class=\"computeMode computeModeTr\">" +
            "            <td class=\"textRight\" id=\"sourceElementName1Title\">" +
            "                来源要素名称" +
            "            </td>" +
            "            <td>" +
            "                <div class=\"layui-inline\">" +
            "                    <div class=\"bt-biz-selectType\" data-name=\"sourceElementName1\" data-biz-name=\"sourceElementName1\"" +
            "                         data-url=\"" + basePath + "/bill/intellect/element/listElement\" data-method=\"post\" data-required=\"true\"" +
            "                         data-valuefield=\"id\" data-textfield=\"elementName\"></div>" +
            "                </div>" +
            "            </td>" +
            "            <td class=\"textRight\"  id=\"sourceElementName2Title\"></td>" +
            "            <td id=\"sourceElementName2Td\"></td>" +
            "        </tr>"
        );
    }
    initTools();
}