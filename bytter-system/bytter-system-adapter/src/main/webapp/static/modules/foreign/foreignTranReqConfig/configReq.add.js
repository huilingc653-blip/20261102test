var form;
var selectType,mulselect,corp,moneyType,dicts,btImport;
// 自定义字段ID
var custFieldIdArr = [];
var rowIndex = 0;
bt.use(['form','layedit','selectTree','select','btdate','corp','moneyType','selectType','dicts','mulselect', 'btImport'], function(){
   $ = bt.jquery, form = bt.form();

    corp = bt.corp.init();
    mulselect = bt.mulselect.init();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    selectType = bt.selectType.init();
    // 加载配置
    load();
    //
    $('#importExcel').click(function (){
        importExcel()
    })
    // isVisible
    $('#isAllVisible').change(function (){
        $('.isVisible').prop('checked',this.checked);
        if(!this.checked){
            bt.each(speicalCode,function (i,item){
                $('[data-code="'+item+'"]').prop('checked',true)
            })
        }
    })
    //
    var speicalCode = ['corpInfo','cotpInfo'] // 对手账户信息 主体单位信息 必须选中
    $('#tableTbody').on('click','.isVisible',function (){
        if(!this.checked){
            var code = $(this).data('code');
            if(speicalCode.indexOf(code) > -1){
                $(this).prop('checked',true);
                checkAll('isAllVisible','isVisible');
            }else{
                $('#isAllVisible').prop('checked',false);
            }
        }else{
            checkAll('isAllVisible','isVisible');
        }
    })
    $('#isAllRequired').change(function (){
        $('.isRequired').prop('checked',this.checked);
        if(!this.checked){
            $('.cdSign_isRequired').prop('checked',true)
        }
    })

    $('#tableTbody').on('click','.isRequired',function (){
        if(!this.checked){
            $('#isAllRequired').prop('checked',false);
        }else{
            checkAll('isAllRequired','isRequired');
        }
    })
    // 是否控制长度 复选框点击事件，当选中时，相对应的长度限制列必填，否则为非必填
    $('#tableTbody').on('click','.isLength',function (){
        if(this.checked){
            $(this).closest('tr').find('.fieldLength').attr('lay-verify','required')
        }else{
            $(this).closest('tr').find('.fieldLength').removeAttr('lay-verify')
        }
    })
    // 限制输入框只能输入正整数 1~35
    $("#tableTbody").on("keyup blur", '.fieldLength', function () {
        let val = $(this).val();
        // 1. 清除所有非数字
        val = val.replace(/[^\d]/g, '');
        // 去除开头多余0：正则替换前缀0
        val = val.replace(/^0+/, '')

        let num = Number(val);
        if (val === '') {
            $(this).val(''); // 空保留空
        } else if (num > 200) {
            $(this).val(200); // 大于200自动改为200
        } else if (num < 1) {
            $(this).val(''); // 0清空，不能存0
        } else {
            $(this).val(num);
        }
    });

    // 排序输入框只能输入正整数
    $("#tableTbody").on("keyup blur", '.fieldSort', function () {
        let val = $(this).val();
        // 1. 清除所有非数字
        val = val.replace(/[^\d]/g, '');
        // 去除开头多余0：正则替换前缀0
        val = val.replace(/^0+/, '')

        let num = Number(val);
        if (val === '') {
            $(this).val(''); // 空保留空
        } else if (num < 1) {
            $(this).val(''); // 0清空，不能存0
        } else {
            $(this).val(num);
        }
    });

    // 排序
    $('#sortBtn').click(function (){

        // 1. 拆分：普通行（参与排序） + 新增行（不参与，放最后）
        var $normalRows = $("#tableTbody").find("tr").not(".add-row"); // 排除 add-row
        var $addRow = $("#tableTbody").find("tr.add-row"); // 新增行单独拿出来


        $normalRows.sort(function (a, b) {
            // 取出排序值
            try {
                var valA = $(a).find(".fieldSort").val().trim();
            }catch (e) {
                var valA =-1;
            }
            try {
                var valB = $(b).find(".fieldSort").val().trim();
            }catch (e) {
                var valB =-1;
            }

            // 转数字，空/非数字 = 99999 排最后
            let numA = isNaN(Number(valA)) ? 99999 : Number(valA);
            let numB = isNaN(Number(valB)) ? 99999 : Number(valB);

            // 核心：先按序号排序，序号相同则保持原有相对顺序
            if (numA === numB) {
                return 0; // 相等不交换 → 自动排在一起
            }
            return numA - numB;
        });

        // 重新渲染表格
        $("#tableTbody").empty().append($normalRows).append($addRow);
    })
    // 删除行
    $('#tableTbody').on('click','.delete-row',function (e){
        // 阻止a标签/# 默认跳转，解决滚到顶部
        if(e) e.preventDefault();

        deleteField(this)
    })

    // 监听提交
    form.on('submit(submit)', function(data){
        var isRepeat = checkSortNoRepeat();
        if(!isRepeat){
            return false
        }
        var templateId = $("#templateId").val();
        var versionNo = $("#versionNo").val();
        var templateConfigs = [];

        $("#templateConfigDiv table tbody tr").each(function (index) {
            var $row = $(this);

            // 获取值
            var fieldSort = $row.find("#fieldSort_" + index).val() || "";
            if (fieldSort){ // 最后一行不用提交
                var fieldId = $row.find("#fieldId_" + index).val() || "";
                var fieldCode = $row.find("#fieldCode_" + index).val() || "";
                var fieldName = $row.find("#fieldName_" + index).val() || "";
                var dataClass = $row.find("#dataClass_" + index).val() || "";
                var dataType = $row.find("#dataType_" + index).val() || "";
                var fieldDictGroup = $row.find("#fieldDictGroup_" + index).val() || "";

                // 复选框状态
                var isRequired = $row.find("#isRequired_" + index).is(":checked") ? "1" : "0";
                var isVisible = $row.find("#isVisible_" + index).is(":checked") ? "1" : "0";
                //var isUse = $row.find("#isUse_" + index).is(":checked") ? "1" : "0";
                var isLength = $row.find("#isLength_" + index).is(":checked") ? "1" : "0";
                var isLimitedDict = $row.find("#isLimitedDict_" + index).is(":checked") ? "1" : "0";
                var isSys = $row.find("#isSys_" + index).is(":checked") ? "1" : "0";




                // 单选框
                var fieldArea = $('[name="fieldArea_'+index+'"]').val();
                var fieldParentCode = $('[name="fieldParentCode_'+index+'"]').val();

                // 自定义字段是否发送银行指令
                var isExc = "";
                if ("otherInfo" == fieldArea){
                    isExc = $row.find("#isExc_" + index).is(":checked") ? "1" : "0";
                }

                var field = {
                    fieldSort: fieldSort,
                    fieldId: fieldId,
                    fieldCode: fieldCode,
                    fieldName: fieldName,
                    dataClass: dataClass,
                    dataType: dataType,
                    fieldArea: fieldArea,
                    fieldParentCode: fieldParentCode,
                    isRequired: isRequired,
                    isVisible: isVisible,
                    //isUse: isUse,
                    isLength: isLength,
                    isSys: isSys,
                    isExc: isExc,
                    fieldDictGroup: fieldDictGroup,
                    isLimitedDict: isLimitedDict,
                    templateId: templateId
                };

                // 长度处理
                if (isLength == "1"){
                    field.fieldLength = $row.find("#fieldLength_" + index).val() || "";
                }

                // 字典处理
                if (isLimitedDict == "1"){
                    var dictArr = bt.mulselect.get("fieldLimitedDict_" + index).val() || [];
                    var dictValues = [];
                    for (var i = 0; i < dictArr.length; i++) {
                        dictValues.push(dictArr[i].value);
                    }
                    field.fieldLimitedDict = dictValues.join(",");
                }
                if ("bt-biz-dict" == dataClass){
                    var fieldDefVal = bt.select.get("fieldDefVal_" + index).val() || '';
                    field.fieldDefVal = fieldDefVal;
                }

                templateConfigs.push(field);
            }
        });

        var saveData = {
            templateId: templateId,
            versionNo: versionNo,
            templateConfigs: templateConfigs
        };

        bt.loading.show();
        var action = "/foreign/fexTranReqConfig/saveTemplateConfig";

        $.ajax({
            url: basePath + action,
            method: 'POST',
            data: JSON.stringify(saveData),
            contentType: "application/json;charset=utf-8",
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                var icon = result.success ? 1 : 2;
                if (parent.layer) {
                    parent.layer.alert(result.msg, { icon: icon });
                } else {
                    layer.alert(result.msg, { icon: icon });
                }
            }
        });
        return false;
    });


    function importExcel () {
        up = bt.btImport.init({
            code: "1000000015",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/foreign/fexTranReqConfig/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    }
});

/**
 * 根据每行的 isLimitedDict 复选框状态，设置对应 fieldLimitedDict 的禁用状态
 */
function initLimitedDictState() {
    $('#tableTbody tr').not('.add-row').each(function() {
        var $row = $(this);
        var $isLimitedDict = $row.find('.isLimitedDict');
        var $fieldLimitedDictDiv = $row.find('.fieldLimitedDict');
        if ($isLimitedDict.length && $fieldLimitedDictDiv.length) {
            var checked = $isLimitedDict.prop('checked');
            var name = $fieldLimitedDictDiv.data('name');
            if (name) {
                var instance = bt.mulselect.get(name);
                if (instance && typeof instance.setDisabled === 'function') {
                    instance.setDisabled(!checked);
                } else {
                    // 降级方案：直接禁用内部 input/select
                    $fieldLimitedDictDiv.find('input, select').prop('disabled', !checked);
                }
            }
        }
    });
}

// 事件委托：点击 isLimitedDict 复选框时联动禁用
$('#tableTbody').on('click', '.isLimitedDict', function() {
    var $row = $(this).closest('tr');
    var $fieldLimitedDictDiv = $row.find('.fieldLimitedDict');
    if ($fieldLimitedDictDiv.length) {
        var name = $fieldLimitedDictDiv.data('name');
        if (name) {
            var instance = bt.mulselect.get(name);
            var disabled = !this.checked;
            if (instance && typeof instance.setDisabled === 'function') {
                instance.setDisabled(disabled);
            } else {
                $fieldLimitedDictDiv.find('input, select').prop('disabled', disabled);
            }
        }
    }
});

// 页面加载渲染配置
function load(custFieldIds) {
    var templateId = $("#templateId").val();
    var versionNo = $("#versionNo").val();
    var action = "/foreign/fexTranReqConfig/getTemplateConfig/" + templateId + "/" + versionNo;
    var custFieldIdsStr = "";
    if (custFieldIds){
        custFieldIdsStr = custFieldIds.join(',');
    }

    $.get(basePath + action, {custFieldIds:custFieldIdsStr}, function (result) {
        if (!result.success) {
            return false;
        }

        var $tbody = $("#templateConfigDiv table tbody");
        $tbody.empty();

        // 索引对象（用于传递引用）
        var globalIndexObj = { value: 0 };

        // 渲染根节点
        $.each(result.obj, function (i, rootItem) {
            if ("otherInfo" == rootItem.fieldArea){
                custFieldIdArr.push(rootItem.fieldId);
            }
            renderItem(rootItem, $tbody, globalIndexObj);

        });
        // 加载最后一行
        renderLastItem($tbody);
        // 初始化组件
        corp.init();
        mulselect.init();
        dicts.init();
        moneyType.init();
        selectType.init();
        form.render('select');
        // 判断是否可见全选
        checkAll('isAllVisible','isVisible');
        // 判断是否必填全选
        checkAll('isAllRequired','isRequired');

        initLimitedDictState();
    }, 'JSON');

    var treeAction = "/foreign/fexTranReqConfig/getTemplateConfigByAereTree/" + templateId + "/" + versionNo;
    $.get(basePath + treeAction, {}, function (result) {
        if (!result.success) {
            return false;
        }
    }, 'JSON');
    return false;
}



// 递归渲染
function renderItem(item, $tbody, globalIndexObj) {
   // var index = globalIndexObj.value;
   var index = rowIndex;

    // 拼接隐藏域
    var hiddenHtml = '';
    hiddenHtml += '<input type="hidden" id="fieldName_' + index + '" name="fieldName_' + index + '" value="' + escapeHtml(item.fieldName) + '">';
    hiddenHtml += '<input type="hidden" id="fieldCode_' + index + '" name="fieldCode_' + index + '" value="' + escapeHtml(item.fieldCode) + '">';
    hiddenHtml += '<input type="hidden" id="fieldId_' + index + '" name="fieldId_' + index + '" value="' + escapeHtml(item.fieldId) + '">';
    hiddenHtml += '<input type="hidden" id="dataClass_' + index + '" name="dataClass_' + index + '" value="' + escapeHtml(item.dataClass) + '">';
    hiddenHtml += '<input type="hidden" id="dataType_' + index + '" name="dataType_' + index + '" value="' + escapeHtml(item.dataType) + '">';
    hiddenHtml += '<input type="hidden" id="fieldDictGroup_' + index + '" name="fieldDictGroup_' + index + '" value="' + escapeHtml(item.fieldDictGroup) + '">';
    hiddenHtml += '<input type="hidden" id="fieldParentCode_' + index + '" name="fieldParentCode_' + index + '" value="' + escapeHtml(item.fieldParentCode) + '">';
    hiddenHtml += '<input type="hidden" id="isSys_' + index + '" name="isSys_' + index + '" value="' + escapeHtml(item.isSys) + '">';

    // 拼接表格行
    var html = '<tr>' +
        '<td class="textCenter">' + item.fieldName + hiddenHtml + '</td>';

    // 上级编码
    /*if (item.fieldParentCode) {
        html += '<td class="">' + item.fieldParentCode + '</td>';
    } else {
        html += '<td class=""></td>';
    }*/

    html += /*'<td class="textCenter">' + getCheckBoxHtml("isUse", index, item.isUse) + '</td>' +*/
        '<td class="textCenter">' + getCheckBoxHtml("isVisible", index, item.isVisible,item.fieldCode) + '</td>' +
        '<td class="textCenter">' + getCheckBoxHtml("isRequired", index, item.isRequired,item.fieldCode) + '</td>';

    if ("text" == item.dataType || "textarea" == item.dataType) {
        html += '<td class="textCenter">' + getCheckBoxHtml("isLength", index, item.isLength,item.fieldCode) + '</td>' +
            '<td class="textCenter">' +
            '<input type="text" id="fieldLength_' + index + '" name="fieldLength_' + index + '" class="layui-input fieldLength" value="' + escapeHtml(item.fieldLength) + '">' +
            '</td>';
    } else {
        html += '<td class="textCenter"></td>' +
            '<td class="textCenter"></td>';
    }

    if(item.fieldCode === 'cdSign'){
        item.fieldLimitedDict = '1';
        item.fieldDefVal = '1'
    }

    // 字典类型列
    if (item.dataClass == "bt-biz-dict") {
        html += '<td class="textCenter">' + getCheckBoxHtml("isLimitedDict", index, item.isLimitedDict,item.fieldCode) + '</td>' +
            '<td >' +
            '<div class="bt-biz-dict fieldLimitedDict" data-type="mulselect" data-group="' + item.fieldDictGroup + '" ' +
            'data-name="fieldLimitedDict_' + index + '" data-biz-name="fieldLimitedDict_' + index + '" data-defaultvalue="' + escapeHtml(item.fieldLimitedDict) + '" data-callback="changeFieldLimite"></div>' +
            '</td>';

        // 默认值列
        html += '<td>' +
            '<div class="bt-biz-dict fieldDefVal" data-type="select" data-group="' + item.fieldDictGroup + '" ' +
            'data-name="fieldDefVal_' + index + '" data-biz-name="fieldDefVal_' + index + '" data-defaultvalue="' + escapeHtml(item.fieldDefVal || item.defaultValue) + '"></div>' +
            //'<input type="text" id="fieldDefVal_' + index + '" name="fieldDefVal_' + index + '" class="layui-input" value="' + escapeHtml(item.fieldDefVal || item.defaultValue) + '">' +
            '</td>';
    } else {
        html += '<td class="textCenter"></td><td class="textCenter"></td><td class="textCenter"></td>';
    }

    if (item.fieldParentCode) {
        html += '<td class="textCenter"></td>';
    } else {
        var html1 = '';
        if(item.fieldArea === 'otherInfo'){
            html1 = 'data-displaydata="otherInfo"';
        }else{
            html1 = 'data-filterData="otherInfo"';
        }
        // 区域
        html += '<td class="">' +
            '<div class="bt-biz-dict" data-type="select" data-more="true" '+html1+' data-required="true" data-group="fex.templateField.dataArea" data-name="fieldArea_' + index + '" data-biz-name="fieldArea_' + index + '" data-defaultvalue="' + escapeHtml(item.fieldArea) + '"></div>' +
            '</td>';
    }


    // 排序
    html += '<td class="textCenter">' +
        '<input type="text" id="fieldSort_' + index + '" name="fieldSort_' + index + '" lay-verify="required" class="layui-input fieldSort" value="' + escapeHtml(item.fieldSort) + '">' +
        '</td>';

    // 是否发送银行
    if ("otherInfo" == item.fieldArea){
        html += '<td class="textCenter">' + getCheckBoxHtml("isExc", index, item.isExc,item.fieldCode) + '</td>';
        html += '<td class="textCenter">' + '<a class="delete-row" href="#" data-id="' + item.fieldId + '" >' + bt.lang("common.button.delete") + '</a>' + '</td>';
    } else {
        html += '<td class="textCenter"></td><td class="textCenter"></td>';
    }
    html += '</tr>';
    var $addRow = $tbody.find('.add-row');
    if ($addRow.length) {
        // 存在 add-row，插在它前面
        $addRow.before(html);
    } else {
        // 不存在，追加到最后
        $tbody.append(html);
    }
    // $tbody.append(html);
    // globalIndexObj.value++;
    rowIndex++

    // 递归渲染子节点
    if (item.children && item.children.length > 0) {
        $.each(item.children, function (i, child) {
            renderItem(child, $tbody, globalIndexObj);
        });
    }
}

// 加载追后一行
function renderLastItem($tbody){
    var html = '<tr class="add-row">';
    html += '<td class="textCenter"><button class="layui-btn" type="button" onclick="addFields()">+</button></td>';
    for (var i=0; i<11; i++){
        html += '<td class="textCenter"></td>';
    }
    html += '</tr>';
    $tbody.append(html);
}

// 通用：生成复选框 HTML
function getCheckBoxHtml(name, index, value,fieldCode) {
    var checked = value == 1 ? 'checked' : '';
    var className = name;
    if((fieldCode === 'corpInfo' || fieldCode === 'cotpInfo' ) && name === 'isVisible'){ // 对手账户信息 主体单位信息 必须选中
        checked = 'checked'
    }
    /*if(fieldCode === 'cdSign' && name === 'isVisible'){
        return ''
    }else if(fieldCode === 'cdSign') {
        checked = 'checked'
        className += ' ';
        className += fieldCode +'_'+name
    }*/

    return '<input type="checkbox" data-code="'+ fieldCode +'" id="' + name + '_' + index + '" name="' + name + '_' + index + '" value="' + (value || '') + '" ' + checked + ' lay-ignore class="config-checkbox '+ className+' ">';
}

// HTML 转义，防止 XSS 漏洞
function escapeHtml(str) {
    if (!str) return "";
    return String(str)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}



function addFields(){
    var custFieldIdsStr = custFieldIdArr.join(',');
    parent.layer.open({
        title: bt.lang("table.fexForeignReq.chooseCustField"),
        type: 2,
        area: ['1000px', '700px'],
        maxmin: true,
        content: basePath + '/foreign/fexTranReqConfig/chooseCustFields?excludeCustFieldIds='+custFieldIdsStr,
    });
}


// 弹窗选择字段回调
function addFieldCallBack(datas){
    if (!datas || datas.length === 0) {
        return false;
    }
    // 取当前最大排序号，新增行从 max+1 起连续编号（避免与最后一行重复）
    var fieldSortNumber = 0;
    $('#tableTbody').find('input.fieldSort').each(function () {
        var n = parseInt($(this).val(), 10);
        if (!isNaN(n) && n > fieldSortNumber) {
            fieldSortNumber = n;
        }
    });
    // 只添加未添加的字段
    for (var i =0;i < datas.length;i++) {
        var item = JSON.parse(JSON.stringify(datas[i]));
        item.fieldSort = ++fieldSortNumber;
        // 自定义字段都是otherInfo
        item.fieldArea = 'otherInfo';
        item.fieldId = item.id;
        item.isRequired = 0;
        item.isVisible = 1;
        if("1" == item.fieldType){
            item.dataType = "text";
        } else if("2" == item.fieldType){
            item.dataType = "number";
            item.dataClass = "bt-biz-moneyType";
        } else if("3" == item.fieldType){
            item.dataType = "day";
            item.dataClass = "bt-biz-dateType";
        } else if("4" == item.fieldType){
            item.dataType = "time";
            item.dataClass = "bt-biz-dateType";
        } else if("5" == item.fieldType){
            item.dataType = "daytime";
            item.dataClass = "bt-biz-dateType";
        } else if ("6" == item.fieldType){
            item.dataType = "select";
            item.dataClass = "bt-biz-dict";
            item.fieldDictGroup = item.dictGroup;
        }
        if (custFieldIdArr.indexOf(item.id) === -1) {
            custFieldIdArr.push(item.id);
            renderItem(item,$("#templateConfigDiv table tbody"))
        }
    }
    initLimitedDictState();
    bt.dicts.init();
    form.render('select');
    //
    // $.each(datas, function (i, data) {
    //     custFieldIdArr.push(data.id)
    // });
    // custFieldIdArr = uniqueArray(custFieldIdArr);
    // load(custFieldIdArr)

}

// 删除自定义字段
function deleteField(that){

    var id = $(that).data('id');
    if(id){
        var idx = custFieldIdArr.indexOf(id);
        if(idx !== -1){
            // 从下标idx开始，删除1个元素
            custFieldIdArr.splice(idx, 1);
        }
    }
    // 移除当前行
    $(that).closest('tr').remove();
    return false;
    // var id = $(that).data('id');
    // custFieldIdArr = uniqueArray(custFieldIdArr);
    // custFieldIdArr = custFieldIdArr.filter(function(item) {
    //     return item !== id;
    // });
    // 重新加载
    // load(custFieldIdArr);
}

/**
 * 根据子复选框选中状态，自动修改全选框勾选状态
 * @param {string} allElemId 全选框ID
 * @param {string} childrenClassName 子复选框class名称
 */
function checkAll(allElemId, childrenClassName) {
    var $items = $('.' + childrenClassName);
    var total = $items.length;
    var checkedLen = $items.filter(':checked').length;

    // 无子项→取消全选；子项全勾选→全选框勾选，否则不勾选
    $('#' + allElemId).prop('checked', total > 0 && total === checkedLen);
}

var hideClass = 'layui-hide-1';

/**
 * 筛选下拉dd显示隐藏
 * @param {Array} selectedArr 要展示的选项数组 [{value:xx,...}]
 * @param {Object} data layui下拉回调data
 */
function changeFieldLimite(selectedArr, data) {
    // 只查询一次DOM，缓存jquery对象
    var $ddWrap = $(data.elem).closest('tr').find('.fieldDefVal');
    var defValSelect = bt.select.get($ddWrap.data('name'));
    if (defValSelect) {
        defValSelect.val('');
    }
    var $allDd = $ddWrap.find('dd');

    if (selectedArr.length) {
        // 全部先隐藏
        $allDd.addClass(hideClass);
        // 遍历需要展示的值
        selectedArr.forEach(item => {
            $allDd.filter(`dd[lay-value="${item.value}"]`).removeClass(hideClass);
        });
        // 空值选项始终显示
        $allDd.filter('dd[lay-value=" "]').removeClass(hideClass);
    } else {
        // 无选中，全部显示
        $allDd.removeClass(hideClass);
    }
}


// 传统去重
function uniqueArray(arr) {
    var result = [];
    for(var i = 0; i < arr.length; i++){
        if(result.indexOf(arr[i]) === -1){
            result.push(arr[i]);
        }
    }
    return result;
}

function checkSortNoRepeat(){
    // 1. 获取所有需要校验的行（排除新增行）
    var $rows = $("#tableTbody tr").not(".add-row");
    const sortValues = []; // 存放所有有效排序号
    var hasDuplicate = false;
    var duplicateNum = "";

    // 2. 遍历收集排序号
    $rows.each(function(){
        try {
            var val = $(this).find(".fieldSort").val().trim();
        }catch (e){
            var val = -1
        }

        // 空值、非数字 不参与重复校验
        if (!val || isNaN(val)) return;

        var num = Number(val);

        // 3. 判断是否重复
        if (sortValues.includes(num)) {
            hasDuplicate = true;
            duplicateNum = num;
            $(this).find(".fieldSort").addClass('layui-form-danger').focus();
            return false; // 跳出循环
        }
        sortValues.push(num);
    });

    // 4. 校验结果
    if (hasDuplicate) {
        layer.msg("提交失败！\n排序号【" + duplicateNum + "】重复，请修改后重试！", {
            icon: 5
            ,shift: 6
        });
        return false
    }

    return  true
}