var dicts;
var form;
bt.use(['form', 'layedit', 'dicts', 'selectTree','corp','selectType'], function () {
    var $ = bt.jquery;
    dicts = bt.dicts.init();
    var corp = bt.corp.init();
    bt.selectType.init();
    form = bt.form();

    form.verify({
        customVerify:function(value,item){
            if($('[data-name="transferMode"]').find('select').val() =='2'){
                if(!/[\S]+/.test(value)){
                    return bt.lang('bill.bvVoucherType.fieldRequiredMsg')//'必填项不能为空'
                }
            }

        }
    })
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/netbank/nisFlowRule/modify";
        $.post(basePath + action, $('#nisFlowRuleForm').serialize(), function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    bt.fun = function (data) {
        tree.setUrl(basePath + "/netbank/nisbustype/getItem?cdSign=" + data.value);
    };
    form.on('select', function (data) {
        var row = data.othis.closest("tr").attr('row');
        if(data.value == 1 || data.value == 2){
            if($("#matchContent"+ row).is(":hidden")) {
                $("#matchContent" + row).val(data.value);
            }
        }
    });
});

getTransferMode = function (data) {
    if(data.value == "1" || data.value == "3"){
        $("#purposeTr").show();
        $("#bankAutoTr").hide();
        $("#purpose").attr("lay-verify",'required');
        $("#purpose").val("");
    }else{
        $("#purposeTr").hide();
        $("#purpose").removeAttr("lay-verify");
        $("#bankAutoTr").show();
    }
    form.renderIcon();//渲染必填
}

/**
 * 增加一行
 */
var rowIndex = 1
function addTr() {
    rowIndex++
    var html = '<tr row="' + rowIndex + '">';
    html += '<td><div class="bt-biz-selectType" data-url="'+basePath+'/basic/business/btbanktype/json" data-valuefield="id" data-textfield="typeName" data-verify="customVerify" data-name="bankTypeId" data-biz-name="bankInfo-bankType_' + rowIndex + '" data-required="true"></div></td>';
    html += '<td><input type="text" name="purpose1" lay-verify="customVerify"  autocomplete="off" class="layui-input" placeholder="用途/摘要"></td>';
    html += '<td><a href="javascript:void (0)" onclick="deleteDtl(this)">' + bt.lang("common.button.delete") + '</a></td>';
    html += '</tr>';
    $("#bankAutoTable").find('tbody').append(html);
    bt.selectType.init()
}
var selectedBankTypeId = [];
function changeBankTypeId(selectedRowData,data,beforeSelectedValue) {
    // 1.
    // var needFilterBankTypeSelects = data.othis.parents('tr').siblings('tr').find('.bt-biz-selectType');
    // if(data.value != ' '){
    //     selectedBankTypeId.push(data.value);
    //     filterSelectedBankTypeValue(data.value,needFilterBankTypeSelects)
    // }else if(beforeSelectedValue && beforeSelectedValue != ' '){
    //     selectedBankTypeId.splice(selectedBankTypeId.indexOf(beforeSelectedValue),1)
    //     needFilterBankTypeSelects.find('[lay-value="'+beforeSelectedValue+'"]').removeClass('layui-hide')
    // }
}

function initBankTypeId(data,$elem) {
   //
   //  if(selectedBankTypeId && selectedBankTypeId.length){
   //      bt.each(selectedBankTypeId,function (i,id) {
   //          filterSelectedBankTypeValue(id,$elem)
   //      })
   //  }
}

function filterSelectedBankTypeValue(id,$elem) {
    if(id && $elem){
        $elem.find('[lay-value="'+id+'"]').addClass('layui-hide')
    }
}
/**
 * 删除
 * @param _this
 */
function deleteDtl(_this) {
    $(_this).parents("tr").remove();
}
function onchangeMatchElements(data) {
    var row = data.othis.closest("tr").attr('row');
    var val = data.value;
    if(val == "amt"){
        $("#matchContent"+row).hide();
        $("#number"+row).show();
        $("#matchContent"+row).val("1");
        dicts.get('matchRelations'+row).attr({'data-filterData': ''});
        dicts.get('matchRelations'+row).val("3");
    }else{
        $("#matchContent"+row).show();
        $("#number"+row).hide();
        $("#matchContent"+row).val("");
        dicts.get('matchRelations'+row).attr({'data-filterData': '5,6,7,8'});
        dicts.get('matchRelations'+row).val("");
    }
}
function onchangeMatchRelations(data) {
    var row = data.othis.closest("tr").attr('row');
    var val = data.value;
    var matchElements = dicts.get('matchElements'+row).val();
    if(matchElements == "amt" && (val == "1" || val == "2" || val == "3" || val == "4")){
        $("#matchContent"+row).hide();
        $("#number"+row).show();
        $("#matchContent"+row).val("1");
    } else {
        $("#matchContent"+row).show();
        $("#number"+row).hide();
        $("#matchContent"+row).val("");
    }
}