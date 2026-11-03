var datatable;
bt.use(['form', 'layedit', 'dateType', 'corp', 'datatable', 'selectTable', 'dateForm', 'selectType','dicts','bank','corp'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
        	'url': basePath+"/bankacc/btbankacc/getDataTableByBifCode"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
			{data:"bankAcc",bSortable:true},
			{data:"accName",bSortable:true},
			{data:"corpName",bSortable:true},
			{data:"curName",bSortable:true},
			{data:"accTypeName",bSortable:true},
			{data:"attributeName",bSortable:true},
			{data:"natureName",bSortable:true},
			{data:"bifName",bSortable:true},
			{data:"bankName",bSortable:true},
			{data:"parentSign",bSortable:true,
                render: function ( data, type, full, meta ) {
			        if(isEmpty(full.parentSign)){
                        full.parentSign = '0';
                    }
                    return bt.dict("sf_yn",full.parentSign);
                }
            },
        ]
    });
    
    //监听提交
    form.on('submit(submit)', function (data) {
    	bt.loading.show();
    	var dataRow = bt.datatable.getSelectData();
    	var len = dataRow.length;
        if(!len){
        	bt.loading.hide();
            layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        	return false;
        }
        var bankAccIds="";
        var bankAccs="";
        var curIds="";
        var bifCodes="";
     	var rowData = dataRow.splice(0,len);
     	for(var i=0;i<rowData.length;i++){
     		if (bankAccIds == "") {
     			bankAccIds += rowData[i].id;
    			bankAccs += rowData[i].bankAcc;
    			curIds += rowData[i].curId;
    			bifCodes += rowData[i].bifCode;
    		} else {
    			bankAccIds += "," + rowData[i].id;
    			bankAccs += "," + rowData[i].bankAcc;
    			curIds += "," + rowData[i].curId;
    			bifCodes += "," + rowData[i].bifCode;
    		}
     	}
     	data.field.bankAccIds = bankAccIds;
     	data.field.bankAccs = bankAccs;
     	data.field.curIds = curIds;
     	data.field.bifCodes = bifCodes;
     	
        var action = "/bdlink/bisquerycmd/save";
        $.post(basePath + action, data.field,function(result){
        	bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
            	parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });

    //单位回调函数
    bt.corpCallback = function (data) {
        bt.datatable.refreshTable();//刷新表格
    };
    bt.corp.init();
    //银企接口回调函数
    bt.bifCodeCallback = function (data) {
    	bt.datatable.refreshTable();//刷新表格
    };
    
    //交易类型回调函数
    bt.transIdCallback = function (data) {
		if(data.value=="900030" || data.value=="900040" || data.value=="900050"){
			$("#dateTr").show();
		}else{
			$("#dateTr").hide();
		}
    };
    bt.dicts.init();

    //账号回调
    bt.accBack = function () {
        bt.datatable.refreshTable();//刷新表格
    }
});

//银行账号回调函数
function accCallback(data) {
    bt.accBack(data);
};

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
