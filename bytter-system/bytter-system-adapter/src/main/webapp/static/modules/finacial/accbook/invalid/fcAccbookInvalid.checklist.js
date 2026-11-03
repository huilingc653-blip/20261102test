bt.use(['form','datatable','btdate','moneynum', 'money', 'moneyType','dateType','corp','bank'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.corp.init();
    bt.moneyType.init();
    bt.bank.init({
  	   elem:'.bank',    
    });
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:2});
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        
        checkbox:true,order: [1,'desc'],
        ajax: {
            'url': basePath+"/finacial/accbook/fcaccbookinvalid/checkdataTable"
        },
        buttons: [
            {
                text: bt.lang('通过'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchCheck(data);
                }
            },
            {
                text: bt.lang('打回'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchBack(data);
                }
            }
        ]
        ,columns: [
            {data:"busRaceId",bSortable:true,className:'center',render: function (data, type, full, meta) {
			    return full.raceName;
			}},
            {data:"voucherType",bSortable:true,className:'center',render: function (data, type, full, meta) {
			    return full.voucherName;
			}},
            {data:"tallyDate",bSortable:true},
            {data:"interestStartDate",bSortable:true},
            {data:"orderNo",bSortable:true},
            {data:"money",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data);
			}},
			{data:"rmk",bSortable:false},
            {data:"voucherState",bSortable:false,render: function (data, type, full, meta) {
                return bt.dict("Finacial.VoucherState", data);
            }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
                <a class="" href="#" data-row="'+meta.row+'" onclick="check(this)">'+bt.lang("Finacial.accbook.invalid.button.check")+'</a>';
                return html;
            }}
        ]
    });
});

function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        /*if(bt.navtab){
        	bt.navtab.addNavtab(bt.lang('Finacial.accbook.invalid.button.view'),basePath + "/finacial/accbook/fcaccbook/view?id=" + data.id);
            return;
        }*/
        parent.layer.open({
            type: 2,
            area: ['1000px', '600px'],
            maxmin: true,
            content: basePath + "/finacial/accbook/fcaccbook/view?id=" + data.id,
            end:function(){
            	bt.navtab.refreshCurrentTable();
            }
        });
    }
}
function check(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
        	bt.navtab.addNavtab(bt.lang('Finacial.accbook.invalid.button.check'),basePath + "/finacial/accbook/fcaccbookinvalid/update?id=" 
            		+ data.id+"&busGlobalId="+data.busGlobalId+ "&opType=check");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['1000px', '600px'],
            maxmin: true,
            content: basePath + "/finacial/accbook/fcaccbookinvalid/update?id="
    				+ data.id+"&busGlobalId="+data.busGlobalId + "&opType=check",
			end:function(){
	        	bt.navtab.refreshCurrentTable();
	        }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/finacial/accbook/fcaccbookblned/delete/" + data.id, function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}


//批量复核通过
function batchCheck(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].busGlobalId;;
        } else {
            ids += "," + rowData[i].busGlobalId;;
        }
    }

    parent.layer.confirm(bt.lang('确认通过？'), function () {
        bt.loading.show();
        $.post(basePath + "/finacial/accbook/fcaccbookinvalid/batchCheck", {
            ids: ids
        }, function (result) {
            bt.loading.hide();
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        }, 'JSON');
    });

}


//批量打回
function batchBack(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].busGlobalId;;
        } else {
            ids += "," + rowData[i].busGlobalId;;
        }
    }

    parent.layer.confirm(bt.lang('确认打回？'),function(){
        parent.layer.prompt({
            title: bt.lang('打回原因'),
            formType: 2
        },function(backMsg) {
            $.post(basePath + "/finacial/accbook/fcaccbookinvalid/batchBack", {ids: ids, backMsg: backMsg}, function (result) {
                datatable && datatable.draw();
                parent.layer?parent.layer.closeAll():layer.closeAll();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    });

}