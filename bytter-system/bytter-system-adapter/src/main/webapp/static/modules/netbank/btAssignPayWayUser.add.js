bt.use(['form','layedit','selectTree','select','datatable','element','mulselect','selectType','corp'], function(){
	var $ = bt.jquery,form = bt.form();
    var element = layui.element();
    bt.mulselect.init();
    bt.selectType.init();
	//下拉框
	$.each($("div[type='list']"),function(i,item){
		bt.select.init({
			elem: "[field='"+$(item).attr("field")+"']",
			name: $(item).attr("field"),
			defaultVal: $(item).attr("defaultVal"),
			valueField: $(item).attr("dictField"),
			textField: $(item).attr("dictText"),
			search: true,
			disabled: false,
			required: true,
			url: $(item).attr("fieldhref")
		});
	});

    // 选择单位回调
    corpCallback = function (id) {
        var child = $('input[name="voucherCodes"]');
        child.each(function(index, item){
            item.checked = false;
            form.render('checkbox');
        });
        if(id!="" && id.length<=32){
            $.ajax({
                url: basePath+"/netbank/btassignpayway?getAssignPayWay",
                method: 'POST',
                data: {corpId: id},
                dataType: 'json',
                success: function(data){
                    if(data){
                        $.each(data.obj,function(i,tem){
                            child.each(function(index, item){
                                if(tem.voucherCode==item.value){
                                    item.checked = true;
                                }
                            });
                            $("select[name='defVoucherCode']").val(tem.defVoucherCode);
                            form.render();
                        });
                    }
                }
            });
        }else{
            child.each(function(index, item){
                item.checked = false;
            });
            $("select[name='defVoucherCode']").val("");
            form.render();
        }
    }

    bt.corp.init();

	//生成列表
    var datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/netbank/btassignpayway/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data:"corpName",bSortable:false,
                render: function ( data, type, full, meta ) {
                	return "(" + full.corpCode + ")" + full.corpName;
                }
            },
            {data:"voucherName",bSortable:false},
            {data:"defVoucherName",bSortable:false},
        ]
    });
    //生成列表
    var userTable = bt.datatable.init({
        elem: "#userTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/netbank/btassignpayway/userDatatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data:"corpName",bSortable:false,
                render: function ( data, type, full, meta ) {
                    return full.userName;
                }
            },
            {data:"voucherName",bSortable:false}
        ]
    });
	
	//监听提交
    form.on('submit(submit)', function(data){
    	//校验默认付款方式必须是分配的付款方式当中的
    	var defVoucherCode = $("select[name='defVoucherCode']").val();
    	var checkVal = [];
        var voucherCodesStr;
    	$('#corpForm').find('input[name="voucherCodes"]:checked').each(function(){
    		checkVal.push($(this).val());
            if ($(this).val()) {
                if (null == voucherCodesStr) {
                    voucherCodesStr = $(this).val();
                } else {
                    voucherCodesStr += "," + $(this).val();
                }
            }
		});
    	var flag = false;
    	if(checkVal && defVoucherCode && checkVal.length>0) {
            for (var i = 0; i < checkVal.length; i++) {
                if (defVoucherCode == checkVal[i]) {
                    flag = true;
                }
            }
        }else{
            flag = true;
        }
    	if(!flag){
    		layer.alert(bt.lang("netbank.message.defvouchercode.mismatch"),{icon:2});
    		return false;
    	}
        var action = "/netbank/btassignpayway/save";
        data.field.voucherCodesStr = voucherCodesStr;
        $.post(basePath + action,data.field,function(result){
            if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            location.reload();
        },'JSON');
        return false;
    });

    //全选
    form.on('checkbox(allChoose)', function(data){
        var child = $('input[name="voucherCodes"]');
        child.each(function(index, item){
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });
    form.on('checkbox(allChooseUser)', function(data){
        var child = $('input[name="voucherCodesUser"]');
        child.each(function(index, item){
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    //监听提交
    form.on('submit(submitUser)', function(data){
        var checkVal = [];
        var voucherCodesStr;
        $('#userForm').find('input[name="voucherCodesUser"]:checked').each(function(){
            checkVal.push($(this).val());
            if ($(this).val()) {
                if (null == voucherCodesStr) {
                    voucherCodesStr = $(this).val();
                } else {
                    voucherCodesStr += "," + $(this).val();
                }
            }
        });
        var action = "/netbank/btassignpayway/saveUser";
        data.field.voucherCodesStr = voucherCodesStr;
        $.post(basePath + action,data.field,function(result){
            if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            bt.navtab.refreshCurrentTable();
        },'JSON');
        return false;
    });

});

function pushTable(tableName){
    bt.datatable.refreshTable(tableName);
}
userIdcallback = function (data) {
    var child = $('input[name="voucherCodesUser"]');
    child.each(function(index, item){
        item.checked = false;
        bt.form().render('checkbox');
    });
    if(data != undefined && data.length == 1){
        $.ajax({
            url: basePath+"/netbank/btassignpayway?getAssignPayWayByUserId",
            method: 'POST',
            data: {userId: data[0].id},
            dataType: 'json',
            success: function(data){
                if(data){
                    bt.jquery.each(data.obj,function(i,tem){
                        child.each(function(index, item){
                            if(tem.voucherCode==item.value){
                                item.checked = true;
                            }
                        });
                        bt.form().render();
                    });
                }
            }
        });
    }else{
        child.each(function(index, item){
            item.checked = false;
        });
        bt.form().render();
    }
}