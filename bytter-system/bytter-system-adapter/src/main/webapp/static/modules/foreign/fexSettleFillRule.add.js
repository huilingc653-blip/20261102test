var selectTypes,dicts;
bt.use(['form', 'jquery','select','selectType', 'bttable', 'dicts','selectTableType','corp', 'bankInfo', 'selectTable','allExternalCorp','allBankInfo'], function () {
    var form = bt.form(), $ = bt.jquery;
    bt.bankInfo.init();//联行号
    dicts=bt.dicts.init();

    var allExternalCorp=bt.allExternalCorp.init();//客商组件初始化
    bt.cotpExternalBeforeClick = function () {
        allExternalCorp.set({
            "type": "external,corp",
        });
        return true;
    }
    var allBankInfo=bt.allBankInfo.init();//银行组件初始化

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }
    form.on('submit(submit)', function(data) {
        var action = "/foreign/fexsettlefillrule/saveOrUpdate";
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


    var addSize=$("#addSize").val();//规则补充数据条数
    var rowNum;
    if(bt.empty(addSize)){
        rowNum=0;
    }else{
        rowNum = Number($("#addSize").val());
    }
    var ruleId=$("#id").val();
    //初始化表格
    bt.bttable.init({
        scrollX: true,
        scrollY: "400px",
        elem: "#ruleDtl",
        url: basePath + "/foreign/fexsettlefillrule/getRuleAddByRuleId?ruleId="+ruleId,
        buttons: [
            {
                text: '<i class="fa fa-plus"></i>' + bt.lang('common.addTr'),
                action: function ($button) {
                    if(rowNum>19){//最多添加20条
                        layer.msg(bt.lang('message.confirm.ruleAdd'));//最多可添加20个补充字段
                        return;
                    }
                    var html = '<tr row="' + rowNum + '">';
                    html+='<td align="center">'+(rowNum+1)+'</td>';
                    html += '<td  width="40%" align="center"><input type="text" id="ruleAddList[' + rowNum + '].fieldName" name="ruleAddList[' + rowNum + '].fieldName" autocomplete="off" class="layui-input" lay-verify="required|strlength" data-maxlength="60"></td>';
                    html += '<td  width="40%" align="center"><input type="text" id="ruleAddList[' + rowNum + '].fieldVal" name="ruleAddList[' + rowNum + '].fieldVal" autocomplete="off" class="layui-input" lay-verify="strlength" data-maxlength="100"></td>';
                    html += '<td  align="center"><a href="javascript:void(0);" class="delete-data">' + bt.lang("common.button.delete") + '</a></td>';
                    html += '</tr>';
                    $("#ruleDtl").append(html);
                    rowNum ++;

                }
            }
        ],
        columns:[
            {
                data:null,className: 'center',
                render: function (data, row) {
                    return (row+1);
                }
            },
            {
                data: 'fieldName',className: 'center',
                render: function (data,row) {
                    return '<div class="layui-input-inline"><input type="text" id="ruleAddList[' + row + '].fieldName" name="ruleAddList[' + row + '].fieldName" autocomplete="off" class="layui-input" lay-verify="required|strlength" data-maxlength="60"  value="'+data.fieldName+'"></div>';
                }
            },
            {
                data: 'fieldVal',className: 'center',
                render: function (data,row) {
                    var fieldVal=data.fieldVal;
                    if(bt.empty(data.fieldVal)){
                        fieldVal="";
                    }
                    return '<div class="layui-input-inline"><input type="text" id="ruleAddList[' + row + '].fieldVal" name="ruleAddList[' + row+ '].fieldVal" autocomplete="off" class="layui-input" lay-verify="strlength" data-maxlength="100" value="'+fieldVal+'"></div>';

                }

            },
            {
                data: null,className: 'center',
                render: function (data, row) {
                    return '<a href="javascript:void(0);" class="delete-data">' + bt.lang("common.button.delete") + '</a>';
                }
            }
        ],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        },
        tableInitCallback: function (data) {
            bt.dicts.init();
            bt.selectType.init();
        }
    });

    $(document).on('click', ".delete-data", function () {
        var tr = $(this).parents('tr');
        var index = tr.attr("row");
        $('input[name="ruleAddList[' + index + '].fieldName"]').attr("value","");
        $('input[name="ruleAddList[' + index + '].fieldVal"]').attr("value","");
        tr.remove();
        rowNum--;
    });


    //付款账号
    var bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath + "/foreign/fexsettlefillrule/getBankAcc",
        urldata: {
            corpId: $("#accCorpId").val(),
            curId : $("#accCurId").val()
        },
        scrollX: true,
        scrollY: "200px",
        searchInput: "#bankAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#bankAcc').val(data.bankAcc);
                $('#accId').val(data.id);
            } else {
                $('input.text').val('');
            }
        }
    });
    //单位回调函数
    bt.corpCallback = function(id) {
        //获取单位信息
        $.ajax({
            url : basePath + "/sys/corp/getSysCorp",
            method : 'POST',
            data : {
                id : id
            },
            dataType : 'json',
            success : function(data) {
                if (data.obj) {
                    if (data.obj.id) {
                        $("#accCorpId").val(data.obj.id);
                    }
                    //重新加载存入账号
                    bankAccInit.get("bankAcc").reload({
                        corpId : $("#accCorpId").val(),
                        curId : $("#accCurId").val()
                    });
                    //清空存入账号信息
                    $('#bankAcc').val("");
                    $("#accId").val("");
                }
                form.render();
            }
        });
    };
    bt.corp.init();

    //币种回调函数
    bt.curIdCallback = function (data) {
        $("#accCurId").val(data.id);
        //重新加载银行账号
        bankAccInit.get("bankAcc").reload({
            corpId : $("#accCorpId").val(),
            curId : data.id
        });
        //清空银行账号信息
        $('#bankAcc').val("");
        $("#accId").val("");
    };
    selectTypes=bt.selectType.init();

});

/**
 * 根据交易类型查找产品
 * @param data
 */
function changeTranType(data){
    var tranType=data.value;
    selectTypes.get("proId").attr({"data-url":basePath + "/foreign/fexproduct/listProduct/"+tranType,"data-value":"","data-defaultvalue":"first"});

    /**交易类型控制交易对手组件选择
     * 1-外汇买卖（旧）、7-即期、8-远期、9-掉期、2-期权 → 银行
     * 3-调拨 → 单位
     * 4-资金收付 → 客商
     */
    if(bt.empty(data.value)){
        $("#oppCorpNameDiv").show();
        $("#externalDiv").hide();
        $("#corpDiv").hide();
        $("#bankDiv").hide();
    }else if("1"==data.value || "2"==data.value || "7"==data.value || "8"==data.value || "9"==data.value){
        $("#oppCorpNameDiv").hide();
        $("#externalDiv").hide();
        $("#corpDiv").hide();
        $("#bankDiv").show();
    }else if("3"==data.value){
        $("#oppCorpNameDiv").hide();
        $("#externalDiv").hide();
        $("#corpDiv").show();
        $("#bankDiv").hide();
    }else if("4"==data.value){
        $("#oppCorpNameDiv").hide();
        $("#externalDiv").show();
        $("#corpDiv").hide();
        $("#bankDiv").hide();
    }else{
        $("#oppCorpNameDiv").show();
        $("#externalDiv").hide();
        $("#corpDiv").hide();
        $("#bankDiv").hide();
    }
    $("#cotpName").val("");
    $("#cotpNameEn").val("");
    $("#oppId").val("");
    $("#eOppCorpName").val("");
    bt.corp.init().get("cOppCorpName").val("");
    $("#bOppCorpName").val("");
}

//交易对手所有组件回调方法
//客商回调
function cotpExternalCallback(data) {
    $("#cotpName").val(data.name);
    $("#cotpNameEn").val(data.nameEn);
    $("#oppId").val(data.id);
    $("#eOppCorpName").val(data.name);
    if(data.isNative=="0"){
        selectTypes.get("countryId").val(data.province);
    }
}
//单位回调
function oppCorpTreeCallback(data) {
    $("#cotpName").val(data.corpName);
    $("#oppId").val(data.id);
}
//银行回调
function bankCallback(data) {
    $("#cotpName").val(data.bankName);
    $("#oppId").val(data.id);
    $("#bOppCorpName").val(data.bankName);
    if(data.type=="outerBank"){
      selectTypes.get("countryId").val(data.countryCode);
    }
}

function oppCorpNameClick() {
    parent.layer.msg(bt.lang("table.fexBankAcc.tranType.null"), {icon: 2});
}
