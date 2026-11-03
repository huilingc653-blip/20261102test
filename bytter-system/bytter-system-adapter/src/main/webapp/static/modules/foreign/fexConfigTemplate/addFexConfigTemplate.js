bt.use([ 'form','jquery','layedit','select','dicts','dictGroup','bttable','uuid','selectType'], function() {
    $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    var dictGroup = bt.dictGroup.init();
    bt.selectType.init();
    //生成列表



    bt.bttable.init({
        data: [{"bankTypeId":"","bifId":"","countryId":"","payType":""}],
        elem: "#fieldDataTable",
        buttons: [
            {
                text: '<i class="layui-icon">&#xe642;</i>'+bt.lang('common.addTr'),
                action:function($button){
                    createTr("","","","");
                }
            }
        ],
        columns:[
            {
                data:"bankTypeId",
                render:function(data, row){
                    return '<div     data-name="bankTypeId"  class="bt-biz-selectType" data-url="'+basePath+'/basic/business/btbanktype/json"     data-valuefield="id" data-textfield="typeName" data-biz-name="bankTypeId" data-callback="bankTypeIdCallBack"></div>';
                }

            },
            {
                data:"bifId",
                render:function(data, row){

                    return '<div    data-name="bifId"  class="bt-biz-selectType"  data-url=""   data-valuefield="id" data-textfield="name"      data-biz-name="bifId"  data-callback="bifIdCallBack"></div>';
                }

            }   ,    {
                data:"countryId",
                render:function(data, row){

                    return '<div     data-name="countryId"  class="bt-biz-selectType"  data-url="'+basePath+'/basic/business/syscountrycust/listSysCountryCust"       data-valuefield="id" data-textfield="nameZh" data-biz-name="countryId" data-callback="countryIdCallBack"></div>';
                }

            },
            {
                data:"payTypeId",
                render:function(data, row){

                    return '<div     data-name="payTypeId"  class="bt-biz-selectType"  data-url=""    data-valuefield="customCode"  data-textfield="bankPayTypeDisplay"    data-biz-name="payTypeId" ></div>';

                }

            },
            {data:null,className:"center",
                render:function(data,row){
                    return '<a href="#"    class="cloneData">'+bt.lang("common.button.clone")+'</a>';
                }
            }

        ],
        delRowCallback:function(data,$tr){
            $tr.remove();
        },
        tableInitCallback:function(){
            dicts.render();
            dictGroup.render();
            bt.selectType.init();
            form.render();
        }
    });

    form.verify({
        //校验输入code是否重复
     /*   checkCode : function(value) {
            if (value) {  //非空字符串
                var success = true;
                $.ajax({
                    type : 'POST',
                    url : basePath + "/bill/blankvou/bvvouchertype/checkVoucherTypeCode",
                    data : {'opType':'add','code':value},
                    async : false,
                    dataType : 'json',
                    success : function(result){
                        if (!result.success){
                            success =false;
                        }
                    }
                });
                if (!success) {
                    return bt.lang('bill.message.code.unique');
                }
            }
        },*/

    });

    // 提交保存数据
    form.on('submit(submit)', function(data) {
        bt.loading.show();
          var  validate=true;
        // 收集明细表数据
        var index=0;
        var children = [];
        // 遍历表格的每一行（跳过表头）
        $("#fieldDataTable tbody tr").each(function() {
            index =index+1;
            var $row = $(this);
            // 获取当前行中各下拉框的选中值
            var bankTypeSelect = $row.find('td').eq(0).find('.bt-biz-selectType');
            var bifCodeSelect  = $row.find('td').eq(1).find('.bt-biz-selectType');
            var countrySelect  = $row.find('td').eq(2).find('.bt-biz-selectType');
            var payTypeSelect  = $row.find('td').eq(3).find('.bt-biz-selectType');

            var bankTypeId = bt.selectType.get(bankTypeSelect.attr('data-biz-name')).getSelectedData().id || '';
            var bifId = bt.selectType.get(bifCodeSelect.attr('data-biz-name')).getSelectedData().id || '';
            var countryId = bt.selectType.get(countrySelect.attr('data-biz-name')).getSelectedData().id || '';
            var payTypeId = bt.selectType.get(payTypeSelect.attr('data-biz-name')).getSelectedData().id || '';
            var payTypeCode = bt.selectType.get(payTypeSelect.attr('data-biz-name')).getSelectedData().customcode  || '';
            var payTypeName = bt.selectType.get(payTypeSelect.attr('data-biz-name')).getSelectedData().customname  || '';
            var bankTypeName = bt.selectType.get(bankTypeSelect.attr('data-biz-name')).getSelectedData().typename  || '';
            var bifName = bt.selectType.get(bifCodeSelect.attr('data-biz-name')).getSelectedData().bifcode  || '';
            var countryName = bt.selectType.get(countrySelect.attr('data-biz-name')).getSelectedData().namezh  || '';

            // 如果必填项都填了才加入（可根据需要调整）
            if (!(bankTypeId &&bifId&&countryId&&payTypeId)) {
                validate= false;
            }else {
                children.push({
                    bankTypeName: bankTypeName,
                    bifName: bifName,
                    countryZhName: countryName,
                    payTypeName:payTypeName,
                    bankTypeId: bankTypeId,
                    bifId: bifId,
                    countryId: countryId,
                    payTypeId: payTypeId,
                    payTypeCode:payTypeCode,
                    sort:index
                });
            }
        });
        if(!validate){
            parent.layer.alert("请完整填写明细表所有必填项", {icon: 2});
            bt.loading.hide();
            return false;
        }

        // 组装表单数据，注意字段映射需根据后台接口的实际接收字段名调整
        var param = {
            templateNo: $.trim($("#templateCode").val()),       // 模板编码
            templateName: $.trim($("#templateName").val()),       // 模板名称
            templateDesc: $.trim($("#templateDesc").val()),       // 模板描述
            validSign: $('input[name="bvValidSign"]:checked').val(), // 有效标记
            remark: $("#rmk").val(),                                 // 备注
            children: children                                    // 明细列表
        };

        $.ajax({
            type : 'post',
            url : basePath + "/foreign/fexTranConfigTemplate/save",
            contentType : 'application/json; charset=UTF-8',
            data : JSON.stringify(param),
            success : function(result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success?1:2})
                if (result.success) {
                    bt.navtab.refreshParentTable();//刷新父页面表格
                    bt.navtab.refreshCurrent();
                }
            },
            dataType : 'json'
        });
        return false;
    })

    //重置时触发
        //重置时触发
        $('#reset').on('click', function(e) {
            e.preventDefault();

            // 重置主表单所有输入框
            $('#templateCode, #templateName, #templateDesc, #rmk').val('');

            // 重置单选框（有效标记默认为"是"，值为1）
            $('input[name="bvValidSign"][value="1"]').prop('checked', true);

            // 清空明细表格，只保留一行空数据
            $("#fieldDataTable tbody").empty();
            createTr("", "", "", "");

            // 重新渲染表单
            form.render();

            return false;
        });


    $("#fieldDataTable").on('click','.cloneData',function () {
        var $currentRow = $(this).closest('tr');
        var $tds = $currentRow.find('td');

        // 获取当前行的所有选中值
        var bankTypeSelect = $tds.eq(0).find('.bt-biz-selectType');
        var bifCodeSelect = $tds.eq(1).find('.bt-biz-selectType');
        var countrySelect = $tds.eq(2).find('.bt-biz-selectType');
        var payTypeSelect = $tds.eq(3).find('.bt-biz-selectType');
        var bankTypeId = bt.selectType.get(bankTypeSelect.attr('data-biz-name')).getSelectedData().id || '';
        var bifId = bt.selectType.get(bifCodeSelect.attr('data-biz-name')).getSelectedData().id || '';
        var countryId = bt.selectType.get(countrySelect.attr('data-biz-name')).getSelectedData().id || '';
        var payTypeId = bt.selectType.get(payTypeSelect.attr('data-biz-name')).getSelectedData().id || '';
        createTr(bankTypeId,bifId,countryId,payTypeId);
    });

    function createTr(bankTypeId,bifId,countryId,payTypeId){
        var rowIndex = $("#fieldDataTable tbody tr").length; // 当前行数
        var uniqueId = Date.now() + '_' + (rowIndex+1); // 生成唯一标识

        var bankTypeIdDefault="";
        var bifIdDefault="";
        var countryIdDefault="";
        var payTypeIdDefault="";
        if(bankTypeId){
            bankTypeIdDefault='data-defaultvalue="'+bankTypeId+'"';
        }
        if(bifId){  // 修正：检查 bifId
            bifIdDefault='data-defaultvalue="'+bifId+'"';
        }
        if(countryId){  // 修正：检查 countryId
            countryIdDefault='data-defaultvalue="'+countryId+'"';
        }
        if(payTypeId){  // 修正：检查 payTypeId
            payTypeIdDefault='data-defaultvalue="'+payTypeId+'"';
        }

        var html='<tr>';
        html+='<td><div class="bt-biz-selectType" data-url="'+basePath+'/basic/business/btbanktype/json" data-biz-name="bankTypeId_'+uniqueId+'" data-name="bankTypeId" data-valuefield="id" data-textfield="typeName" data-callback="bankTypeIdCallBack" '+bankTypeIdDefault+' ></div></td>';
        html+='<td><div class="bt-biz-selectType" data-url="' + basePath + '/basic/business/bisbifinit/getByBankType/' + (bankTypeId || '0') + '" data-biz-name="bifCode_' + uniqueId + '" data-name="bifCode" data-valueField="id" data-textField="bifCode" data-callback="bifIdCallBack" '+bifIdDefault+' ></div></td>';
        html+='<td><div class="bt-biz-selectType" data-url="'+basePath+'/basic/business/syscountrycust/listSysCountryCust" data-biz-name="countryId_'+uniqueId+'" data-name="countryId" data-valuefield="id" data-textfield="nameZh" data-callback="countryIdCallBack" '+countryIdDefault+' ></div></td>';
        html+='<td><div class="bt-biz-selectType" data-url="' + basePath + '/basic/business/btbanktype/listByBankTypeAndBifCodeAndCountryId?bankType=' + (bankTypeId || '0') + '&bifCode=' + (bifId || '0') + '&countryId=' + (countryId || '0') + '" data-biz-name="payTypeId_' + uniqueId + '" data-name="payTypeId" data-valueField="id" data-textField="bankPayTypeDisplay" '+payTypeIdDefault+' ></div></td>';
        html+='<td>' +
            '<a href="#" data-type="del" class="">'+bt.lang("common.button.delete")+'</a>'+
            '<span class="text-explode">|</span>' +
            '<a  href="#"  class="cloneData">'+bt.lang("common.button.clone")+'</a>' +
            '</td>';
        html+='</tr>';
        $("#fieldDataTable tbody").append(html);
        bt.selectType.init();
        form.render();
    }
});





//选择银行类型回调
function bankTypeIdCallBack(selectedData, selectObj, bv, wrapObj) {
    var $currentRow = wrapObj.closest('tr');
    if ($currentRow.length === 0) {
        console.error('无法获取当前行');
        return;
    }
    var rowIndex = $currentRow.index();
    // 获取当前行中 bifCode 所在的单元格（第2列，索引为1）
    var $bifTd = $currentRow.find('td').eq(1);
    // 构建新的URL
    var baseUrl = basePath + '/basic/business/bisbifinit/getByBankType/';
    var newUrl = baseUrl + (selectedData.id || '00000');

    // 完全重新创建 bifCode 下拉框
    var newHtml = '<div class="bt-biz-selectType" ' +
        'data-url="' + newUrl + '" ' +
        'data-name="bifCode" ' +
        'data-biz-name="bifCode_' +rowIndex+'"'+
        ' data-valuefield="id" ' +
        'data-textfield="bifCode" ' +
        'data-callback="bifIdCallBack"' +
        '</div>';

    // 替换原有内容
    $bifTd.html(newHtml);

    // 重新初始化selectType组件
    bt.selectType.init();

    // 重新渲染表单
    form.render();
}
//选择接口代码回调
function bifIdCallBack(selectedData, selectObj, bv, wrapObj) {
    var $currentRow = wrapObj.closest('tr');
    if ($currentRow.length === 0) {
        console.log('无法获取当前行');
        return;
    }
    var rowIndex = $currentRow.index();
    var $payType = $currentRow.find('td').eq(3);
    var selectedDataOfbankType = bt.selectType.get($currentRow.find('td').eq(0).find('.bt-biz-selectType').attr('data-biz-name')).getSelectedData();
    var selectedDataOfCountry = bt.selectType.get($currentRow.find('td').eq(2).find('.bt-biz-selectType').attr('data-biz-name')).getSelectedData();
    if(!selectedDataOfbankType.id  ||!selectedData.id || !selectedDataOfCountry.id){
        console.log('没有全选值');
        return;
    }
    // 构建新的URL
    var newUrl = basePath + '/basic/business/btbanktype/listByBankTypeAndBifCodeAndCountryId?bankType='+ selectedDataOfbankType.id+'&bifCode='+selectedData.id+'&countryId='+selectedDataOfCountry.id;
    // 完全重新创建 bifCode 下拉框
    var newHtml = '<div class="bt-biz-selectType" ' +
        'data-url="' + newUrl + '" ' +
        'data-name="payTypeId" ' +
        'data-biz-name="payTypeId_' +rowIndex+'"'+
        ' data-valuefield="id" ' +
        'data-textfield="bankPayTypeDisplay" ' +
        '</div>';
    // 替换原有内容
    $payType.html(newHtml);

    // 重新初始化selectType组件
    bt.selectType.init();

    // 重新渲染表单
    form.render();

}
//选择国家回调
function countryIdCallBack(selectedData, selectObj, bv, wrapObj) {
    var $currentRow = wrapObj.closest('tr');
    if ($currentRow.length === 0) {
        console.log('无法获取当前行');
        return;
    }
    var rowIndex = $currentRow.index();
    var $payType = $currentRow.find('td').eq(3);
    var selectedDataOfbankType = bt.selectType.get($currentRow.find('td').eq(0).find('.bt-biz-selectType').attr('data-biz-name')).getSelectedData();
    var selectedDataOfCountry = bt.selectType.get($currentRow.find('td').eq(1).find('.bt-biz-selectType').attr('data-biz-name')).getSelectedData();
    if(!selectedDataOfbankType.id  || !selectedData.id || !selectedDataOfCountry.id){
        console.log('没有全选值');
        return;
    }
    // 构建新的URL
    var newUrl = basePath + '/basic/business/btbanktype/listByBankTypeAndBifCodeAndCountryId?bankType='+ selectedDataOfbankType.id+'&bifCode='+selectedDataOfCountry.id+'&countryId='+selectedData.id;
    // 完全重新创建 bifCode 下拉框
    var newHtml = '<div class="bt-biz-selectType" ' +
        'data-url="' + newUrl + '" ' +
        'data-name="payTypeId" ' +
        'data-biz-name="payTypeId_' +rowIndex+'"'+
        ' data-valuefield="id" ' +
        'data-textfield="bankPayTypeDisplay" ' +
        '</div>';
    // 替换原有内容
    $payType.html(newHtml);

    // 重新初始化selectType组件
    bt.selectType.init();

    // 重新渲染表单
    form.render();
}





