var form, element, corp;
bt.use(["jquery", "element", "datatable", "corp", "selectType", "moneyType", "dateType"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element;
    corp = bt.corp.init();//单位
    selectType = bt.selectType.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();

    datatable = bt.datatable.init({
        elem: "#appTable",
        paging: false,
        ignoreButtons: ['colvis', 'output', 'print'],
        ajax: {
            "url": basePath + "/foreign/fexCotp/getChireddataTable"
        },
        searchForm: {
            formElem: "#myform",
            formFilter: "search"
        },
        columns: [
            {
                data: "cotpName",
                className: "center"
            },
            {
                data: "cotpLevel",
                className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("fex.cotp.level", data);
                }
            },
            {
                data: "shortName",
                className: "center"
            },
            {
                data: "officialName",
                className: "center"
            },
            {
                data: null,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    var split = "<span class=\"text-explode\">|</span>"
                    html += "<a href=\"#\" onclick=\"deleteChildById('" + full.id + "')\">" + bt.lang("common.button.delete") + "</a>";
                    html += split;
                    html += "<a href=\"#\" onclick=\"view('" + full.id + "')\">" + bt.lang("common.button.view") + "</a>";
                    return html;
                }
            }
        ]
    });
//监听提交
    form.on("submit(submit)", function (data) {
        var action = "/foreign/fexCotp/save";
        bt.loading.show();
        data.field.status = this.value;
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
    //这里填写新的js代码，请参考前端文档
});

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 选择交易对手组
 *
 */
function selectCotpGroup() {
    parent.layer.open({
        title: bt.lang("fex.cotp.selectGroup"),
        type: 2,
        area: ["70%", "70%"],
        btn: [bt.lang("common.button.confirm"), bt.lang("common.button.close")],
        btnAlign: "c",
        content: basePath + "/foreign/fexCotp/selectFexCotpGroup",
        yes: function (index, layero) {
            //按钮【按钮一】的回调
            var iframeObj = layero.find('iframe')[0];
            var selectData = iframeObj.contentWindow.getSelectDate();
            $("#parentId").val(selectData.id);
            $("#parentName").val(selectData.cotpName);
            // 创建自定义字段
            getCustFields(selectData.id);
            parent.layer.close(index);
        },
        btn2: function (index, layero) {
            parent.layer.close(index);
        }
    });
}


/**
 *选择交易对手/交易对数组
 */
function selectCotp() {
    parent.layer.open({
        title: bt.lang("common.placeholder.select"),
        type: 2,
        area: ["70%", "70%"],
        btn: ["确定", "关闭"],
        btnAlign: "c",
        content: basePath + "/foreign/fexCotp/selectFexCotp",
        yes: function (index, layero) {
            //按钮【按钮一】的回调
            var iframeObj = layero.find('iframe')[0];
            var data = iframeObj.contentWindow.appendChild($("#id").val());
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            datatable.draw();
            parent.layer.close(index);
        },
        btn2: function (index, layero) {
            parent.layer.close(index);
        }
    });
}

/**
 * 查看
 * @param id
 */
function view(id) {
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('table.query.message'), basePath + "/foreign/fexCotp/view?id=" + id+"&flag=1");
        return;
    }
    parent.layer.open({
        type: 2,
        title: bt.lang('table.query.message'),
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexCotp/view?id=" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

/**
 * 删除组内数据
 * @param id
 */
function deleteChildById(id) {
    parent.layer.confirm(bt.lang("table.sysTask.deletedsw")+"?",function () {
        var url = "/foreign/fexCotp/deleteChildById?id=" + id;
        bt.loading.show();
        $.post(basePath + url,function(data){
            bt.loading.hide();
            parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            datatable.draw();
        },'JSON');
    });
}

/**
 * 交易对手名校验
 * @param obj
 */
function checkName(obj) {
    var id = $("#id").val();
    $.ajax({
        url: basePath + "/foreign/fexCotp/checkName",
        type: "post",
        data:{"name": obj.value, "id": id},
        dataType: "json",
        success: function (data) {
            if (!data.success) {
                layer.alert(bt.lang("fex.cotp.nameExists"), {icon: 2});
                obj.value = "";
            }
        }
    });
}



/**
 * 获取自定义字段信息
 * @param cotpGroupId
 */
function getCustFields(cotpGroupId) {
    $.ajax({
        url: basePath + "/foreign/fexcustfieldrule/listByCotp",
        data: {"cotpGroupId": cotpGroupId},
        type: "post",
        dataType: "json",
        success: function (data) {
            if (data.success) {
                if (null != data.obj && data.obj.length > 0) {
                    // 创建自定义字段
                    createCustField(data.obj);
                }
            } else {
                parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
            }
        }
    });
}

/**
 * 构建自定义字段
 * @param fields
 */
function createCustField(fields) {
    var html = "";
    var tr = "";
    $.each(fields, function (index, item) {
        if (((index+1) % 2) == 1) {
            html += "<tr>"
            html += createFiledName(item, index);
            html += createFied(item, index);
        } else {
            html += createFiledName(item, index);
            html += createFied(item, index);
            html += "</tr>"
        }
    });
    if (fields.length == 1) {
        html += "<td style=\"width: 10%\"></td><td></td></tr>";
    }
    $("#custFields").empty();
    $("#custFields").append(html);
    selectType.render();
    moneyType.render();
    dateType.render();
    form.render("select");
    form.renderIcon();
}

/**
 * 创建字段名
 * @param filed
 * @returns {string}
 */
function createFiledName(filed,index) {
    var fieldName = "";
    fieldName += "<td class=\"textRight\" style=\"width: 10%\">"
    fieldName += filed.fieldName;
    fieldName += "<input type=\"hidden\" name=\"fexCotpCustInfoVOS[" + index + "].fieldId\" value=\"" + filed.id + "\">";
    fieldName += "</td>";
    return fieldName;
}

/**
 * 构建控件
 * @param field
 * @returns {string}
 */
function createFied(field, index) {
    var filedStr = "";
    if (isNotEmpty(field.presetVal) && field.presetVal.indexOf(",") != -1) {
        filedStr = createSelectFied(field, index);
    } else {
        if (1 == field.fieldType) {
            filedStr = createTextFied(field, index);
        } else if (2 == field.fieldType) {
            filedStr = createNumFied(field, index);
        } else if (3 == field.fieldType) {
            filedStr = createDateFied(field, index);
        } else if (4 == field.fieldType) {
            filedStr = createTimeFied(field, index);
        } else if (5 == field.fieldType) {
            filedStr = createDateTimeFied(field, index);
        }
    }
    var td = "<td><div class=\"layui-input-inline\">";
    td += filedStr
    // td += "<span data-srcValue=\"" + field.presetVal + "\" style=\"color: red;display: none\">" + bt.lang("fex.cotp.custField.defaultValCkeck") + "</span>";
    td += "</div>";
    td += "</td>"
    return td;
}

/**
 * 构建下拉列表控件
 * @param field
 * @param index
 * @returns {*}
 */
function createSelectFied(field, index) {
    var filedStr = "<select name=\"fexCotpCustInfoVOS["+index+"].fieldVal\" >";
    $.each(field.presetVal.split(","), function (index, item) {
        filedStr += "<option value=\""+item+"\">" + item + "</option>";
    });
    filedStr += "</select>"
    return filedStr;
}


/**
 * 构建文本控件
 * @param field
 * @param index
 * @returns {string}
 */
function createTextFied(field, index) {
    var filedStr = "<input type=\"text\" class=\"layui-input\" name=\"fexCotpCustInfoVOS[" + index + "].fieldVal\" style=\"display: inline\"";
    if (isNotEmpty(field.presetVal)) {
        filedStr += "value=\"" + field.presetVal + "\" ";
    }
    var controlRule = field.controlRule;
    if (isNotEmpty(controlRule)) {
        if (controlRule.indexOf("4") != -1) {
            filedStr += " lay-verify=\"required\"";
        }
        if (controlRule.indexOf("2") == -1) {
            filedStr += " readonly=\"readonly\"";
        }
        if (controlRule.indexOf("3") != -1) {
            filedStr += " onblur=\"checkSrcVal(this)\"";
        }
    }
    filedStr += ">";
    return filedStr;
}

/**
 * 构建数据控件
 * @param field
 * @param index
 * @returns {string}
 */
function createNumFied(field, index) {
    var filedStr = "<input type=\"text\" data-id=\"FexCotpCustInfo" + index + "\" class=\"layui-input bt-biz-moneyType\" name=\"fexCotpCustInfoVOS[" + index + "].fieldVal\" style=\"display: inline\"";
    if (isNotEmpty(field.presetVal)) {
        filedStr += "value=\"" + field.presetVal + "\" ";
    }
    var controlRule = field.controlRule;
    if (isNotEmpty(controlRule)) {
        if (controlRule.indexOf("4") != -1) {
            filedStr += " lay-verify=\"required\"";
        }
        if (controlRule.indexOf("2") == -1) {
            filedStr += " readonly=\"readonly\"";
        }
        if (controlRule.indexOf("3") != -1) {
            filedStr += " data-callback=\"checkNumSrcVal\"";
        }
    }
    if (isNotEmpty(field.minVal)) {
        filedStr += " data-min=\"" + field.minVal + "\"";
    }
    if (isNotEmpty(field.maxtVal)) {
        filedStr += " data-max=\"" + field.maxtVal + "\"";
    }
    if (isNotEmpty(field.decimalDigit)) {
        filedStr += " data-decimal=\"" + field.decimalDigit + "\"";
    }
    return filedStr;
}

/**
 * 构建日期控件
 * @param field
 * @param index
 * @returns {string}
 */
function createDateFied(field, index) {
    var filedStr = "<input type=\"text\" class=\"layui-input bt-biz-dateType\" data-id=\"FexCotpCustInfo$velocityCount\" data-type=\"date\" name=\"fexCotpCustInfoVOS[" + index + "].fieldVal\" style=\"display: inline\"";
    if (isNotEmpty(field.presetVal)) {
        filedStr += "value=\"" + field.presetVal + "\" ";
    }
    var controlRule = field.controlRule;
    if (isNotEmpty(controlRule)) {
        if (controlRule.indexOf("4") != -1) {
            filedStr += " lay-verify=\"required\"";
        }
        if (controlRule.indexOf("2") == -1) {
            filedStr += " readonly=\"readonly\"";
        }
        if (controlRule.indexOf("3") != -1) {
            filedStr += " data-callback=\"checkDateSrcVal\"";
        }
    }
    filedStr += ">";
    return filedStr;
}

/**
 * 构建时间控件
 * @param field
 * @param index
 * @returns {string}
 */
function createTimeFied(field, index) {
    var filedStr = "<input type=\"text\" class=\"layui-input bt-biz-dateType\" data-id=\"FexCotpCustInfo$velocityCount\" data-type=\"time\" name=\"fexCotpCustInfoVOS[" + index + "].fieldVal\" style=\"display: inline\"";
    if (isNotEmpty(field.presetVal)) {
        filedStr += "value=\"" + field.presetVal + "\" ";
    }
    var controlRule = field.controlRule;
    if (isNotEmpty(controlRule)) {
        if (controlRule.indexOf("4") != -1) {
            filedStr += " lay-verify=\"required\"";
        }
        if (controlRule.indexOf("2") == -1) {
            filedStr += " readonly=\"readonly\"";
        }
        if (controlRule.indexOf("3") != -1) {
            filedStr += " data-callback=\"checkDateSrcVal\"";
        }
    }
    filedStr += ">";
    return filedStr;
}

/**
 * 构建日期时间控件
 * @param field
 * @param index
 * @returns {string}
 */
function createDateTimeFied(field, index) {
    var filedStr = "<input type=\"text\" class=\"layui-input bt-biz-dateType\" data-id=\"FexCotpCustInfo$velocityCount\" data-type=\"datetime\" name=\"fexCotpCustInfoVOS[" + index + "].fieldVal\" style=\"display: inline\"";
    if (isNotEmpty(field.presetVal)) {
        filedStr += "value=\"" + field.presetVal + "\" ";
    }
    var controlRule = field.controlRule;
    if (isNotEmpty(controlRule)) {
        if (controlRule.indexOf("4") != -1) {
            filedStr += " lay-verify=\"required\"";
        }
        if (controlRule.indexOf("2") == -1) {
            filedStr += " readonly=\"readonly\"";
        }
        if (controlRule.indexOf("3") != -1) {
            filedStr += " data-callback=\"checkDateSrcVal\"";
        }
    }
    filedStr += ">";
    return filedStr;
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
 * 数字非默认值检查
 * @param nextValue
 * @param elem
 * @param preValue
 */
function checkNumSrcVal(nextValue , elem,preValue) {
    var span = elem.next("span");
    var val = span.attr("data-srcValue");
    if (nextValue != val) {
        span.css("display", "");
    } else {
        span.css("display", "none");
    }
}
