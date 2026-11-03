var dicts, selectType, form, moneyType, $, corp, externalCorpBankAcc, dateType, radioType, mulselect;
bt.use(['form', 'dicts', 'element',"selectType", 'moneyType', 'corp', 'externalCorpBankAcc', 'dateType', 'radioType', 'mulselect'], function () {
    $ = bt.jquery
    form = bt.form();
    moneyType = bt.moneyType.init()
    dicts = bt.dicts.init();
    corp = bt.corp.init();
    selectType = bt.selectType.init();
    dateType = bt.dateType.init();
    radioType = bt.radioType.init();
    mulselect = bt.mulselect.init();

    externalCorpBankAcc = bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        externalCorpBankAcc.set({
            "corpId" : "0",
            type: "2"
        });
        return true;
    }

    form.on("submit(submit)", function (data) {
        bt.loading.show();
        $.post(basePath + "/bill/intellect/ruleDtl/update", data.field, function (result) {
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

    setHtmlByScreenType($('#screenTypeVal').val(), $('#scopeModeVal').val())
    elementNameCallback({
        id: $('#elementId').val(),
        elementname: $('#elementNameVal').val(),
        datatype: $('#dataType').val(),
        valueVal: $('#valueVal').val(),
        sourcetype: $('#sourceTypeVal').val(),
        dictgroup: $('#dictGroupVal').val()
    })
});

/**
 * 是否为要素回调方法
 */
function isElemCallback(data) {
    if (data.value === '1') {
        // value:要素下拉框组件
        $('#changeVal').html(
            "  <div class=\"bt-biz-selectType\" data-url=\"" + basePath + "/bill/intellect/element/listElement\" " +
            "     data-method =\"post\"  data-disabled=\"true\" data-name =\"value\" data-biz-name = \"value\"\n" +
            "     data-valueField=\"id\" data-textField=\"elementName\"></div>\n")
    } else {
        // value:组件
        var html = setValueByDataType($('#valueVal').val(), 'value');
        html = html.substring(html.indexOf('>') + 1, html.lastIndexOf("<"))
        $('#changeVal').html(html)
    }

    initTools()
}

/**
 * 上级选票要素回调函数
 * @param data 选中的值
 */
function parentNameCallback(data, elem, beforeVal) {
    $('#parentId').val(data.id)
}

/**
 * 选票要素回调函数
 * @param data 选中的值
 */
function elementNameCallback(data, elem, beforeVal) {
    $('#elementId').val(data.id)
    $('#dataType').val(data.datatype)
    if (data.elementname === '票据单位') {
        dicts.get('screenType').val('scope')
        setHtmlByScreenType('scope', 'series')
        // value:票据单位组件
        $('#changeVal').html(
            "           <div class=\"layui-input-inline\">\n" +
            "               <div class=\"bt-biz-corp\" data-biz-name=\"value\" data-tree-type=\"check\" data-biz-type=\"corp\" \n" +
            "                    data-type=\"selectTree\" data-name=\"value\" data-scope=\"all\" data-disabled=\"true\" data-defaultvalue=\"" + data.valueVal + "\">\n" +
            "               </div>\n" +
            "           </div>"
        )
    } else if (data.elementname === '供应商') {
        dicts.get('screenType').val('scope')
        setHtmlByScreenType('scope', 'series')
        dicts.get('scopeMode').val('series')
        data.valueVal = data.valueVal === undefined ? '' : data.valueVal
        $('#changeVal').html("<div class=\"layui-input-inline\" id=\"payeeAccDiv\">\n" +
            "                                <input type=\"text\" id=\"value\" name=\"value\" disabled\n" +
            "                                       autocomplete=\"off\" class=\"layui-input\" value=\"" + data.valueVal + "\">\n" +
            "                                <div class=\"bt-div-search\">\n" +
            "                                    <button  class=\"bt-biz-externalCorpBankAcc\" id=\"selPayeeAccBut\" type=\"button\" data-beforeClick=\"bt.externalCorpBankAccParams\"\n" +
            "                                             data-callback=\"externalCorpCallback\" disabled title=\"#springMessage('common.button.select')\">\n" +
            "                                        <i class=\"layui-icon\">&#xe615;</i>\n" +
            "                                    </button>\n" +
            "                                </div>\n" +
            "                            </div>")
    } else if  (dicts.get("scopeMode") && dicts.get("scopeMode").val() === 'valSco') { //如果筛选类型为范围，且范围方式为介于值之间
        setHtmlByScreenType('scope', 'valSco')
    } else if (dicts.get("screenType").val() === 'val' && $('#valueIsElementVal').val() === '1') { //如果筛选类型为值，且是否为要素为是
        dicts.get('valueIsElement').val('1')
        $('#changeVal').html(
            "  <div class=\"bt-biz-selectType\" data-url=\"" + basePath + "/bill/intellect/element/listElement\" " +
            "     data-method =\"post\"  data-disabled=\"true\" data-name =\"value\" data-biz-name = \"value\"\n data-defaultvalue=" + data.valueVal +
            "     data-valueField=\"id\" data-textField=\"elementName\"></div>\n")
    } else if (data.sourcetype === '1') { // 来源类型为数据字典
        dicts.get('screenType').val('scope')
        setHtmlByScreenType('scope', 'series')
        dicts.get('scopeMode').val('series')
        $('#changeVal').html(
            "     <div class=\"layui-inline\">\n" +
            "         <div class=\"bt-biz-dict\" data-type=\"mulselect\" data-disabled=\"true\" data-group="+ data.dictgroup +"\n" +
            "              data-name=\"value\" data-biz-name=\"value\"  data-defaultvalue="+ data.valueVal +">\n" +
            "         </div>\n" +
            "     </div>\n")
    } else {
        // value
        var html = setValueByDataType($('#valueVal').val(), 'value');
        html = html.substring(html.indexOf('>') + 1, html.lastIndexOf("<"))
        $('#changeVal').html(html)
    }


    initTools()
}

/**
 * 范围方式回调方法
 * @param data 选中的值
 */
function scopeModeCallback(data) {
    setHtmlByScreenType('scope', data.value)
    dicts.get('scopeMode').val(data.value)
}

/**
 * 根据筛选类型、范围方式设置元素显示
 * @param data 筛选类型
 * @param type 范围方式
 */
function setHtmlByScreenType(data, type) {
    disabledTools()

    if (data === 'sort') {
        showSort()
    } else if (data === 'val') {
        showVal()
    } else {
        if (type === 'series') {
            showScopeSeries(type)
        } else {
            showScopeValSco(type)
        }
    }

    initTools()
}

/**
 * 筛选类型回调方法
 * @param data 选中的值
 * @param text 选中的内容
 * @param selectedData 选中的所有数据
 */
function screenTypeCallback(data, text, selectedData) {
    setHtmlByScreenType(data.value, 'series')
}

/**
 * 显示筛选类型为排序的组件
 */
function showSort() {
    // 排序方式
    $('#showSort').html("<td class=\"textRight\">排序方式</td>\n" +
        "                        <td>\n" +
        "                            <div class=\"layui-inline\">\n" +
        "                                <div class=\"bt-biz-dict\" data-type=\"select\" data-group=\"sort.order\" data-name=\"sortOrder\"\n" +
        "                                     data-disabled=\"true\" data-biz-name=\"sortOrder\" data-selecttext=\"false\" data-defaultvalue=" + $('#sortOrderVal').val() +">\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </td>")

}

/**
 * 显示筛选类型为范围，且范围类型为系列的组件
 */
function showScopeSeries(type) {
    var $showSort = $('#showSort');
    // 范围方式，值
    var scopeModeVal = type !== undefined ? type : $('#scopeModeVal').val()
    $showSort.html("<td class=\"textRight \">范围方式</td>\n" +
        "                        <td >\n" +
        "                            <div class=\"layui-inline\">\n" +
        "                                <div class=\"bt-biz-dict\" data-type=\"select\" data-group=\"scope.mode\" data-name=\"scopeMode\"\n data-defaultvalue=" + scopeModeVal +
        "                                     data-disabled=\"true\" data-biz-name=\"scopeMode\" data-selecttext=\"false\" data-callback=\"scopeModeCallback\">\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </td>\n")
    $showSort.append(" <td class=\"textRight\">值</td>\n")
    $showSort.append(setValueByDataType($('#valueVal').val(), 'value'))

}

/**
 * 显示筛选类型为范围，且范围类型为介于值之间的组件
 */
function showScopeValSco(type) {
    // 范围方式
    var scopeModeVal = type !== undefined ? type : $('#scopeModeVal').val()
    $('#showSort').html("<td class=\"textRight \">范围方式</td>\n" +
        "                        <td >\n" +
        "                            <div class=\"layui-inline\">\n" +
        "                                <div class=\"bt-biz-dict\" data-type=\"select\" data-group=\"scope.mode\" data-name=\"scopeMode\"\n data-defaultvalue=" + scopeModeVal +
        "                                     data-disabled=\"true\" data-biz-name=\"scopeMode\" data-selecttext=\"false\" data-callback=\"scopeModeCallback\">\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </td>\n")

    //起始值，起始值关系
    var $showValSco = $('#showValSco');
    $showValSco.html(" <td class=\"textRight\">起始值</td>\n")
    $showValSco.append(setValueByDataType($('#startValueVal').val(), 'startValue','startValueTd'))
    $showValSco.append(
        "                        <td class=\"textRight\">关系</td>\n" +

        "                        <td >\n" +
        "                            <div class=\"layui-inline\">\n" +
        "                                <div class=\"bt-biz-dict\" data-type=\"select\" data-group=\"sort.relation\" data-name=\"startRelation\" data-displayData=\"3,4\"\n" +
        "                                     data-biz-name=\"startRelation\" data-disabled=\"true\" data-selecttext=\"false\" data-defaultvalue="+ $('#startRelationVal').val() +">\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </td>")

    //结束值，结束值关系
    var $showValSco2 = $('#showValSco2');
    $showValSco2.html(" <td class=\"textRight\">结束值</td>\n")
    $showValSco2.append(setValueByDataType($('#endValueVal').val(), 'endValue','endValueTd'))
    $showValSco2.append(
        "                <td class=\"textRight\">关系</td>\n" +
        "                <td >\n" +
        "                    <div class=\"layui-inline\">\n" +
        "                        <div class=\"bt-biz-dict\" data-type=\"select\" data-group=\"sort.relation\" data-name=\"endRelation\" data-displayData=\"5,6\"\n" +
        "                             data-biz-name=\"endRelation\" data-disabled=\"true\" data-selecttext=\"false\" data-defaultvalue="+ $('#endRelationVal').val() +">\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </td>")
}

/**
 * 显示筛选类型为值的组件
 */
function showVal() {
    // 关系，值
    var $showVal = $('#showVal');
    $showVal.html("<td class=\"textRight\">关系</td>\n" +
        "                        <td >\n" +
        "                            <div class=\"layui-inline\">\n" +
        "                                <div class=\"bt-biz-dict\" data-type=\"select\" data-group=\"sort.relation\" data-name=\"relation\"\n" +
        "                                     data-disabled=\"true\" data-biz-name=\"relation\" data-selecttext=\"false\" data-defaultvalue="+ $('#relationVal').val() +">\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </td>\n" +
        "                        \n" +
        "                        <td class=\"textRight\">值</td>\n")
    $showVal.append(setValueByDataType($('#valueVal').val(),'value'))

    // 值是否为要素
    $('#showVal2').html("<td class=\"textRight\">值是否为要素</td>\n" +
        "                        <td >\n" +
        "                            <div class=\"layui-inline\">\n" +
        "                                <div class=\"bt-biz-dict\" data-type=\"radio\" data-defaultvalue=\"0\" data-group=\"sf_yn\" data-disabled=\"true\"\n" +
        "                                     data-name=\"valueIsElement\" data-biz-name=\"valueIsElement\" data-callback=\"isElemCallback\" data-defaultvalue="+ $('#valueIsElementVal').val() +">\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </td>")
}



/**
 * 重新渲染组件
 */
function initTools() {
    dicts.render();
    selectType.render();
    mulselect.render();
    moneyType.render()
    dateType.render()
    corp.render();
    form.render();
    form.renderIcon()
}

/**
 * 修改元素为不必填
 */
function disabledTools() {
    $('#showSort').html('')
    $('#showVal').html('')
    $('#showVal2').html('')
    $('#showValSco').html('')
    $('#showValSco2').html('')
}

/**
 * 供应商组件回调函数
 */
function externalCorpCallback(data) {
    if (typeof data.netId === "undefined") { // 选择客商信息
        $("#value").val(data.externalAcc);
    } else {
        $("#value").val(data.bankAcc);
    }
}

/**
 * 根据要素类型决定值用哪个组件
 * @param data 默认值
 * @param val  设置组件的name，id值
 * @param tdId 设置td的id值
 * @return{string} 具体组件的html
 */
function setValueByDataType(data, val, tdId) {
    var dataType = $('#dataType').val();
    var html = ''
    tdId = tdId === undefined ? 'changeVal'  : tdId
    if (dataType === 'money' || dataType === 'num') {
        html =  "<td id="+tdId+">\n" +
            "<div class=\"layui-input-inline\">\n" +
            "    <input type=\"text\" class=\"layui-input bt-biz-moneyType\" " +
            "           disabled data-biz-name=\"" + val + "\" data-name=\"" + val +"\" value=" + data + ">\n" +
            "</div>" +
            "</td>"
    } else if (dataType === 'date') {
        data = data === undefined ? bt.date.format(new Date(), 'yyyy-MM-dd') : data
        html = "<td id="+tdId+">\n" +
            "<div class=\"layui-inline\">\n" +
            "<input type=\"text\" class=\"layui-input bt-biz-dateType\" disabled data-type=\"date\" data-today=\"true\"\n" +
            "   data-biz-name='" + val + "' data-name='" + val + "' value=" + data + ">\n" +
            "</div>" +
            "</td>"
    } else { // (dataType === 'text')
        html =  " <td id="+tdId+">\n" +
            "     <div class=\"layui-inline\">\n" +
            "         <input type=\"text\" class=\"layui-input\" disabled " +
            "                name=" + val + " id=" + val + " value=" + data + ">\n" +
            "     </div>\n" +
            " </td>"
    }
    return html
}